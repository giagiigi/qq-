/*
 * Copyright (c) 2015, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC V300R002
 * Module Name :
 * Date Created: 2015-7-18
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
package com.h3c.o2o.portal.rs.entity;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * add description of types here
 *
 * @author j09980
 */
@XmlRootElement(name = "smsCode")
@XmlAccessorType(XmlAccessType.FIELD)
public class SmsCode implements Serializable {

    /**
     * add description of field here
     */
    private static final long serialVersionUID = -6937316586844622272L;

    private String code;

    /**验证的有效时间，单位为秒*/
    private int expired_in;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getExpired_in() {
        return expired_in;
    }

    public void setExpired_in(int expired_in) {
        this.expired_in = expired_in;
    }

}
