package com.h3c.o2o.portal.cache.update;

/**
 * 更新缓存接口<br>
 * 目前缓存采用的是Map方式存储，没有老化机制，这里定义一个缓存更新接口，来实现定期的缓存更新。<br>
 * 
 * @author heqiao
 *
 */
public interface ICacheUpdate {

	/**
	 * 更新缓存
	 */
	public void updateCache() throws Exception;
}
