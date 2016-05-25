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
package com.h3c.o2o.portal.user.func;

import java.util.List;

import com.h3c.o2o.portal.entity.OnlineUser;
import com.h3c.o2o.portal.protocol.entity.User;

/**
 * 在线用户管理Mgr
 *
 * @author j09980
 */
public interface OnlineUserMgr {

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
	 * 查询在线用户
	 * @param identifier 第三方用户的唯一标识
	 * @param offset 偏移量
	 * @param limit 记录数
	 *
	 * */
	List<OnlineUser> queryOnlineUser(String identifier, int offset,
			int limit);

	/**
     * 查询注册用户
     *
     * @param mac
     * @return OnlineUser
     */
    OnlineUser queryOnlineUser(String mac);
    
    /**
     * 通过IP地址查询用户
     * @param ip
     * @return
     */
    public List<OnlineUser> queryOnlineUser(Long ip, Long storeId);
}
