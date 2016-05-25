/*
 * Copyright (c) 2007-2013, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC v7
 * Module Name : iMC
 * Date Created: 2015-6-19
 * Creator     : dkf5133
 * Description :
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 * 2015-6-19     dkf5133          iMC project, new code file.
 *
 *------------------------------------------------------------------------------
 */
package com.h3c.o2o.portal.login.func;

import java.io.IOException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.google.gson.Gson;
import com.h3c.o2o.portal.PortalConstant;
import com.h3c.o2o.portal.PortalErrorCodes;
import com.h3c.o2o.portal.PortalException;
import com.h3c.o2o.portal.cache.api.CacheApiMgr;
import com.h3c.o2o.portal.cache.key.RegistUserCacheKey;
import com.h3c.o2o.portal.common.CommonUtils;
import com.h3c.o2o.portal.common.FuncUtil;
import com.h3c.o2o.portal.common.HttpOrHttpsMgr;
import com.h3c.o2o.portal.entity.AuthCfg;
import com.h3c.o2o.portal.entity.PublishMngPara;
import com.h3c.o2o.portal.entity.RegistUser;
import com.h3c.o2o.portal.entity.ShopWifiInfo;
import com.h3c.o2o.portal.entity.ThemePage;
import com.h3c.o2o.portal.entity.ThemeTemplate;
import com.h3c.o2o.portal.entity.WeixinAccount;
import com.h3c.o2o.portal.login.entity.AuthCfgResp;
import com.h3c.o2o.portal.login.entity.Extend;
import com.h3c.o2o.portal.login.entity.HomePageReq;
import com.h3c.o2o.portal.login.entity.LoginReq;
import com.h3c.o2o.portal.login.entity.PageImageReq;
import com.h3c.o2o.portal.login.entity.SmsLoginReq;
import com.h3c.o2o.portal.login.entity.WeixinConnectWifiPara;
import com.h3c.o2o.portal.login.entity.WxWifiOptRequest4Json;
import com.h3c.o2o.portal.login.entity.WxWifiOptResponse4Json;
import com.h3c.o2o.portal.message.func.MessageMgr;
import com.h3c.o2o.portal.protocol.entity.AuthMessageCode;
import com.h3c.o2o.portal.protocol.func.TokenMgr;
import com.h3c.o2o.portal.shopwifi.func.ShopWifiMgr;
import com.h3c.o2o.portal.theme.func.ThemePageMgr;
import com.h3c.o2o.portal.user.func.RegistUserMgr;
import com.h3c.oasis.o2oserver.util.StringManager;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author dkf5133
 *
 */
public class LoginMgrImpl implements LoginMgr {

    /** 资源访问对象。 */
    private static StringManager sm = StringManager
        .getManager("com.h3c.o2o.portal");

    private DateFormat formater1 = new SimpleDateFormat("yyyy-MM-dd");

    private DateFormat formater2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    
    private RabbitTemplate rabbitTemplate;

    private ThemePageMgr themePageMgr;

    private MessageMgr messageMgr;
    
    private ShopWifiMgr shopWifiMgr;

    private TokenMgr tokenMgr;

    private RegistUserMgr userMgr;
    
    private HttpOrHttpsMgr httpOrHttps;
    
    private String wxWifiAuthUrl = "/weixinwifi/clientauth";

    /** 数据缓存接口 */
    private CacheApiMgr cacheApiMgr;

    private Log log = LogFactory.getLog(getClass());

    @Override
    public JSONObject getPageImageInfo(PageImageReq pageReq) {
        JSONObject result = null;
        if (pageReq.getPageType() == null) {
            throw new IllegalArgumentException("Illegal pageType.");
        }

        if (pageReq.getTemplateId() != null) {
            // 根据模板ID查询模板
			ThemeTemplate template = cacheApiMgr.getThemeTemplate(
					pageReq.getNasId(), pageReq.getSsid(),
					Long.valueOf(pageReq.getTemplateId()));
            // 如果没找到，抛出异常
            if (template == null) {
                throw new PortalException(
                    sm.getString("o2o.portal.quickLogin.store.notConfigued"));
            }
            // 获取图片信息
            ThemePage page = themePageMgr.getThemePage(template,
                Integer.valueOf(pageReq.getPageType()));
            result = getPageConfig(page);
        } else {
            throw new IllegalArgumentException("Illegal templateId.");
        }
        return result;
    }

    /**
     * 获取认证配置
     */
    public AuthCfgResp getAuthConfig(LoginReq confPara) {
        AuthCfgResp resp = new AuthCfgResp();
        AuthCfg authconf = null;
		if (null != confPara.getNasId()) {
			authconf = (AuthCfg) cacheApiMgr.getAuthCfg(
			    confPara.getNasId(), confPara.getSsid());
		}
        // 如果没找到店铺认证配置，禁用所有登陆方式
        if (authconf == null) {
            resp.setPhone2guding(0);
            resp.setQq(0);
            resp.setWeixin(0);
            return resp;
        }

        // 固定账号和短信认证：0、都不启用，1、短信认证，2、固定账号，3、都启用
        if (authconf.getIsEnableAccount() == AuthCfg.IS_ENABLE_YES) {
            if (authconf.getIsEnableSms() == AuthCfg.IS_ENABLE_YES) {
                resp.setPhone2guding(3);
            } else {
                resp.setPhone2guding(2);
            }
        } else {
            if (authconf.getIsEnableSms() == AuthCfg.IS_ENABLE_YES) {
                resp.setPhone2guding(1);
            } else {
                resp.setPhone2guding(0);
            }
        }
        // 微信认证：0、禁用，1、启用
        resp.setWeixin(authconf.getIsEnableWinxin());
        // QQ认证：0、禁用，1、启用
        resp.setQq(authconf.getIsEnableQQ());
        // 微信连Wifi: 这里只需要知道微信连Wi-Fi是否已启用即可
        WeixinConnectWifiPara paras = new WeixinConnectWifiPara();
        paras.setIsWeixinConnectWifi(authconf.getIsWeixinConnectWifi());
        resp.setWeixinConnectWifi(paras);
        return resp;
    }
    
    /**
     * 
     * 获取微信连wifi参数 
     *
     * @param authconf
     * @param confPara
     * @return
     */
    public WeixinConnectWifiPara getWxWifiPara(AuthCfg authconf, LoginReq confPara) {
        WeixinConnectWifiPara result = new WeixinConnectWifiPara();
        if (authconf.getIsWeixinConnectWifi() == AuthCfg.IS_ENABLE_NO) {
            result.setIsWeixinConnectWifi(AuthCfg.IS_ENABLE_NO);
            if (log.isDebugEnabled()) {
                log.debug("Weixin connect wifi disabled.");
            }
            return result;
        }
        // 格式化mac地址
        formatMacAdd(confPara);
        result.setIsWeixinConnectWifi(AuthCfg.IS_ENABLE_YES);
        // 查询shopWifiInfo
        ShopWifiInfo shopWifiInfo = shopWifiMgr.queryShopWifiInfo(confPara.getNasId());
        // 校验配置
        try{
        	checkWxWifiConfig(shopWifiInfo, confPara);
        } catch (PortalException e){
        	log.warn(null, e);
        	result.setError(e.getErrorMsg() + " Error Code:" + e.getErrorCode());
        	return result;
        } catch (Exception e){
        	log.warn(null, e);
        	result.setError(e.getMessage());
        	return result;
        }
        // 查询公众号并获取appid
        String appId = getWeixinAcount(shopWifiInfo).getAppId();
        // 设置扩展信息
        Extend extend = new Extend();
        extend.setNasid(String.valueOf(confPara.getNasId()));
        extend.setSsid(confPara.getSsid());
        extend.setUserip(confPara.getUserip());
        extend.setUsermac(confPara.getUsermac());
        extend.setRedirect_uri(tokenMgr.getRedirectUri(confPara.getNasId()));
        String extendStr = null;
        try {
            extendStr = Extend.toExtend(extend);
            result.setExtend(URLEncoder.encode(extendStr, "UTF-8"));
        } catch (Exception e) {
            log.warn("Caculate extend failed.", e);
            result.setIsWeixinConnectWifi(AuthCfg.IS_ENABLE_NO);
            result.setError(sm.getString("o2o.portal.caculate.wxwifi.sign.failed"));
            return result;
        }
        
        String timestemp = String.valueOf(System.currentTimeMillis()); 
        String authUrl = httpOrHttps.getHttpOrHttps()+"://" + FuncUtil.getWeixinWifiDomain() + ":"
            + FuncUtil.getWeixinWifiPort() + wxWifiAuthUrl;
        if(log.isDebugEnabled()){
        	log.debug("Generic AuthUrl： " + authUrl);
        }
        String secretkey = shopWifiInfo.getSecretkey();
        result.setUsermac(confPara.getUsermac());
        // 使用空的bssid
        result.setBssid(null);
        
        result.setAppId(appId);
        result.setAuthUrl(authUrl);
        result.setTimestamp(timestemp);
        result.setShopId(shopWifiInfo.getShopId());
        
        try {
            // 计算签名
            result.setSign(calculateSign(appId, extendStr, timestemp,
                String.valueOf(shopWifiInfo.getShopId()), authUrl,
                confPara.getUsermac(), confPara.getSsid(), 
                secretkey));
            if(log.isDebugEnabled()){
            	log.debug("Generic Sign: " + result.getSign() + " timestamp: " + result.getTimestamp());
            }
        } catch(Exception e) {
            log.warn("MD5 caculate failed when getWxWifiPara.", e);
            result.setIsWeixinConnectWifi(AuthCfg.IS_ENABLE_NO);
            result.setError(sm.getString("o2o.portal.caculate.wxwifi.sign.failed"));
            return result;
        }
        if (log.isDebugEnabled()) {
            log.debug("Weixin connect wifi parameters:" + result.toString());
        }
        return result;
    }
    
	/**
	 * 校验微信连Wifi配置
	 * 
	 * @param shopWifiInfo
	 * @param confPara
	 * @throws PortalException
	 */
	private void checkWxWifiConfig(ShopWifiInfo shopWifiInfo, LoginReq confPara) throws PortalException {
		// 若未找到相关设置，抛出异常给予提示.
		if (null == shopWifiInfo) {
			String errorInfo = "Weixin Wifi info is not found for shop:" + confPara.getNasId() + " LoginReq: "
					+ confPara;
			log.warn(errorInfo);
			throw new PortalException(sm.getString("errorCode.60022", errorInfo));
		} else {
			// 校验SSID，避免用户连接错误的SSID。
//			String ssidFromDb = shopWifiInfo.getSsid();
//			if (StringUtils.isNotBlank(ssidFromDb)) {
//				if (!ssidFromDb.trim().equals(confPara.getSsid())) {
//					throw new PortalException(sm.getString("errorCode.60023", ssidFromDb));
//				}
//			} else {
//				String errorInfo = "Ssid is empty for shop:" + confPara.getNasId();
//				throw new PortalException(sm.getString("errorCode.60022", errorInfo));
//			}
		}
	}
    
    /**
     * 格式化Mac地址
     */
    private void formatMacAdd(LoginReq confPara){
    	if(null == confPara ){
    		log.warn("LoginReq is empty when format Mac address.");
    		return ;
    	}
    	if(log.isDebugEnabled()){
    		log.debug("before formatMacAdd: " + confPara);
    	}
    	String mac = confPara.getUsermac();
    	confPara.setUsermac(convertMac(mac));
    	String bassid = confPara.getBssid();
    	confPara.setBssid(convertMac(bassid));
    	if(log.isDebugEnabled()){
    		log.debug("after formatMacAdd: " + confPara);
    	}
    }
    
    /**
     * 微信连Wifi的Mac地址有要求，这里转换一下<br>
     * 用户手机mac地址，格式冒号分隔，字符长度17个，并且字母小写，例如：00:1f:7a:ad:5c:a8<br>
     * @param mac
     * @return
     */
    private String convertMac(String mac){
    	if(StringUtils.isNotBlank(mac)){
    		return mac.replace('-', ':').toLowerCase();
    	} else {
    		return mac;
    	}
    }
    
    /**
     * get sign
     * 
     * @param appId
     * @param extend
     * @param timestamp
     * @param shop_id
     * @param authUrl
     * @param mac
     * @param ssid
     * @param secretkey
     * @return
     * @throws NoSuchAlgorithmException
     */
    private String calculateSign(String... args) throws NoSuchAlgorithmException {
        StringBuffer sBuffer = new StringBuffer();
        for (int i = 0; null != args && i < args.length; i++) {
            sBuffer.append(args[i]);
        }
        String src = sBuffer.toString();
        // MD5签名生成
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(src.getBytes());
        byte[] digest = md.digest();
        StringBuffer hexstr = new StringBuffer();
        String shaHex = "";
        for (int i = 0; i < digest.length; i++) {
            shaHex = Integer.toHexString(digest[i] & 0xFF);
            if (shaHex.length() < 2) {
                hexstr.append(0);
            }
            hexstr.append(shaHex);
        }
        return hexstr.toString();
    }

    /**
     * 
     * 获取微信公众号 
     *
     * @param shopWifiInfo
     * @return
     */
    private WeixinAccount getWeixinAcount(ShopWifiInfo shopWifiInfo) {
        WeixinAccount account = shopWifiInfo.getWeixinAccount();
        if (account != null) {
            return account;
        }
        
        // 如果没有设置门店信息，使用系统默认的公众号
        WxWifiOptResponse4Json response = new WxWifiOptResponse4Json();
        WxWifiOptRequest4Json request = new WxWifiOptRequest4Json();
        
        Gson gson = new Gson();
        request.setMsgType(WxWifiOptRequest4Json.MSG_TYPE_DEFAULT_ACCOUNT);
        try {
            // 发送消息，并获取系统默认的公众号信息
            response = gson.fromJson(
                (String) rabbitTemplate.convertSendAndReceive(
                    PortalConstant.WXWIFI_DEFAULT_ACCOUNT_EXCHANGE,
                    PortalConstant.WXWIFI_DEFAULT_ACCOUNT_EXCHANGE_KEY,
                    new Gson().toJson(request)),
                WxWifiOptResponse4Json.class);
        } catch (Exception e) {
            throw new PortalException("Query default weixin account error.");
        }
        if (StringUtils.isNotBlank(response.getErrcode())
            && !response.getErrcode().equals("0")) {
            throw new PortalException(response.getErrmsg());
        }
        return gson.fromJson(response.getRepData(), WeixinAccount.class);
    }
    
    /**
     * 获取页面配置信息
     * @param page
     * @return
     */
    @SuppressWarnings({ "rawtypes" })
    private JSONObject getPageConfig(ThemePage page) {
        JSONObject result = new JSONObject();

        JSONObject object = JSONObject.fromObject(page.getPageCfg());

        Iterator ite = object.keys();
        while (ite.hasNext()) {
            JSONArray returnItem = new JSONArray();

            String currentKey = (String) ite.next();
            // outer格式：[["bgImg.jpg", "-1", "-1", "http://baidu.com"], ["bgImg2.jpg", "-1", "-1", "http://sina.com"]]
            JSONArray outer = (JSONArray) object.get(currentKey);
            for (int i = 0; i < outer.size(); i++) {
                JSONArray inner = (JSONArray) outer.get(i);
                // 如果没有找到当前时间启用的图片，默认显示第一个
                if (i == 0) {
                    returnItem = inner;
                }

                try {
                    if (isImageEnabled(inner)) {
                        returnItem = inner;
                        break;
                    }
                } catch (ParseException e) {
                    // 如果发生时间格式化异常，认为当前图片不符合要求。什么都不用做
                    log.warn(null, e);
                }
            }
            result.put(currentKey, returnItem);
        }

        return result;
    }

    /**
     * 校验图片是否启用
     * 1）当数组格式为：["bgImg.jpg"]，不区分时间，返回true。
     * 2）当数组格式为：["bgImg.jpg", "-1", "-1", "http://baidu.com"]，
     * 根据第二、第三个元素（生效时间）判断该图片当前是否启用。
     * @param array
     * @return true:启用，false:不启用
     * @throws ParseException 时间格式化异常
     */
    private boolean isImageEnabled(JSONArray array) throws ParseException {
        if (array.size() == 1) {
            return true;
        }

        // 当前时间
        Date currTime = new Date();
        if (!array.get(1).equals("-1")) {
            // 开始时间
            Date startTime = formater1.parse((String) array.get(1));
            if (!array.get(2).equals("-1")) {
                // 终止时间
                String endTimeStr = array.get(2) + " 23:59:59";
                Date endTime = formater2.parse(endTimeStr);

                if (currTime.after(startTime) && currTime.before(endTime)) {
                    return true;
                }
            } else {
                if (currTime.after(startTime)) {
                    return true;
                }
            }
        } else {
            if (!array.get(2).equals("-1")) {
                String endTimeStr = array.get(2) + " 23:59:59";
                Date endTime = formater2.parse(endTimeStr);
                if (currTime.before(endTime)) {
                    return true;
                }
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * 快速登录
     * @throws IOException
     */
    @Override
    public String quickLogin(LoginReq loginReq) {
        // 获取店铺认证配置信息
        AuthCfg config = null;
        Long storeId = loginReq.getNasId();
		if (storeId != null) {
			config = (AuthCfg) cacheApiMgr.getAuthCfg(storeId, loginReq.getSsid());
		} else {
			throw new IllegalArgumentException(
					sm.getString("o2o.portal.error.unknow.store"));
		}

		// 判断认证类型
		if (config.getAuthType().intValue() == AuthCfg.AUTH_TYPE_QUICKlOGIN) {
			// 注册并返回授权码
			String code = quickRegister(loginReq);
			// 重定向URI，从参数中获取
			String redirect_uri = FuncUtil.getLoginRedirectURI(loginReq.getRedirect_uri(), code, loginReq.getUserip(),
					false);
			return redirect_uri;
		} else if (config.getAuthType().intValue() == AuthCfg.AUTH_TYPE_ACCOUNTLOGIN) {
			Long templateId = loginReq.getTemplateId();
			// 获得模板信息
			ThemeTemplate template = cacheApiMgr.getThemeTemplate(storeId,
					loginReq.getSsid(), templateId);
			// 如果模板发生变化，页面参数也跟着一起变化
			loginReq.setTemplateId(template.getId());
			// 返回登录页面URI
			return cacheApiMgr.getThemePageURI(template,
					ThemePage.PAGE_TYPE_LOGIN) + loginReq.toParamString();
		}
        return null;
    }

    private String quickRegister(LoginReq loginReq) {
        RegistUser user = null;
        int userType = RegistUser.USER_TYPE_ONEBTN;
        // 生成缓存key,一键登录的用户取mac地址作为用户名
        RegistUserCacheKey key = new RegistUserCacheKey(loginReq.getNasId(),
            loginReq.getUsermac(), userType);
        if (StringUtils.isNotBlank(loginReq.getUsermac())) {
            // 查询用户
            user = cacheApiMgr.getRegistUserCacheMgr().get(key);
        }

        // 如果用户不存在，创建用户
        if (user == null) {
        	long ownerId = cacheApiMgr.getOwnerId(loginReq.getNasId(),
    				loginReq.getSsid());
            user = createQuickLoginUser(key, ownerId);
        } else {
            // 判断是否为黑名单用户
            if (cacheApiMgr.isBlackUser(user)) {
                throw new PortalException(PortalErrorCodes.BLACK_USER,
                    sm.getString("errorCode.60003"));
            }
        }
		return tokenMgr.generateCode(loginReq.getUsermac(), loginReq.getUserip(), loginReq.getNasId(),
				loginReq.getSsid(), userType, user);
	  }

    /**
     * 创建注册用户
     * @return 注册用户，用于增加在线用户和接入明细
     */
    private RegistUser createQuickLoginUser(RegistUserCacheKey key, long ownerId) {
        RegistUser user = new RegistUser();
        user.setStoreId(key.getStoreId());
        user.setUserName(key.getUserName());
        // 一键登录时，密码为“h3c绿洲平台”的简写
        user.setUserPassword(CommonUtils.encryptData("h3clzpt"));
        user.setUserType(RegistUser.USER_TYPE_ONEBTN);
        user.setLoginTime(System.currentTimeMillis());
        user.setOwnerId(ownerId);
        // 保存
        userMgr.saveRegistUser(user);
        cacheApiMgr.getRegistUserCacheMgr().put(key, user);
        return user;
    }

    /**
     * 短信认证登录
     */
    @Override
    public String smsLogin(SmsLoginReq loginReq) {
		// 注册并返回code
		String code = smsRegister(loginReq);
		// 重定向URI，从参数中获取
		return FuncUtil.getLoginRedirectURI(loginReq.getRedirect_uri(), code, loginReq.getUserip(), false);
    }

    private String smsRegister(SmsLoginReq param) {
        // 校验短信验证码
        AuthMessageCode authMessageCode = tokenMgr.getSmsCode(param
            .getUserName());
        if (authMessageCode == null) {
            throw new PortalException(
                sm.getString("o2o.portal.send.auth.message.not.send"));
        } else if ((System.currentTimeMillis() - authMessageCode
            .getCreateTime()) > authMessageCode.getExpire_in()) {
            throw new PortalException(
                sm.getString("o2o.portal.send.auth.message.expire"));
        } else if (!param.getPassword().equals(authMessageCode.getCode())) {
            throw new PortalException(
                sm.getString("o2o.portal.send.auth.message.not.matched"));
        }
        // 查询用户是否存在
        int userType = RegistUser.USER_TYPE_MESSAGE;
        // 生成缓存key
        RegistUserCacheKey key = new RegistUserCacheKey(param.getNasId(),
            param.getUserName(), userType);
        // 查询用户
        RegistUser user = cacheApiMgr.getRegistUserCacheMgr().get(key);

        if (user == null) {
        	long ownerId = cacheApiMgr.getOwnerId(param.getNasId(),
            		param.getSsid());
            // 如果不存在， 创建用户
            user = createSmsUser(param, key, ownerId);
        } else {
            // 判断是否为黑名单用户
            if (cacheApiMgr.isBlackUser(user)) {
                throw new PortalException(PortalErrorCodes.BLACK_USER,
                    sm.getString("errorCode.60003"));
            }
        }
        // 设置手机号码
        user.setPhoneNumber(param.getUserName());
        // 创建code
		return tokenMgr.generateCode(param.getUsermac(), param.getUserip(), param.getNasId(), param.getSsid(), userType,
				user);
	   }

    /**
     * 创建注册用户
     * @return 注册用户，用于增加在线用户和接入明细
     */
    private RegistUser createSmsUser(SmsLoginReq param, RegistUserCacheKey key, long ownerId) {
        RegistUser user = new RegistUser();
        user.setStoreId(param.getNasId());
        user.setUserName(param.getUserName());
        user.setUserPassword(CommonUtils.encryptData(param.getPassword()));
        user.setUserType(RegistUser.USER_TYPE_MESSAGE);
        user.setLoginTime(System.currentTimeMillis());
        user.setOwnerId(ownerId);

        // 保存
        userMgr.saveRegistUser(user);
        cacheApiMgr.getRegistUserCacheMgr().put(key, user);
        return user;
    }

	@Override
	public String accountLogin(SmsLoginReq loginReq) {
		// 注册并返回code
		String code = accountRegister(loginReq);
		// 重定向URI，从参数中获取
		String redirect_uri = FuncUtil.getLoginRedirectURI(loginReq.getRedirect_uri(), code, loginReq.getUserip(),
				false);
		return redirect_uri;
	}

    private String accountRegister(SmsLoginReq param) {
        // 查询用户是否存在
        int userType = RegistUser.USER_TYPE_ACCOUNT;
        RegistUser user = cacheApiMgr.getRegistUserCacheMgr().get(
            new RegistUserCacheKey(param.getNasId(), param.getUserName(),
                userType));

        if (user == null) {
            // 如果不存在
            throw new PortalException(
                sm.getString("o2o.portal.send.auth.account.user.not.exist"));
        } else {
            // 判断是否为黑名单用户
            if (cacheApiMgr.isBlackUser(user)) {
                throw new PortalException(PortalErrorCodes.BLACK_USER,
                    sm.getString("errorCode.60003"));
            }
            if (StringUtils.isBlank(param.getPassword()) || 
            		!param.getPassword().equals(user.getAndDecryptPassword())) {
                // 如果密码错误
                throw new PortalException(
                    sm.getString("o2o.portal.send.auth.account.password.incorrect"));
            }
        }
        // 创建code
		return tokenMgr.generateCode(param.getUsermac(), param.getUserip(), param.getNasId(), param.getSsid(), userType,
				user);
	   }

    @Override
	public String redirectToHome(HomePageReq homePageReq) {
		// 检查店铺认证配置，如果配置了认证成功页面，优先跳转至该页面
		String url = cacheApiMgr.getConfigureParam(homePageReq.getNas_id(),
				homePageReq.getSsid(), AuthCfg.STORE_CONFIGUE_URL);
		if (StringUtils.isNotBlank(url)) {
			return url;
		}
		// 获取home页面URI
		ThemeTemplate template = cacheApiMgr.getThemeTemplate(
				homePageReq.getNas_id(), homePageReq.getSsid(),
				homePageReq.getTemplateId());
		// 如果模板发生变化，页面参数也跟着一起变化
		homePageReq.setTemplateId(template.getId());
		String prefix = cacheApiMgr.getThemePageURI(template,
				ThemePage.PAGE_TYPE_HOME);

		return prefix + homePageReq.toParamString();
	}

    @Override
    public void sendAuthMessage(String phoneNO, Long storeId, String ssid) {
        AuthCfg authCfg = (AuthCfg) cacheApiMgr.getAuthCfg(storeId, ssid);
		String interval = cacheApiMgr.getPublishPara(storeId, 
				PublishMngPara.REPORT_INTERVAL_PARA_NAME);
        messageMgr.sendAuthMessage(phoneNO, authCfg.getSmsAuthContent(), interval);
    }

    public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void setMessageMgr(MessageMgr messageMgr) {
        this.messageMgr = messageMgr;
    }

    public void setThemePageMgr(ThemePageMgr themePageMgr) {
        this.themePageMgr = themePageMgr;
    }

    public void setUserMgr(RegistUserMgr userMgr) {
        this.userMgr = userMgr;
    }

    public void setShopWifiMgr(ShopWifiMgr shopWifiMgr) {
        this.shopWifiMgr = shopWifiMgr;
    }

    public void setTokenMgr(TokenMgr tokenMgr) {
        this.tokenMgr = tokenMgr;
    }

    public void setCacheApiMgr(CacheApiMgr cacheApiMgr) {
        this.cacheApiMgr = cacheApiMgr;
    }

    public void setWxWifiAuthUrl(String wxWifiAuthUrl) {
        this.wxWifiAuthUrl = wxWifiAuthUrl;
    }

	public HttpOrHttpsMgr getHttpOrHttps() {
		return httpOrHttps;
	}

	public void setHttpOrHttps(HttpOrHttpsMgr httpOrHttps) {
		this.httpOrHttps = httpOrHttps;
	}

}
