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
 * REST请求实体
 *
 * @author j09980
 */
public class RestRequest implements Serializable {

    /**
     * 序列化ID
     */
    private static final long serialVersionUID = -3708370956886159780L;

    /**
     * 认证用户名
     */
    private String username;

    /**
     * 参数列表
     */
    private List<Parameter> parameters;

    /**
     * 请求体
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
