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

import com.h3c.o2o.portal.entity.AuthCfg;

/**
 * add description of types here
 *
 * @author j09980
 */
public interface AuthCfgMgr {

    /**
     * 查询店铺认证配置
     * @param id 认证配置ID
     * @return 认证配置实体
     */
    AuthCfg findAuthConfig(Long id);

}
