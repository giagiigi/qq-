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
public class ErrorInfo implements Serializable {

    /**
     * add description of field here
     */
    private static final long serialVersionUID = 9193468509165144776L;

    private int errcode;

    private String errmsg;
    
    private String errurl;

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getErrurl() {
        return errurl;
    }

    public void setErrurl(String errurl) {
        this.errurl = errurl;
    }

}
