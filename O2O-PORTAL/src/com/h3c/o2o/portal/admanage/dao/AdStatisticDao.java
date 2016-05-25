/*
 * Copyright (c) 2016, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC V300R002
 * Module Name :
 * Date Created: 2016-2-19
 * Creator     : d12150
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
package com.h3c.o2o.portal.admanage.dao;

import com.h3c.o2o.portal.entity.AdvertisementTime;;
/**
 * add description of types here
 *
 * @author d12150
 */
public interface AdStatisticDao {
    /**
     * 保存广告统计数据
     * 
     * */
    void insertAdTime(AdvertisementTime advertisementTime);
}