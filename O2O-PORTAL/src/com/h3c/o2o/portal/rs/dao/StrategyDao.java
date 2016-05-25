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

import java.util.List;

import com.h3c.o2o.portal.rs.entity.ThirdPartyStrategy;

public interface StrategyDao {

	/**
	 * 保存
	 * @param entity 待保存对象
	 */
	void save(ThirdPartyStrategy entity);

	/**
	 * 移除
	 *
	 * @param mac
	 * @param type
	 */
	void delete(String mac, Integer type);

	/**
	 * 根据条件查询
	 * @param mac MAC地址
	 * @param type 类型 允许为空，为空时表示不区分类型
	 * @return
	 */
	List<ThirdPartyStrategy> query(String mac, Integer type);
}
