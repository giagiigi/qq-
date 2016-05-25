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

import java.util.List;

import com.h3c.o2o.portal.entity.PublishMng;

public interface PublishMngDao {

	List<PublishMng> queryAllEnabledPublishMng();

	PublishMng queryPublishMng(long id);

	PublishMng queryPublishMng(long storeId, String ssid);
}
