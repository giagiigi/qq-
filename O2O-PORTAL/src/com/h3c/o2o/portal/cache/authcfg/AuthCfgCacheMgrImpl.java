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
package com.h3c.o2o.portal.cache.authcfg;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.h3c.o2o.portal.auth.func.AuthCfgMgr;
import com.h3c.o2o.portal.cache.CacheMgr;
import com.h3c.o2o.portal.entity.AuthCfg;

/**
 * 认证配置缓存。主键：ID
 *
 * @author dkf5133
 */
public class AuthCfgCacheMgrImpl implements CacheMgr<Long, AuthCfg> {

    private Log log = LogFactory.getLog(getClass());

    private AuthCfgMgr authCfgMgr;

    /**
     * 认证配置缓存数据集合
     */
    private ConcurrentMap<Long, AuthCfg> authCfgMap
        = new ConcurrentHashMap<Long, AuthCfg>();

    /* (non-Javadoc)
     * @see com.h3c.o2o.portal.cache.CacheMgr#init()
     */
    @Override
    public void init() {
    	throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see com.h3c.o2o.portal.cache.CacheMgr#put(java.io.Serializable, java.io.Serializable)
     */
    @Override
    public void put(Long key, AuthCfg value) {
        authCfgMap.put(key, value);
    }

    /* (non-Javadoc)
     * @see com.h3c.o2o.portal.cache.CacheMgr#putIfAbsent(java.io.Serializable, java.io.Serializable)
     */
    @Override
    public boolean putIfAbsent(Long key, AuthCfg value) {
        return authCfgMap.putIfAbsent(key, value) == null ? false : true;
    }

    /* (non-Javadoc)
     * @see com.h3c.o2o.portal.cache.CacheMgr#remove(java.io.Serializable)
     */
    @Override
    public void remove(Long key) {
        authCfgMap.remove(key);
    }

    /* (non-Javadoc)
     * @see com.h3c.o2o.portal.cache.CacheMgr#removeByRelation(java.io.Serializable)
     */
    @Override
    public void removeByRelation(Long id) {
    	throw new UnsupportedOperationException();
    }

	@Override
	public AuthCfg getFromCache(Long key) {
		 AuthCfg result = authCfgMap.get(key);
        if (result == null) {
            // 缓存中没有
            log.warn("there isn't config wehre id = " + key + " in the cache.");
        }
        return result;
	}

    /* (non-Javadoc)
     * @see com.h3c.o2o.portal.cache.CacheMgr#get(java.io.Serializable)
     */
    @Override
    public AuthCfg get(Long key) {
         AuthCfg result = authCfgMap.get(key);
        if (result == null) {
            // 缓存中没有
            log.warn("there isn't config where id = " + key + " in the cache.");
            AuthCfg cfg = authCfgMgr.findAuthConfig((long) key);
            if (cfg == null) {
                // 数据库中没有
                log.warn("there isn't config wehre id = " + key
                    + " in the database.");
                return null;
            }
            put(key, cfg);
            return cfg;
        }
        return result;
    }

    /* (non-Javadoc)
     * @see com.h3c.o2o.portal.cache.CacheMgr#getKeys()
     */
    @Override
    public Set<Long> getKeys() {
        return authCfgMap.keySet();
    }

    /* (non-Javadoc)
     * @see com.h3c.o2o.portal.cache.CacheMgr#clear()
     */
    @Override
    public void clear() {
        authCfgMap.clear();
    }

    public AuthCfgMgr getAuthCfgMgr() {
        return authCfgMgr;
    }

    public void setAuthCfgMgr(AuthCfgMgr authCfgMgr) {
        this.authCfgMgr = authCfgMgr;
    }

	public ConcurrentMap<Serializable, Serializable> getAuthCfgMap() {
		return new ConcurrentHashMap<Serializable, Serializable>(
				this.authCfgMap);
	}

	@Override
	public void updateCache() throws Exception {
		clear();
		// 根据访问加载，这里只需清空即可。
		// init();
	}

}
