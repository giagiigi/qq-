/*
 * Copyright (c) 2006, Hangzhou Huawei-3Com Technology Co., Ltd.
 * All rights reserved.
 * <http://www.huawei-3com.com/>
 */
package com.h3c.o2o.portal.blacklist.func;

import java.util.List;

import com.h3c.o2o.portal.PortalException;
import com.h3c.o2o.portal.blacklist.dao.BlackListDao;
import com.h3c.o2o.portal.entity.BlackList;
import com.h3c.o2o.portal.rs.entity.ErrorEnum;

/**
 * 黑名单业务逻辑管理实现。
 * 
 * @author dkf5133
 *
 */
public class BlackListMgrImpl implements BlackListMgr {

    /** 黑名单数据访问Dao */
    private BlackListDao blackListDao;

    /* (non-Javadoc)
     * @see com.h3c.o2o.portal.blacklist.func.BlackListMgr#findBlackList(java.lang.Long)
     */
    @Override
    public BlackList findBlackList(Long id) {
        return blackListDao.queryBlackList(id);
    }

    /* (non-Javadoc)
     * @see com.h3c.o2o.portal.blacklist.func.BlackListMgr#findBlackListByUser(java.lang.Long)
     */
    @Override
    public BlackList findBlackListByUser(Long userId) {
        return blackListDao.queryBlackListByUser(userId);
    }

    /* (non-Javadoc)
     * @see com.h3c.o2o.portal.blacklist.func.BlackListMgr#findAllBlackList()
     */
    @Override
    public List<BlackList> findAllBlackList() {
        return blackListDao.queryAllBlackList();
    }

    public void setBlackListDao(BlackListDao blackListDao) {
        this.blackListDao = blackListDao;
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public void saveBlacklist(List<BlackList> list) {
        if (list == null) {
            return;
        }
        try {
            for (BlackList entity : list) {
                this.blackListDao.save(entity);
            }
        } catch (Exception e) {
            throw new PortalException(ErrorEnum.UNKNOWN_ERROR.getErrorCode(),
                ErrorEnum.UNKNOWN_ERROR.getErrorMsg());
        }

    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public void deleteBlacklist(List<BlackList> list) {
        
    }

}
