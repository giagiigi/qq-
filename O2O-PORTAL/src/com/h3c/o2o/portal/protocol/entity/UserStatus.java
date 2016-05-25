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
import java.util.ArrayList;
import java.util.List;

/**
 * add description of types here
 *
 * @author j09980
 */
public class UserStatus implements Serializable {
	
	/**
     * add description of field here
     */
    private static final long serialVersionUID = -5571840684136147373L;

    public UserStatus() {
		this.onlines = new ArrayList<Online>();
		this.offlines = new ArrayList<Offline>();
	}

    private List<Online> onlines;

    private List<Offline> offlines;

    public List<Online> getOnlines() {
        return onlines;
    }

    public void setOnlines(List<Online> onlines) {
        this.onlines = onlines;
    }

    public List<Offline> getOfflines() {
        return offlines;
    }

    public void setOfflines(List<Offline> offlines) {
        this.offlines = offlines;
    }

    @Override
    public String toString() {
        return "UserStatus [onlines=" + onlines + ", offlines=" + offlines
            + "]";
    }

}
