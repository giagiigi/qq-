/*
 * Copyright (c) 2015, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC V300R002
 * Module Name :
 * Date Created: 2015-9-10
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

/**
 * REST����ʵ��
 *
 * @author j09980
 */
public class RestRequest implements Serializable {

    /**
     * ���л�ID
     */
    private static final long serialVersionUID = -3708370956886159780L;

    /**
     * ��֤�û���
     */
    private String username;

    /**
     * �����б�
     */
    private List<Parameter> parameters;

    /**
     * ������
     */
    private String body;

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "RestRequest [username=" + username + ", parameters="
            + parameters + ", body=" + body + "]";
    }

}
