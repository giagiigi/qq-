/*
 * Copyright (c) 2007-2015, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC V700R001
 * Module Name :
 * Date Created: 2015年9月30日
 * Creator     : dkf5133
 * Description :
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 * 2015年9月30日  dkf5133             O2O-PORTAL-0827 project, new code file.
 *
 *------------------------------------------------------------------------------
 */

package com.h3c.o2o.portal.rs.dao;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.h3c.o2o.portal.rs.entity.ThirdPartyStrategy;

public class StrategyDaoImpl extends HibernateDaoSupport
		implements StrategyDao {

	@Override
	public void save(ThirdPartyStrategy entity) {
		getHibernateTemplate().save(entity);
	}

	@Override
	public void delete(final String mac, final Integer type) {
		final String sql = "delete from TBL_UAM_TP_STRATEGY where mac=:mac and type=:type";
		getHibernateTemplate().execute(new HibernateCallback<Object>() {
			@Override
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				session.createSQLQuery(sql)
						.setParameter("mac", mac)
						.setParameter("type", type)
						.executeUpdate();
				return null;
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ThirdPartyStrategy> query(String mac, Integer type) {
		String hql = null;
		if (type == null) {
			hql = "from ThirdPartyStrategy where mac=?";
			return getHibernateTemplate().find(hql, mac);
		} else {
			hql = "from ThirdPartyStrategy where mac=? and type=?";
			return getHibernateTemplate().find(hql, mac, type);
		}

	}

}
