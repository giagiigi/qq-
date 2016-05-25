package com.h3c.o2o.portal.haihaiconfig.func;

import java.util.List;

import com.h3c.o2o.portal.entity.HaihaiPlatform;

public interface HaihaiAppMgr {
    
    /**
     * 
     * 根据appId查询app配置信息
     *
     * @param appId
     * @return
     */
    public List<HaihaiPlatform> queryAppConfigByAppWeixinId(Long storeId);
    
}
