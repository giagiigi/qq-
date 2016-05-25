/*
 * Copyright (c) 2007-2013, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC v7
 * Module Name : iMC
 * Date Created: 2015-7-9
 * Creator     : dkf5133
 * Description : 
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 * 2015-7-9     dkf5133          iMC project, new code file.
 *
 *------------------------------------------------------------------------------
 */
package com.h3c.o2o.portal.protocol.entity;

import java.io.Serializable;

import com.h3c.o2o.portal.entity.RegistUser;

/**
 * @author dkf5133
 *
 */
public class TokenInfo implements Serializable {
	
	/**
     * add description of field here
     */
    private static final long serialVersionUID = 5926151499125901205L;

    private AccessToken accessToken;
	
	private Long tokenCreateTime;
	
	private RegistUser registUser;
	
	public AccessToken getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(AccessToken accessToken) {
		this.accessToken = accessToken;
	}

	public Long getTokenCreateTime() {
		return tokenCreateTime;
	}

	public void setTokenCreateTime(Long tokenCreateTime) {
		this.tokenCreateTime = tokenCreateTime;
	}

	public RegistUser getRegistUser() {
		return registUser;
	}

	public void setRegistUser(RegistUser registUser) {
		this.registUser = registUser;
	}

}
