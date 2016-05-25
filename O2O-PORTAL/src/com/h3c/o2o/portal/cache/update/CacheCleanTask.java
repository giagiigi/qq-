package com.h3c.o2o.portal.cache.update;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * 更新缓存定时任务<br>
 * 目前缓存采用的是Map方式存储，没有老化机制，这里定义一个更新缓存定时任务，来实现定期的缓存更新。<br>
 * 
 * @author heqiao
 *
 */
public class CacheCleanTask {

	/**
	 * Log
	 */
	private Log log = LogFactory.getLog(getClass());
	
	/**
	 * 需被清理的带缓存的MGR(注入)
	 */
	private List<ICacheUpdate> cacheMgrs;
	
	/**
	 * update
	 */
	public void update(){
		long startTime = System.currentTimeMillis();
		if(null != getCacheMgrs() && !getCacheMgrs().isEmpty()){
			// 依次调用各个Mgr的update方法
			for(ICacheUpdate cacheUpdate : getCacheMgrs()){
				if(log.isDebugEnabled()){
					log.debug("updating cache, mgr : " + cacheUpdate.getClass().getName());
				}
				try {
					cacheUpdate.updateCache();
				} catch (Exception e) {
					log.warn("Error occured when update cache.", e); 
				}
			}
		}
		long endTime = System.currentTimeMillis();
		log.info("Cache updated in " + (endTime - startTime)/1000 + "s"); 
	}

	// getter and setter
	public List<ICacheUpdate> getCacheMgrs() {
		return cacheMgrs;
	}

	public void setCacheMgrs(List<ICacheUpdate> cacheMgrs) {
		this.cacheMgrs = cacheMgrs;
	}
	
}
