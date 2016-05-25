/*
 * Copyright (c) 2015, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC V300R002
 * Module Name :
 * Date Created: 2015-7-21
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
package com.h3c.o2o.portal.rs.func;

import com.h3c.o2o.portal.rs.entity.AccessDetailList;
import com.h3c.o2o.portal.rs.entity.AddUser;
import com.h3c.o2o.portal.rs.entity.AuthMac;
import com.h3c.o2o.portal.rs.entity.AuthResult;
import com.h3c.o2o.portal.rs.entity.AuthUser;
import com.h3c.o2o.portal.rs.entity.ErrorInfo;
import com.h3c.o2o.portal.rs.entity.Mac;
import com.h3c.o2o.portal.rs.entity.MacList;
import com.h3c.o2o.portal.rs.entity.ModifyUser;
import com.h3c.o2o.portal.rs.entity.OnlineUserList;
import com.h3c.o2o.portal.rs.entity.PhoneCode;
import com.h3c.o2o.portal.rs.entity.SmsCode;

/**
 * add description of types here
 *
 * @author j09980
 */
public interface ThirdPartyAuthMgr {

    ErrorInfo createUser(AddUser user);

    ErrorInfo modifyUser(ModifyUser user);

    AuthResult authUser(AuthUser user);

    AuthResult authMac(AuthMac authMac);

    OnlineUserList getOnlines(String identifier, int offset, int limit);

    AccessDetailList getAccessDetails(String identifier, int offset, int limit);

    SmsCode getSmsCode(String phoneNumber);

    ErrorInfo verifySmsCode(PhoneCode code);

    ErrorInfo addToBlacklist(MacList list);

    ErrorInfo addToWhitelist(MacList list);

    ErrorInfo removeFromBlacklist(MacList list);

    ErrorInfo removeFromWhitelist(MacList list);

    /** 用户下线处理。主动给设备发一个下线请求，其他就不用再考虑了*/
    ErrorInfo loginOut(Mac mac);
}
