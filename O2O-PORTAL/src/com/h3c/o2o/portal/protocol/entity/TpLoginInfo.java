/*
 * Copyright (c) 2007-2013, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC v7
 * Module Name : iMC
 * Date Created: 2015-7-10
 * Creator     : dkf5133
 * Description :
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 * 2015-7-10     dkf5133          iMC project, new code file.
 *
 *------------------------------------------------------------------------------
 */
package com.h3c.o2o.portal.protocol.entity;

import java.io.Serializable;

/**
 * @author dkf5133
 *
 */
public class TpLoginInfo implements Serializable {

	/**
     * add description of field here
     */
    private static final long serialVersionUID = -7913378786162972059L;

    private String openId;
    
    /**
     * ΢����WiFiɨһɨ���̣���������ֶδ���SSID
     */
    private String ssid;
    
    /**
     * ΢����WiFiɨһɨ���̣���������ֶδ���USERMAC
     */
    private String userMac;
    
    /**
     * ΢����WiFiɨһɨ���̣���������ֶδ���userIp
     */
    private String userIp;

    /** ��������֤���ͣ���ע���û����Ͷ�Ӧ��4��֧������֤��5��QQ��֤��6��΢�Ź��ں���֤ */
	private Integer type;

	/** ����ID */
	private Long storeId;

	/** ��չ��Ϣ */
	private String extInfo;

	private Long ownerId;

	/**
	 * ��΢����Wifi������redirect_uri��Ҫ��URL�л�ȡ
	 */
	private String redirect_uri;
	
	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public String getExtInfo() {
		return extInfo;
	}

	public void setExtInfo(String extInfo) {
		this.extInfo = extInfo;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public String getRedirect_uri() {
		return redirect_uri;
	}

	public void setRedirect_uri(String redirect_uri) {
		this.redirect_uri = redirect_uri;
	}

	public String getSsid() {
		return ssid;
	}

	public void setSsid(String ssid) {
		this.ssid = ssid;
	}

	public String getUserMac() {
		return userMac;
	}

	public void setUserMac(String userMac) {
		this.userMac = userMac;
	}

	public String getUserIp() {
		return userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}

	@Override
	public String toString() {
		return "TpLoginInfo [openId=" + openId + ", ssid=" + ssid + ", userMac=" + userMac + ", userIp=" + userIp
				+ ", type=" + type + ", storeId=" + storeId + ", extInfo=" + extInfo + ", ownerId=" + ownerId
				+ ", redirect_uri=" + redirect_uri + "]";
	}
	
}
