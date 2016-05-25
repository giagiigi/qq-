/*
 * Copyright (c) 2006, Hangzhou Huawei-3Com Technology Co., Ltd.
 * All rights reserved.
 * <http://www.huawei-3com.com/>
 */
package com.h3c.o2o.portal.blacklist.dao;

import java.util.List;

import com.h3c.o2o.portal.entity.BlackList;


/**
 * 黑名单数据访问接口。
 * 
 * @author dkf5133
 *
 */
public interface BlackListDao {

    /**
     * 根据黑名单ID查询黑名单信息。
     * @param id 黑名单ID
     * @return 黑名单信息
     */
    BlackList queryBlackList(Long id);
    
    /**
     * 
     * 根据用户Id查询黑名单信息。
     *
     * @param userId 用户ID
     * @return 黑名单信息
     */
    BlackList queryBlackListByUser(Long userId);
    
    /**
     * 查询所有黑名单信息
     *
     * @return 黑名单集合
     */
    List<BlackList> queryAllBlackList();
    
    /**
     * 增加黑名单，一般是第三方接口使用
     * */
    void save(BlackList entity);
    
    /**
     * 解除黑名单，一般是第三方接口使用
     * */
    void delete(BlackList entity);

}
