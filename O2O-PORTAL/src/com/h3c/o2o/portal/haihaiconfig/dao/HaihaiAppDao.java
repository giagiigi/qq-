package com.h3c.o2o.portal.haihaiconfig.dao;

import java.util.List;

import com.h3c.o2o.portal.entity.HaihaiPlatform;

public interface HaihaiAppDao {

    
    /**
     * 
     * 根据weixin查询海海app配置信息
     *
     * @param appId
     * @return
     */
    public List<HaihaiPlatform> queryAppConfigByAppWeixinId(Long storeId);
    
}
