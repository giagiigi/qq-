package com.h3c.o2o.portal.haihaiconfig.dao;

import java.util.List;

import com.h3c.o2o.portal.entity.HaihaiPlatform;

public interface HaihaiAppDao {

    
    /**
     * 
     * ����weixin��ѯ����app������Ϣ
     *
     * @param appId
     * @return
     */
    public List<HaihaiPlatform> queryAppConfigByAppWeixinId(Long storeId);
    
}
