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

import com.h3c.o2o.portal.entity.OnlineUser;
import com.h3c.o2o.portal.protocol.entity.User;

/**
 * add description of types here
 *
 * @author wkf4532
 */
public interface OnlineUserDao {

    void saveOnlineUser(OnlineUser user);

    void deleteOnlineUser(OnlineUser user);
    
    void deleteOnlineUserByMac(String mac);
    
    void deleteOnlineUserByStoreIdAndIp(Long storeId, Long ip);

    void updateOnlineUser(OnlineUser user);
    
    /**
	 * 修改在线表信息，包括接入时长、累计流入字节数、累计流出字节数
	 * @param user
	 */
	void updateOnlineInfo(User user);

    /**
	 * 查询全部在线用户
	 * @return
	 */
	List<OnlineUser> queryAllOnlineUser();

    /**
     * 带偏移量的查询在线用户
     * @param identifier 第三方唯一标示
     * @param offset 偏移量
     * @param limit 记录数
     *
     * */
    List<OnlineUser> queryOnlineUser(String identifier,int offset, int limit);

    OnlineUser queryOnlineUser(String mac);
    
    /**
     * 通过IP地址，场所ID查询用户
     * @param ip
     * @return
     */
    public List<OnlineUser> queryOnlineUser(Long ip, Long storeId);

}
