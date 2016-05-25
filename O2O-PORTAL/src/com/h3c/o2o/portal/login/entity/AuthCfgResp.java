/*
 * Copyright (c) 2015, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC V300R002
 * Module Name : 
 * Date Created: 2015-7-27
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
package com.h3c.o2o.portal.login.entity;

import java.io.Serializable;

/**
 * add description of types here
 *
 * @author j09980
 */
public class AuthCfgResp implements Serializable {

    /**
     * add description of field here
     */
    private static final long serialVersionUID = -1801852270704356462L;

    private int phone2guding;

    private int weixin;

    private int qq;
    
    private WeixinConnectWifiPara weixinConnectWifi;

    public int getPhone2guding() {
        return phone2guding;
    }

    public void setPhone2guding(int phone2guding) {
        this.phone2guding = phone2guding;
    }

    public int getWeixin() {
        return weixin;
    }

    public void setWeixin(int weixin) {
        this.weixin = weixin;
    }

    public int getQq() {
        return qq;
    }

    public void setQq(int qq) {
        this.qq = qq;
    }

    public WeixinConnectWifiPara getWeixinConnectWifi() {
        return weixinConnectWifi;
    }

    public void setWeixinConnectWifi(WeixinConnectWifiPara weixinConnectWifi) {
        this.weixinConnectWifi = weixinConnectWifi;
    }

}
