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
public class Online implements Serializable {
	
	/**
     * add description of field here
     */
    private static final long serialVersionUID = 6728458168763301826L;

    public Online(String ip, long sessionTimeOut) {
		this.ip = ip;
		this.sessionTimeOut = sessionTimeOut;
	}

    private String ip;

    private long sessionTimeOut;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

	public long getSessionTimeOut() {
		return sessionTimeOut;
	}

	public void setSessionTimeOut(long sessionTimeOut) {
		this.sessionTimeOut = sessionTimeOut;
	}

    @Override
    public String toString() {
        return "Online [ip=" + ip + ", sessionTimeOut=" + sessionTimeOut + "]";
    }

}
