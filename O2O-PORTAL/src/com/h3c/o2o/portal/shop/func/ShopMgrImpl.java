package com.h3c.o2o.portal.shop.func;

import java.io.Serializable;

import com.h3c.o2o.portal.shop.dao.ShopDao;

/**
 * –£—ÈShopIdMGR
 * 
 * @author heqiao
 *
 */
public class ShopMgrImpl implements ShopMgr, Serializable {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -7419791023251232552L;
	private ShopDao dao;

	public ShopDao getDao() {
		return dao;
	}

	public void setDao(ShopDao dao) {
		this.dao = dao;
	}

	@Override
	public String getShopNameById(Long shopId) {
		return this.getDao().getShopNameById(shopId);
	}

	@Override
	public boolean isShopIdValide(Long shopId) {
		return this.getDao().isShopExists(shopId);
	}
}
