/*
 * Copyright (c) 2015, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC V300R002
 * Module Name :
 * Date Created: 2015-7-30
 * Creator     : dkf5133
 * Description :
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 * 2015-7-30  dkf5133           o2o project, new code file.
 *
 *------------------------------------------------------------------------------
 */
package com.h3c.o2o.portal.cache.registuser;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.h3c.o2o.portal.cache.CacheMgr;
import com.h3c.o2o.portal.cache.key.RegistUserCacheKey;
import com.h3c.o2o.portal.entity.RegistUser;
import com.h3c.o2o.portal.user.func.RegistUserMgr;

/**
 * 注册用户缓存。主键：com.h3c.o2o.portal.user.entity.RegistUserCacheKey
 *
 * @author dkf5133
 */
public class RegistUserCacheMgrImpl implements CacheMgr<RegistUserCacheKey, RegistUser> {

    private Log log = LogFactory.getLog(RegistUserCacheMgrImpl.class);

    private RegistUserMgr userMgr;

    /**
     * 注册用户缓存数据集合
     */
    private ConcurrentMap<RegistUserCacheKey, RegistUser> registUserMap =
        new ConcurrentHashMap<RegistUserCacheKey, RegistUser>();

    /**
     * 注册用户实体ID与缓存数据KEY之间的关系缓存。key：实体ID。
     */
    private ConcurrentMap<Long, RegistUserCacheKey> keyIdRelationMap
        = new ConcurrentHashMap<Long, RegistUserCacheKey>();

    /* (non-Javadoc)
     * @see com.h3c.o2o.portal.cache.CacheMgr#init()
     */
    @Override
    public void init() {
        List<RegistUser> users = userMgr.findAllRegistUser();
        for (RegistUser user : users) {
            RegistUserCacheKey key = new RegistUserCacheKey(user.getStoreId(),
                user.getUserName(), user.getUserType());
            registUserMap.put(key, user);
            keyIdRelationMap.put(user.getId(), key);
        }
    }

    /* (non-Javadoc)
     * @see com.h3c.o2o.portal.cache.CacheMgr#put(java.io.Serializable, java.io.Serializable)
     */
    @Override
    public void put(RegistUserCacheKey key, RegistUser value) {
        registUserMap.put(key, value);
        RegistUser user = (RegistUser) value;
        keyIdRelationMap.put(user.getId(), key);
    }

    /* (non-Javadoc)
     * @see com.h3c.o2o.portal.cache.CacheMgr#putIfAbsent(java.io.Serializable, java.io.Serializable)
     */
    @Override
    public boolean putIfAbsent(RegistUserCacheKey key, RegistUser value) {
    	throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see com.h3c.o2o.portal.cache.CacheMgr#removeByRelation(java.io.Serializable)
     */
    @Override
    public void removeByRelation(Long id) {
        Serializable user = keyIdRelationMap.get(id);
        if (user != null ) {
            registUserMap.remove(user);
        }
        keyIdRelationMap.remove(id);
    }

	@Override
	public RegistUser getFromCache(RegistUserCacheKey key) {
		throw new UnsupportedOperationException();
	}

    /* (non-Javadoc)
     * @see com.h3c.o2o.portal.cache.CacheMgr#get(java.io.Serializable)
     */
    @Override
    public RegistUser get(RegistUserCacheKey key) {
         RegistUser result = registUserMap.get(key);
        if (result == null) {
            // 缓存中没有
            log.warn("there isn't RegistUser where id = " + key.toString()
                + " in the cache.");
            RegistUserCacheKey param = (RegistUserCacheKey) key;
            RegistUser user = userMgr.queryRegistUser(param.getStoreId(),
                param.getUserName(), param.getUserType());
            if (user == null) {
                // 数据库中没有
                log.warn("there isn't RegistUser where id = " + key.toString()
                    + " in the database.");
                return null;
            }
            put(key, user);
            return user;
        }
        return result;
    }

    /* (non-Javadoc)
     * @see com.h3c.o2o.portal.cache.CacheMgr#getKeys()
     */
    @Override
    public Set<RegistUserCacheKey> getKeys() {
        return registUserMap.keySet();
    }

    /* (non-Javadoc)
     * @see com.h3c.o2o.portal.cache.CacheMgr#clear()
     */
    @Override
    public void clear() {
        registUserMap.clear();
        keyIdRelationMap.clear();
    }

    public RegistUserMgr getUserMgr() {
        return userMgr;
    }

    public void setUserMgr(RegistUserMgr userMgr) {
        this.userMgr = userMgr;
    }

	@Override
	public void remove(RegistUserCacheKey key) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void updateCache() throws Exception {
		clear();
		init();
	}

}
