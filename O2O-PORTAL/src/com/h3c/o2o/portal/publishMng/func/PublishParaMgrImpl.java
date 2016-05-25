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

package com.h3c.o2o.portal.publishMng.func;

import java.util.List;

import com.h3c.o2o.portal.entity.PublishMngPara;
import com.h3c.o2o.portal.publishMng.dao.PublishParaDao;

public class PublishParaMgrImpl implements PublishParaMgr {

	private PublishParaDao publishParaDao;

	@Override
	public List<PublishMngPara> findAllPublishMngParam(List<String> paraNames) {
		return publishParaDao.queryAllPublishMngParam(paraNames);
	}

	@Override
	public PublishMngPara findPubParamById(long id) {
		return publishParaDao.queryPubParamById(id);
	}

	@Override
	public List<PublishMngPara> findPubParamByStoreId(long storeId, List<String> paraNames) {
		return publishParaDao.queryPubParamByStoreId(storeId, paraNames);
	}

	public void setPublishParaDao(PublishParaDao publishParaDao) {
		this.publishParaDao = publishParaDao;
	}
}
