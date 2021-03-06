/*
 * Copyright (c) 2015, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC V300R002
 * Module Name : 
 * Date Created: 2015-7-18
 * Creator     : j09980
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
package com.h3c.o2o.portal.user.dao;

import java.util.List;

import com.h3c.o2o.portal.entity.RegistUser;

/**
 * add description of types here
 *
 * @author j09980
 */
public interface RegistUserDao {

    void saveRegistUser(RegistUser user);

    void updateRegistUser(RegistUser user);

    /**
     * 查询注册用户
     * @param id
     * @return
     */
    RegistUser queryRegistUser(Long id);
    
    /**
     * 查询注册用户
     * @param storeId 店铺ID
     * @param userName 用户名
     * @param userType 用户类型
     * @return
     */
    RegistUser queryRegistUser(Long storeId, String userName, Integer userType);
    
    /**
     * 查询所有注册用户
     *
     * @return
     */
    List<RegistUser> queryAllRegistUser();

    /**
     * 查询注册用户 
     * @param identifier 第三方唯一标示
     * @param userName 用户名称
     * @return
     * */
    RegistUser queryRegistUser(String userName ,String identifier, Integer userType);
    
}
