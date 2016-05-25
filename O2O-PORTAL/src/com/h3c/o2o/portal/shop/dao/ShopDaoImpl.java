package com.h3c.o2o.portal.shop.dao;

import java.io.Serializable;
import java.util.List;

import org.springframework.util.StringUtils;

import com.h3c.o2o.portal.entity.Shop;
import com.h3c.o2o.portal.util.WifiUtils;
import com.h3c.oasis.o2oserver.common.CommonDaoSupport;
/**
 * 校验ShopIdDao实现
 * @author heqiao
 *
 */
public class ShopDaoImpl extends CommonDaoSupport implements ShopDao, Serializable{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -272985873184960868L;

	@Override
	public String getShopNameById(Long shopId) {
		if( !WifiUtils.isValidId(shopId)){
			return null;
		}
		final String hql = "from Shop shop where shop.id = ?";
		@SuppressWarnings("unchecked")
		List<Shop> ret = getHibernateTemplate().find(hql);
		return null!=ret ? ret.get(0).getName() : null;
	}

	@Override
	public boolean isShopExists(Long shopId) {
		return StringUtils.hasText(getShopNameById(shopId));
	}

}
