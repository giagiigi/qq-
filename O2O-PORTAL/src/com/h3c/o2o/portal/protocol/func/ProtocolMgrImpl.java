/*
 * Copyright (c) 2015, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC V300R002
 * Module Name :
 * Date Created: 2015-6-26
 * Creator     : j09980
 * Description :
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 * YYYY-MM-DD  zhangshan        XXXX project, new code file.
 *
 *------------------------------------------------------------------------------
 */
package com.h3c.o2o.portal.protocol.func;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.h3c.o2o.portal.PortalErrorCodes;
import com.h3c.o2o.portal.PortalException;
import com.h3c.o2o.portal.authuser.func.AuthUserMgr;
import com.h3c.o2o.portal.cache.api.CacheApiMgr;
import com.h3c.o2o.portal.cache.key.RegistUserCacheKey;
import com.h3c.o2o.portal.common.CommonUtils;
import com.h3c.o2o.portal.common.FuncUtil;
import com.h3c.o2o.portal.common.HttpOrHttpsMgr;
import com.h3c.o2o.portal.entity.AccessDetail;
import com.h3c.o2o.portal.entity.AuthCfg;
import com.h3c.o2o.portal.entity.AuthParam;
import com.h3c.o2o.portal.entity.AuthUser;
import com.h3c.o2o.portal.entity.OnlineUser;
import com.h3c.o2o.portal.entity.PublishMng;
import com.h3c.o2o.portal.entity.RegistUser;
import com.h3c.o2o.portal.entity.ThemePage;
import com.h3c.o2o.portal.entity.ThemeTemplate;
import com.h3c.o2o.portal.entity.UserAgent;
import com.h3c.o2o.portal.haihaiconfig.func.HaihaiAppMgr;
import com.h3c.o2o.portal.login.entity.LoginReq;
import com.h3c.o2o.portal.login.entity.WeixinConnectWifiPara;
import com.h3c.o2o.portal.login.func.LoginMgr;
import com.h3c.o2o.portal.protocol.dao.ProtocolDao;
import com.h3c.o2o.portal.protocol.entity.AccessToken;
import com.h3c.o2o.portal.protocol.entity.CodeInfo;
import com.h3c.o2o.portal.protocol.entity.LoginParam;
import com.h3c.o2o.portal.protocol.entity.Offline;
import com.h3c.o2o.portal.protocol.entity.Online;
import com.h3c.o2o.portal.protocol.entity.OnlineUserInfo;
import com.h3c.o2o.portal.protocol.entity.State;
import com.h3c.o2o.portal.protocol.entity.TokenInfo;
import com.h3c.o2o.portal.protocol.entity.TokenReq;
import com.h3c.o2o.portal.protocol.entity.TpLoginInfo;
import com.h3c.o2o.portal.protocol.entity.User;
import com.h3c.o2o.portal.protocol.entity.UserInfo;
import com.h3c.o2o.portal.protocol.entity.UserStatus;
import com.h3c.o2o.portal.user.func.AccessUserDetailMgr;
import com.h3c.o2o.portal.user.func.OnlineUserMgr;
import com.h3c.o2o.portal.user.func.RegistUserMgr;
import com.h3c.o2o.portal.useragent.func.UserAgentMgr;
import com.h3c.o2o.portal.util.WifiUtils;
import com.h3c.oasis.o2oserver.bootstrap.ServerContext;
import com.h3c.oasis.o2oserver.util.StringManager;

/**
 * O2O协议处理实现类
 *
 * @author j09980
 */
public class ProtocolMgrImpl implements ProtocolMgr {

	/**
	 * 唯一实例--在servlet中使用
	 */
	private static ProtocolMgrImpl INSTANCE;

	/** 记录运行时日志对象。 */
	private Log log = LogFactory.getLog(getClass());

	private ProtocolDao protocolDao;

	private UserAgentMgr userAgentMgr;

	private TokenMgr tokenMgr;

	/** 资源访问对象。 */
	private static StringManager sm = StringManager.getManager("com.h3c.o2o.portal");

	private RegistUserMgr userMgr;

	private OnlineUserMgr onlineUserMgr;

	private AccessUserDetailMgr accessUserDetailMgr;

	private HttpOrHttpsMgr httpOrHttps;

	@SuppressWarnings("unused")
	private HaihaiAppMgr haihaiAppMgr;

	/** 数据缓存接口 */
	private CacheApiMgr cacheApiMgr;

	private OfflineCallback callback;

	private LoginMgr loginMgr;

	/**
	 * 获取第三方认证用户信息
	 */
	private AuthUserMgr authUserMgr;
	
	/**
	 * 如果没有启用一键认证，是否跳过一键认证页面
	 */
	private Boolean skipQuickLogin = true;
	
	private String skipQuickLoginInAuth = "1";
	
	/**
	 * 如果仅仅只启用了微信连WiFi，是否直接跳转到拉起微信客户端页面
	 */
	private Boolean skipLoginIfOnlyWxWifi = false;
	
	private String skipLoginIfOnlyWxWifiInAuth = "1";
	
	/** 微信公众号认证路径：写在spring配置文件中 */
	private String weChatAuthUrl = "/weixin/redirect";
	
	/**
	 * 获取唯一实例
	 *
	 * @return 实例
	 */
	public static final ProtocolMgr get() {
		return INSTANCE;
	}

	/**
	 * 初始化
	 */
	public void init() {
		INSTANCE = this;
	}

	/**
	 * 获取URLencode之后的State
	 * 
	 * @param storeId
	 * @param ownerId
	 * @param accountId
	 * @param ssid
	 * @param userMac
	 * @return
	 */
	private String getStateEncodedStr(Long storeId, Long ownerId, Long accountId, String ssid, String userMac,
			String userIp) {
		State st = new State();
		st.setAccountId(accountId);
		st.setStoreId(storeId);
		st.setOwnerId(ownerId);
		st.setSsid(ssid);
		st.setUsermac(userMac);
		st.setUserIp(null != userIp ? userIp.trim() : "");
		try {
			String stStr = State.toState(st);
			if(null != stStr && stStr.length() > 128){
				log.warn("State is toooo long! state = " + st);
			}
			return stStr;
		} catch (Exception e) {
			log.warn(null, e);
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String loginRedirect(LoginParam param, String ua) {
		log.info("ua:" + ua);
		log.info(param);

		// 获取storeid(实际上保存在nas_id参数中)
		Long storeId = param.getNasId();

		// 无感知时长
		String noSensationTimeStr = cacheApiMgr.getConfigureParam(storeId, param.getSsid(), AuthCfg.NO_SENSATION_TIME);
		if (log.isDebugEnabled()) {
			log.debug("noSensationTime: " + noSensationTimeStr);
		}

		// 获取URL
		String url = null;
		// 先进行无感知登录
		if (noSensationTimeStr != null && !noSensationTimeStr.trim().isEmpty()) {
			// 如已被拉黑，推送错误页面
			url = noSensationLogin(noSensationTimeStr, storeId, param);
		}

		// 模板ID
		ThemeTemplate template = cacheApiMgr.getThemeTemplate(storeId, param.getSsid(), null);
		Long templateId = template.getId();
		log.info("templateId" + templateId);
		// 如果无感知登录失败
		if (url == null) {
			// 如果是微信请求，进行微信认证
			if (isWeChatRequest(ua)) {
				String weixinServerDomain = FuncUtil.getWeixinServerDomain();
				PublishMng pubMng = cacheApiMgr.getPublishMng(storeId, param.getSsid());
				checkAuthCfg4WechatOauth(storeId, param.getSsid());
				url = httpOrHttps.getHttpOrHttps() + "://" + weixinServerDomain + weChatAuthUrl + "?state="
						+ getStateEncodedStr(storeId, pubMng.getOwnerId(), pubMng.getWeixinAccountId(), param.getSsid(),
								param.getUsermac(), param.getUserip());
			} else {
				// 跳转至登录页面
				url = getRedirectUrl(template, param);
				tokenMgr.removeUserUrl(param.getUsermac());
			}
		} else {
			// 无感知登录不显示任何模板页面，认证成功后跳转回原来用户访问的页面
			tokenMgr.putUserUrl(param.getUsermac(), param.getUserurl());
		}

		log.info("url:" + url);

		tokenMgr.putRedirectUri(storeId, param.getRedirect_uri());
		tokenMgr.putTemplateId(param.getUsermac(), templateId);
		if (ua != null) {
			tokenMgr.putUserAgent(param.getUsermac(), ua);
		}
		tokenMgr.putSsid(param.getUsermac(), param.getSsid());
		return url;
	}

	/**
	 * 微信Oauth认证校验发布配置<br>
	 * 要求：需要启用微信Oauth认证<br>
	 * 
	 * @param storeId
	 * @param ssid
	 */
	private void checkAuthCfg4WechatOauth(Long storeId, String ssid) throws PortalException {
		AuthCfg authCfg = cacheApiMgr.getAuthCfg(storeId, ssid);
		if (authCfg.getIsEnableWinxin().intValue() != 1) {
			// 微信Oauth认证未启用
			throw new PortalException(PortalErrorCodes.WECHAT_OAUTH_DISABLED, sm.getString("errorCode.60020"));
		}
	}

	/**
	 * 是否仅仅启用了微信连Wifi
	 * 
	 * @param param
	 * @return
	 */
	public boolean isOnlyEnabledWxWifi(LoginParam param) {
		AuthCfg authCfg = cacheApiMgr.getAuthCfg(param.getNasId(), param.getSsid());
		return isOnlyEnabledWxWifi(authCfg);
	}

	/**
	 * 是否仅仅启用了微信连Wifi
	 * 
	 * @param authCfg
	 * @return
	 */
	private boolean isOnlyEnabledWxWifi(AuthCfg authCfg) {
		Integer enabled = Integer.valueOf(1);
		return null != authCfg && enabled.equals(authCfg.getIsWeixinConnectWifi())
				&& !enabled.equals(authCfg.getIsEnableWinxin()) && !enabled.equals(authCfg.getIsEnableAccount())
				&& !enabled.equals(authCfg.getIsEnableAli()) && !enabled.equals(authCfg.getIsEnableQQ())
				&& !enabled.equals(authCfg.getIsEnableSms()) && !enabled.equals(authCfg.getIsEnableWinxin());
	}

	/**
	 * 组装一个LoginReq
	 * 
	 * @param param
	 * @param template
	 * @return
	 */
	private LoginReq packLoginInfo(LoginParam param, ThemeTemplate template) {
		LoginReq req = new LoginReq();
		req.setBssid("");
		req.setNasId(param.getNasId());
		req.setRedirect_uri(param.getRedirect_uri());
		req.setSsid(param.getSsid());
		req.setTemplateId(null != template ? template.getId() : 0L);
		req.setUserip(param.getUserip());
		req.setUsermac(param.getUsermac());
		req.setUserurl(param.getUserurl());
		return req;
	}

	/**
	 * 获取微信连Wifi的登录页面URL
	 * 
	 * @param req
	 * @return
	 */
	private String getWifiLoginUrl(LoginReq req) {
		String prefix = "";
		int port = FuncUtil.getPortalServerPort();
		if ("https".equals(httpOrHttps.getHttpOrHttps())) {
			prefix = "https://" + FuncUtil.getPortalDomain() + (443 != port ? ":" + port : "");
		} else {
			prefix = "http://" + FuncUtil.getPortalDomain() + (80 != port ? ":" + port : "");
		}
		String suffix = "/portal/wifilogin.jsp?" + "nas_id=" + req.getNasId() + "&redirect_uri=" + req.getRedirect_uri()
				+ "&ssid=" + WifiUtils.safeEncode(req.getSsid()) + "&templateId=" + req.getTemplateId() + "&userip="
				+ req.getUserip() + "&usermac=" + req.getUsermac() + "&userurl=" + req.getUserurl();
		return prefix + suffix;
	}

	/**
	 * 获取登录页面地址
	 * 
	 * @param template
	 * @param param
	 * @return
	 */
	private String getRedirectUrl(ThemeTemplate template, LoginParam param) {
		String url = null;
		AuthCfg authCfg = cacheApiMgr.getAuthCfg(param.getNasId(), param.getSsid());
		List<AuthParam> authParamList = authCfg.getAuthParamList();
		boolean skipQuickLogin = getSkipQuickLogin();
		boolean skipLoginIfOnlyWxWifi = getSkipLoginIfOnlyWxWifi();
		try {
			for (AuthParam authParam : authParamList) {
				if("SKIP_QUICK_LOGIN".equals(authParam.getAuthParamName())){
					if(skipQuickLoginInAuth.equals(authParam.getAuthParamValue())){
						skipQuickLogin = true;
					}else{
						skipQuickLogin = false;
					}
				}
				if("SKIP_LOGIN_ONLY_WXWIFI".equals(authParam.getAuthParamName())){
					if(skipLoginIfOnlyWxWifiInAuth.equals(authParam.getAuthParamValue())){
						skipLoginIfOnlyWxWifi = true;
					}else{
						skipLoginIfOnlyWxWifi = false;
					}
				}
			}
		} catch (Exception e) {
			log.debug("get skip param error we will use default param");
		}
		if (skipQuickLogin && AuthCfg.AUTH_TYPE_QUICKlOGIN != authCfg.getAuthType()) {
			// 如果没有启用一键登录，则直接跳转到登录页面
			url = cacheApiMgr.getThemePageURI(template, ThemePage.PAGE_TYPE_LOGIN);
			// 如果仅仅启用了微信连Wifi，则跳转到单独的微信连Wifi登录页
			if (skipLoginIfOnlyWxWifi && isOnlyEnabledWxWifi(authCfg)) {
				LoginReq loginInfo = packLoginInfo(param, template);
				return getWifiLoginUrl(loginInfo);
			}
		} else {
			// 如果启用一键登录跳转至点我上网页面
			url = cacheApiMgr.getThemePageURI(template, ThemePage.PAGE_TYPE_QUICKLOGIN);
		}
		url += "?templateId=" + template.getId() + param.toParamString();
		return url;
	}

	/**
	 * 获取微信连Wifi参数
	 * 
	 * @param loginInfo
	 * @return
	 */
	public WeixinConnectWifiPara getWxWifiPara(LoginReq loginInfo) {
		AuthCfg authCfg = cacheApiMgr.getAuthCfg(loginInfo.getNasId(), loginInfo.getSsid());
		return this.getLoginMgr().getWxWifiPara(authCfg, loginInfo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.h3c.o2o.portal.protocol.func.ProtocolMgr#appRedirect(com.h3c.o2o.
	 * portal.protocol.entity.LoginParam, java.lang.String)
	 */
	@Override
	public String appRedirect(LoginParam param, String ua) {
		// 获取storeid(实际上保存在nas_id参数中)
		Long storeId = param.getNasId();

		// 生成缓存key
		RegistUserCacheKey key = new RegistUserCacheKey(storeId, param.getUsermac(), RegistUser.USER_TYPE_LZ_APP);
		// 查询用户是否存在
		RegistUser user = cacheApiMgr.getRegistUserCacheMgr().get(key);

		if (user == null) {
			Long ownerId = protocolDao.queryUserId(storeId);
			if (ownerId == null) {
				throw new PortalException(PortalErrorCodes.NOT_EXISTS_STORE, sm.getString("errorCode.71006"));
			}
			// 如果不存在， 创建用户
			user = createAppUser(param, key, ownerId);
		}
		// 生成授权码
		String code = tokenMgr.generateCode(param.getUsermac(), param.getUserip(), param.getNasId(), param.getSsid(),
				RegistUser.USER_TYPE_LZ_APP, user);

		tokenMgr.putRedirectUri(storeId, param.getRedirect_uri());
		if (ua != null) {
			tokenMgr.putUserAgent(param.getUsermac(), ua);
		}
		tokenMgr.putSsid(param.getUsermac(), param.getSsid());
		return FuncUtil.getLoginRedirectURI(param.getRedirect_uri(), code, param.getUserip(), false );
	}

	/**
	 * 
	 * 创建绿洲app用户
	 *
	 * @param param
	 * @param key
	 * @param ownerId
	 * @return
	 */
	private RegistUser createAppUser(LoginParam param, RegistUserCacheKey key, Long ownerId) {
		RegistUser user = new RegistUser();
		user.setStoreId(param.getNasId());
		user.setUserName(param.getUsermac());
		user.setUserPassword(CommonUtils.encryptData("h3clzpt"));
		user.setUserType(RegistUser.USER_TYPE_LZ_APP);
		user.setLoginTime(System.currentTimeMillis());
		user.setOwnerId(ownerId);

		// 保存
		userMgr.saveRegistUser(user);
		cacheApiMgr.getRegistUserCacheMgr().put(key, user);
		return user;
	}

	/**
	 * 判断请求是否来自微信浏览器
	 * 
	 * @param ua
	 *            user agent
	 * @return
	 */
	private boolean isWeChatRequest(String ua) {
		if (StringUtils.isNotBlank(ua)) {
			return ua.matches("^.*MicroMessenger.*$");
		} else {
			return false;
		}
	}

	/**
	 * 无感知登录
	 * 
	 * @param noSensationTimeStr
	 *            无感知时间（天）
	 * @param storeId
	 *            店铺ID
	 * @param param
	 *            登录参数
	 * @return 登录成功页面url，如果不进行无感知登录返回null
	 */
	private String noSensationLogin(String noSensationTimeStr, Long storeId, LoginParam param) {
		Integer noSensationTime = Integer.valueOf(noSensationTimeStr.trim());
		// 如果不为空
		if (noSensationTime != null && noSensationTime > 0) {
			Calendar c = Calendar.getInstance();
			// 设置无感知时长为自然天，
			c.add(Calendar.DAY_OF_YEAR, -noSensationTime);
			c.set(Calendar.HOUR_OF_DAY, 23);
			c.set(Calendar.MINUTE, 59);
			c.set(Calendar.SECOND, 59);
			c.set(Calendar.MILLISECOND, 999);

			// 1、固定账号是可以删除的，从接入明细中查询出用户后，如果用户不存在，走正常登陆流程
			// 2、第三方用户登录时，注册用户表中保存的信息是旧的，暂不获取最新信息
			// 3、如果使用同一个MAC地址，从接入明细表中查询出多条记录，使用最近一次接入的记录
			AccessDetail accessDetail = accessUserDetailMgr.queryAccessDetail(storeId, param.getSsid(),
					param.getUsermac(), c.getTimeInMillis());
			if (accessDetail != null) {
				RegistUser registUser = cacheApiMgr.getRegistUserCacheMgr().get(new RegistUserCacheKey(
						accessDetail.getStoreId(), accessDetail.getUserName(), accessDetail.getUserType()));
				if (registUser != null) {
					// 判断是否为黑名单用户
					if (cacheApiMgr.isBlackUser(registUser)) {
						throw new PortalException(PortalErrorCodes.NO_SENSE_LOGIN_BLACK_USER,
								sm.getString("errorCode.60012"));
					}
					// 无感知登录时需要更新用户场所ID，用户MAC和IP地址
					registUser.setStoreId(param.getNasId());
					registUser.setUserMac(param.getUsermac());
					registUser.setUserIp(param.getUserip());
					String code = tokenMgr.generateCode(param.getUsermac(), param.getUserip(), param.getNasId(),
							param.getSsid(), registUser.getUserType(), registUser);
					return FuncUtil.getLoginRedirectURI(param.getRedirect_uri(), code, param.getUserip(),
							RegistUser.USER_TYPE_WX_WIFI == registUser.getUserType().intValue());
				}
			}
		}
		return null;
	}

	/**
	 * 第三方登录认证
	 * 
	 * @param openId
	 * @param type
	 *            第三方认证类型，与注册用户类型对应：4、支付宝认证；5、QQ认证；6、微信公众号认证
	 * @param storeId
	 * @param extInfo
	 */
	@Override
	public String tpLoginVerify(TpLoginInfo info) {
		if (log.isDebugEnabled()) {
			log.debug("login paras: " + info);
		}
		RegistUser user = cacheApiMgr.getRegistUserCacheMgr()
				.get(new RegistUserCacheKey(info.getStoreId(), info.getOpenId(), info.getType()));
		// 判断是否为黑名单用户
		if (null != user && cacheApiMgr.isBlackUser(user)) {
			throw new PortalException(PortalErrorCodes.BLACK_USER, sm.getString("errorCode.60003"));
		}
		// 创建code
		String code = tokenMgr.generateCode(info.getUserMac(), info.getUserIp(), info.getStoreId(), info.getSsid(),
				info.getType(), null);
		tokenMgr.putTpLoginInfo(code, info);

		// 获取重定向URI
		String redirectURIHeader = null;
		boolean isWxWiFi = RegistUser.USER_TYPE_WX_WIFI == info.getType().intValue();
		if (isWxWiFi) {
			redirectURIHeader = getWxWiFiRedirectUri(info, code);
		} else {
			redirectURIHeader = tokenMgr.getRedirectUri(info.getStoreId());
		}
		String redirectURL = FuncUtil.getLoginRedirectURI(redirectURIHeader, code, info.getUserIp(), isWxWiFi);
		if (StringUtils.isBlank(redirectURIHeader)) {
			// 重定向URI不能为空
			throw new PortalException(PortalErrorCodes.REDIRECT_URI_NULL, sm.getString("errorCode.60013"));
		} else if (log.isDebugEnabled()) {
			log.debug("Redirect URL: " + redirectURL);
		}

		return redirectURL;
	}

	/**
	 * 创建微信连WiFi重定向URi
	 * 
	 * @return
	 */
	private String getWxWiFiRedirectUri(TpLoginInfo info, String code) {
		String redirectURIHeader = null;
		try {
			tokenMgr.putSsid(URLDecoder.decode(info.getUserMac(), "UTF-8"), URLDecoder.decode(info.getSsid(), "UTF-8"));
			redirectURIHeader = URLDecoder.decode(info.getRedirect_uri(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.warn("Unsupported Encoding", e);
		}
		if (StringUtils.isBlank(redirectURIHeader)) {
			// 重定向URI不能为空
			throw new PortalException(PortalErrorCodes.REDIRECT_URI_NULL, sm.getString("errorCode.60013"));
		}
		return redirectURIHeader;
	}
	
	/**
	 * 创建注册用户
	 * 
	 * @return 注册用户，用于增加在线用户和接入明细
	 */
	private RegistUser createTPUser(TpLoginInfo tpLoginInfo, RegistUserCacheKey key) {
		RegistUser user = new RegistUser();
		user.setStoreId(tpLoginInfo.getStoreId());
		user.setUserName(tpLoginInfo.getOpenId());
		// 密码为“h3c绿洲平台”的简写
		user.setUserPassword(CommonUtils.encryptData("h3clzpt"));
		user.setUserTokenId(tpLoginInfo.getOpenId());
		user.setUserExtInfo(tpLoginInfo.getExtInfo());

		user.setUserType(tpLoginInfo.getType());
		user.setLoginTime(System.currentTimeMillis());
		user.setOwnerId(tpLoginInfo.getOwnerId());
		
		// 第三方认证带入的IP和MAC地址
		user.setUserIp(tpLoginInfo.getUserIp());
		user.setUserMac(tpLoginInfo.getUserMac());

		// 保存
		userMgr.saveRegistUser(user);
		cacheApiMgr.getRegistUserCacheMgr().put(key, user);
		return user;
	}

	/**
	 * 创建或修改第三方用户
	 * 
	 * @param tpLoginInfo
	 *            用户信息
	 * @param loginType
	 *            用户类型
	 * @return
	 */
	private RegistUser createOrUpdateTPUser(TpLoginInfo tpLoginInfo) {
		// 生成缓存key
		RegistUserCacheKey key = new RegistUserCacheKey(tpLoginInfo.getStoreId(), tpLoginInfo.getOpenId(),
				tpLoginInfo.getType());
		// 查询用户
		RegistUser user = cacheApiMgr.getRegistUserCacheMgr().get(key);
		// 若用户不存在，创建用户；否则修改用户扩展信息
		if (user == null) {
			user = createTPUser(tpLoginInfo, key);
		} else {
			// 更新用户IP和MAC
			user.setUserMac(tpLoginInfo.getUserMac());
			user.setUserIp(tpLoginInfo.getUserIp());
			user.setUserExtInfo(tpLoginInfo.getExtInfo());
			userMgr.updateRegistUser(user);
			cacheApiMgr.getRegistUserCacheMgr().put(key, user);
		}
		return user;
	}

	/**
	 * 保存或者更新在线用户
	 * 
	 * @param user
	 *            注册用户
	 * @param param
	 * @param loginType
	 */
	private void saveOrUpdateOnlineUser(RegistUser user, long accessStartTime, String remoteHostIp, long ownerId) {
		// 插入在线表
		OnlineUser onlineUser = new OnlineUser();
		onlineUser.setStoreId(user.getStoreId());
		onlineUser.setUserId(user.getId());
		onlineUser.setOwnerId(ownerId);
		onlineUser.setUserName(user.getUserName());
		onlineUser.setUserType(user.getUserType());
		onlineUser.setAccessStartTime(accessStartTime);
		onlineUser.setAccessAcIp(FuncUtil.convertHostIpToLong(remoteHostIp));
		onlineUser.setAccessSsid(tokenMgr.getSsid(user.getUserMac()));
		if (log.isDebugEnabled()) {
			log.debug("User login ip: " + user.getUserIp());
			log.debug("User login ip(converted): " + FuncUtil.convertHostIpToLong(user.getUserIp()));
		}
		onlineUser.setUserIp(FuncUtil.convertHostIpToLong(user.getUserIp()));
		onlineUser.setUserMac(user.getUserMac());
		onlineUser.setDevManufacturer(user.getDevManufacturer());
		onlineUser.setDevOsType(user.getDevOsType());
		onlineUser.setMobileNo(user.getPhoneNumber());

		// 保存或者更新在线用户
		OnlineUser oldOnLineUser = cacheApiMgr.getOnlineUserCacheMgr().get(user.getUserMac());
		if (null != oldOnLineUser) {
			// 如果已经登录，则直接更新在线数据
			onlineUser.setId(oldOnLineUser.getId());
			onlineUserMgr.updateOnlineUser(onlineUser);
			cacheApiMgr.getOnlineUserCacheMgr().put(user.getUserMac(), onlineUser);
			if (log.isDebugEnabled()) {
				StringBuilder sbuilder = new StringBuilder();
				sbuilder.append("Update old onLineUser:\n");
				sbuilder.append("Old onLineUser: " + oldOnLineUser + "\n");
				sbuilder.append("New onLineUser: " + onlineUser);
				log.debug(sbuilder.toString());
			}
		} else {
			// 保存
			onlineUserMgr.saveOnlineUser(onlineUser);
		}

		// 抛弃ssid缓存
		tokenMgr.removeSsid(user.getUserMac());
		cacheApiMgr.getOnlineUserCacheMgr().put(user.getUserMac(), onlineUser);
	}

	/**
	 * {@inheritDoc}。 生成token后，抛弃code，用户信息只能通过token获取
	 */
	@Override
	public AccessToken getAccessToken(TokenReq tokenReq) {
		CodeInfo info = tokenMgr.getCodeInfo(tokenReq.getCode(), true);
		// 校验code有效期
		if (info == null) {
			if (log.isDebugEnabled()) {
				log.debug("code not exist! code=" + tokenReq.getCode());
			}
			// 抛出异常
			throw new PortalException(PortalErrorCodes.CODE_EXPIRE, sm.getString("errorCode.60001"));
		} else if (info != null
				&& ((System.currentTimeMillis() - info.getCreateTime())
						/ 1000) > info.getExpire_in()) {
			if (log.isDebugEnabled()) {
				log.debug("code is expired! code=" + tokenReq.getCode());
			}
			// 抛弃code
			tokenMgr.removeCode(tokenReq.getCode());
			// 抛出异常
			throw new PortalException(PortalErrorCodes.CODE_EXPIRE, sm.getString("errorCode.60001"));
		}

		RegistUser user = null;
		if (info.getType() == RegistUser.USER_TYPE_ONEBTN || info.getType() == RegistUser.USER_TYPE_MESSAGE
				|| info.getType() == RegistUser.USER_TYPE_LZ_APP
				|| (info.getType() == RegistUser.USER_TYPE_ACCOUNT && null != info.getRegistUser())
				|| tokenMgr.getUserUrl(tokenReq.getUserMac()) != null) {
			// 一键登录、app认证、短信认证和固定账号认证以及无感知认证在获取code时已生成user，可直接取得
			user = info.getRegistUser();
		} else {
			// QQ、微信认证、第三方认证（类型为固定账号，但并未创建用户）
			TpLoginInfo tpLoginInfo = tokenMgr.getTpLoginInfo(tokenReq.getCode(), true);
			// 创建或修改用户
			user = createOrUpdateTPUser(tpLoginInfo);
		}
		// 临时属性，生成在线用户和接入明细时使用
		user.setUserMac(tokenReq.getUserMac());
		user.setUserIp(tokenReq.getUserIp());
		UserAgent agent = userAgentMgr.parseUserAgent(tokenMgr.getUserAgent(user.getUserMac()));
		user.setDevManufacturer(agent.getTerminalVendor());
		user.setDevOsType(agent.getTerminalOs());

		// 使用后抛弃code
		tokenMgr.removeCode(tokenReq.getCode());
		// 抛弃UserAgent缓存
		tokenMgr.removeUserAgent(user.getUserMac());
		// 抛弃TpLoginInfo缓存
		tokenMgr.removeTpLoginInfo(tokenReq.getCode());
		// 返回token
		return tokenMgr.generateToken(user);
	}

	/**
	 * 获取用户昵称，如果可以的话<br>
	 * 某些第三方认证能够拿到用户昵称<br>
	 * 
	 * @param regiestUser
	 */
	private String getSutiableUserName(RegistUser regiestUser) {
		Assert.notNull(regiestUser, "A RegistUser is required!");
		String userName = regiestUser.getUserName();
		boolean isThirdPartAuth = RegistUser.USER_TYPE_WX_WIFI == regiestUser.getUserType().intValue()
				|| RegistUser.USER_TYPE_WECHATPUBLICER == regiestUser.getUserType().intValue();
		if (isThirdPartAuth) {
			List<AuthUser> authUsers = getAuthUserMgr().queryByOpenIdAndUserType(regiestUser.getUserName(), null);
			if (null != authUsers && authUsers.size() > 0) {
				String nickName = authUsers.get(0).getNickname();
				if (StringUtils.isNotBlank(nickName)) {
					// 有昵称
					userName = nickName;
				} else {
					// 没有关注公众号，无法获取昵称
					userName = regiestUser.getUserMac();
				}
			} else {
				// 没有第三方认证用户信息
				userName = regiestUser.getUserMac();
			}
		} 
		return userName;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserInfo getUserInfo(String token, String remoteHostIp) {
		UserInfo result = new UserInfo();
		// 校验并获得token
		TokenInfo tokenInfo = validateToken(token);
		Long storeId = tokenInfo.getRegistUser().getStoreId();
		// 取得用户信息
		RegistUser regiestUser = tokenInfo.getRegistUser();
		result.setUsername(getSutiableUserName(regiestUser));
		result.setAuth_type(regiestUser.getUserType());
		
		// 设置剩余在线时长（0表示不限）
		boolean isThirdParty = regiestUser.getUserType().intValue() == RegistUser.USER_TYPE_ACCOUNT
				&& regiestUser.getUserTokenId() != null;
		String ssid = tokenMgr.getSsid(regiestUser.getUserMac());
		// 如果是绿洲第三方认证
		if (isThirdParty) {
			result.setSessionTimeOut(tokenMgr.getThirdPartyParam(regiestUser.getId()).getSessionTimeout());
			result.setLogin_url(tokenMgr.getThirdPartyParam(regiestUser.getId()).getSuccessUrl());
			// 抛弃缓存
			tokenMgr.removeThirdPartyParam(regiestUser.getId());
		} else {
			String configueTime = cacheApiMgr.getConfigureParam(storeId, ssid, AuthCfg.STORE_CONFIGUE_TIME);
			if (configueTime != null && !configueTime.equals("0")) {
				result.setSessionTimeOut(Integer.valueOf(configueTime));
			} else {
				result.setSessionTimeOut(0);
			}

			// 认证成功页面路径
			result.setLogin_url(getLoginSuccessUri(storeId, ssid, regiestUser.getUserMac()));
		}
		// 闲置切断时长（分钟）、闲置切断流量（字节）
		String cutTime = cacheApiMgr.getConfigureParam(storeId, ssid, AuthCfg.IDLE_CUT_TIME);
		if (log.isDebugEnabled()) {
			log.debug("Store param: IDLE_CUT_TIME ~ " + cutTime);
		}
		result.setIdleCutTime(Integer.valueOf(cutTime));
		String cutFlow = cacheApiMgr.getConfigureParam(storeId, ssid, AuthCfg.IDLE_CUT_FLOW);
		if (log.isDebugEnabled()) {
			log.debug("Store param: IDLE_CUT_FLOW ~ " + cutFlow);
		}
		result.setIdleCutFlow(Integer.valueOf(cutFlow));

		// 创建在线用户
		createOnlineUser(regiestUser, remoteHostIp, storeId, ssid);
		return result;
	}

	/**
	 * 
	 * 校验token
	 *
	 * @param token
	 * @return
	 */
	private TokenInfo validateToken(String token) {
		TokenInfo tokenInfo = tokenMgr.getTokenInfo(token, true);
		if (tokenInfo == null) {
			if (log.isDebugEnabled()) {
				log.debug("Token is cleared: " + token);
			}
			throw new PortalException(PortalErrorCodes.TOKEN_EXPIRE, sm.getString("errorCode.60002"));
		}
		// 判断token是否有效
		if (((System.currentTimeMillis() - tokenInfo.getTokenCreateTime()) / 1000) > tokenInfo.getAccessToken()
				.getExpire_in()) {
			if (log.isDebugEnabled()) {
				log.debug("Token is expired: " + token);
			}
			// 抛弃token
			tokenMgr.removeToken(token);
			// 抛出异常
			throw new PortalException(PortalErrorCodes.TOKEN_EXPIRE, sm.getString("errorCode.60002"));
		}
		return tokenInfo;
	}

	/**
	 * 
	 * 创建在线用户
	 *
	 * @param regiestUser
	 *            注册用户
	 * @param remoteHostIp
	 *            ap设备ip地址
	 * @param storeId
	 *            场所id
	 */
	private void createOnlineUser(RegistUser regiestUser, String remoteHostIp, Long storeId, String ssid) {
		Long ownerId = null;
		if (regiestUser.getUserType() == RegistUser.USER_TYPE_LZ_APP) {
			ownerId = protocolDao.queryUserId(storeId);
		} else {
			ownerId = cacheApiMgr.getOwnerId(storeId, ssid);
		}
		// 如果没有登录,或者登录用户的登录方式，场所以及SSID有不相同的，则插入在线表
		saveOrUpdateOnlineUser(regiestUser, System.currentTimeMillis(), remoteHostIp, ownerId);
	}

	/**
	 * 获取登录成功页面。无感知登录时，返回用户认证前访问的页面。否则，返回登录成功 模板页面。
	 * 
	 * @param storeId
	 *            店铺ID
	 * @param ssid
	 *            接入ssid
	 * @param userMac
	 *            终端MAC地址
	 * @return
	 */
	private String getLoginSuccessUri(long storeId, String ssid, String userMac) {
		// 用户重定向前访问的页面
		String resultUri = tokenMgr.getUserUrl(userMac);
		// 如果没有缓存userUri，说明不是无感知登录返回登录成功模板页面
		// 如果是portal页面，返回登录成功模板页面
		if (resultUri == null || resultUri.matches("^.*/portal/theme/\\d*/.*\\.jsp.*$")) {
			Long templateId = tokenMgr.getTemplateId(userMac);

			ThemeTemplate template = cacheApiMgr.getThemeTemplate(storeId, ssid, templateId);
			// 如果模板发生变化，页面参数也跟着一起变化
			templateId = template.getId();
			StringBuilder builder;
			try {
				builder = new StringBuilder().append(httpOrHttps.getHttpOrHttps() + "://")
						.append(FuncUtil.getPortalDomain()).append(":").append(FuncUtil.getPortalServerPort())
						.append("/portal/")
						.append(cacheApiMgr.getThemePageURI(template, ThemePage.PAGE_TYPE_LOGINSUCCESS))
						.append("?templateId=").append(templateId).append("&userMac=").append(userMac)
						.append("&nas_id=").append(storeId).append("&ssid=")
						.append(URLEncoder.encode(URLEncoder.encode(ssid, "utf-8"), "utf-8"));
			} catch (UnsupportedEncodingException e) {
				throw new PortalException("Unsupported encode.");
			}
			resultUri = builder.toString();
			// 使用后抛弃
			tokenMgr.removeTemplateId(userMac);
		} else {
			// 返回用户认证前访问的页面
		}
		tokenMgr.removeUserUrl(userMac);
		return resultUri;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserStatus uploadOnlines(OnlineUserInfo onlineUserInfo) {
		UserStatus status = new UserStatus();
		if (null == onlineUserInfo) {
			return status;
		}
		for (User user : onlineUserInfo.getUsers()) {
			// 校验用户是否存在于在线表
			OnlineUser onlineUser = cacheApiMgr.getOnlineUserCacheMgr().get(user.getMac());

			if (onlineUser != null) {
				if (log.isDebugEnabled()) {
					log.debug("OnlineUser: " + onlineUser.toString());
				}
				// 查询店铺认证配置中的允许接入时长
				String configueTime = cacheApiMgr.getConfigureParam(onlineUser.getStoreId(), onlineUser.getAccessSsid(),
						AuthCfg.STORE_CONFIGUE_TIME);
				if (log.isDebugEnabled()) {
					log.debug("configueTime: " + configueTime);
				}
				// 接入时长配置为空或‘0’时，表示不限制接入时长
				if (configueTime != null && !configueTime.equals("0")) {
					// 剩余接入时长
					long sessionTimeout = Long.valueOf(configueTime) - user.getSessionTime();
					if (log.isDebugEnabled()) {
						log.debug("The rest of access time: " + sessionTimeout);
					}
					if (sessionTimeout > 0) {
						// 加入在线列表
						updateOnlineUserInfo(user, status, sessionTimeout);
					} else {
						// 如果接入超时，加入下线列表
						updateOfflineUserInfo(user, onlineUser, status);
					}
				} else {
					// 加入在线列表，0表示不限制接入时长
					updateOnlineUserInfo(user, status, 0);
				}
			} else {
				if (log.isDebugEnabled()) {
					log.debug("User is already offline: " + user.toString());
				}
				// 如果在线表中不存在该用户，加入下线列表
				updateOfflineUserInfo(user, null, status);
			}
		}

		return status;
	}

	@Override
	public void userOffline(String mac) {
		// 校验用户是否存在于在线表
		OnlineUser onlineUser = cacheApiMgr.getOnlineUserCacheMgr().get(mac);

		if (onlineUser != null) {
			if (log.isDebugEnabled()) {
				log.debug("OnlineUser: " + onlineUser.toString());
			}
			// 删除在线用户
			onlineUserMgr.deleteOnlineUser(onlineUser);
			// 更新缓存
			cacheApiMgr.getOnlineUserCacheMgr().remove(mac);
			// 创建接入明细
			accessUserDetailMgr.createAccessDetail(onlineUser);
		} else {
			log.warn("user already offline with mac:" + mac);
		}
		OfflineCallback callback = getOfflineCallbak();
		if (callback != null) {
			log.info("Third party offline callback.");
			callback.callbak(mac);
		} else {
			log.warn("No Third party offline callback found!");
		}
	}

	/**
	 * 获取第三方下线回调接口
	 *
	 * @return 回调接口
	 */
	private OfflineCallback getOfflineCallbak() {
		if (callback != null) {
			return callback;
		}
		AbstractApplicationContext ctx = ServerContext.getRootAppContext();
		Map<String, OfflineCallback> beans = BeanFactoryUtils.beansOfTypeIncludingAncestors(ctx, OfflineCallback.class);
		for (Entry<String, OfflineCallback> entry : beans.entrySet()) {
			OfflineCallback value = entry.getValue();
			if (value != null) {
				callback = value;
				break;
			}
		}
		return callback;
	}

	/**
	 * 修改在线用户信息
	 * 
	 * @param user
	 *            用户在线信息
	 * @param status
	 *            返回报文内容
	 * @param sessionTimeout
	 *            剩余接入时长
	 */
	private void updateOnlineUserInfo(User user, UserStatus status, long sessionTimeout) {
		// 加入在线列表
		status.getOnlines().add(new Online(user.getIp(), sessionTimeout));
		// 修改在线用户表
		onlineUserMgr.updateOnlineInfo(user);
		// 更新缓存数据
		OnlineUser cacheUser = cacheApiMgr.getOnlineUserCacheMgr().get(user.getMac());
		cacheUser.setAccessDuration(user.getSessionTime());
		cacheUser.setInputBytes(user.getInputbytes());
		cacheUser.setOutputBytes(user.getOutputbytes());
	}

	/**
	 * 修改下线用户信息
	 * 
	 * @param user
	 *            用户在线信息
	 * @param status
	 *            返回报文内容
	 */
	@Transactional
	private void updateOfflineUserInfo(User user, OnlineUser onlineUser, UserStatus status) {
		// 加入下线列表
		status.getOfflines().add(new Offline(user.getIp()));
		if (onlineUser != null) {
			// 删除在线用户
			onlineUserMgr.deleteOnlineUser(onlineUser);
			// 更新缓存
			cacheApiMgr.getOnlineUserCacheMgr().remove(user.getMac());
			// 修改接入明细表
			onlineUser.setAccessDuration(user.getSessionTime());
			onlineUser.setInputBytes(user.getInputbytes());
			onlineUser.setOutputBytes(user.getOutputbytes());
			accessUserDetailMgr.createAccessDetail(onlineUser);
		}
	}

	public void setProtocolDao(ProtocolDao protocolDao) {
		this.protocolDao = protocolDao;
	}

	public void setUserAgentMgr(UserAgentMgr userAgentMgr) {
		this.userAgentMgr = userAgentMgr;
	}

	public void setTokenMgr(TokenMgr tokenMgr) {
		this.tokenMgr = tokenMgr;
	}

	public void setUserMgr(RegistUserMgr userMgr) {
		this.userMgr = userMgr;
	}

	public void setOnlineUserMgr(OnlineUserMgr onlineUserMgr) {
		this.onlineUserMgr = onlineUserMgr;
	}

	public void setAccessUserDetailMgr(AccessUserDetailMgr accessUserDetailMgr) {
		this.accessUserDetailMgr = accessUserDetailMgr;
	}

	public void setCacheApiMgr(CacheApiMgr cacheApiMgr) {
		this.cacheApiMgr = cacheApiMgr;
	}

	public void setWeChatAuthUrl(String weChatAuthUrl) {
		this.weChatAuthUrl = weChatAuthUrl;
	}

	public HttpOrHttpsMgr getHttpOrHttps() {
		return httpOrHttps;
	}

	public void setHttpOrHttps(HttpOrHttpsMgr httpOrHttps) {
		this.httpOrHttps = httpOrHttps;
	}

	public void setHaihaiAppMgr(HaihaiAppMgr haihaiAppMgr) {
		this.haihaiAppMgr = haihaiAppMgr;
	}

	public LoginMgr getLoginMgr() {
		return loginMgr;
	}

	public void setLoginMgr(LoginMgr loginMgr) {
		this.loginMgr = loginMgr;
	}

	public Boolean getSkipQuickLogin() {
		return skipQuickLogin;
	}

	public void setSkipQuickLogin(Boolean skipQuickLogin) {
		this.skipQuickLogin = skipQuickLogin;
	}

	public Boolean getSkipLoginIfOnlyWxWifi() {
		return skipLoginIfOnlyWxWifi;
	}

	public void setSkipLoginIfOnlyWxWifi(Boolean skipLoginIfOnlyWxWifi) {
		this.skipLoginIfOnlyWxWifi = skipLoginIfOnlyWxWifi;
	}

	public AuthUserMgr getAuthUserMgr() {
		return authUserMgr;
	}

	public void setAuthUserMgr(AuthUserMgr authUserMgr) {
		this.authUserMgr = authUserMgr;
	}
	
	public String getSkipQuickLoginInAuth() {
		return skipQuickLoginInAuth;
	}

	public void setSkipQuickLoginInAuth(String skipQuickLoginInAuth) {
		this.skipQuickLoginInAuth = skipQuickLoginInAuth;
	}

	public String getSkipLoginIfOnlyWxWifiInAuth() {
		return skipLoginIfOnlyWxWifiInAuth;
	}

	public void setSkipLoginIfOnlyWxWifiInAuth(String skipLoginIfOnlyWxWifiInAuth) {
		this.skipLoginIfOnlyWxWifiInAuth = skipLoginIfOnlyWxWifiInAuth;
	}
	
}
