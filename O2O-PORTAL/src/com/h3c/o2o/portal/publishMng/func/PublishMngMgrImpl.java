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

package com.h3c.o2o.portal.publishMng.func;

import java.util.List;

import com.h3c.o2o.portal.entity.PublishMng;
import com.h3c.o2o.portal.publishMng.dao.PublishMngDao;

public class PublishMngMgrImpl implements PublishMngMgr {

	private PublishMngDao publishMngDao;

	@Override
	public List<PublishMng> findAllEnabledPublishMng() {
		return publishMngDao.queryAllEnabledPublishMng();
	}

	@Override
	public PublishMng findPublishMng(long id) {
		return publishMngDao.queryPublishMng(id);
	}

	@Override
	public PublishMng findPublishMng(long storeId, String ssid) {
		return publishMngDao.queryPublishMng(storeId, ssid);
	}

	public void setPublishMngDao(PublishMngDao publishMngDao) {
		this.publishMngDao = publishMngDao;
	}

}
