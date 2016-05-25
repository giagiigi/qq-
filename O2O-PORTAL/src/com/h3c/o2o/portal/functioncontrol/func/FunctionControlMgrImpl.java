/*
 * Copyright (c) 2007-2015, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC V700R001
 * Module Name :
 * Date Created: 2015年10月28日
 * Creator     : dkf5133
 * Description :
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 * 2015年10月28日  dkf5133             O2O-UAM-UI project, new code file.
 *
 *------------------------------------------------------------------------------
 */

package com.h3c.o2o.portal.functioncontrol.func;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.util.Assert;

import com.google.gson.Gson;
import com.h3c.o2o.mng.rabbitmq.entity.CommandResponse;
import com.h3c.o2o.mng.rabbitmq.entity.PortalMqRequest;
import com.h3c.o2o.portal.PortalConstant;
import com.h3c.o2o.portal.PortalErrorCodes;
import com.h3c.o2o.portal.PortalException;
import com.h3c.o2o.portal.cache.api.CacheApiMgr;
import com.h3c.o2o.portal.common.FuncUtil;
import com.h3c.o2o.portal.entity.OnlineUser;
import com.h3c.o2o.portal.functioncontrol.entity.OffLineRepMsg;
import com.h3c.o2o.portal.functioncontrol.entity.OffLineReqMsg;
import com.h3c.o2o.portal.functioncontrol.entity.XxbDevMq;
import com.h3c.o2o.portal.rs.entity.ErrorInfo;
import com.h3c.o2o.portal.rs.entity.Mac;
import com.h3c.o2o.portal.rs.func.ThirdPartyAuthMgr;
import com.h3c.o2o.portal.shop.func.ShopMgr;
import com.h3c.o2o.portal.user.func.OnlineUserMgr;
import com.h3c.o2o.portal.util.WifiUtils;
import com.h3c.oasis.o2oserver.util.StringManager;

public class FunctionControlMgrImpl implements FunctionControlMgr {

	private static FunctionControlMgrImpl INSTANCE;

	/**
	 * RabbitTemplate
	 */
	private RabbitTemplate rabbitTemplate;

	/**
	 * Log
	 */
	private Log log = LogFactory.getLog(getClass());

	/**
	 * 数据缓存接口
	 */
	private CacheApiMgr cacheApiMgr;

	/**
	 * 查询在线用户，不带缓存
	 */
	private OnlineUserMgr onlineUserMgr;

	/**
	 * 第三方认证。
	 */
	private ThirdPartyAuthMgr thirdPartyAuthMgr;

	private ShopMgr shopMgr;

	/**
	 * 在线放行时长: 为了保证设备和绿洲在线用户数据一致，这里设置一个在线放行时长（单位秒）<br>
	 * 当绿洲上用户在线时长已经超过这个时间，当用户重复认证时，直接放通，不抛用户已经在线的提示。<br>
	 */
	private Integer limitPassTime = 10;

	/**
	 * 资源访问对象。
	 */
	private static StringManager sm = StringManager.getManager("com.h3c.o2o.portal");

	/**
	 * 获取该类的唯一实例
	 * 
	 * @return
	 */
	public static FunctionControlMgr getInstance() {
		return INSTANCE;
	}

	/**
	 * spring中调用
	 */
	public void init() {
		INSTANCE = this;
	}

	private boolean QQAuthEnable;

	public boolean getQQAuthEnable() {
		return QQAuthEnable;
	}

	public void setQQAuthEnable(boolean QQAuthEnable) {
		this.QQAuthEnable = QQAuthEnable;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.h3c.o2o.portal.functioncontrol.func.FunctionControlMgr#isXXBDevice(
	 * long)
	 */
	@Override
	public boolean isXXBDevice(long storeId) {
		XxbDevMq req = new XxbDevMq();
		req.setShopId(storeId);
		XxbDevMq resp = null;

		Gson gson = new Gson();
		try {
			// 发送，并获取结果
			String respStr = (String) rabbitTemplate.convertSendAndReceive(PortalConstant.QUERY_DEV_XXB_EXC,
					PortalConstant.QUERY_DEV_XXB_KEY, gson.toJson(req));
			resp = gson.fromJson(respStr, XxbDevMq.class);
			if (log.isDebugEnabled()) {
				log.debug("response string: " + respStr);
			}
		} catch (Exception e) {
			log.warn(null, e);
			throw new PortalException("Query dev type error." + e.getMessage());
		}
		// 失败
		if (null == resp) {
			log.warn("Response is empty.");
			throw new PortalException("Query dev type error.");
		}
		return resp.getIsXxbDev();
	}

	/**
	 * 根据Mac地址踢掉用户<br>
	 * 注：这里不会返回空
	 */
	@Override
	public ErrorInfo logoutByMac(String macStr) {
		Mac mac = new Mac();
		mac.setMac(macStr);
		ErrorInfo errorinfo = this.getThirdPartyAuthMgr().loginOut(mac);
		if (null == errorinfo) {
			errorinfo = new ErrorInfo();
			errorinfo.setError_code("-1");
			errorinfo.setError_msg("Nothing returned.");
		}
		if ("success".equals(errorinfo.getError_code())) {
			if (log.isInfoEnabled()) {
				log.info("user [ mac=" + macStr + "] was logout.");
			}
		} else {
			log.warn("WARN!!!: Kick out user error: [ code=" + errorinfo.getError_code() + " msg:"
					+ errorinfo.getError_msg() + "]; online user was deleted!");
			this.onlineUserMgr.deleteOnlineUserByMac(macStr);
		}
		cacheApiMgr.getOnlineUserCacheMgr().remove(macStr); // 踢下线之后，移除缓存
		return errorinfo;
	}

	/**
	 * 用户在线时间，是否超出LimitPassTime
	 * 
	 * @return
	 */
	private boolean existsLonggerThanLimitPassTime(OnlineUser onlineUser) {
		Assert.notNull(onlineUser, "OnlineUser must not be null!");
		Long startTime = onlineUser.getAccessStartTime();
		Long currentTime = System.currentTimeMillis();
		Long existsTimeInSeconds = (currentTime - startTime) / 1000;
		if (log.isInfoEnabled()) {
			log.info("OnlineUser exists " + existsTimeInSeconds + "s");
		}
		return existsTimeInSeconds > getLimitPassTime();
	}

	/**
	 * 检查上线参数
	 * 
	 * @param userMac
	 * @param storeId
	 * @param ssid
	 * @param userType
	 * @return
	 */
	private boolean checkOnlineParas(String userMac, String ip, Long storeId, String ssid, Integer userType) {
		if (null == userType) {
			log.warn("UserType is null! [mac=" + userMac + "]");
			return false;
		} else if (StringUtils.isBlank(userMac)) {
			log.warn("Empty usermac!");
			return false;
		} else if (!WifiUtils.isValidId(storeId)) {
			log.warn("Wrong storeId：" + storeId + "[mac=" + userMac + "]");
			return false;
		} else if (StringUtils.isBlank(ssid)) {
			log.warn("Empty ssid! [mac=" + userMac + "]");
			return false;
		} else if (StringUtils.isBlank(ip)) {
			log.warn("Empty ip! [mac=" + userMac + "]");
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 判断是否为同一个在线用户
	 * 
	 * @param onlineUser
	 * @param userMac
	 * @param ip
	 * @param storeId
	 * @param ssid
	 * @param userType
	 * @return
	 */
	private boolean isSameOnlineUser(OnlineUser onlineUser, String userMac, String userIp, Long storeId, String ssid,
			Integer userType) {
		return null != onlineUser && onlineUser.getAccessSsid().trim().equals(ssid)
				&& onlineUser.getStoreId().equals(storeId) && onlineUser.getUserType().equals(userType)
				&& onlineUser.getUserMac().trim().equals(userMac)
				&& StringUtils.isNotBlank(userIp) && onlineUser.getUserIp().equals(FuncUtil.convertHostIpToLong(userIp));
	}

	/**
	 * 是否更换了场所
	 * 
	 * @return
	 */
	private boolean isStoreChanged(OnlineUser onlineUser, Long storeId) {
		return null != onlineUser && !onlineUser.getStoreId().equals(storeId);
	}

	/**
	 * 判断用户是否已经在线<br>
	 * 如果在线抛出异常，如果切换了场所，旧用户先踢下线<br>
	 * 
	 */
	@Override
	public void onlineCheck(String userMac, String ip, Long storeId, String ssid, Integer userType)
			throws PortalException {
		if (!checkOnlineParas(userMac, ip, storeId, ssid, userType)) {
			return;
		}
		// 1.根据MAC地址查询在线用户
		OnlineUser onlineUser = this.getOnlineUserMgr().queryOnlineUser(userMac);
		if (null != onlineUser) {
			// 有用户在线，打印信息
			logSwitchUserInfo(onlineUser, userMac, ip, storeId, ssid, userType);
		} else {
			if (log.isDebugEnabled()) {
				log.debug("User mac: " + userMac + " is not online.");
			}
		}
		if (isSameOnlineUser(onlineUser, userMac, ip, storeId, ssid, userType)) {
			// mac, ip, ssid, storeId, usertype相同
			if (existsLonggerThanLimitPassTime(onlineUser)) {
				// 用户在线已经超过指定时间，不抛用户已经在线
				log.warn("WARN!!!: User already online, online time longger than " + getLimitPassTime()
						+ "s, there may be something wrong in authentication proceess. [mac: " + userMac + "]");
			} else {
				// 用户在线没有超过指定时间，抛用户已经在线
				log.warn("User already online, online time shortter than " + getLimitPassTime() + "s online user: "
						+ onlineUser);
				throw new PortalException(PortalErrorCodes.USER_ALLREADY_ONLINE, sm.getString("errorCode.60021"));
			}
		} else if (isStoreChanged(onlineUser, storeId)) {
			if (log.isInfoEnabled()) {
				log.info("user store(AC) changed, [ old storeId=" + onlineUser.getStoreId() + " new storeId=" + storeId
						+ "]");
			}
			// 用户切换了场所，原在线用户做下线处理
			logoutByMac(userMac);
		}

		// 2.通过IP地址查询在线用户
		Long ipInLongType = FuncUtil.convertHostIpToLong(ip);
		List<OnlineUser> ipConflictUsers = this.getOnlineUserMgr().queryOnlineUser(ipInLongType, storeId);
		if (null != ipConflictUsers && !ipConflictUsers.isEmpty()) {
			// IP冲突
			log.warn("WARN!!!: Ip confilct [ storeId=" + storeId + "mac=" + userMac + " conflictIp=" + ip
					+ " ipInLongType= " + ipInLongType + " number of ip conflict user= " + ipConflictUsers.size()
					+ "]");
			// 取MAC地址相同的IP冲突用户，将其下线
			OnlineUser u = findOnlineUserByMac(ipConflictUsers, userMac);
			if (null != u) {
				kickOutByIp(storeId, ip, ipInLongType, userMac);
			} else {
				log.warn("WARN!!!: Ip confilct in deffrent users (different mac), no users was kicked out.");
			}
			// 提示IP冲突
			throw new PortalException(PortalErrorCodes.USER_IP_CONFLICT, sm.getString("errorCode.60024"));
		}
	}

	/**
	 * 通过MAC地址，在列表中查找用户
	 * 
	 * @param users
	 * @param mac
	 * @return
	 */
	private OnlineUser findOnlineUserByMac(List<OnlineUser> users, String mac) {
		OnlineUser user = null;
		if (StringUtils.isNotBlank(mac)) {
			for (int i = 0; null != users && i < users.size(); i++) {
				if (mac.trim().equals(users.get(i).getUserMac())) {
					return users.get(i);
				}
			}
		}
		return user;
	}

	private void kickOutByIp(Long storeId, String ip, Long ipInLongType, String userMac){
		if(StringUtils.isNotBlank(ip) && WifiUtils.isValidId(storeId)){
			List<String> ipList = new ArrayList<String>();
			ipList.add(ip);
			OffLineRepMsg rep = this.logoutByIpList(storeId, ipList);
			if(null != rep && PortalErrorCodes.ERROR_CODE_ERROR.equals(rep.getError_code())){
				log.info("user [ ip=" + ip + "] was logout.");
			} else {
				log.warn("WARN!!!: Kick out user error: [ code=" + rep.getError_code() + " msg:"
						+ rep.getError_msg() + "]; online user was deleted!");
				this.onlineUserMgr.deleteOnlineUserByStoreIdAndIp(storeId, ipInLongType);
			}
			cacheApiMgr.getOnlineUserCacheMgr().remove(userMac); // 踢下线之后，移除缓存
		}
	}
	
	/**
	 * 根据场所ID和IP地址列表踢掉用户
	 * 
	 * @param macStr
	 * @return
	 */
	public OffLineRepMsg logoutByIpList(Long storeId, List<String> ipList) {
		// build request
		PortalMqRequest req = new PortalMqRequest();
		req.setShopId(storeId);
		OffLineReqMsg offlineMsg = new OffLineReqMsg();
		offlineMsg.setOptType(OffLineReqMsg.OFFLINT_TYPE);
		offlineMsg.setUuid(UUID.randomUUID().toString());
		offlineMsg.setOfflineList(ipList);
		req.setMessage(offlineMsg.toOffLineMsg());
		// build return response
		OffLineRepMsg ret = new OffLineRepMsg();
		// check request
		if (null == ipList || ipList.isEmpty()) {
			ret.setError_code(PortalErrorCodes.ERROR_CODE_ERROR);
			ret.setError_msg("Ip list is Empty!");
			return ret;
		} else {
			// check storeId
			if (!getShopMgr().isShopIdValide(storeId)) {
				ret.setError_code(PortalErrorCodes.ERROR_CODE_ERROR);
				ret.setError_msg("No store found by Id:" + storeId + "!");
				return ret;
			} else {
				CommandResponse resp = (CommandResponse) rabbitTemplate.convertSendAndReceive(
						OffLineReqMsg.OFFLINE_EXCHANGE_NAME, OffLineReqMsg.OFFLINE_ROUNTING_KEY, req);
				if (null != resp) {
					ret.setError_code(PortalErrorCodes.ERROR_CODE_OK);
					ret.setError_msg("");
					ret.setRespData(resp);
				} else {
					ret.setError_code(PortalErrorCodes.ERROR_CODE_ERROR);
					ret.setError_msg("No data was recieved!");
				}
			}
		}
		StringBuilder logInfo = new StringBuilder();
		logInfo.append("OffLine Req [ storeId=" + storeId + " ipList=" + WifiUtils.toJson(ipList) + "] \n");
		logInfo.append("OffLine Rep [ " + WifiUtils.toJson(ret) + "] ");
		log.info(logInfo.toString());
		return ret;
	}

	/**
	 * 打印用户切换后的信息<br>
	 * 
	 * @param userMac
	 * @param storeId
	 * @param ssid
	 */
	private void logSwitchUserInfo(OnlineUser onlineUser, String userMac, String ip, Long storeId, String ssid,
			Integer userType) {
		StringBuilder sbuilder = new StringBuilder();
		sbuilder.append("=== User online  ===\n");
		sbuilder.append("Old " + onlineUser + "\n");
		sbuilder.append("New Auth Info: [ usermac=" + userMac + " userIp:" + ip + " storeId= " + storeId + " ssid= "
				+ ssid + "userType:" + userType + " ]\n");
		sbuilder.append("\n");
		if (log.isInfoEnabled()) {
			log.info(sbuilder.toString());
		}
	}

	// getter and setter
	public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	public CacheApiMgr getCacheApiMgr() {
		return cacheApiMgr;
	}

	public void setCacheApiMgr(CacheApiMgr cacheApiMgr) {
		this.cacheApiMgr = cacheApiMgr;
	}

	public ThirdPartyAuthMgr getThirdPartyAuthMgr() {
		return thirdPartyAuthMgr;
	}

	public void setThirdPartyAuthMgr(ThirdPartyAuthMgr thirdPartyAuthMgr) {
		this.thirdPartyAuthMgr = thirdPartyAuthMgr;
	}

	public Integer getLimitPassTime() {
		return limitPassTime;
	}

	public void setLimitPassTime(Integer limitPassTime) {
		this.limitPassTime = limitPassTime;
	}

	public OnlineUserMgr getOnlineUserMgr() {
		return onlineUserMgr;
	}

	public void setOnlineUserMgr(OnlineUserMgr onlineUserMgr) {
		this.onlineUserMgr = onlineUserMgr;
	}

	public ShopMgr getShopMgr() {
		return shopMgr;
	}

	public void setShopMgr(ShopMgr shopMgr) {
		this.shopMgr = shopMgr;
	}
}
