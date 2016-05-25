/*
 * Copyright (c) 2007-2015, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC V700R001
 * Module Name :
 * Date Created: 2015年9月12日
 * Creator     : dkf5133
 * Description :
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 * 2015年9月12日  dkf5133             O2O-PORTAL-0827 project, new code file.
 *
 *------------------------------------------------------------------------------
 */

package com.h3c.o2o.portal.cache.key;

import java.io.Serializable;

/**
 * 主题模板缓存key
 * @author dkf5133
 *
 */
public class ThemeTemplateCacheKey implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 8238996587675274863L;

	public ThemeTemplateCacheKey() {
	}

	public ThemeTemplateCacheKey(Long storeId, String ssid) {
		this.storeId = storeId;
		this.ssid = ssid;
	}

	/** 店铺ID */
	private Long storeId;

	/** 接入SSID */
	private String ssid;

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public String getSsid() {
		return ssid;
	}

	public void setSsid(String ssid) {
		this.ssid = ssid;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ssid == null) ? 0 : ssid.hashCode());
		result = prime * result + ((storeId == null) ? 0 : storeId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ThemeTemplateCacheKey other = (ThemeTemplateCacheKey) obj;
		if (ssid == null) {
			if (other.ssid != null)
				return false;
		} else if (!ssid.equals(other.ssid))
			return false;
		if (storeId == null) {
			if (other.storeId != null)
				return false;
		} else if (!storeId.equals(other.storeId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ThemeTemplateCacheKey [storeId=" + storeId + ", ssid=" + ssid
				+ "]";
	}

}
