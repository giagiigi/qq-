package com.h3c.o2o.portal.cache.update;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * ���»��涨ʱ����<br>
 * Ŀǰ������õ���Map��ʽ�洢��û���ϻ����ƣ����ﶨ��һ�����»��涨ʱ������ʵ�ֶ��ڵĻ�����¡�<br>
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
	 * �豻����Ĵ������MGR(ע��)
	 */
	private List<ICacheUpdate> cacheMgrs;
	
	/**
	 * update
	 */
	public void update(){
		long startTime = System.currentTimeMillis();
		if(null != getCacheMgrs() && !getCacheMgrs().isEmpty()){
			// ���ε��ø���Mgr��update����
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
