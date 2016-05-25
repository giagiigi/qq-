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
package com.h3c.o2o.portal.auth.func;

import com.h3c.o2o.portal.auth.dao.AuthCfgDao;
import com.h3c.o2o.portal.entity.AuthCfg;

/**
 * add description of types here
 *
 * @author j09980
 */
public class AuthCfgMgrImpl implements AuthCfgMgr {

    private AuthCfgDao authCfgDao;

    @Override
    public AuthCfg findAuthConfig(Long id) {
        return authCfgDao.findAuthConfig(id);
    }

    public void setAuthCfgDao(AuthCfgDao authCfgDao) {
        this.authCfgDao = authCfgDao;
    }

}
