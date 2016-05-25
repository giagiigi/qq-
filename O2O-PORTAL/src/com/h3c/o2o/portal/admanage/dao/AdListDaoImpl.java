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

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.h3c.o2o.portal.entity.Advertisement;

/**
 * add description of types here
 *
 * @author d12150
 */
public class AdListDaoImpl extends HibernateDaoSupport implements AdListDao {

    // 广告未删除标识
    private final int AD_NOT_DELETE = 0;

    /**
     * 查询广告列表
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Advertisement> queryAdList(long templateId, int pageType) {
        List<Advertisement> adsList = getHibernateTemplate().find(
            "from Advertisement where "
                + "adTempId= ? and adType=? and adIsDelete=" + AD_NOT_DELETE,
            templateId, pageType);

        if (null != adsList && adsList.size() > 0) {
            return adsList;
        }
        return null;

    }

    /**
     * 查询广告id
     */
    @Override
    public Advertisement findAdId(long tempId, int pageType, String imageId) {
        @SuppressWarnings("unchecked")
        List<Object> ad = getHibernateTemplate().find(
            "select id from Advertisement where "
                + "adTempId= ? and adType=? and adElementId=? and adIsDelete=" + AD_NOT_DELETE,
            tempId, pageType, imageId);

        if (null != ad && ad.size() > 0) {
            Advertisement temp = new Advertisement();
            temp.setId((Long) ad.get(0));
            return temp;
        }
        return null;
    }

    /**
     * 查询广告id
     */
    @Override
    public Advertisement findAdById(long adId) {
        @SuppressWarnings("unchecked")
        List<Object> ad = getHibernateTemplate().find("from Advertisement where id = ?", adId);

        if (null != ad && ad.size() > 0) {
            return (Advertisement) ad.get(0);
        }
        return null;
    }
}
