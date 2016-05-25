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
import com.h3c.o2o.portal.user.dao.OnlineUserDao;

/**
 * 用户管理Mgr实现类
 *
 * @author j09980
 */
public class OnlineUserMgrImpl implements OnlineUserMgr {

	private OnlineUserDao onlineUserDao;

	@Override
	public void saveOnlineUser(OnlineUser user) {
		onlineUserDao.saveOnlineUser(user);
	}

	@Override
	public void deleteOnlineUser(OnlineUser user) {
		onlineUserDao.deleteOnlineUser(user);
	}

	@Override
	public void updateOnlineUser(OnlineUser user){
		onlineUserDao.updateOnlineUser(user);
	}
	
	@Override
	public void updateOnlineInfo(User user) {
		onlineUserDao.updateOnlineInfo(user);
	}

	@Override
	public List<OnlineUser> queryAllOnlineUser() {
		return onlineUserDao.queryAllOnlineUser();
	}

	@Override
	public List<OnlineUser> queryOnlineUser(String identifier, int offset,
			int limit) {
		return this.onlineUserDao.queryOnlineUser(identifier, offset, limit);
	}

	public void setOnlineUserDao(OnlineUserDao onlineUserDao) {
		this.onlineUserDao = onlineUserDao;
	}

    @Override
    public OnlineUser queryOnlineUser(String mac) {
        return this.onlineUserDao.queryOnlineUser(mac);
    }
    
	/**
	 * 通过IP地址，场所ID查询用户
	 * 
	 * @param ip
	 * @return
	 */
	public List<OnlineUser> queryOnlineUser(Long ip, Long storeId) {
		if (null == ip || Long.valueOf(0L).equals(ip) || null == storeId) {
			return null;
		} else {
			return this.onlineUserDao.queryOnlineUser(ip, storeId);
		}
	}

	@Override
	public void deleteOnlineUserByMac(String mac) {
		this.onlineUserDao.deleteOnlineUserByMac(mac);
	}
	
	@Override
	public void deleteOnlineUserByStoreIdAndIp(Long storeId, Long ip){
		this.onlineUserDao.deleteOnlineUserByStoreIdAndIp(storeId, ip);
	}
}
