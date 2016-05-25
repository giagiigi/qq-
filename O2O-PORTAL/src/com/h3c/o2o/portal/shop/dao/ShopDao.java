package com.h3c.o2o.portal.shop.dao;

/**
 * 校验ShopIdDao
 * @author heqiao
 *
 */
public interface ShopDao {
	/**
	 * 获取Shop名称
	 * @param shopId
	 * @return
	 */
	public String getShopNameById(Long shopId);
	
	/**
	 * 获取ShopId
	 * @param shopId
	 * @return
	 */
	public boolean isShopExists(Long shopId);
}
