/*
 * Copyright (c) 2015, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC V300R002
 * Module Name :
 * Date Created: 2015-7-21
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
package com.h3c.o2o.portal.rs.func;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.h3c.o2o.auth.rabbitmq.MqRequest;
import com.h3c.o2o.auth.rabbitmq.MqResponse;
import com.h3c.o2o.portal.PortalConstant;
import com.h3c.o2o.portal.PortalErrorCodes;
import com.h3c.o2o.portal.PortalException;
import com.h3c.o2o.portal.cache.api.CacheApiMgr;
import com.h3c.o2o.portal.cache.key.RegistUserCacheKey;
import com.h3c.o2o.portal.common.CommonUtils;
import com.h3c.o2o.portal.common.FuncUtil;
import com.h3c.o2o.portal.entity.AccessDetail;
import com.h3c.o2o.portal.entity.OnlineUser;
import com.h3c.o2o.portal.entity.RegistUser;
import com.h3c.o2o.portal.message.func.MessageMgr;
import com.h3c.o2o.portal.protocol.entity.AuthMessageCode;
import com.h3c.o2o.portal.protocol.entity.ThirdPartyParam;
import com.h3c.o2o.portal.protocol.func.TokenMgr;
import com.h3c.o2o.portal.rs.entity.AccessDetailList;
import com.h3c.o2o.portal.rs.entity.AddUser;
import com.h3c.o2o.portal.rs.entity.AuthMac;
import com.h3c.o2o.portal.rs.entity.AuthResult;
import com.h3c.o2o.portal.rs.entity.AuthUser;
import com.h3c.o2o.portal.rs.entity.ErrorEnum;
import com.h3c.o2o.portal.rs.entity.ErrorInfo;
import com.h3c.o2o.portal.rs.entity.Mac;
import com.h3c.o2o.portal.rs.entity.MacList;
import com.h3c.o2o.portal.rs.entity.ModifyUser;
import com.h3c.o2o.portal.rs.entity.OnlineUserList;
import com.h3c.o2o.portal.rs.entity.PhoneCode;
import com.h3c.o2o.portal.rs.entity.SmsCode;
import com.h3c.o2o.portal.rs.entity.ThirdPartyStrategy;
import com.h3c.o2o.portal.user.func.AccessUserDetailMgr;
import com.h3c.o2o.portal.user.func.OnlineUserMgr;
import com.h3c.o2o.portal.user.func.RegistUserMgr;

/**
 * add description of types here
 *
 * @author j09980
 */
public class ThirdPartyAuthMgrImpl implements ThirdPartyAuthMgr {
    private Log log = LogFactory.getLog(getClass());

    private RegistUserMgr userMgr;

    private OnlineUserMgr onlineUserMgr;

    private AccessUserDetailMgr accessUserDetailMgr;

    private MessageMgr messageMgr;

    private TokenMgr tokenMgr;

    /** 数据缓存接口 */
    private CacheApiMgr cacheApiMgr;

    private RabbitTemplate rabbitTemplate;

    private StrategyMgr strategyMgr;

    /**创建接入用户*/
    @Override
    public ErrorInfo createUser(AddUser user) {
        try {
            if (user == null) {
                return ErrorEnum.USER_NO_EXISTS.getErrorInfo();
            }
            RegistUser regUser = new RegistUser();
            // 商店id
            regUser.setStoreId(user.getNas_id());
            if (null == user.getName()) {
                return ErrorEnum.USER_NAME_ILLEAGAL.getErrorInfo();
            }
            // 用户名
            regUser.setUserName(user.getName());
            String pwd = CommonUtils.encryptData(user.getPassword());
            // 密码
            regUser.setUserPassword(pwd);
            regUser.setLoginTime(new Date().getTime());
            // 第三方id
            regUser.setUserTokenId(user.getIdentifier());
            regUser.setUserType(RegistUser.USER_TYPE_ACCOUNT);

            //封装缓存信息
            RegistUserCacheKey key = configCacheKey(regUser);
            userMgr.saveRegistUser(regUser);
            cacheApiMgr.getRegistUserCacheMgr().put(key, regUser);
        } catch (PortalException e) {
            return ErrorEnum.getErrorInfo(e.getErrorCode());
        } catch (Exception ee) {
            log.info(null, ee);
            return ErrorEnum.UNKNOWN_ERROR.getErrorInfo();
        }
        return ErrorEnum.SUCCESS.getErrorInfo();
    }

    /**
     * 封装缓存信息
     * */
    private RegistUserCacheKey configCacheKey(RegistUser regUser){
        RegistUserCacheKey key = new RegistUserCacheKey();
        key.setStoreId(regUser.getStoreId());
        key.setUserName(regUser.getUserName());
        key.setUserType(regUser.getUserType());
        return key;
    }

    @Override
    public ErrorInfo modifyUser(ModifyUser user) {
        try {
            RegistUser ru = this.userMgr.queryRegistUser(user.getName(),
                user.getIdentifier(), RegistUser.USER_TYPE_ACCOUNT);
            if (ru == null) {
                return ErrorEnum.USER_NO_EXISTS.getErrorInfo();
            }
            String oldPwd = CommonUtils.encryptData(user.getOld_password());
            if (!oldPwd.equals(ru.getUserPassword())) {
                return ErrorEnum.PASSWORD_ERROR.getErrorInfo();
            }
            String newPwd = CommonUtils.encryptData(user.getNew_password());
            ru.setUserPassword(newPwd);

            RegistUserCacheKey key = configCacheKey(ru);
            this.userMgr.updateRegistUser(ru);
            cacheApiMgr.getRegistUserCacheMgr().put(key, ru);
        } catch (PortalException pe) {
            return ErrorEnum.getErrorInfo(pe.getErrorCode());
        } catch (Exception e) {
            log.warn(null, e);
            return ErrorEnum.UNKNOWN_ERROR.getErrorInfo();
        }
        return ErrorEnum.SUCCESS.getErrorInfo();
    }

    @Override
    public AuthResult authUser(AuthUser user) {
    	AuthResult ar = new AuthResult();
        RegistUser ru = this.userMgr.queryRegistUser(user.getName(),
            user.getIdentifier(), RegistUser.USER_TYPE_ACCOUNT);
        if (ru == null) {
            ar.setError_code(ErrorEnum.USER_NO_EXISTS.getErrorCode());
            ar.setError_msg(ErrorEnum.USER_NO_EXISTS.getErrorMsg());
            return ar;
        }
        String pwd = CommonUtils.encryptData(user.getPassword());
        if (!ru.getUserPassword().equals(pwd)) {
            ar.setError_code(ErrorEnum.NAME_OR_PASSWORD_WRONG.getErrorCode());
            ar.setError_msg(ErrorEnum.NAME_OR_PASSWORD_WRONG.getErrorMsg());
            return ar;
        }
		String code = tokenMgr.generateCode(ru.getUserMac(), ru.getUserIp(), ru.getStoreId(), null,
				RegistUser.USER_TYPE_ACCOUNT, ru);
		ThirdPartyParam param = new ThirdPartyParam();
        param.setSessionTimeout(user.getSession_timeout());
        param.setSuccessUrl(user.getResult_url());
        tokenMgr.putThirdPartyParam(ru.getId(), param);
		ar.setRedirect_url(FuncUtil.getLoginRedirectURI(user.getRedirect_uri(), code, ru.getUserIp(),
				RegistUser.USER_TYPE_WX_WIFI == ru.getUserType().intValue()));
        ar.setExpired_in(tokenMgr.getCodeInfo(code, false).getExpire_in());
        return ar;
    }

    @Override
    public AuthResult authMac(AuthMac authMac) {
    	AuthResult ar = new AuthResult();

    	// 校验mac地址格式
    	String mac = authMac.getUser_mac().toUpperCase();
		boolean matched = mac.matches("([0-9A-F]{2}-){5}[0-9A-F]{2}");
		if (!matched) {
			ar.setError_code(ErrorEnum.MAC_ILLEGAL.getErrorCode());
            ar.setError_msg(String.format("Mac '%s' is illegal.", mac));
            return ar;
		}

    	// 校验黑名单
		List<ThirdPartyStrategy> strategies = strategyMgr.query(
			authMac.getUser_mac(), ThirdPartyStrategy.STRATEGY_TYPE_BLACK_LIST);
		if (strategies != null && strategies.size() > 0) {
			ar.setError_code(ErrorEnum.USER_EXIST_BLACKLIST.getErrorCode());
            ar.setError_msg(ErrorEnum.USER_EXIST_BLACKLIST.getErrorMsg());
            return ar;
		}

		RegistUser user = tpRegister(authMac);
		// 获取Code
		String code = tokenMgr.generateCode(user.getUserMac(), user.getUserIp(), user.getStoreId(), null,
				RegistUser.USER_TYPE_ACCOUNT, user);

        ar.setRedirect_url(FuncUtil.getLoginRedirectURI(authMac.getRedirect_uri(),
            code, user.getUserIp(), false));
        ar.setExpired_in(tokenMgr.getCodeInfo(code, false).getExpire_in());
    	return ar;
    }

    /**
     * 使用mac注册第三方用户
     * @param authMac
     * @return  
     */
    private RegistUser tpRegister(AuthMac authMac) {
        RegistUser user = null;
        int userType = RegistUser.USER_TYPE_ACCOUNT;
        // 生成缓存key,合作方的用户取mac地址作为用户名
        RegistUserCacheKey key = new RegistUserCacheKey(authMac.getNas_id(),
        		authMac.getUser_mac(), userType);
        // 查询用户
        user = cacheApiMgr.getRegistUserCacheMgr().get(key);
        // 如果用户不存在，创建用户
        if (user == null) {
            user = createTpUser(key, authMac.getIdentifier());
        }
        if (log.isDebugEnabled()) {
        	log.debug("Logining third party user:" + user.toString());
        }
        ThirdPartyParam param = new ThirdPartyParam();
        param.setSessionTimeout(authMac.getSession_timeout());
        param.setSuccessUrl(authMac.getResult_url());
        tokenMgr.putThirdPartyParam(user.getId(), param);
		return user;
	 }

    /**
     * 使用mac地址创建合作方注册用户
     * @param key
     * @param userTokenId
     * @return
     */
    private RegistUser createTpUser(RegistUserCacheKey key, String userTokenId) {
    	RegistUser user = new RegistUser();
        user.setStoreId(key.getStoreId());
        user.setUserName(key.getUserName());
        // 密码为“h3c绿洲平台”的简写
        user.setUserPassword(CommonUtils.encryptData("h3clzpt"));
        user.setUserType(key.getUserType());
        user.setUserTokenId(userTokenId);
        user.setLoginTime(System.currentTimeMillis());
        // 保存
        userMgr.saveRegistUser(user);
        cacheApiMgr.getRegistUserCacheMgr().put(key, user);
        return user;
    }

    @Override
    public OnlineUserList getOnlines(String identifier, int offset, int limit) {
        OnlineUserList oul = new OnlineUserList();
        try {
            if (limit == 0) {
                limit = Integer.MAX_VALUE;
            }
            List<OnlineUser> list = this.onlineUserMgr.queryOnlineUser(
                identifier, offset, limit);
            oul.setUsers(configOnlineUser(list));

        } catch (Exception e) {
            log.warn("thrid party auth query online user error:", e);
        }
        return oul;
    }

    @Override
    public AccessDetailList getAccessDetails(String identifier, int offset,
        int limit) {
        AccessDetailList list = new AccessDetailList();
        try {
            if (limit == 0) {
                limit = Integer.MAX_VALUE;
            }
            List<AccessDetail> accessList = this.accessUserDetailMgr
                .queryAccessUser(identifier, offset, limit);
            list.setUsers(configAccess(accessList));
        } catch (Exception e) {
            log.warn(null, e);
        }
        return list;
    }

    /**
     *  获取短信验证码
     * */
    @Override
    public SmsCode getSmsCode(String phoneNumber) {
        AuthMessageCode amc = this.messageMgr.getCode(phoneNumber);
        if (amc == null) {
            throw new PortalException(ErrorEnum.PHONE_NO_ERROR.getErrorCode(),
                ErrorEnum.PHONE_NO_ERROR.getErrorMsg());
        }
        SmsCode sc = new SmsCode();
        sc.setCode(amc.getCode());
        sc.setExpired_in(amc.getExpire_in() / 1000);
        return sc;
    }

    @Override
    public ErrorInfo verifySmsCode(PhoneCode code) {
        //通过电话号码取得缓存内的校验码
        AuthMessageCode amc = this.tokenMgr.getSmsCode(code.getPhone_number());
        if (amc == null) {
            return ErrorEnum.UNKNOWN_ERROR.getErrorInfo();
        }
        if ((System.currentTimeMillis() - amc.getCreateTime()) > amc
            .getExpire_in()) {
            return ErrorEnum.SMS_CODE_OUTTIME.getErrorInfo();
        }
        if (!amc.getCode().equals(code.getCode())) {
            return ErrorEnum.SMS_CODE_CHECK_FAILD.getErrorInfo();
        }
        return ErrorEnum.SUCCESS.getErrorInfo();
    }

    @Transactional
	@Override
	public ErrorInfo addToBlacklist(MacList list) {
		if (list == null) {
			return ErrorEnum.UNKNOWN_ERROR.getErrorInfo();
		}
		// 校验mac地址
		List<String> validatedList = validateMacList(list.getMac_list());
		// 保存
		batchSaveStrategy(validatedList,
				ThirdPartyStrategy.STRATEGY_TYPE_BLACK_LIST);
		return ErrorEnum.SUCCESS.getErrorInfo();
	}

    @Transactional
	@Override
	public ErrorInfo addToWhitelist(MacList list) {
		if (list == null) {
			return ErrorEnum.UNKNOWN_ERROR.getErrorInfo();
		}
		// 校验mac地址
		List<String> validatedList = validateMacList(list.getMac_list());
		// 保存
		batchSaveStrategy(validatedList,
				ThirdPartyStrategy.STRATEGY_TYPE_WHITE_LIST);
		return ErrorEnum.SUCCESS.getErrorInfo();
	}

    @Transactional
    @Override
    public ErrorInfo removeFromBlacklist(MacList list) {
    	if (list == null) {
			return ErrorEnum.UNKNOWN_ERROR.getErrorInfo();
		}

    	// 校验
		List<String> validatedList = validateMacList(list.getMac_list());
		// 移除
    	batchDeleteStrategy(validatedList,
				ThirdPartyStrategy.STRATEGY_TYPE_BLACK_LIST);
    	return ErrorEnum.SUCCESS.getErrorInfo();
    }

    @Transactional
    @Override
    public ErrorInfo removeFromWhitelist(MacList list) {
    	if (list == null) {
			return ErrorEnum.UNKNOWN_ERROR.getErrorInfo();
		}
    	// 校验
		List<String> validatedList = validateMacList(list.getMac_list());
		// 移除
    	batchDeleteStrategy(validatedList,
				ThirdPartyStrategy.STRATEGY_TYPE_WHITE_LIST);
    	return ErrorEnum.SUCCESS.getErrorInfo();
    }

    /**
     * 校验移除黑名单和白名单的mac地址集合
     * @param macs mac地址
     * @return 过滤后的mac地址集合
     * @throws PortalException mac地址非法
     */
    private List<String> validateMacList(List<String> macs) {
    	List<String> result = new ArrayList<String>();
    	for (String mac : macs) {
    		// 转大写
    		String macUpper = mac.toUpperCase();
    		// 过滤重复项
    		if (result.contains(macUpper)) {
    			continue;
    		}
    		// 校验mac地址格式
    		boolean matched = macUpper.matches("([0-9A-F]{2}-){5}[0-9A-F]{2}");
			if (!matched) {
				throw new PortalException(ErrorEnum.MAC_ILLEGAL.getErrorCode(),
						String.format("Mac '%s' is illegal.", mac));
			}
			// 加入待操作集合
			result.add(macUpper);
    	}
    	return result;
    }

    /**
     * 校验mac是否已存在
     * @param mac
     * @param type 黑名单或白名单
     * @return 如果在指定列表中存在，返回true，如果在指定列表和对立列表中都不存在，
     *  返回false
     * @throws PortalException 如果在对立列表中已存在，如：正在保存的是黑名单，但是
     * 	已在白名单中存在
     */
    private boolean validateMacExist(String mac, int type) {
    	// 校验数据库中是否已存在
		List<ThirdPartyStrategy> strategies = strategyMgr.query(
				mac, null);
		boolean toNextLoop = false;
		if (strategies != null && strategies.size() > 0) {
			int existType = strategies.get(0).getType().intValue();

			if (existType == type) {
				// 如果mac已加入指定列表，结束单次循环
				toNextLoop = true;
				// 日志
				if (log.isDebugEnabled()) {
					if (existType == ThirdPartyStrategy.STRATEGY_TYPE_BLACK_LIST) {
						log.debug("Mac with " + mac + "is already exist in blackList");
					} else {
						log.debug("Mac with " + mac + "is already exist in whiteList");
					}
				}
			} else {
				// 如果mac已加入对立列表，抛出异常
				if (existType == ThirdPartyStrategy.STRATEGY_TYPE_BLACK_LIST) {
					throw new PortalException(
						ErrorEnum.USER_EXIST_BLACKLIST.getErrorCode(),
						String.format(
							"Mac '%s' is already exist in blacklist.",
							mac));
				} else {
					throw new PortalException(
						ErrorEnum.USER_EXIST_WHITELIST.getErrorCode(),
						String.format(
							"Mac '%s' is already exist in whitelist.",
							mac));
				}
			}
		}
    	return toNextLoop;
    }

    /**
     * 批量保存策略
     * @param macs
     * @param type 黑名单或白名单
     */
    private void batchSaveStrategy(List<String> macs, int type) {
    	for (String mac : macs) {
			if (validateMacExist(mac, type)) {
				// 单步结束
				continue;
			}
			// 构造数据并保存
        	ThirdPartyStrategy strategy = new ThirdPartyStrategy();
        	strategy.setMac(mac);
        	strategy.setType(type);
        	strategy.setCreateTime(System.currentTimeMillis());
        	strategyMgr.save(strategy);
        }
    }

    /**
     * 批量删除策略
     * @param macs
     * @param type
     */
    private void batchDeleteStrategy(List<String> macs, int type) {
    	for (String mac : macs) {
        	strategyMgr.delete(mac, type);
        }
    }

    private List<com.h3c.o2o.portal.rs.entity.OnlineUser> configOnlineUser(
        List<OnlineUser> list) {
        List<com.h3c.o2o.portal.rs.entity.OnlineUser> listOnline = new ArrayList<com.h3c.o2o.portal.rs.entity.OnlineUser>();
        com.h3c.o2o.portal.rs.entity.OnlineUser ou = new com.h3c.o2o.portal.rs.entity.OnlineUser();
        for (OnlineUser oo : list) {
            ou = new com.h3c.o2o.portal.rs.entity.OnlineUser();
            ou.setUser_name(null == oo.getUserName() ? null : oo.getUserName());
            ou.setAccess_start_time(null == oo.getAccessStartTime() ? 0 : oo
                .getAccessStartTime());
            ou.setAccess_duration(null == oo.getAccessDuration() ? 0 : oo
                .getAccessDuration());
            ou.setAccess_ssid(null == oo.getAccessSsid() ? null : oo
                .getAccessSsid());
            ou.setUser_ip(null == oo.getUserIp() + "" ? null : oo.getUserIp()
                + "");
            ou.setUser_mac(null == oo.getUserMac() ? null : oo.getUserMac());
            ou.setPhone_number(null == oo.getMobileNo() ? null : oo
                .getMobileNo());
            ou.setManufacturer(null == oo.getDevManufacturer() ? null : oo
                .getDevManufacturer());
            ou.setOs_type(null == oo.getDevOsType() ? null : oo.getDevOsType());
            listOnline.add(ou);
        }
        return listOnline;
    }

    private List<com.h3c.o2o.portal.rs.entity.AccessDetail> configAccess(
        List<AccessDetail> list) {
        List<com.h3c.o2o.portal.rs.entity.AccessDetail> accessList = new ArrayList<com.h3c.o2o.portal.rs.entity.AccessDetail>();
        com.h3c.o2o.portal.rs.entity.AccessDetail accessDetail = new com.h3c.o2o.portal.rs.entity.AccessDetail();
        for (AccessDetail ad : list) {
			accessDetail = new com.h3c.o2o.portal.rs.entity.AccessDetail();
			accessDetail.setUser_name(ad.getUserName());
			accessDetail.setAccess_start_time(ad.getAccessStartTime());
			accessDetail.setAccess_duration(ad.getAccessDuration());
			accessDetail.setAccess_end_time(ad.getAccessEndTime());
			accessDetail.setAccess_ssid(ad.getAccessSsid());
			accessDetail.setUser_ip(ad.getUserIp() + "");
			accessDetail.setUser_mac(ad.getUserMac());
			accessDetail.setPhone_number(ad.getMobileNo());
			accessDetail.setManufacturer(ad.getDevManufacturer());
			accessDetail.setOs_type(ad.getDevOsType());
			accessList.add(accessDetail);
        }
        return accessList;
    }


    /**{@inheritDoc}*/
    @Override
    public ErrorInfo loginOut(Mac mac) {
        //校验用户
        OnlineUser ou = onlineUserMgr.queryOnlineUser(mac.getMac());
        if(ou == null){
            return ErrorEnum.USER_LOGIN_OUT.getErrorInfo();
        }
        try {
            sendOptMessage(ou.getId());
        } catch (PortalException e) {
            ErrorInfo errorInfo = new ErrorInfo();
            errorInfo.setError_code(e.getErrorCode()+"");
            errorInfo.setError_msg(e.getErrorMsg());
            return errorInfo;
        }

        return ErrorEnum.SUCCESS.getErrorInfo();
    }

    private void sendOptMessage(Long id)  {
        List<Long> ids = new ArrayList<Long>();
        ids.add(id);
        // 更新缓存
        MqRequest request = new MqRequest();
        request.setType(PortalConstant.OPTTYPE_DELETE);
        request.setIds(ids);

        try {
            MqResponse strResult = (MqResponse) rabbitTemplate.convertSendAndReceive(
                PortalConstant.LOGINOUT_EXCHANGE, PortalConstant.LOGINOUT_EXCHANGE_KEY, request);
            if (null == strResult || !strResult.isSuccess()) {
                throw new PortalException(PortalErrorCodes.MSG_OPERATE_ERROR);
            }
        } catch (Exception e) {
            throw new PortalException(PortalErrorCodes.MSG_OPERATE_ERROR);
        }
    }

    public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
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

    public void setMessageMgr(MessageMgr messageMgr) {
        this.messageMgr = messageMgr;
    }

    public void setTokenMgr(TokenMgr tokenMgr) {
        this.tokenMgr = tokenMgr;
    }

    public void setCacheApiMgr(CacheApiMgr cacheApiMgr) {
        this.cacheApiMgr = cacheApiMgr;
    }

	public void setStrategyMgr(StrategyMgr strategyMgr) {
		this.strategyMgr = strategyMgr;
	}

}
