/*
 * Copyright (c) 2015, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC V300R002
 * Module Name :
 * Date Created: 2015-10-19
 * Creator     : dkf5133
 * Description :
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 * 2015-10-19  dkf5133          portal project, new code file.
 *
 *------------------------------------------------------------------------------
 */
package com.h3c.o2o.portal.cache.key;

import java.io.Serializable;

public class PublishMngCacheKey implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -5460214321683977991L;

	/**
	 * constructor
	 * @param storeId
	 * @param ssid
	 */
	public PublishMngCacheKey(Long storeId, String ssid) {
		super();
		this.storeId = storeId;
		this.ssid = ssid;
	}

	/** µÍ∆ÃID */
	private Long storeId;

	/** Ω”»ÎSSID */
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
		PublishMngCacheKey other = (PublishMngCacheKey) obj;
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
		return new StringBuilder().append("PublishCacheKey [storeId=")
				.append(storeId).append(", ssid=").append(ssid).append("]")
				.toString();
	}

}
