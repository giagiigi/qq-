/*
 * Copyright (c) 2006, Hangzhou Huawei-3Com Technology Co., Ltd.
 * All rights reserved.
 * <http://www.huawei-3com.com/>
 */
package com.h3c.o2o.portal.blacklist.func;

import java.util.List;

import com.h3c.o2o.portal.entity.BlackList;


/**
 * 黑名单业务逻辑管理接口。
 *
 * @author dkf5133
 *
 */
public interface BlackListMgr {

    /**
     * 根据黑名单ID查询黑名单信息。
     * @param id 黑名单ID
     * @return 黑名单信息
     */
    BlackList findBlackList(Long id);

    /**
     *
     * 根据用户Id查询黑名单信息。
     *
     * @param userId 用户ID
     * @return 黑名单信息
     */
    BlackList findBlackListByUser(Long userId);

    /**
     * 查询所有黑名单信息
     *
     * @return 黑名单集合
     */
    List<BlackList> findAllBlackList();

    /**
     * 批量增加黑名单，一般是第三方接口使用
     * @param list 待增加的黑名单list
     * */
    void saveBlacklist(List<BlackList> list);

    /**
     * 批量解除黑名单，一般是第三方接口使用
     * @param list 待解除的黑名单list
     * */
    void deleteBlacklist(List<BlackList> list);
}
