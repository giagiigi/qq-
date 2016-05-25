/*
 * Copyright (c) 2015, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC V300R002
 * Module Name :
 * Date Created: 2015-7-15
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
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.h3c.o2o.portal.PortalException;
import com.h3c.o2o.portal.entity.RegistUser;
import com.h3c.o2o.portal.functioncontrol.func.FunctionControlMgrImpl;
import com.h3c.o2o.portal.protocol.entity.AccessToken;
import com.h3c.o2o.portal.protocol.entity.AuthMessageCode;
import com.h3c.o2o.portal.protocol.entity.CodeInfo;
import com.h3c.o2o.portal.protocol.entity.ThirdPartyParam;
import com.h3c.o2o.portal.protocol.entity.TokenInfo;
import com.h3c.o2o.portal.protocol.entity.TpLoginInfo;
import com.h3c.o2o.portal.util.TokenGenerator;

/**
 * Token��Code����ʵ����
 *
 * @author j09980
 */
public class TokenMgrImpl implements TokenMgr {

    /**
     * ��־����
     */
    private Log log = LogFactory.getLog(getClass());

    /** ���code���û��Ķ�Ӧ��ϵ��key:code */
    private ConcurrentMap<String, CodeInfo> codeMap = new ConcurrentHashMap<String, CodeInfo>();

    /** ���token���� */
    private ConcurrentMap<String, TokenInfo> tokenMap = new ConcurrentHashMap<String, TokenInfo>();

    /** ��Ŷ���У����,key:phoneNO */
    private ConcurrentMap<String, AuthMessageCode> smsCodeMap = new ConcurrentHashMap<String, AuthMessageCode>();

    /** ��ŵ�¼�ض���URI����������֤ʱ���޷�Я������Ҫ�����档key:storeId */
    private ConcurrentMap<Long, String> redirectUriMap = new ConcurrentHashMap<Long, String>();

    /** ģ��ID����֤�ɹ����������޷�Я������Ҫ�����档key:userMac */
    private ConcurrentMap<String, Long> templateIdMap = new ConcurrentHashMap<String, Long>();

    /** ���userAgent�����а�������ϵͳ���ն˳��̵���Ϣ�����������û��ͽ�����ϸʱʹ�á�
     * ��������֤ʱ���޷�Я������Ҫ�����档key:userMac
     * ��ȡ��token��������
     */
    private ConcurrentMap<String, String> userAgentMap = new ConcurrentHashMap<String, String>();

    /** ��ŵ�������¼��Ĳ�����Ϣ, key:code*/
    private ConcurrentMap<String, TpLoginInfo> tpLoginInfoMap = new ConcurrentHashMap<String, TpLoginInfo>();

    /** ��ŵ�����ҳ����֤ʱ�û�����ʱ�����ɹ�ҳ��URL, key:userid*/
    private ConcurrentMap<Long, ThirdPartyParam> thirdPartyParamMap = new ConcurrentHashMap<Long, ThirdPartyParam>();

    /** ��ŵ���SSID, key:userMac*/
    private ConcurrentMap<String, String> ssidMap = new ConcurrentHashMap<String, String>();

    /** ����û�������ض���ǰ��URI, key:userMac*/
    private ConcurrentMap<String, String> userUrlMap = new ConcurrentHashMap<String, String>();

    /**
     * ����Token��Code���-��λ��
     */
    private int interval;

    /**
     * ��ʼ��
     */
    public void init() {
        new TokenValidThread().start();
    }
    
    /**
     * Ϊ�˱��ⳡ��ID��IP��MAC�仯���������һ��
     * @param mac
     * @param ip
     * @param storeId
     * @param user
     */
	private void updateRegisterUser(String mac, String ip, Long storeId, RegistUser user) {
		if (null == user) {
			return;
		}
		if (null != storeId) {
			user.setStoreId(storeId);
		}
		if (StringUtils.isNotBlank(ip)) {
			user.setUserIp(ip);
		}
		if (StringUtils.isNotBlank(mac)) {
			user.setUserMac(mac);
		}
	}
    
	/**
	 * {@inheritDoc}
	 * 
	 * @throws Exception
	 */
	@Override
	public String generateCode(String mac, String ip, Long storeId, String ssid, int userType, RegistUser user)
			throws PortalException {
		// У���û��Ƿ�����, ������߽��ߵ����û���
		mac = (null != user && StringUtils.isBlank(mac)) ? user.getUserMac() : mac;
		ip = (null != user && StringUtils.isBlank(ip)) ? user.getUserIp() : ip;
		ssid = (StringUtils.isNotBlank(mac) && StringUtils.isBlank(ssid)) ? getSsid(mac) : ssid;
		FunctionControlMgrImpl.getInstance().onlineCheck(mac, ip, storeId, ssid, userType);

		updateRegisterUser(mac, ip, storeId, user);

		String code = TokenGenerator.generate();
		CodeInfo info = new CodeInfo();
		info.setRegistUser(user);
		info.setCreateTime(System.currentTimeMillis());
		info.setType(userType);
		info.setExpire_in(CodeInfo.CODE_ALIVE_TIME);
		codeMap.put(code, info);
		if (log.isDebugEnabled()) {
			log.debug("login code: " + code);
		}
		return code;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AccessToken generateToken(RegistUser user) throws PortalException {
		/**
		 * У���û��Ƿ�����,������߽��ߵ����û���
		 */
		if (null != user) {
			String ssid = getSsid(user.getUserMac());
			FunctionControlMgrImpl.getInstance().onlineCheck(user.getUserMac(), user.getUserIp(),
					user.getStoreId(), ssid, user.getUserType());
		} else {
			log.warn("RegistUser is null!");
		}

		// ����token
		String tokenStr = TokenGenerator.generate();
		AccessToken token = new AccessToken(tokenStr, AccessToken.TOKEN_ALIVE_TIME);
		TokenInfo tokenInfo = new TokenInfo();
		tokenInfo.setAccessToken(token);
		tokenInfo.setTokenCreateTime(System.currentTimeMillis());
		tokenInfo.setRegistUser(user);
		// ����token
		tokenMap.put(tokenStr, tokenInfo);
		return token;
	}

    /**
     * ��ȡCode<br>
     * Ϊ�˱��Ⲣ��ʱ���ظ���ȡ�������ĺ󼴷�<br>
     */
    @Override
    public CodeInfo getCodeInfo(String key, boolean isAutoRemove) {
        return isAutoRemove ? codeMap.remove(key) : codeMap.get(key);
    }

	/**
	 * ��ȡAccessToken<br>
	 * Ϊ�˱��Ⲣ��ʱ���ظ���ȡ�������ĺ󼴷�<br>
	 */
	@Override
	public TokenInfo getTokenInfo(String key, boolean isAutoRemove) {
		return isAutoRemove ? tokenMap.remove(key) : tokenMap.get(key);
	}

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeCode(String key) {
        codeMap.remove(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeToken(String key) {
        tokenMap.remove(key);
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    @Override
    public void putSmsCode(String key, AuthMessageCode code) {
        smsCodeMap.put(key, code);
    }

    @Override
    public AuthMessageCode getSmsCode(String key) {
        return smsCodeMap.get(key);
    }

    @Override
    public void removeSmsCode(String key) {
        smsCodeMap.remove(key);
    }

    @Override
    public void putRedirectUri(Long storeId, String redirectUri) {
        redirectUriMap.put(storeId, redirectUri);
    }

    @Override
    public String getRedirectUri(Long storeId) {
        return redirectUriMap.get(storeId);
    }

    @Override
    public void removeRedirectUri(Long storeId) {
        redirectUriMap.remove(storeId);
    }
    @Override
    public void putTemplateId(String userMac, Long templateId) {
        templateIdMap.put(userMac, templateId);
    }

    @Override
    public Long getTemplateId(String userMac) {
        return templateIdMap.get(userMac);
    }

    @Override
    public void removeTemplateId(String userMac) {
    	templateIdMap.remove(userMac);
    }

    @Override
    public void putUserAgent(String userMac, String userAgent) {
        userAgentMap.put(userMac, userAgent);
    }

    @Override
    public String getUserAgent(String userMac) {
        return userAgentMap.get(userMac);
    }

    @Override
    public void removeUserAgent(String userMac) {
        userAgentMap.remove(userMac);
    }

    @Override
    public void putTpLoginInfo(String code, TpLoginInfo info) {
        tpLoginInfoMap.put(code, info);
    }

    @Override
    public TpLoginInfo getTpLoginInfo(String code, boolean isAutoRemove) {
        return isAutoRemove ? tpLoginInfoMap.remove(code) : tpLoginInfoMap.get(code);
    }

    @Override
    public void removeTpLoginInfo(String code) {
        tpLoginInfoMap.remove(code);
    }

    @Override
    public void putThirdPartyParam(Long userId, ThirdPartyParam param) {
        thirdPartyParamMap.put(userId, param);
    }

    @Override
    public ThirdPartyParam getThirdPartyParam(Long userId) {
        return thirdPartyParamMap.get(userId);
    }

    @Override
    public void removeThirdPartyParam(Long userId) {
        thirdPartyParamMap.remove(userId);
    }

    @Override
    public String getSsid(String userMac) {
		return ssidMap.get(userMac);
	}

	@Override
	public void putSsid(String userMac, String ssid) {
		try {
			this.ssidMap.put(userMac, URLDecoder.decode(ssid, "utf-8"));
		} catch (UnsupportedEncodingException e) {
			throw new PortalException("Unsupported encode.");
		}
	}

    @Override
    public void removeSsid(String userMac) {
    	this.ssidMap.remove(userMac);
    }

	/**
     * Token��Ч�Լ���߳�
     *
     * @author j09980
     */
    private class TokenValidThread extends Thread {

        /**
         * ���췽��
         */
        public TokenValidThread() {
            setName("TokenValidThread");
            setDaemon(true);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void run() {
            for (;;) {
                try {
                    for (Entry<String, CodeInfo> entry : codeMap.entrySet()) {
                        String key = entry.getKey();
                        CodeInfo value = entry.getValue();
                        if (System.currentTimeMillis() - value.getCreateTime() > value
                            .getExpire_in() * 1000) {
                            removeCode(key);
                        }
                    }
                    for (Entry<String, TokenInfo> entry : tokenMap.entrySet()) {
                        String key = entry.getKey();
                        TokenInfo value = entry.getValue();
                        if (System.currentTimeMillis()
                            - value.getTokenCreateTime() > value
                            .getAccessToken().getExpire_in() * 1000) {
                            removeToken(key);
                        }
                    }
                    for (Entry<String, AuthMessageCode> entry : smsCodeMap
                        .entrySet()) {
                        String key = entry.getKey();
                        AuthMessageCode value = entry.getValue();
                        if (System.currentTimeMillis() - value.getCreateTime() > value
                            .getExpire_in()) {
                            removeSmsCode(key);
                        }
                    }
                    TimeUnit.SECONDS.sleep(interval);
                } catch (Exception e) {
                    log.warn(null, e);
                }
            }
        }
    }

	@Override
	public void putUserUrl(String userMac, String userUrl) {
		if (StringUtils.isBlank(userMac)) {
			throw new PortalException("User mac cannot be NULL.");
		}
		if (StringUtils.isBlank(userUrl)) {
			return;
		}
		this.userUrlMap.put(userMac, userUrl);
	}

	@Override
	public String getUserUrl(String userMac) {
		return this.userUrlMap.get(userMac);
	}

	@Override
	public void removeUserUrl(String userMac) {
		this.userUrlMap.remove(userMac);
	}

}
