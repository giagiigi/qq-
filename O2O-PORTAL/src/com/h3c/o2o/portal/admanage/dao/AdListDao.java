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
     * �����û�ģ��Id��ҳ�����Ͳ�ѯҳ���ϵĹ�档
     *
     * @param templateId ģ��ID
     * @param pageType ҳ������
     * @return �����Ϣ
     */
    List<Advertisement> queryAdList(long templateId, int pageType);

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
     * ��ѯ���
     */
    public Advertisement findAdById(long adId);

}