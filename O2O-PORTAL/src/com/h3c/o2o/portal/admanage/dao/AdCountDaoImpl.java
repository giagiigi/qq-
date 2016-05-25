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

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.h3c.o2o.portal.entity.AdvertisementPageTime;

/**
 * add description of types here
 *
 * @author d12150
 */
public class AdCountDaoImpl extends HibernateDaoSupport implements AdCountDao {

    @Override
    public void insertAdCount(AdvertisementPageTime advertisementPageTime) {
            getHibernateTemplate().save(advertisementPageTime);
        
    }

}
