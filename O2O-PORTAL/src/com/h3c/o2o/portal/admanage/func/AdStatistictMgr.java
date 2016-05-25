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

import com.h3c.o2o.portal.entity.Advertisement;
import com.h3c.o2o.portal.entity.AdvertisementTime;

/**
 * add description of types here
 *
 * @author d12150
 */
public interface AdStatistictMgr {
    
    /**
     * 
     * ��ѯ���Id
     *
     * @param tempId
     * @param pageType
     * @param imageId
     * @return
     */
    Advertisement findAdId(long tempId, int pageType, String imageId);
    
    /**
     * 
     *��������չʾ����
     *
     * @param advertisementPageTime
     */
    void saveAdCount(AdvertisementTime advertisementTime);

    /**
     * 
     *ȡ�õ�ǰ����ҳ���е����й��
     *
     * @param adTempId ģ��id
     * @param adPageType ҳ������
     */
    public List<Advertisement> queryAdList(long adTempId, int adPageType);
    
    /**
     * ��ѯ���
     */
    public Advertisement findAdById(long adId);
}
