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
package com.h3c.o2o.portal.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.AccessType;

/**
 * @author dkf5133
 *
 */
@Entity
@Table(name = "TBL_UAM_ONLINE_USER")
@AccessType("field")
public class OnlineUser implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -5596436394266771952L;

	/**主键*/
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

    /**店铺ID*/
    @Column(name = "STORE_ID", nullable = false)
	private Long storeId;

    /**用户ID*/
    @Column(name = "USER_ID")
	private Long userId;

    /**商户ID*/
    @Column(name = "OWNER_ID", nullable = false)
	private Long ownerId;

    /**用户名：一键认证是MAC地址；短信认证是手机号；微信客户端认证是微信号；支付宝认证是支付宝帐号名称*/
    @Column(name = "USER_NAME", length = 128)
	private String userName;

    /**用户认证类型*/
    @Column(name = "USER_TYPE", nullable = false)
	private Integer userType;

    /**接入开始时间*/
    @Column(name = "ACCESS_START_TIME", nullable = false)
	private Long accessStartTime;

    /**接入时长（单位：秒）*/
    @Column(name = "ACCESS_DURATION")
	private Long accessDuration;

    /**接入AP地址*/
    @Column(name = "ACCESS_AC_IP")
	private Long accessAcIp;

    /**接入SSID*/
    @Column(name = "ACCESS_SSID")
	private String accessSsid;

    /**累计流入字节数*/
    @Column(name = "INPUT_BYTES")
    private Long inputBytes;

    /**累计流出字节数*/
    @Column(name = "OUTPUT_BYTES")
    private Long outputBytes;

    /**终端IP地址*/
    @Column(name = "USER_IP")
	private Long userIp;

    /**终端MAC地址*/
	@Column(name = "USER_MAC", length = 64)
	private String userMac;

	/**手机号。如果有多个，使用“；”分隔*/
	@Column(name = "MOBILE_NO", length = 128)
	private String mobileNo;

	/**终端厂商名称*/
	@Column(name = "DEV_MANUFACTURER", length = 64)
	private String devManufacturer;

	/**终端操作系统*/
	@Column(name = "DEV_OS_TYPE", length = 64)
	private String devOsType;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public Long getAccessStartTime() {
		return accessStartTime;
	}

	public void setAccessStartTime(Long accessStartTime) {
		this.accessStartTime = accessStartTime;
	}

	public Long getAccessDuration() {
		return accessDuration;
	}

	public void setAccessDuration(Long accessDuration) {
		this.accessDuration = accessDuration;
	}

	public Long getAccessAcIp() {
		return accessAcIp;
	}

	public void setAccessAcIp(Long accessAcIp) {
		this.accessAcIp = accessAcIp;
	}

	public String getAccessSsid() {
		return accessSsid;
	}

	public void setAccessSsid(String accessSsid) {
		this.accessSsid = accessSsid;
	}

	public Long getInputBytes() {
		return inputBytes;
	}

	public void setInputBytes(Long inputBytes) {
		this.inputBytes = inputBytes;
	}

	public Long getOutputBytes() {
		return outputBytes;
	}

	public void setOutputBytes(Long outputBytes) {
		this.outputBytes = outputBytes;
	}

	public Long getUserIp() {
		return userIp;
	}

	public void setUserIp(Long userIp) {
		this.userIp = userIp;
	}

	public String getUserMac() {
		return userMac;
	}

	public void setUserMac(String userMac) {
		this.userMac = userMac;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getDevManufacturer() {
		return devManufacturer;
	}

	public void setDevManufacturer(String devManufacturer) {
		this.devManufacturer = devManufacturer;
	}

	public String getDevOsType() {
		return devOsType;
	}

	public void setDevOsType(String devOsType) {
		this.devOsType = devOsType;
	}

	@Override
	public String toString() {
		return "OnlineUser [id=" + id + ", storeId=" + storeId + ", userId="
				+ userId + ", ownerId=" + ownerId + ", userName=" + userName
				+ ", userType=" + userType + ", accessStartTime="
				+ accessStartTime + ", accessDuration=" + accessDuration
				+ ", accessAcIp=" + accessAcIp + ", accessSsid=" + accessSsid
				+ ", inputBytes=" + inputBytes + ", outputBytes=" + outputBytes
				+ ", userIp=" + userIp + ", userMac=" + userMac + ", mobileNo="
				+ mobileNo + ", devManufacturer=" + devManufacturer
				+ ", devOsType=" + devOsType + "]";
	}

}
