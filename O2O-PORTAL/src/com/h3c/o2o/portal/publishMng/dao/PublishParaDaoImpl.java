/*
 * Copyright (c) 2007-2015, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC V700R001
 * Module Name :
 * Date Created: 2015年10月31日
 * Creator     : dkf5133
 * Description :
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 * 2015年10月31日  dkf5133             O2O-PORTAL-UI project, new code file.
 *
 *------------------------------------------------------------------------------
 */

package com.h3c.o2o.portal.publishMng.dao;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.h3c.o2o.portal.entity.PublishMngPara;

public class PublishParaDaoImpl extends HibernateDaoSupport implements
		PublishParaDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<PublishMngPara> queryAllPublishMngParam(final List<String> paraNames) {
		final String sql = "select p.ID,s.ID as STORE_ID,p.PARAM_NAME,p.PARAM_VALUE from TBL_WSM_SHOP s left join TBL_UAM_PUBLISH_PARAM p on s.ID=p.STORE_ID where p.PARAM_NAME in(:paraNames) order by s.ID asc";
		List<PublishMngPara> params = (List<PublishMngPara>) getHibernateTemplate()
				.execute(new HibernateCallback<List<PublishMngPara>>() {
					@Override
					public List<PublishMngPara> doInHibernate(Session session)
							throws HibernateException, SQLException {
						return (List<PublishMngPara>) session
								.createSQLQuery(sql)
								.addEntity(PublishMngPara.class)
								.setParameterList("paraNames", paraNames)
								.list();
					}
				});
		return params;
	}

	@SuppressWarnings("unchecked")
	@Override
	public PublishMngPara queryPubParamById(final long id) {
		final String sql = "select p.ID,s.ID as STORE_ID,p.PARAM_NAME,p.PARAM_VALUE from TBL_WSM_SHOP s left join TBL_UAM_PUBLISH_PARAM p on s.ID=p.STORE_ID where p.ID=:paramId order by s.ID asc";
		List<PublishMngPara> params = (List<PublishMngPara>) getHibernateTemplate()
				.execute(new HibernateCallback<List<PublishMngPara>>() {
					@Override
					public List<PublishMngPara> doInHibernate(Session session)
							throws HibernateException, SQLException {
						return (List<PublishMngPara>) session
								.createSQLQuery(sql)
								.addEntity(PublishMngPara.class)
								.setParameter("paramId", id)
								.list();
					}
				});
		if (params != null && params.size() > 0) {
			return params.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PublishMngPara> queryPubParamByStoreId(final long storeId, final List<String> paraNames) {
		final String sql = "select p.ID,s.ID as STORE_ID,p.PARAM_NAME,p.PARAM_VALUE from TBL_WSM_SHOP s left join TBL_UAM_PUBLISH_PARAM p on s.ID=p.STORE_ID where p.STORE_ID=:storeId and p.PARAM_NAME in (:paraNames) order by s.ID asc";
		List<PublishMngPara> params = (List<PublishMngPara>) getHibernateTemplate()
				.execute(new HibernateCallback<List<PublishMngPara>>() {
					@Override
					public List<PublishMngPara> doInHibernate(Session session)
							throws HibernateException, SQLException {
						return (List<PublishMngPara>) session
								.createSQLQuery(sql)
								.addEntity(PublishMngPara.class)
								.setParameterList("paraNames", paraNames)
								.setParameter("storeId", storeId)
								.list();
					}
				});
		return params;
	}

}
