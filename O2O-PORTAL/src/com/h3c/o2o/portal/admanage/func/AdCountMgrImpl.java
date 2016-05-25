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
package com.h3c.o2o.portal.admanage.func;

import com.h3c.o2o.portal.admanage.dao.AdCountDao;
import com.h3c.o2o.portal.entity.AdvertisementPageTime;

/**
 * add description of types here
 *
 * @author d12150
 */
public class AdCountMgrImpl implements AdCountMgr {
            
    private AdCountDao adCountDao;

    @Override
    public void saveAdCount(AdvertisementPageTime advertisementPageTime) {
        this.adCountDao.insertAdCount(advertisementPageTime);
        
    }

    public void setAdCountDao(AdCountDao adCountDao) {
        this.adCountDao = adCountDao;
    }

}
