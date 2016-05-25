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

import com.h3c.o2o.portal.login.entity.LoginReq;
import com.h3c.o2o.portal.login.entity.WeixinConnectWifiPara;
import com.h3c.o2o.portal.protocol.entity.AccessToken;
import com.h3c.o2o.portal.protocol.entity.LoginParam;
import com.h3c.o2o.portal.protocol.entity.OnlineUserInfo;
import com.h3c.o2o.portal.protocol.entity.TokenReq;
import com.h3c.o2o.portal.protocol.entity.TpLoginInfo;
import com.h3c.o2o.portal.protocol.entity.UserInfo;
import com.h3c.o2o.portal.protocol.entity.UserStatus;

/**
 * O2O协议处理接口
 *
 * @author j09980
 */
public interface ProtocolMgr {

    /**
     * 登录重定向报文处理
     *
     * @param param 登录参数
     * @return 重定向URL地址和storeId
     */
    String loginRedirect(LoginParam param, String ua);
    
    /**
     * 
     * 绿洲app重定向：直接认证成功，生成注册用户，推送默认模板template01的
     * ‘登录成功页面’ 
     *
     * @param param 重定向参数，userurl中包含authtype=lvzhouapp
     * @param ua
     * @return
     */
    String appRedirect(LoginParam param, String ua);
    
    String tpLoginVerify(TpLoginInfo info);

    /**
     * 获取AccessToken
     *
     * @return AccessToken
     */
    AccessToken getAccessToken(TokenReq tokenReq);

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    UserInfo getUserInfo(String token, String remoteHostIp);

    /**
     * 上传在线用户列表
     *
     * @param onlineUserInfo 在线用户列表
     * @return 用户状态
     */
    UserStatus uploadOnlines(OnlineUserInfo onlineUserInfo);

    /**
     * 用户下线操作
     */
    void userOffline(String ip);
    
    /**
     * 是否仅仅启用了微信连Wifi
     * @param param
     * @return
     */
    public boolean isOnlyEnabledWxWifi(LoginParam param);
    
    /**
     * 获取微信连Wifi参数
     * @param loginInfo
     * @return
     */
    public WeixinConnectWifiPara getWxWifiPara(LoginReq loginInfo);
}
