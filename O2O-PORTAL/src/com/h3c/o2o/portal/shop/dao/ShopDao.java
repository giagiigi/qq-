package com.h3c.o2o.portal.shop.dao;

/**
 * У��ShopIdDao
 * @author heqiao
 *
 */
public interface ShopDao {
	/**
	 * ��ȡShop����
	 * @param shopId
	 * @return
	 */
	public String getShopNameById(Long shopId);
	
	/**
	 * ��ȡShopId
	 * @param shopId
	 * @return
	 */
	public boolean isShopExists(Long shopId);
}
