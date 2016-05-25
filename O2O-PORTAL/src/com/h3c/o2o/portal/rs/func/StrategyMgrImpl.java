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

package com.h3c.o2o.portal.rs.func;

import java.util.List;

import com.h3c.o2o.portal.rs.dao.StrategyDao;
import com.h3c.o2o.portal.rs.entity.ThirdPartyStrategy;

public class StrategyMgrImpl implements StrategyMgr {

	private StrategyDao strategyDao;

	@Override
	public void save(ThirdPartyStrategy entity) {
		strategyDao.save(entity);
	}

	@Override
	public void delete(String mac, Integer type) {
		strategyDao.delete(mac, type);
	}

	@Override
	public List<ThirdPartyStrategy> query(String mac, Integer type) {
		return strategyDao.query(mac, type);
	}

	public void setStrategyDao(StrategyDao strategyDao) {
		this.strategyDao = strategyDao;
	}

}
