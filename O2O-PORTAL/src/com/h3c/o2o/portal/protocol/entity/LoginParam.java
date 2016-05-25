/*
 * Copyright (c) 2015, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC V300R002
 * Module Name :
 * Date Created: 2015-6-26
 * Creator     : j09980
 * Description :
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 * YYYY-MM-DD  zhangshan        XXXX project, new code file.
 *
 *------------------------------------------------------------------------------
 */
package com.h3c.o2o.portal.protocol.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.lang.StringUtils;

import com.h3c.o2o.portal.PortalException;

/**
 * add description of types here
 *
 * @author j09980
 */
public class LoginParam implements Serializable {

    /**
     * add description of field here
     */
    private static final long serialVersionUID = 7549583764677792568L;

    private String redirect_uri;

    private String usermac;

    private String userip;

    private String userurl;

    private Long nasId;

    private String ssid;

    public String getRedirect_uri() {
        return redirect_uri;
    }

    public void setRedirect_uri(String redirect_uri) {
        this.redirect_uri = redirect_uri;
    }

    public String getUsermac() {
        return usermac;
    }

    public void setUsermac(String usermac) {
        this.usermac = usermac;
    }

    public String getUserip() {
        return userip;
    }

    public void setUserip(String userip) {
        this.userip = userip;
    }

    public String getUserurl() {
        return userurl;
    }

    public void setUserurl(String userurl) {
        this.userurl = userurl;
    }

    public Long getNasId() {
        return nasId;
    }

    public void setNasId(Long nasId) {
        this.nasId = nasId;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    @Override
    public String toString() {
        return "LoginParam [redirect_uri=" + redirect_uri + ", usermac="
            + usermac + ", userip=" + userip + ", userurl=" + userurl
            + ", nasId=" + nasId + ", ssid=" + ssid + "]";
    }

    public String toParamString() {
        try {
            return new StringBuilder()
            		.append("&redirect_uri=").append(encodeAcceptNull(redirect_uri))
            		.append("&nas_id=").append(nasId)
            		.append("&ssid=").append(encodeAcceptNull(ssid))
            		.append("&usermac=").append(encodeAcceptNull(usermac))
            		.append("&userip=").append(encodeAcceptNull(userip))
            		.append("&userurl=").append(encodeAcceptNull(userurl)).toString();
        } catch (UnsupportedEncodingException e) {
            throw new PortalException("Unsupported encode.");
        }
    }
    
    private String encodeAcceptNull(String str4encode) throws UnsupportedEncodingException{
		if(StringUtils.isNotBlank(str4encode)){
			return URLEncoder.encode(str4encode, "utf-8");
		} else {
			return str4encode;
		}
	}
}
