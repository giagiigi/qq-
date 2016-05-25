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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.h3c.o2o.portal.cache.CacheMgr;
import com.h3c.o2o.portal.entity.PublishMngPara;
import com.h3c.o2o.portal.publishMng.func.PublishParaMgr;

/**
 * 注册用户缓存。key：storeId, value:ArrayList
 *
 * @author dkf5133
 */
public class PublishParaCacheMgrImpl implements
    CacheMgr<Long, ArrayList<PublishMngPara>> {

    private Log log = LogFactory.getLog(getClass());

    private PublishParaMgr publishParaMgr;

    /**
     * 发布参数缓存数据集合
     */
    private ConcurrentMap<Long, ArrayList<PublishMngPara>> paraMap = new ConcurrentHashMap<Long, ArrayList<PublishMngPara>>();

    /* (non-Javadoc)
     * @see com.h3c.o2o.portal.cache.CacheMgr#init()
     */
    @Override
    public void init() {
        List<PublishMngPara> paras = publishParaMgr
            .findAllPublishMngParam(PublishMngPara.getParaNames());
        if (paras == null || paras.size() == 0) {
            return;
        }
        long lastStoreId = -1;
        ArrayList<PublishMngPara> currentStoreParas = new ArrayList<PublishMngPara>();

        for (int i = 0; i < paras.size(); i++) {
            // 如果是第一条数据
            if (i == 0) {
                lastStoreId = paras.get(0).getStore_id().longValue();
            }
            if (paras.get(i).getStore_id().longValue() != lastStoreId) {
                paraMap.put(lastStoreId, currentStoreParas);
                currentStoreParas = new ArrayList<PublishMngPara>();
                lastStoreId = paras.get(i).getStore_id().longValue();
            }
            currentStoreParas.add(paras.get(i));

            // 如果是最后一条数据
            if (i == paras.size() - 1) {
                paraMap.put(lastStoreId, currentStoreParas);
            }
        }
    }

    /* (non-Javadoc)
     * @see com.h3c.o2o.portal.cache.CacheMgr#put(java.io.Serializable, java.io.Serializable)
     */
    @Override
    public void put(Long key, ArrayList<PublishMngPara> value) {
        paraMap.put(key, value);
    }

    /* (non-Javadoc)
     * @see com.h3c.o2o.portal.cache.CacheMgr#putIfAbsent(java.io.Serializable, java.io.Serializable)
     */
    @Override
    public boolean putIfAbsent(Long key, ArrayList<PublishMngPara> value) {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see com.h3c.o2o.portal.cache.CacheMgr#removeByRelation(java.io.Serializable)
     */
    @Override
    public void removeByRelation(Long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ArrayList<PublishMngPara> getFromCache(Long key) {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see com.h3c.o2o.portal.cache.CacheMgr#get(java.io.Serializable)
     */
    @Override
    public ArrayList<PublishMngPara> get(Long key) {
        ArrayList<PublishMngPara> result = paraMap.get(key);
        if (result == null) {
            // 缓存中没有
            log.warn("there isn't PublishMngPara wehre store = "
                + key.toString() + " in the cache.");
            List<PublishMngPara> paras = publishParaMgr.findPubParamByStoreId(
                (Long) key, PublishMngPara.getParaNames());
            if (paras == null) {
                // 数据库中没有
                log.warn("there isn't PublishMngPara wehre store = "
                    + key.toString() + " in the database.");
                return null;
            }
            ArrayList<PublishMngPara> params = new ArrayList<PublishMngPara>(
                paras);
            put(key, params);
            return params;
        }
        return result;
    }

    /* (non-Javadoc)
     * @see com.h3c.o2o.portal.cache.CacheMgr#getKeys()
     */
    @Override
    public Set<Long> getKeys() {
        return paraMap.keySet();
    }

    /* (non-Javadoc)
     * @see com.h3c.o2o.portal.cache.CacheMgr#clear()
     */
    @Override
    public void clear() {
        paraMap.clear();
    }

    @Override
    public void remove(Long key) {
        paraMap.remove(key);
    }

    @Override
	public void updateCache() throws Exception {
		clear();
		init();
	}
    
    public void setPublishParaMgr(PublishParaMgr publishParaMgr) {
        this.publishParaMgr = publishParaMgr;
    }

}
