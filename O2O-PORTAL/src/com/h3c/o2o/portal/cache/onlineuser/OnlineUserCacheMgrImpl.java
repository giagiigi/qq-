/*
 * Copyright (c) 2007-2015, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC V700R001
 * Module Name :
 * Date Created: 2015年9月9日
 * Creator     : dkf5133
 * Description :
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 * 2015年9月9日  dkf5133             O2O-PORTAL-0827 project, new code file.
 *
 *------------------------------------------------------------------------------
 */

package com.h3c.o2o.portal.cache.onlineuser;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.h3c.o2o.portal.cache.CacheMgr;
import com.h3c.o2o.portal.entity.OnlineUser;
import com.h3c.o2o.portal.user.func.OnlineUserMgr;

/**
 * 在线用户缓存，key：mac address
 * @author dkf5133
 *
 */
public class OnlineUserCacheMgrImpl implements CacheMgr<String, OnlineUser> {

    private Log log = LogFactory.getLog(getClass());

    private OnlineUserMgr onlineUserMgr;

    /**
     * 在线用户缓存数据集合
     */
    private ConcurrentMap<String, OnlineUser> onlineUserMap = new ConcurrentHashMap<String, OnlineUser>();

    @Override
    public void init() {
        List<OnlineUser> onlineUsers = onlineUserMgr.queryAllOnlineUser();
        for (OnlineUser onlineUser : onlineUsers) {
            onlineUserMap.put(onlineUser.getUserMac(), onlineUser);
        }
    }

    @Override
    public void put(String key, OnlineUser value) {
        onlineUserMap.put(key, value);
    }

    @Override
    public boolean putIfAbsent(String key, OnlineUser value) {
        return onlineUserMap.putIfAbsent(key, value) == null ? false : true;
    }

    @Override
    public void remove(String key) {
        onlineUserMap.remove(key);
    }

    @Override
    public void removeByRelation(Long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public OnlineUser getFromCache(String key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public OnlineUser get(String key) {
        OnlineUser result = onlineUserMap.get(key);
        if (result == null) {
            // 缓存中没有
            log.warn("there isn't onlineUser wehre mac = " + key.toString()
                + " in the cache.");
            OnlineUser user = onlineUserMgr.queryOnlineUser((String) key);
            if (user == null) {
                // 数据库中没有
                log.warn("there isn't onlineUser wehre mac = " + key
                    + " in the database.");
                return null;
            }
            put(key, user);
            return user;
        }
        return result;
    }

    @Override
    public Set<String> getKeys() {
        return onlineUserMap.keySet();
    }

    @Override
    public void clear() {
        onlineUserMap.clear();
    }

    public void setOnlineUserMgr(OnlineUserMgr onlineUserMgr) {
        this.onlineUserMgr = onlineUserMgr;
    }

	@Override
	public void updateCache() throws Exception {
		clear();
		init();
	}

}
