/*
 * Copyright (c) 2007-2015, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC V700R001
 * Module Name :
 * Date Created: 2015年10月19日
 * Creator     : dkf5133
 * Description :
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 * 2015年10月19日  dkf5133             O2O-PORTAL-UI project, new code file.
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

@Entity
@Table(name = "TBL_UAM_PUBLISH_MANAGEMENT")
@AccessType("field")
public class PublishMng implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -2137668545457435267L;

	/** 已发布： 否 */
	public static final int IS_PUBLISHED_FALSE = 0;
	/** 已发布： 是 */
	public static final int IS_PUBLISHED_TRUE = 1;

	/** 主键 */
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/** 店铺ID */
	@Column(name = "STORE_ID", nullable = false)
	private Long storeId;

	/**商户ID*/
    @Column(name = "OWNER_ID", nullable = false)
	private Long ownerId;

	/** ssid ID */
	@Column(name = "SSID_ID")
	private Long ssidId;

	/** ssid name */
	@Column(name = "SSID_NAME")
	private String ssidName;

	/** 认证配置ID */
	@Column(name = "AUTH_CFG_ID")
	private Long authCfgId;

	/** 主题模板ID */
	@Column(name = "THEME_TEMPLATE_ID")
	private Long themeTemplateId;

	/** 创建时间 */
	@Column(name = "CREATE_TIME")
	private Long createTime;

	/** 是否已发布，0：否，1：是 */
	@Column(name = "ISPUBLISHED")
	private Integer isPublished;

	/** 微信公众号ID */
	@Column(name = "WEIXIN_ACCOUNT_ID")
	private Long weixinAccountId;

	/** 描述 */
	@Column(name = "DESCRIPTION")
	private String description;

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

	public Long getSsidId() {
		return ssidId;
	}

	public void setSsidId(Long ssidId) {
		this.ssidId = ssidId;
	}

	public String getSsidName() {
		return ssidName;
	}

	public void setSsidName(String ssidName) {
		this.ssidName = ssidName;
	}

	public Long getAuthCfgId() {
		return authCfgId;
	}

	public void setAuthCfgId(Long authCfgId) {
		this.authCfgId = authCfgId;
	}

	public Long getThemeTemplateId() {
		return themeTemplateId;
	}

	public void setThemeTemplateId(Long themeTemplateId) {
		this.themeTemplateId = themeTemplateId;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Integer getIsPublished() {
		return isPublished;
	}

	public void setIsPublished(Integer isPublished) {
		this.isPublished = isPublished;
	}

	public Long getWeixinAccountId() {
		return weixinAccountId;
	}

	public void setWeixinAccountId(Long weixinAccountId) {
		this.weixinAccountId = weixinAccountId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return new StringBuilder().append("PublishMng [id=").append(id)
				.append(", storeId=").append(storeId).append(", ownerId=")
				.append(ownerId).append(", ssidId=").append(ssidId)
				.append(", ssidName=").append(ssidName).append(", authCfgId=")
				.append(authCfgId).append(", themeTemplateId=")
				.append(themeTemplateId).append(", createTime=")
				.append(createTime).append(", isPublished=")
				.append(isPublished).append(", weixinAccountId=")
				.append(weixinAccountId).append(", description=")
				.append(description).append("]").toString();
	}

}
