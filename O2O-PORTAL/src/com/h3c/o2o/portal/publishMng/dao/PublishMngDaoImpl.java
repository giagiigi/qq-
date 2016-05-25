/*
 * Copyright (c) 2007-2015, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC V700R001
 * Module Name :
 * Date Created: 2015年10月19日
 * Creator     : dkf5133
 * Description :
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 * 2015年10月19日  dkf5133             O2O-PORTAL-UI project, new code file.
 *
 *------------------------------------------------------------------------------
 */

package com.h3c.o2o.portal.publishMng.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.h3c.o2o.portal.entity.PublishMng;

public class PublishMngDaoImpl extends HibernateDaoSupport implements PublishMngDao {

	@Override
	public List<PublishMng> queryAllEnabledPublishMng() {
		final String hql = "select id,storeId,ssidName,authCfgId,themeTemplateId,ownerId,weixinAccountId from PublishMng where isPublished=?";
		@SuppressWarnings("unchecked")
		List<Object> publishes = getHibernateTemplate().find(hql, PublishMng.IS_PUBLISHED_TRUE);

		List<PublishMng> result = new ArrayList<PublishMng>();
		if (publishes != null && publishes.size() > 0) {
			for (Object item : publishes) {
				Object[] itemArry = (Object[]) item;
				PublishMng publish = new PublishMng();
				publish.setId((long)itemArry[0]);
				publish.setStoreId((long)itemArry[1]);
				publish.setSsidName((String)itemArry[2]);
				publish.setAuthCfgId((long)itemArry[3]);
				publish.setThemeTemplateId((long)itemArry[4]);
				publish.setOwnerId((long)itemArry[5]);
				// 微信公众号允许为空
				publish.setWeixinAccountId((Long)itemArry[6]);
				result.add(publish);
			}
		}
 		return result;
	}

	@Override
	public PublishMng queryPublishMng(final long id) {
		final String hql = "select id,storeId,ssidName,authCfgId,themeTemplateId,ownerId,weixinAccountId from PublishMng where id=? and isPublished=?";
		@SuppressWarnings("unchecked")
		List<Object> publishes = getHibernateTemplate().find(hql, id, PublishMng.IS_PUBLISHED_TRUE);
		if (publishes != null && publishes.size() > 0) {
			Object[] item = (Object[]) publishes.get(0);
			PublishMng publish = new PublishMng();
			publish.setId((long)item[0]);
			publish.setStoreId((long)item[1]);
			publish.setSsidName((String)item[2]);
			publish.setAuthCfgId((long)item[3]);
			publish.setThemeTemplateId((long)item[4]);
			publish.setOwnerId((long)item[5]);
			// 微信公众号允许为空
			publish.setWeixinAccountId((Long)item[6]);
			return publish;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public PublishMng queryPublishMng(final long storeId, final String ssid) {
		final String hql;
		List<Object> publishes;
		if (ssid != null) {
			hql = "select id,storeId,ssidName,authCfgId,themeTemplateId,ownerId,weixinAccountId from PublishMng where storeId=? and ssidName=? and isPublished=?";
			publishes = getHibernateTemplate().find(hql, storeId, ssid, PublishMng.IS_PUBLISHED_TRUE);
		} else {
			hql = "select id,storeId,ssidName,authCfgId,themeTemplateId,ownerId,weixinAccountId from PublishMng where storeId=? and ssidName is null and isPublished=?";
			publishes = getHibernateTemplate().find(hql, storeId, PublishMng.IS_PUBLISHED_TRUE);
		}
		if (publishes != null && publishes.size() > 0) {
			Object[] item = (Object[]) publishes.get(0);
			PublishMng publish = new PublishMng();
			publish.setId((long)item[0]);
			publish.setStoreId((long)item[1]);
			publish.setSsidName((String)item[2]);
			publish.setAuthCfgId((long)item[3]);
			publish.setThemeTemplateId((long)item[4]);
			publish.setOwnerId((long)item[5]);
			// 微信公众号允许为空
			publish.setWeixinAccountId((Long)item[6]);
			return publish;
		}
		return null;
	}

}
