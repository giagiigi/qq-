/*
 * Copyright (c) 2015, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC V300R002
 * Module Name :
 * Date Created: 2015-6-27
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

/**
 * add description of types here
 *
 * @author j09980
 */
public class UserInfo implements Serializable {

    /**
     * add description of field here
     */
    private static final long serialVersionUID = 8527600396796497266L;

    private String username;

    private int sessionTimeOut;

    private String login_url;

    private int idleCutTime;

    private int idleCutFlow;
    
    // 认证类型
    private int auth_type; 
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getSessionTimeOut() {
        return sessionTimeOut;
    }

    public void setSessionTimeOut(int sessionTimeOut) {
        this.sessionTimeOut = sessionTimeOut;
    }

    public String getLogin_url() {
        return login_url;
    }

    public void setLogin_url(String login_url) {
        this.login_url = login_url;
    }

    public int getIdleCutTime() {
        return idleCutTime;
    }

    public void setIdleCutTime(int idleCutTime) {
        this.idleCutTime = idleCutTime;
    }

    public int getIdleCutFlow() {
        return idleCutFlow;
    }

    public void setIdleCutFlow(int idleCutFlow) {
        this.idleCutFlow = idleCutFlow;
    }

	public int getAuth_type() {
		return auth_type;
	}

	public void setAuth_type(int auth_type) {
		this.auth_type = auth_type;
	}
    
}
