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

    /** ��Դ���ʶ��� */
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

    /** ���ݻ���ӿ� */
    private CacheApiMgr cacheApiMgr;

    private Log log = LogFactory.getLog(getClass());

    @Override
    public JSONObject getPageImageInfo(PageImageReq pageReq) {
        JSONObject result = null;
        if (pageReq.getPageType() == null) {
            throw new IllegalArgumentException("Illegal pageType.");
        }

        if (pageReq.getTemplateId() != null) {
            // ����ģ��ID��ѯģ��
			ThemeTemplate template = cacheApiMgr.getThemeTemplate(
					pageReq.getNasId(), pageReq.getSsid(),
					Long.valueOf(pageReq.getTemplateId()));
            // ���û�ҵ����׳��쳣
            if (template == null) {
                throw new PortalException(
                    sm.getString("o2o.portal.quickLogin.store.notConfigued"));
            }
            // ��ȡͼƬ��Ϣ
            ThemePage page = themePageMgr.getThemePage(template,
                Integer.valueOf(pageReq.getPageType()));
            result = getPageConfig(page);
        } else {
            throw new IllegalArgumentException("Illegal templateId.");
        }
        return result;
    }

    /**
     * ��ȡ��֤����
     */
    public AuthCfgResp getAuthConfig(LoginReq confPara) {
        AuthCfgResp resp = new AuthCfgResp();
        AuthCfg authconf = null;
		if (null != confPara.getNasId()) {
			authconf = (AuthCfg) cacheApiMgr.getAuthCfg(
			    confPara.getNasId(), confPara.getSsid());
		}
        // ���û�ҵ�������֤���ã��������е�½��ʽ
        if (authconf == null) {
            resp.setPhone2guding(0);
            resp.setQq(0);
            resp.setWeixin(0);
            return resp;
        }

        // �̶��˺źͶ�����֤��0���������ã�1��������֤��2���̶��˺ţ�3��������
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
        // ΢����֤��0�����ã�1������
        resp.setWeixin(authconf.getIsEnableWinxin());
        // QQ��֤��0�����ã�1������
        resp.setQq(authconf.getIsEnableQQ());
        // ΢����Wifi: ����ֻ��Ҫ֪��΢����Wi-Fi�Ƿ������ü���
        WeixinConnectWifiPara paras = new WeixinConnectWifiPara();
        paras.setIsWeixinConnectWifi(authconf.getIsWeixinConnectWifi());
        resp.setWeixinConnectWifi(paras);
        return resp;
    }
    
    /**
     * 
     * ��ȡ΢����wifi���� 
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
        // ��ʽ��mac��ַ
        formatMacAdd(confPara);
        result.setIsWeixinConnectWifi(AuthCfg.IS_ENABLE_YES);
        // ��ѯshopWifiInfo
        ShopWifiInfo shopWifiInfo = shopWifiMgr.queryShopWifiInfo(confPara.getNasId());
        // У������
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
        // ��ѯ���ںŲ���ȡappid
        String appId = getWeixinAcount(shopWifiInfo).getAppId();
        // ������չ��Ϣ
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
        	log.debug("Generic AuthUrl�� " + authUrl);
        }
        String secretkey = shopWifiInfo.getSecretkey();
        result.setUsermac(confPara.getUsermac());
        // ʹ�ÿյ�bssid
        result.setBssid(null);
        
        result.setAppId(appId);
        result.setAuthUrl(authUrl);
        result.setTimestamp(timestemp);
        result.setShopId(shopWifiInfo.getShopId());
        
        try {
            // ����ǩ��
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
	 * У��΢����Wifi����
	 * 
	 * @param shopWifiInfo
	 * @param confPara
	 * @throws PortalException
	 */
	private void checkWxWifiConfig(ShopWifiInfo shopWifiInfo, LoginReq confPara) throws PortalException {
		// ��δ�ҵ�������ã��׳��쳣������ʾ.
		if (null == shopWifiInfo) {
			String errorInfo = "Weixin Wifi info is not found for shop:" + confPara.getNasId() + " LoginReq: "
					+ confPara;
			log.warn(errorInfo);
			throw new PortalException(sm.getString("errorCode.60022", errorInfo));
		} else {
			// У��SSID�������û����Ӵ����SSID��
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
     * ��ʽ��Mac��ַ
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
     * ΢����Wifi��Mac��ַ��Ҫ������ת��һ��<br>
     * �û��ֻ�mac��ַ����ʽð�ŷָ����ַ�����17����������ĸСд�����磺00:1f:7a:ad:5c:a8<br>
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
        // MD5ǩ������
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
     * ��ȡ΢�Ź��ں� 
     *
     * @param shopWifiInfo
     * @return
     */
    private WeixinAccount getWeixinAcount(ShopWifiInfo shopWifiInfo) {
        WeixinAccount account = shopWifiInfo.getWeixinAccount();
        if (account != null) {
            return account;
        }
        
        // ���û�������ŵ���Ϣ��ʹ��ϵͳĬ�ϵĹ��ں�
        WxWifiOptResponse4Json response = new WxWifiOptResponse4Json();
        WxWifiOptRequest4Json request = new WxWifiOptRequest4Json();
        
        Gson gson = new Gson();
        request.setMsgType(WxWifiOptRequest4Json.MSG_TYPE_DEFAULT_ACCOUNT);
        try {
            // ������Ϣ������ȡϵͳĬ�ϵĹ��ں���Ϣ
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
     * ��ȡҳ��������Ϣ
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
            // outer��ʽ��[["bgImg.jpg", "-1", "-1", "http://baidu.com"], ["bgImg2.jpg", "-1", "-1", "http://sina.com"]]
            JSONArray outer = (JSONArray) object.get(currentKey);
            for (int i = 0; i < outer.size(); i++) {
                JSONArray inner = (JSONArray) outer.get(i);
                // ���û���ҵ���ǰʱ�����õ�ͼƬ��Ĭ����ʾ��һ��
                if (i == 0) {
                    returnItem = inner;
                }

                try {
                    if (isImageEnabled(inner)) {
                        returnItem = inner;
                        break;
                    }
                } catch (ParseException e) {
                    // �������ʱ���ʽ���쳣����Ϊ��ǰͼƬ������Ҫ��ʲô��������
                    log.warn(null, e);
                }
            }
            result.put(currentKey, returnItem);
        }

        return result;
    }

    /**
     * У��ͼƬ�Ƿ�����
     * 1���������ʽΪ��["bgImg.jpg"]��������ʱ�䣬����true��
     * 2���������ʽΪ��["bgImg.jpg", "-1", "-1", "http://baidu.com"]��
     * ���ݵڶ���������Ԫ�أ���Чʱ�䣩�жϸ�ͼƬ��ǰ�Ƿ����á�
     * @param array
     * @return true:���ã�false:������
     * @throws ParseException ʱ���ʽ���쳣
     */
    private boolean isImageEnabled(JSONArray array) throws ParseException {
        if (array.size() == 1) {
            return true;
        }

        // ��ǰʱ��
        Date currTime = new Date();
        if (!array.get(1).equals("-1")) {
            // ��ʼʱ��
            Date startTime = formater1.parse((String) array.get(1));
            if (!array.get(2).equals("-1")) {
                // ��ֹʱ��
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
     * ���ٵ�¼
     * @throws IOException
     */
    @Override
    public String quickLogin(LoginReq loginReq) {
        // ��ȡ������֤������Ϣ
        AuthCfg config = null;
        Long storeId = loginReq.getNasId();
		if (storeId != null) {
			config = (AuthCfg) cacheApiMgr.getAuthCfg(storeId, loginReq.getSsid());
		} else {
			throw new IllegalArgumentException(
					sm.getString("o2o.portal.error.unknow.store"));
		}

		// �ж���֤����
		if (config.getAuthType().intValue() == AuthCfg.AUTH_TYPE_QUICKlOGIN) {
			// ע�Ტ������Ȩ��
			String code = quickRegister(loginReq);
			// �ض���URI���Ӳ����л�ȡ
			String redirect_uri = FuncUtil.getLoginRedirectURI(loginReq.getRedirect_uri(), code, loginReq.getUserip(),
					false);
			return redirect_uri;
		} else if (config.getAuthType().intValue() == AuthCfg.AUTH_TYPE_ACCOUNTLOGIN) {
			Long templateId = loginReq.getTemplateId();
			// ���ģ����Ϣ
			ThemeTemplate template = cacheApiMgr.getThemeTemplate(storeId,
					loginReq.getSsid(), templateId);
			// ���ģ�巢���仯��ҳ�����Ҳ����һ��仯
			loginReq.setTemplateId(template.getId());
			// ���ص�¼ҳ��URI
			return cacheApiMgr.getThemePageURI(template,
					ThemePage.PAGE_TYPE_LOGIN) + loginReq.toParamString();
		}
        return null;
    }

    private String quickRegister(LoginReq loginReq) {
        RegistUser user = null;
        int userType = RegistUser.USER_TYPE_ONEBTN;
        // ���ɻ���key,һ����¼���û�ȡmac��ַ��Ϊ�û���
        RegistUserCacheKey key = new RegistUserCacheKey(loginReq.getNasId(),
            loginReq.getUsermac(), userType);
        if (StringUtils.isNotBlank(loginReq.getUsermac())) {
            // ��ѯ�û�
            user = cacheApiMgr.getRegistUserCacheMgr().get(key);
        }

        // ����û������ڣ������û�
        if (user == null) {
        	long ownerId = cacheApiMgr.getOwnerId(loginReq.getNasId(),
    				loginReq.getSsid());
            user = createQuickLoginUser(key, ownerId);
        } else {
            // �ж��Ƿ�Ϊ�������û�
            if (cacheApiMgr.isBlackUser(user)) {
                throw new PortalException(PortalErrorCodes.BLACK_USER,
                    sm.getString("errorCode.60003"));
            }
        }
		return tokenMgr.generateCode(loginReq.getUsermac(), loginReq.getUserip(), loginReq.getNasId(),
				loginReq.getSsid(), userType, user);
	  }

    /**
     * ����ע���û�
     * @return ע���û����������������û��ͽ�����ϸ
     */
    private RegistUser createQuickLoginUser(RegistUserCacheKey key, long ownerId) {
        RegistUser user = new RegistUser();
        user.setStoreId(key.getStoreId());
        user.setUserName(key.getUserName());
        // һ����¼ʱ������Ϊ��h3c����ƽ̨���ļ�д
        user.setUserPassword(CommonUtils.encryptData("h3clzpt"));
        user.setUserType(RegistUser.USER_TYPE_ONEBTN);
        user.setLoginTime(System.currentTimeMillis());
        user.setOwnerId(ownerId);
        // ����
        userMgr.saveRegistUser(user);
        cacheApiMgr.getRegistUserCacheMgr().put(key, user);
        return user;
    }

    /**
     * ������֤��¼
     */
    @Override
    public String smsLogin(SmsLoginReq loginReq) {
		// ע�Ტ����code
		String code = smsRegister(loginReq);
		// �ض���URI���Ӳ����л�ȡ
		return FuncUtil.getLoginRedirectURI(loginReq.getRedirect_uri(), code, loginReq.getUserip(), false);
    }

    private String smsRegister(SmsLoginReq param) {
        // У�������֤��
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
        // ��ѯ�û��Ƿ����
        int userType = RegistUser.USER_TYPE_MESSAGE;
        // ���ɻ���key
        RegistUserCacheKey key = new RegistUserCacheKey(param.getNasId(),
            param.getUserName(), userType);
        // ��ѯ�û�
        RegistUser user = cacheApiMgr.getRegistUserCacheMgr().get(key);

        if (user == null) {
        	long ownerId = cacheApiMgr.getOwnerId(param.getNasId(),
            		param.getSsid());
            // ��������ڣ� �����û�
            user = createSmsUser(param, key, ownerId);
        } else {
            // �ж��Ƿ�Ϊ�������û�
            if (cacheApiMgr.isBlackUser(user)) {
                throw new PortalException(PortalErrorCodes.BLACK_USER,
                    sm.getString("errorCode.60003"));
            }
        }
        // �����ֻ�����
        user.setPhoneNumber(param.getUserName());
        // ����code
		return tokenMgr.generateCode(param.getUsermac(), param.getUserip(), param.getNasId(), param.getSsid(), userType,
				user);
	   }

    /**
     * ����ע���û�
     * @return ע���û����������������û��ͽ�����ϸ
     */
    private RegistUser createSmsUser(SmsLoginReq param, RegistUserCacheKey key, long ownerId) {
        RegistUser user = new RegistUser();
        user.setStoreId(param.getNasId());
        user.setUserName(param.getUserName());
        user.setUserPassword(CommonUtils.encryptData(param.getPassword()));
        user.setUserType(RegistUser.USER_TYPE_MESSAGE);
        user.setLoginTime(System.currentTimeMillis());
        user.setOwnerId(ownerId);

        // ����
        userMgr.saveRegistUser(user);
        cacheApiMgr.getRegistUserCacheMgr().put(key, user);
        return user;
    }

	@Override
	public String accountLogin(SmsLoginReq loginReq) {
		// ע�Ტ����code
		String code = accountRegister(loginReq);
		// �ض���URI���Ӳ����л�ȡ
		String redirect_uri = FuncUtil.getLoginRedirectURI(loginReq.getRedirect_uri(), code, loginReq.getUserip(),
				false);
		return redirect_uri;
	}

    private String accountRegister(SmsLoginReq param) {
        // ��ѯ�û��Ƿ����
        int userType = RegistUser.USER_TYPE_ACCOUNT;
        RegistUser user = cacheApiMgr.getRegistUserCacheMgr().get(
            new RegistUserCacheKey(param.getNasId(), param.getUserName(),
                userType));

        if (user == null) {
            // ���������
            throw new PortalException(
                sm.getString("o2o.portal.send.auth.account.user.not.exist"));
        } else {
            // �ж��Ƿ�Ϊ�������û�
            if (cacheApiMgr.isBlackUser(user)) {
                throw new PortalException(PortalErrorCodes.BLACK_USER,
                    sm.getString("errorCode.60003"));
            }
            if (StringUtils.isBlank(param.getPassword()) || 
            		!param.getPassword().equals(user.getAndDecryptPassword())) {
                // ����������
                throw new PortalException(
                    sm.getString("o2o.portal.send.auth.account.password.incorrect"));
            }
        }
        // ����code
		return tokenMgr.generateCode(param.getUsermac(), param.getUserip(), param.getNasId(), param.getSsid(), userType,
				user);
	   }

    @Override
	public String redirectToHome(HomePageReq homePageReq) {
		// ��������֤���ã������������֤�ɹ�ҳ�棬������ת����ҳ��
		String url = cacheApiMgr.getConfigureParam(homePageReq.getNas_id(),
				homePageReq.getSsid(), AuthCfg.STORE_CONFIGUE_URL);
		if (StringUtils.isNotBlank(url)) {
			return url;
		}
		// ��ȡhomeҳ��URI
		ThemeTemplate template = cacheApiMgr.getThemeTemplate(
				homePageReq.getNas_id(), homePageReq.getSsid(),
				homePageReq.getTemplateId());
		// ���ģ�巢���仯��ҳ�����Ҳ����һ��仯
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
