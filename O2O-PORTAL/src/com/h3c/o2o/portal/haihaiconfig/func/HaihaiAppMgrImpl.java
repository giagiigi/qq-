package com.h3c.o2o.portal.haihaiconfig.func;

import java.util.List;

import com.h3c.o2o.portal.entity.HaihaiPlatform;
import com.h3c.o2o.portal.haihaiconfig.dao.HaihaiAppDao;

public class HaihaiAppMgrImpl implements HaihaiAppMgr{

    private HaihaiAppDao haihaiAppDao;

    
    /**
     * 
     * 根据appId查询app配置信息
     *
     * @param appId
     * @return
     */
    @Override
    public List<HaihaiPlatform> queryAppConfigByAppWeixinId(Long storeId) {
        List<HaihaiPlatform> hahaiAppConfigList = this.haihaiAppDao.queryAppConfigByAppWeixinId(storeId);
        return hahaiAppConfigList;
    }

    public HaihaiAppDao getHaihaiAppDao() {
        return haihaiAppDao;
    }

    public void setHaihaiAppDao(HaihaiAppDao haihaiAppDao) {
        this.haihaiAppDao = haihaiAppDao;
    }



}
