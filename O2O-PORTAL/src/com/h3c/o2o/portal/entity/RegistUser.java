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
import javax.persistence.Transient;

import org.hibernate.annotations.AccessType;
import org.springframework.util.StringUtils;

import com.h3c.o2o.portal.common.CommonUtils;

/**
 * @author dkf5133
 *
 */
@Entity
@Table(name = "TBL_UAM_REGIST_USER")
@AccessType("field")
public class RegistUser implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -8290181383303910808L;

    public static final int USER_TYPE_ONEBTN = 1;

    public static final int USER_TYPE_MESSAGE = 2;

    public static final int USER_TYPE_WECHAT = 3;

    public static final int USER_TYPE_ALIPAY = 4;

    public static final int USER_TYPE_QQ = 5;

    public static final int USER_TYPE_WECHATPUBLICER = 6;

    public static final int USER_TYPE_WX_WIFI = 7;
    
    public static final int USER_TYPE_LZ_APP = 8;
    
    public static final int USER_TYPE_ACCOUNT = 100;

    /**主键*/
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**店铺ID*/
    @Column(name = "STORE_ID", nullable = false)
    private Long storeId;

    /**商户ID*/
    @Column(name = "OWNER_ID", nullable = false)
	private Long ownerId;

    /**用户名：一键认证是MAC地址；短信认证是手机号；微信客户端认证是微信号；支付宝认证是支付宝帐号名称*/
    @Column(name = "USER_NAME", nullable = false)
    private String userName;

    /**固定用户的密码*/
    @Column(name = "USER_PASSWORD")
    private String userPassword;

    /**用户类型。1：一键认证；2、短信认证；3：微信客户端认证；4、支付宝认证；5、QQ认证；6、微信公众号认证；100：固定帐号认证*/
    @Column(name = "USER_TYPE", nullable = false)
    private Integer userType;

    /**用户在第三方应用中的标识。微信客户端认证是微信产生的标识码；支付宝认证是支付宝产生的标识码*/
    @Column(name = "USER_TOKEN_ID")
    private String userTokenId;

    /**用户扩展信息，从第三方获取的扩展信息，使用JSON格式保存*/
    @Column(name = "USER_EXT_INFO")
    private String userExtInfo;

    /**注册时间*/
    @Column(name = "LOGIN_TIME", nullable = false)
    private Long loginTime;

    @Transient
    private String userMac;

    @Transient
    private String userIp;

    @Transient
    private String devManufacturer;

    @Transient
    private String devOsType;

    @Transient
    private String phoneNumber;

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

    public String getUserPassword() {
        return userPassword;
    }

    public String getAndDecryptPassword() {
    	if(StringUtils.hasText(userPassword)){
    		return CommonUtils.decryptData(userPassword);
    	} else {
    		return "";
    	}
    }

    public void setAndEncryptPassword(String password) {
        this.userPassword = CommonUtils.encryptData(password);
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public String getUserTokenId() {
        return userTokenId;
    }

    public void setUserTokenId(String userTokenId) {
        this.userTokenId = userTokenId;
    }

    public String getUserExtInfo() {
        return userExtInfo;
    }

    public void setUserExtInfo(String userExtInfo) {
        this.userExtInfo = userExtInfo;
    }

    public Long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Long loginTime) {
        this.loginTime = loginTime;
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

	public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "RegistUser [id=" + id + ", storeId=" + storeId + ", ownerId="
            + ownerId + ", userName=" + userName + ", userPassword="
            + userPassword + ", userType=" + userType + ", userTokenId="
            + userTokenId + ", userExtInfo=" + userExtInfo + ", loginTime="
            + loginTime + ", userMac=" + userMac + ", userIp=" + userIp
            + ", devManufacturer=" + devManufacturer + ", devOsType="
            + devOsType + ", phoneNumber=" + phoneNumber + "]";
    }

}
