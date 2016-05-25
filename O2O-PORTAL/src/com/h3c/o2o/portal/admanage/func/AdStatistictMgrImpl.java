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

import java.util.List;

import com.h3c.o2o.portal.admanage.dao.AdListDao;
import com.h3c.o2o.portal.admanage.dao.AdStatisticDao;
import com.h3c.o2o.portal.entity.Advertisement;
import com.h3c.o2o.portal.entity.AdvertisementTime;

/**
 * add description of types here
 *
 * @author d12150
 */
public class AdStatistictMgrImpl implements AdStatistictMgr {

    private AdListDao adListDao;

    private AdStatisticDao adStatisticDao;
    
    /**
     * ��������չʾ����
     */
    @Override
    public void saveAdCount(AdvertisementTime advertisement) {
        adStatisticDao.insertAdTime(advertisement);
        
    }
    
    /**
     * ��ѯ���Id
     */
    @Override
    public Advertisement findAdId(long tempId, int pageType, String imageId) {
        Advertisement ad = adListDao.findAdId(tempId, pageType, imageId);
        return ad;
    }
    
    /**
     * ��ѯ���
     */
    @Override
    public Advertisement findAdById(long adId) {
        Advertisement ad = adListDao.findAdById(adId);
        return ad;
    }

    public void setAdListDao(AdListDao adListDao) {
        this.adListDao = adListDao;
    }
    
    public void setAdStatisticDao(AdStatisticDao adStatisticDao) {
        this.adStatisticDao = adStatisticDao;
    }

    /**
     * 
     *ȡ�õ�ǰ����ҳ���е����й��
     *
     * @param adTempId ģ��id
     * @param adPageType ҳ������
     */
    @Override
    public List<Advertisement> queryAdList(long adTempId, int adPageType) {
        List<Advertisement> adList = adListDao.queryAdList(adTempId, adPageType);
        return adList;
    }
    
}
