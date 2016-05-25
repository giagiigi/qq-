/*
 * Copyright (c) 2016, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC V300R002
 * Module Name : 
 * Date Created: 2016年1月15日
 * Creator     : donglicong
 * Description : 
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 * 2016年1月15日  donglicong        O2O-PORTAL project, new code file.
 * 
 *------------------------------------------------------------------------------
 */
package com.h3c.o2o.portal.login.entity;

/**
 * 微信连wifi参数
 *
 * @author donglicong
 */
public class WeixinConnectWifiPara {
    
    public static final String EXTEND_SEPARATOR = ",";

    private String appId;
    
    private String extend;
    
    private String timestamp;
    
    private String sign;
    
    private String authUrl;
    
    private int isWeixinConnectWifi;
    
    private String usermac;
    
    private String bssid;
    
    private String shopId;
    
    private String error;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getAuthUrl() {
        return authUrl;
    }

    public void setAuthUrl(String authUrl) {
        this.authUrl = authUrl;
    }

    public int getIsWeixinConnectWifi() {
        return isWeixinConnectWifi;
    }

    public void setIsWeixinConnectWifi(int isWeixinConnectWifi) {
        this.isWeixinConnectWifi = isWeixinConnectWifi;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getBssid() {
		return bssid;
	}

	public void setBssid(String bssid) {
		this.bssid = bssid;
	}

	public String getUsermac() {
		return usermac;
	}

	public void setUsermac(String usermac) {
		this.usermac = usermac;
	}

	@Override
	public String toString() {
		return "WeixinConnectWifiPara [appId=" + appId + ", extend=" + extend + ", timestamp=" + timestamp + ", sign="
				+ sign + ", authUrl=" + authUrl + ", isWeixinConnectWifi=" + isWeixinConnectWifi + ", usermac="
				+ usermac + ", bssid=" + bssid + ", shopId=" + shopId + ", error=" + error + "]";
	}
	

}
