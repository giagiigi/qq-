/*
 * Copyright (c) 2007-2015, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC V700R001
 * Module Name :
 * Date Created: 2015年9月15日
 * Creator     : dkf5133
 * Description :
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 * 2015年9月15日  dkf5133             O2O-PORTAL-0827 project, new code file.
 *
 *------------------------------------------------------------------------------
 */

package com.h3c.o2o.portal.login.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.h3c.o2o.portal.PortalException;

public class HomePageReq implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -1561228310757133185L;

	private Long templateId;

    private Long nas_id;

    private String ssid;

	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

	public Long getNas_id() {
		return nas_id;
	}

	public void setNas_id(Long nas_id) {
		this.nas_id = nas_id;
	}

	public String getSsid() {
		return ssid;
	}

	public void setSsid(String ssid) {
		this.ssid = ssid;
	}

	@Override
	public String toString() {
		return "HomePageReq [templateId=" + templateId + ", nas_id=" + nas_id
				+ ", ssid=" + ssid + "]";
	}

	public String toParamString() {
		try {
            return new StringBuilder().append("?nas_id=").append(nas_id)
            		.append("&ssid=")
            		.append(URLEncoder.encode(URLEncoder.encode(ssid, "utf-8"), "utf-8"))
            		.append("&templateId=")
            		.append(templateId).toString();
        } catch (UnsupportedEncodingException e) {
            throw new PortalException("Unsupported encode.");
        }
	}
}
