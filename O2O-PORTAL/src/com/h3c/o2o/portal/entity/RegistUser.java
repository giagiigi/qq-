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

    /**����*/
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**����ID*/
    @Column(name = "STORE_ID", nullable = false)
    private Long storeId;

    /**�̻�ID*/
    @Column(name = "OWNER_ID", nullable = false)
	private Long ownerId;

    /**�û�����һ����֤��MAC��ַ��������֤���ֻ��ţ�΢�ſͻ�����֤��΢�źţ�֧������֤��֧�����ʺ�����*/
    @Column(name = "USER_NAME", nullable = false)
    private String userName;

    /**�̶��û�������*/
    @Column(name = "USER_PASSWORD")
    private String userPassword;

    /**�û����͡�1��һ����֤��2��������֤��3��΢�ſͻ�����֤��4��֧������֤��5��QQ��֤��6��΢�Ź��ں���֤��100���̶��ʺ���֤*/
    @Column(name = "USER_TYPE", nullable = false)
    private Integer userType;

    /**�û��ڵ�����Ӧ���еı�ʶ��΢�ſͻ�����֤��΢�Ų����ı�ʶ�룻֧������֤��֧���������ı�ʶ��*/
    @Column(name = "USER_TOKEN_ID")
    private String userTokenId;

    /**�û���չ��Ϣ���ӵ�������ȡ����չ��Ϣ��ʹ��JSON��ʽ����*/
    @Column(name = "USER_EXT_INFO")
    private String userExtInfo;

    /**ע��ʱ��*/
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
