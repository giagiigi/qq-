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

import com.h3c.o2o.portal.PortalException;
import com.h3c.o2o.portal.entity.RegistUser;
import com.h3c.o2o.portal.protocol.entity.AccessToken;
import com.h3c.o2o.portal.protocol.entity.AuthMessageCode;
import com.h3c.o2o.portal.protocol.entity.CodeInfo;
import com.h3c.o2o.portal.protocol.entity.ThirdPartyParam;
import com.h3c.o2o.portal.protocol.entity.TokenInfo;
import com.h3c.o2o.portal.protocol.entity.TpLoginInfo;

/**
 * Token��Code����ӿ�
 *
 * @author j09980
 */
public interface TokenMgr {

    /**
     * ����Code
     *
     * @param userType �û�����
     * @param user ע���û�
     * @return code
     */
	public String generateCode(String mac, String ip, Long storeId, String ssid, int userType, RegistUser user) throws PortalException;

    /**
     *  ����Token
     *
     * @param user ע���û�
     * @return Token
     */
    AccessToken generateToken(RegistUser user) throws PortalException;

    /**
     * ��ȡCodeInfo
     *
     * @param key key
     * @param isAutoRemove �Ƿ��Զ���Map���Ƴ�
     * @return CodeInfo
     */
    CodeInfo getCodeInfo(String key, boolean isAutoRemove);

    /**
     * ��ȡTokenInfo
     *
     * @param key key
     * @param isAutoRemove �Ƿ��Զ���Map���Ƴ�
     * @return TokenInfo
     */
    TokenInfo getTokenInfo(String key, boolean isAutoRemove);

    /**
     * ɾ��Code
     *
     * @param key key
     */
    void removeCode(String key);

    /**
     * ɾ��Token
     *
     * @param key key
     */
    void removeToken(String key);

    /**
     * ���������֤��
     *
     * @param key �ֻ�����
     * @param code ��֤��
     */
    void putSmsCode(String key, AuthMessageCode code);

    /**
     * ��ȡ������֤
     *
     * @param key �ֻ�����
     * @return ��֤��
     */
    AuthMessageCode getSmsCode(String key);

    /**
     * ɾ��������֤
     *
     * @param key �ֻ�����
     */
    void removeSmsCode(String key);

    /**
     * add description of methods here
     *
     * @param storeId
     * @param redirectUri
     */
    void putRedirectUri(Long storeId, String redirectUri);

    String getRedirectUri(Long storeId);

    void putTemplateId(String userMac, Long templateId);

    Long getTemplateId(String userMac);

    void removeTemplateId(String userMac);

    void putUserAgent(String userMac, String userAgent);

    String getUserAgent(String userMac);

    void putTpLoginInfo(String code, TpLoginInfo info);

    TpLoginInfo getTpLoginInfo(String code, boolean isAutoRemove);

    void putThirdPartyParam(Long userId, ThirdPartyParam param);

    ThirdPartyParam getThirdPartyParam(Long userId);

	String getSsid(String userMac);

	void putSsid(String userMac, String ssid);

	void removeSsid(String userMac);

	void removeUserAgent(String userMac);

	void removeRedirectUri(Long storeId);

	void removeTpLoginInfo(String code);

	void removeThirdPartyParam(Long userId);

	void putUserUrl(String userMac, String userUrl);

	String getUserUrl(String userMac);

	void removeUserUrl(String userMac);

}
