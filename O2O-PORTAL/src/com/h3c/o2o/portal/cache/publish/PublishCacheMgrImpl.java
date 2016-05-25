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
 * 2015-10-19  dkf5133           o2o project, new code file.
 *
 *------------------------------------------------------------------------------
 */
package com.h3c.o2o.portal.cache.publish;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.h3c.o2o.portal.cache.CacheMgr;
import com.h3c.o2o.portal.cache.key.PublishMngCacheKey;
import com.h3c.o2o.portal.entity.PublishMng;
import com.h3c.o2o.portal.publishMng.func.PublishMngMgr;

/**
 * 注册用户缓存。主键：storeId + ssid
 *
 * @author dkf5133
 */
public class PublishCacheMgrImpl implements CacheMgr<PublishMngCacheKey, PublishMng> {

    private Log log = LogFactory.getLog(getClass());

    private PublishMngMgr publishMngMgr;

    /**
     * 发布缓存数据集合
     */
    private ConcurrentMap<PublishMngCacheKey, PublishMng> publishMap =
        new ConcurrentHashMap<PublishMngCacheKey, PublishMng>();

    /**
     * 发布实体ID与缓存数据KEY之间的关系缓存。key：实体ID。
     */
    private ConcurrentMap<Long, PublishMngCacheKey> keyIdRelationMap
        = new ConcurrentHashMap<Long, PublishMngCacheKey>();

    /* (non-Javadoc)
     * @see com.h3c.o2o.portal.cache.CacheMgr#init()
     */
    @Override
    public void init() {
        List<PublishMng> publishes = publishMngMgr.findAllEnabledPublishMng();
        for (PublishMng publish : publishes) {
			PublishMngCacheKey key = new PublishMngCacheKey(
					publish.getStoreId(), publish.getSsidName());
            publishMap.put(key, publish);
            keyIdRelationMap.put(publish.getId(), key);
        }
    }

    /* (non-Javadoc)
     * @see com.h3c.o2o.portal.cache.CacheMgr#put(java.io.Serializable, java.io.Serializable)
     */
    @Override
    public void put(PublishMngCacheKey key, PublishMng value) {
        publishMap.put(key, value);
        PublishMng publish = (PublishMng) value;
        keyIdRelationMap.put(publish.getId(), key);
    }

    /* (non-Javadoc)
     * @see com.h3c.o2o.portal.cache.CacheMgr#putIfAbsent(java.io.Serializable, java.io.Serializable)
     */
    @Override
    public boolean putIfAbsent(PublishMngCacheKey key, PublishMng value) {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see com.h3c.o2o.portal.cache.CacheMgr#removeByRelation(java.io.Serializable)
     */
    @Override
    public void removeByRelation(Long id) {
        Serializable publishKey = keyIdRelationMap.get(id);
        if (publishKey != null ) {
            publishMap.remove(publishKey);
        }
        keyIdRelationMap.remove(id);
    }

	@Override
	public PublishMng getFromCache(PublishMngCacheKey key) {
		throw new UnsupportedOperationException();
	}

    /* (non-Javadoc)
     * @see com.h3c.o2o.portal.cache.CacheMgr#get(java.io.Serializable)
     */
    @Override
	public PublishMng get(PublishMngCacheKey key) {
        PublishMng result = publishMap.get(key);
		if (result == null) {
			// 缓存中没有
			log.warn("there isn't PublishMng wehre key = " + key.toString()
					+ " in the cache.");
			PublishMngCacheKey param = (PublishMngCacheKey) key;
			PublishMng publish = publishMngMgr.findPublishMng(
					param.getStoreId(), param.getSsid());
			if (publish == null) {
				// 数据库中没有
				log.warn("there isn't PublishMng wehre key = " + key.toString()
						+ " in the database.");
				return null;
			}
			put(key, publish);
			return publish;
		}
		return result;
	}

    /* (non-Javadoc)
     * @see com.h3c.o2o.portal.cache.CacheMgr#getKeys()
     */
    @Override
    public Set<PublishMngCacheKey> getKeys() {
        return publishMap.keySet();
    }

    /* (non-Javadoc)
     * @see com.h3c.o2o.portal.cache.CacheMgr#clear()
     */
    @Override
    public void clear() {
        publishMap.clear();
        keyIdRelationMap.clear();
    }

	@Override
	public void remove(PublishMngCacheKey key) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateCache() throws Exception {
		clear();
		init();
	}
	
	public void setPublishMngMgr(PublishMngMgr publishMngMgr) {
		this.publishMngMgr = publishMngMgr;
	}

}
