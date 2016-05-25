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

public interface PublishParaMgr {

	/**
	 * 查询所有发布管理参数
	 * @return 发布参数列表，默认按店铺id升序排列
	 */
	List<PublishMngPara> findAllPublishMngParam(List<String> paraNames);

	/**
	 * 根据ID查询发布管理参数
	 * @param id
	 * @return
	 */
	PublishMngPara findPubParamById(long id);

	/**
	 * 根据场所ID查询发布管理参数
	 * @param storeId
	 * @return
	 */
	List<PublishMngPara> findPubParamByStoreId(long storeId, List<String> paraNames);

}
