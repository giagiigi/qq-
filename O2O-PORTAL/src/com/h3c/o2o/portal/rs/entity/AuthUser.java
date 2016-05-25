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

@XmlRootElement(name = "authUser")
@XmlAccessorType(XmlAccessType.FIELD)
public class AuthUser implements Serializable {

    /**
     * add description of field here
     */
    private static final long serialVersionUID = 3662129375600207240L;

    private String name;

    private String password;

    /**Ϊ��ǰ�û�����ʱ������λ�룬��ʾ����ʱ������Я����ȡϵͳĬ��ʱ����*/
    private int session_timeout;

    private String identifier;

    /**Ϊ��֤�ɹ������ض��򵽴�url*/
    private String redirect_uri;

    private String result_url;

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

    public int getSession_timeout() {
        return session_timeout;
    }

    public void setSession_timeout(int session_timeout) {
        this.session_timeout = session_timeout;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getRedirect_uri() {
        return redirect_uri;
    }

    public void setRedirect_uri(String redirect_uri) {
        this.redirect_uri = redirect_uri;
    }

    public String getResult_url() {
        return result_url;
    }

    public void setResult_url(String result_url) {
        this.result_url = result_url;
    }

    @Override
    public String toString() {
        return "AuthUser [name=" + name + ", password=" + password
            + ", session_timeout=" + session_timeout + ", identifier="
            + identifier + ", redirect_uri=" + redirect_uri + ", result_url="
            + result_url + "]";
    }

}
