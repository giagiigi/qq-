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

@XmlRootElement(name = "addUser")
@XmlAccessorType(XmlAccessType.FIELD)
public class AddUser implements Serializable {

    /**
     * add description of field here
     */
    private static final long serialVersionUID = -2150398847691547365L;

    /**�û���*/
    private String name;

    /**����*/
    private String password;

    /**ip�ֶ�ȡֵΪ�ض���URL����*/
    private long nas_id;

    /**ȡֵΪ������ϵͳΨһ��ʶ������������ҪЯ���˲���*/
    private String identifier;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getNas_id() {
        return nas_id;
    }

    public void setNas_id(long nas_id) {
        this.nas_id = nas_id;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String toString() {
        return "AddUser [name=" + name + ", password=" + password + ", nas_id="
            + nas_id + ", identifier=" + identifier + "]";
    }

}
