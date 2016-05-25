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
public class User implements Serializable {

    /**
     * add description of field here
     */
    private static final long serialVersionUID = -6710996869783616061L;

    private String ip;

    private String mac;

    private long sessionTime;

    private long inputbytes;

    private long outputbytes;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public long getSessionTime() {
		return sessionTime;
	}

	public void setSessionTime(long sessionTime) {
		this.sessionTime = sessionTime;
	}

    public long getInputbytes() {
		return inputbytes;
	}

	public void setInputbytes(long inputbytes) {
		this.inputbytes = inputbytes;
	}

	public long getOutputbytes() {
		return outputbytes;
	}

	public void setOutputbytes(long outputbytes) {
		this.outputbytes = outputbytes;
	}

	@Override
    public String toString() {
        return "User [ip=" + ip + ", mac=" + mac + ", sessionTime="
            + sessionTime + ", inputbytes=" + inputbytes + ", outputbytes="
            + outputbytes + "]";
    }

}
