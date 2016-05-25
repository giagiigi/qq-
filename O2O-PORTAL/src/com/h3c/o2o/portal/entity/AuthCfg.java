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

	/** 店铺认证配置参数:认证成功后跳转页面URL */
	public static final String STORE_CONFIGUE_URL = "URL_AFTER_AUTH";

  	/** 店铺认证配置参数:允许接入时长 */
	public static final String STORE_CONFIGUE_TIME = "ONLINE_MAX_TIME";

	/** 店铺认证配置参数:闲置切断时长（分钟） */
	public static final String IDLE_CUT_TIME = "IDLE_CUT_TIME";

	/** 店铺认证配置参数:闲置切断流量（字节） */
	public static final String IDLE_CUT_FLOW = "IDLE_CUT_FLOW";

	/** 店铺认证配置参数:无感知时常（天）0表示不启用  默认为 0 */
	public static final String NO_SENSATION_TIME = "NO_SENSATION_TIME";

	/**主键*/
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

    /**商户ID*/
    @Column(name = "OWNER_ID", nullable = false)
	private Long ownerId;

	/**认证方式*/
    @Column(name = "AUTH_TYPE", nullable = false)
	private Integer authType;

    /**每次上线最大时长（秒），0表示不限制*/
    @Column(name = "ONLINE_MAX_TIME", nullable = false)
	private Integer onlineMaxTime;

    /**认证成功后跳转页面*/
    @Column(name = "URL_AFTER_AUTH", length = 256)
	private String urlAfterAuth;

	/**是否启用短信认证，1：启用；0：禁用*/
    @Column(name = "IS_ENABLE_SMS", nullable = false)
	private Integer isEnableSms;

    /**短信认证内容*/
    @Column(name = "SMS_AUTH_CONTENT", length = 512)
	private String smsAuthContent;

	/**是否启用微信认证，1：启用；0：禁用*/
    @Column(name = "IS_ENABLE_WINXIN", nullable = false)
	private Integer isEnableWinxin;

    /**微信认证URL*/
    @Column(name = "WINXIN_AUTH_URL", length = 256)
	private String winxinAuthUrl;

	/**是否启用支付宝认证，1：启用；0：禁用*/
    @Column(name = "IS_ENABLE_ALI", nullable = false)
	private Integer isEnableAli;

    /**是否启用固定帐号认证，1：启用；0：禁用*/
    @Column(name = "IS_ENABLE_ACCOUNT", nullable = false)
	private Integer isEnableAccount;

    /** 是否启用QQ认证，1：启用；0：禁用。*/
    @Column(name = "IS_ENABLE_QQ")
    private Integer isEnableQQ;
    
    /** 微信连wifi，1：启用；0：禁用 */
    @Column(name="IS_WEIXIN_CONNECT_WIFI")
    private Integer isWeixinConnectWifi;

    /** 认证子表信息 */
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
