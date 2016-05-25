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

import java.util.List;

import com.h3c.o2o.portal.entity.Advertisement;

/**
 * add description of types here
 *
 * @author d12150
 */
public interface AdListDao {

    /**
     * 
     * 根据用户模板Id和页面类型查询页面上的广告。
     *
     * @param templateId 模板ID
     * @param pageType 页面类型
     * @return 广告信息
     */
    List<Advertisement> queryAdList(long templateId, int pageType);

    /**
     * 
     * 查询广告Id
     *
     * @param tempId
     * @param pageType
     * @param imageId
     * @return
     */
    Advertisement findAdId(long tempId, int pageType, String imageId);
    
    /**
     * 查询广告
     */
    public Advertisement findAdById(long adId);

}