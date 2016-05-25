/*
 * Copyright (c) 2007-2013, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC v7
 * Module Name : iMC
 * Date Created: 2015-7-10
 * Creator     : dkf5133
 * Description : 
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 * 2015-7-10     dkf5133          iMC project, new code file.
 *
 *------------------------------------------------------------------------------
 */
package com.h3c.o2o.portal.protocol.entity;

import java.io.Serializable;

/**
 * @author dkf5133
 *
 */
public class AuthMessageCode implements Serializable {
	
	/**
     * add description of field here
     */
    private static final long serialVersionUID = 5256879188537630421L;
    
    /** 短信校验码有效时长：60,000ms */
    public static final int MESSAGE_CODE_ALIVE_TIME = 60 * 1000;

    public AuthMessageCode(String code) {
		this.code = code;
		this.createTime = System.currentTimeMillis();
	}

	private String code;
	
	private long createTime;
	
	private int expire_in; 

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

    public int getExpire_in() {
        return expire_in;
    }

    public void setExpire_in(int expire_in) {
        this.expire_in = expire_in;
    }
	
}
