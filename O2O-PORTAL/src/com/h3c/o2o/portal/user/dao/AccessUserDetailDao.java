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
package com.h3c.o2o.portal.user.dao;

import java.util.List;

import com.h3c.o2o.portal.entity.AccessDetail;

/**
 * add description of types here
 *
 * @author j09980
 */
public interface AccessUserDetailDao {

    void saveAccessDetail(AccessDetail detail);

    AccessDetail queryAccessDetail(Long storeId, String ssid, String usermac, long accessStartTime);

    /**
     * 查询接入开始时间
     * @param userId
     * @return
     */
    Long queryAccessStartTime(long userId);
    
    /**
     * 带偏移量  获取接入明细
     * @param identifier 第三方唯一标示
     * @param offset 偏移量
     * @param limit 记录数
     * 
     * */
    List<AccessDetail> queryAccessUser(String identifier,int offset, int limit);
    
}
