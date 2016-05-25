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
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * add description of types here
 *
 * @author j09980
 */
@XmlRootElement(name = "accessDetailList")
@XmlAccessorType(XmlAccessType.FIELD)
public class AccessDetailList implements Serializable {

    /**
     * add description of field here
     */
    private static final long serialVersionUID = -1658609980841528777L;

    private List<AccessDetail> users;

    public List<AccessDetail> getUsers() {
        return users;
    }

    public void setUsers(List<AccessDetail> users) {
        this.users = users;
    }
}
