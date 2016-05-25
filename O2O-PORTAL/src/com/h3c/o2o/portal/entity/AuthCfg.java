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
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.AccessType;

/**
 * @author dkf5133
 *
 */
@Entity
@Table(name = "TBL_UAM_AUTH_CFG")
@AccessType("field")
public class AuthCfg implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -5037928103104000596L;

	public static int AUTH_TYPE_QUICKlOGIN = 1;
	public static int AUTH_TYPE_ACCOUNTLOGIN = 2;

	public static final int IS_ENABLE_YES = 1;

	public static final int IS_ENABLE_NO = 0;

	/** ������֤���ò���:��֤�ɹ�����תҳ��URL */
	public static final String STORE_CONFIGUE_URL = "URL_AFTER_AUTH";

  	/** ������֤���ò���:�������ʱ�� */
	public static final String STORE_CONFIGUE_TIME = "ONLINE_MAX_TIME";

	/** ������֤���ò���:�����ж�ʱ�������ӣ� */
	public static final String IDLE_CUT_TIME = "IDLE_CUT_TIME";

	/** ������֤���ò���:�����ж��������ֽڣ� */
	public static final String IDLE_CUT_FLOW = "IDLE_CUT_FLOW";

	/** ������֤���ò���:�޸�֪ʱ�����죩0��ʾ������  Ĭ��Ϊ 0 */
	public static final String NO_SENSATION_TIME = "NO_SENSATION_TIME";

	/**����*/
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

    /**�̻�ID*/
    @Column(name = "OWNER_ID", nullable = false)
	private Long ownerId;

	/**��֤��ʽ*/
    @Column(name = "AUTH_TYPE", nullable = false)
	private Integer authType;

    /**ÿ���������ʱ�����룩��0��ʾ������*/
    @Column(name = "ONLINE_MAX_TIME", nullable = false)
	private Integer onlineMaxTime;

    /**��֤�ɹ�����תҳ��*/
    @Column(name = "URL_AFTER_AUTH", length = 256)
	private String urlAfterAuth;

	/**�Ƿ����ö�����֤��1�����ã�0������*/
    @Column(name = "IS_ENABLE_SMS", nullable = false)
	private Integer isEnableSms;

    /**������֤����*/
    @Column(name = "SMS_AUTH_CONTENT", length = 512)
	private String smsAuthContent;

	/**�Ƿ�����΢����֤��1�����ã�0������*/
    @Column(name = "IS_ENABLE_WINXIN", nullable = false)
	private Integer isEnableWinxin;

    /**΢����֤URL*/
    @Column(name = "WINXIN_AUTH_URL", length = 256)
	private String winxinAuthUrl;

	/**�Ƿ�����֧������֤��1�����ã�0������*/
    @Column(name = "IS_ENABLE_ALI", nullable = false)
	private Integer isEnableAli;

    /**�Ƿ����ù̶��ʺ���֤��1�����ã�0������*/
    @Column(name = "IS_ENABLE_ACCOUNT", nullable = false)
	private Integer isEnableAccount;

    /** �Ƿ�����QQ��֤��1�����ã�0�����á�*/
    @Column(name = "IS_ENABLE_QQ")
    private Integer isEnableQQ;
    
    /** ΢����wifi��1�����ã�0������ */
    @Column(name="IS_WEIXIN_CONNECT_WIFI")
    private Integer isWeixinConnectWifi;

    /** ��֤�ӱ���Ϣ */
    @OneToMany(mappedBy="authCfg",fetch=FetchType.EAGER,cascade=CascadeType.ALL)
    private List<AuthParam> authParamList = new ArrayList<AuthParam>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public Integer getAuthType() {
		return authType;
	}

	public void setAuthType(Integer authType) {
		this.authType = authType;
	}

	public Integer getOnlineMaxTime() {
		return onlineMaxTime;
	}

	public void setOnlineMaxTime(Integer onlineMaxTime) {
		this.onlineMaxTime = onlineMaxTime;
	}

	public String getUrlAfterAuth() {
		return urlAfterAuth;
	}

	public void setUrlAfterAuth(String urlAfterAuth) {
		this.urlAfterAuth = urlAfterAuth;
	}

	public Integer getIsEnableSms() {
		return isEnableSms;
	}

	public void setIsEnableSms(Integer isEnableSms) {
		this.isEnableSms = isEnableSms;
	}

	public String getSmsAuthContent() {
		return smsAuthContent;
	}

	public void setSmsAuthContent(String smsAuthContent) {
		this.smsAuthContent = smsAuthContent;
	}

	public Integer getIsEnableWinxin() {
		return isEnableWinxin;
	}

	public void setIsEnableWinxin(Integer isEnableWinxin) {
		this.isEnableWinxin = isEnableWinxin;
	}

	public String getWinxinAuthUrl() {
		return winxinAuthUrl;
	}

	public void setWinxinAuthUrl(String winxinAuthUrl) {
		this.winxinAuthUrl = winxinAuthUrl;
	}

	public Integer getIsEnableAli() {
		return isEnableAli;
	}

	public void setIsEnableAli(Integer isEnableAli) {
		this.isEnableAli = isEnableAli;
	}

	public Integer getIsEnableAccount() {
		return isEnableAccount;
	}

	public void setIsEnableAccount(Integer isEnableAccount) {
		this.isEnableAccount = isEnableAccount;
	}

	public Integer getIsEnableQQ() {
		return isEnableQQ;
	}

	public void setIsEnableQQ(Integer isEnableQQ) {
		this.isEnableQQ = isEnableQQ;
	}

    public Integer getIsWeixinConnectWifi() {
		return isWeixinConnectWifi;
	}

	public void setIsWeixinConnectWifi(Integer isWeixinConnectWifi) {
		this.isWeixinConnectWifi = isWeixinConnectWifi;
	}

	public List<AuthParam> getAuthParamList() {
        return authParamList;
    }

    public void setAuthParamList(List<AuthParam> authParamList) {
        this.authParamList = authParamList;
    }

    @Override
    public String toString() {
        return "AuthCfg [id=" + id + ", ownerId=" + ownerId + ", authType="
            + authType + ", onlineMaxTime=" + onlineMaxTime + ", urlAfterAuth="
            + urlAfterAuth + ", isEnableSms=" + isEnableSms
            + ", smsAuthContent=" + smsAuthContent + ", isEnableWinxin="
            + isEnableWinxin + ", winxinAuthUrl=" + winxinAuthUrl
            + ", isEnableAli=" + isEnableAli + ", isEnableAccount="
            + isEnableAccount + ", isEnableQQ=" + isEnableQQ
            + ", authParamList=" + authParamList + "]";
    }

}
