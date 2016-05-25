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
package com.h3c.o2o.portal.cache.theme;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.h3c.o2o.portal.cache.CacheMgr;
import com.h3c.o2o.portal.entity.ThemePage;
import com.h3c.o2o.portal.entity.ThemeTemplate;
import com.h3c.o2o.portal.theme.func.ThemePageMgr;

/**
 * 主题模板缓存。主键：id；值：店铺当前启用的模板
 *
 * @author dkf5133
 */
public class ThemeTemplateCacheMgrImpl implements CacheMgr<Long, ThemeTemplate> {

    private Log log = LogFactory.getLog(ThemeTemplateCacheMgrImpl.class);

    private ThemePageMgr themePageMgr;

    /**
     * 主题模板缓存数据集合
     */
    private ConcurrentMap<Long, ThemeTemplate> themeTemplateMap
        = new ConcurrentHashMap<Long, ThemeTemplate>();

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
	public void put(Long key, ThemeTemplate value) {
		themeTemplateMap.put(key, value);
	}

    /* (non-Javadoc)
     * @see com.h3c.o2o.portal.cache.CacheMgr#putIfAbsent(java.io.Serializable, java.io.Serializable)
     */
    @Override
    public boolean putIfAbsent(Long key, ThemeTemplate value) {
        return themeTemplateMap.putIfAbsent(key, value) == null ? false : true;
    }

    /* (non-Javadoc)
     * @see com.h3c.o2o.portal.cache.CacheMgr#remove(java.io.Serializable)
     */
    @Override
    public void remove(Long key) {
        themeTemplateMap.remove(key);
    }

    /* (non-Javadoc)
     * @see com.h3c.o2o.portal.cache.CacheMgr#removeByRelation(java.io.Serializable)
     */
    @Override
    public void removeByRelation(Long id) {
    	throw new UnsupportedOperationException();
    }

	@Override
	public ThemeTemplate getFromCache(Long key) {
		throw new UnsupportedOperationException();
	}

    /* (non-Javadoc)
     * @see com.h3c.o2o.portal.cache.CacheMgr#get(java.io.Serializable)
     */
    @Override
    public ThemeTemplate get(Long key) {
        ThemeTemplate result = themeTemplateMap.get(key);
        if (result == null) {
            // 缓存中没有
            log.warn("there isn't Template wehre id = " + key
                + " in the cache.");
			ThemeTemplate temp = themePageMgr.findTemplateForCache((long)key);
            if (temp == null) {
                // 数据库中没有
                log.warn("there isn't Template wehre id = " + key
                    + " in the database.");
                return null;
            }
            List<ThemePage> pages = themePageMgr.findThemePagesForCache(temp);
            temp.setPages(pages);
            put(key, temp);
            return temp;
        }
        return result;
    }

    /* (non-Javadoc)
     * @see com.h3c.o2o.portal.cache.CacheMgr#getKeys()
     */
    @Override
    public Set<Long> getKeys() {
        return themeTemplateMap.keySet();
    }

    /* (non-Javadoc)
     * @see com.h3c.o2o.portal.cache.CacheMgr#clear()
     */
    @Override
    public void clear() {
        themeTemplateMap.clear();
    }

    @Override
	public void updateCache() throws Exception {
		clear();
		// 根据访问加载，这里只需清空即可。
		// init();
	}
    
    public ThemePageMgr getThemePageMgr() {
        return themePageMgr;
    }

    public void setThemePageMgr(ThemePageMgr themePageMgr) {
        this.themePageMgr = themePageMgr;
    }

}
