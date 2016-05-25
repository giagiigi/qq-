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
 * Token与Code管理接口
 *
 * @author j09980
 */
public interface TokenMgr {

    /**
     * 生成Code
     *
     * @param userType 用户类型
     * @param user 注册用户
     * @return code
     */
	public String generateCode(String mac, String ip, Long storeId, String ssid, int userType, RegistUser user) throws PortalException;

    /**
     *  生成Token
     *
     * @param user 注册用户
     * @return Token
     */
    AccessToken generateToken(RegistUser user) throws PortalException;

    /**
     * 获取CodeInfo
     *
     * @param key key
     * @param isAutoRemove 是否自动从Map中移除
     * @return CodeInfo
     */
    CodeInfo getCodeInfo(String key, boolean isAutoRemove);

    /**
     * 获取TokenInfo
     *
     * @param key key
     * @param isAutoRemove 是否自动从Map中移除
     * @return TokenInfo
     */
    TokenInfo getTokenInfo(String key, boolean isAutoRemove);

    /**
     * 删除Code
     *
     * @param key key
     */
    void removeCode(String key);

    /**
     * 删除Token
     *
     * @param key key
     */
    void removeToken(String key);

    /**
     * 缓存短信验证码
     *
     * @param key 手机号码
     * @param code 验证码
     */
    void putSmsCode(String key, AuthMessageCode code);

    /**
     * 获取短信验证
     *
     * @param key 手机号码
     * @return 验证码
     */
    AuthMessageCode getSmsCode(String key);

    /**
     * 删除短信验证
     *
     * @param key 手机号码
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
