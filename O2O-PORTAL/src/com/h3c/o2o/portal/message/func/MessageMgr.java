/*
 * Copyright (c) 2007-2013, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC v7
 * Module Name : iMC
 * Date Created: 2015-6-29
 * Creator     : dkf5133
 * Description :
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 * 2015-6-29     dkf5133          iMC project, new code file.
 *
 *------------------------------------------------------------------------------
 */
package com.h3c.o2o.portal.message.func;

import com.h3c.o2o.portal.protocol.entity.AuthMessageCode;


/**
 * @author dkf5133
 *
 */
public interface MessageMgr {

	void sendAuthMessage(String phoneNO, String authCfgMsgCont, String interval);

	public AuthMessageCode getCode(String phoneNO);

}
