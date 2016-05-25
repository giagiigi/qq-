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
package com.h3c.o2o.portal.cache.blacklist;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.h3c.o2o.portal.blacklist.func.BlackListMgr;
import com.h3c.o2o.portal.cache.CacheMgr;
import com.h3c.o2o.portal.entity.BlackList;

/**
 * 黑名单缓存。主键：用户ID
 *
 * @author dkf5133
 */
public class BlackListCacheMgrImpl implements CacheMgr<Long, BlackList> {

    private Log log = LogFactory.getLog(BlackListCacheMgrImpl.class);

    private BlackListMgr blackListMgr;

    /**
     * 认证配置缓存数据集合
     */
    private ConcurrentMap<Long, BlackList> blackListMap
        = new ConcurrentHashMap<Long, BlackList>();

    /**
     * 实体ID与缓存数据KEY（店铺ID）之间的关系缓存。key：实体ID。
     */
    private ConcurrentMap<Long, Long> keyIdRelationMap
        = new ConcurrentHashMap<Long, Long>();

    /* (non-Javadoc)
     * @see com.h3c.o2o.portal.cache.CacheMgr#init()
     */
    @Override
    public void init() {
        List<BlackList> list = blackListMgr.findAllBlackList();
        for (BlackList item : list) {
            blackListMap.put(item.getUserId(), item);
            keyIdRelationMap.put(item.getId(), item.getUserId());
        }
    }

    /* (non-Javadoc)
     * @see com.h3c.o2o.portal.cache.CacheMgr#put(java.io.Serializable, java.io.Serializable)
     */
    @Override
    public void put(Long key, BlackList value) {
        blackListMap.put(key, value);
        BlackList blackList = (BlackList) value;
        keyIdRelationMap.put(blackList.getId(), blackList.getUserId());
    }

    /* (non-Javadoc)
     * @see com.h3c.o2o.portal.cache.CacheMgr#putIfAbsent(java.io.Serializable, java.io.Serializable)
     */
    @Override
    public boolean putIfAbsent(Long key, BlackList value) {
        BlackList blackList = (BlackList) value;
        keyIdRelationMap.putIfAbsent(blackList.getId(), blackList.getUserId());
        return blackListMap.putIfAbsent(key, value) == null ? false : true;
    }

    /* (non-Javadoc)
     * @see com.h3c.o2o.portal.cache.CacheMgr#remove(java.io.Serializable)
     */
//    @Override
//    public void remove(Serializable key) {
//        authCfgMap.remove(key);
//    }

    /* (non-Javadoc)
     * @see com.h3c.o2o.portal.cache.CacheMgr#removeByRelation(java.io.Serializable)
     */
    @Override
    public void removeByRelation(Long id) {
        Serializable userId = keyIdRelationMap.get(id);
        if (userId != null) {
            blackListMap.remove(userId);
        }
        keyIdRelationMap.remove(id);


        if (log.isDebugEnabled()) {
            log.debug("blackListMap: ");
            for (Serializable key : blackListMap.keySet()) {
                log.debug("key: "
                    + key.toString()
                    + " ~ value: "
                    + blackListMap.get(key)
                        .toString());
            }
            log.debug("keyIdRelationMap: ");
            for (Serializable key : keyIdRelationMap.keySet()) {
                log.debug("key: "
                    + key.toString()
                    + " ~ value: "
                    + keyIdRelationMap.get(key)
                        .toString());
            }
        }
    }

	@Override
	public BlackList getFromCache(Long key) {
		throw new UnsupportedOperationException();
	}

    /* (non-Javadoc)
     * @see com.h3c.o2o.portal.cache.CacheMgr#get(java.io.Serializable)
     */
    @Override
    public BlackList get(Long key) {
        BlackList result = blackListMap.get(key);
        if (result == null) {
            // 缓存中没有
        	if(log.isDebugEnabled()){
        		log.debug("no blackList found by id = " + key + " in the cache.");
        	}
        }
        return result;
    }

    /* (non-Javadoc)
     * @see com.h3c.o2o.portal.cache.CacheMgr#getKeys()
     */
    @Override
    public Set<Long> getKeys() {
        return blackListMap.keySet();
    }

    /* (non-Javadoc)
     * @see com.h3c.o2o.portal.cache.CacheMgr#clear()
     */
    @Override
    public void clear() {
        blackListMap.clear();
        keyIdRelationMap.clear();
    }

    public void setBlackListMgr(BlackListMgr blackListMgr) {
        this.blackListMgr = blackListMgr;
    }

	@Override
	public void remove(Long key) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateCache() throws Exception {
		clear();
		init();
	}

}
