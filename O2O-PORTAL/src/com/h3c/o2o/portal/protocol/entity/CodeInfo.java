/*
 * Copyright (c) 2007-2013, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC v7
 * Module Name : iMC
 * Date Created: 2015-7-9
 * Creator     : dkf5133
 * Description : 
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 * 2015-7-9     dkf5133          iMC project, new code file.
 *
 *------------------------------------------------------------------------------
 */
package com.h3c.o2o.portal.protocol.entity;

import java.io.Serializable;

import com.h3c.o2o.portal.entity.RegistUser;

/**
 * @author dkf5133
 *
 */
public class CodeInfo implements Serializable {
	
	/**
     * add description of field here
     */
    private static final long serialVersionUID = -6861563053530623664L;
    
    public static final int CODE_ALIVE_TIME = 60 * 5;

    private RegistUser registUser;
	
	private Long createTime;
	
	private int type;
	
	private int expire_in;

	public RegistUser getRegistUser() {
		return registUser;
	}

	public void setRegistUser(RegistUser registUser) {
		this.registUser = registUser;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

    public int getExpire_in() {
        return expire_in;
    }

    public void setExpire_in(int expire_in) {
        this.expire_in = expire_in;
    }
	
}
