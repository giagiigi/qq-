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

import java.util.List;

import com.h3c.o2o.portal.entity.PublishMngPara;

public interface PublishParaDao {

	List<PublishMngPara> queryAllPublishMngParam(List<String> paraNames);

	PublishMngPara queryPubParamById(long id);

	List<PublishMngPara> queryPubParamByStoreId(long storeId, List<String> paraNames);
}
