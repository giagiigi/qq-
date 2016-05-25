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
public class OfflineByMac implements Serializable {

	/**
     * add description of field here
     */
    private static final long serialVersionUID = 7840366680647286957L;

    public OfflineByMac() {
    }

    public OfflineByMac(String mac) {
		this.mac = mac;
	}

    private String mac;

    public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	@Override
    public String toString() {
        return "OfflineByMac [mac=" + mac + "]";
    }

}
