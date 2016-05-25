/*
 * Copyright (c) 2016, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC V300R002
 * Module Name : 
 * Date Created: 2016��2��2��
 * Creator     : donglicong
 * Description : 
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 * 2016��2��2��  donglicong        O2O-PORTAL project, new code file.
 * 
 *------------------------------------------------------------------------------
 */
package com.h3c.o2o.portal.protocol.dao;

/**
 * add description of types here
 *
 * @author donglicong
 */
public interface ProtocolDao {

    /**
     * ���ݳ���id��ѯ�û�id
     * @param shopId
     * @return
     */
    Long queryUserId(long shopId);
}
