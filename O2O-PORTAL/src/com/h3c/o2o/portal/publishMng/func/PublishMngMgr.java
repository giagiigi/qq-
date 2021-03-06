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

public interface PublishMngMgr {

	/**
	 * 查询所有发布管理
	 * @return
	 */
	List<PublishMng> findAllEnabledPublishMng();

	/**
	 * 根据ID查询发布管理
	 * @param id
	 * @return
	 */
	PublishMng findPublishMng(long id);

	/**
	 * 根据店铺ID和SSID查询发布管理
	 * @param storeId
	 * @param ssid
	 * @return
	 */
	PublishMng findPublishMng(long storeId, String ssid);
}
