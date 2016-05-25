/*
 * Copyright (c) 2015, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC V300R002
 * Module Name : 
 * Date Created: 2015-6-30
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
package com.h3c.o2o.portal.util;

import java.util.UUID;

/**
 * Token生成器-采用UUID实现，以确保全局唯一。
 *
 * @author j09980
 */
public final class TokenGenerator {

    /**
     * 私有构造
     */
    private TokenGenerator() {
    }

    /**
     * 生成一个Token 
     *
     * @return Token
     */
    public static String generate() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
