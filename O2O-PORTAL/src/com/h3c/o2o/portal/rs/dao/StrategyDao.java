/*
 * Copyright (c) 2007-2015, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC V700R001
 * Module Name :
 * Date Created: 2015��9��30��
 * Creator     : dkf5133
 * Description :
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 * 2015��9��30��  dkf5133             O2O-PORTAL-0827 project, new code file.
 *
 *------------------------------------------------------------------------------
 */

package com.h3c.o2o.portal.rs.dao;

import java.util.List;

import com.h3c.o2o.portal.rs.entity.ThirdPartyStrategy;

public interface StrategyDao {

	/**
	 * ����
	 * @param entity ���������
	 */
	void save(ThirdPartyStrategy entity);

	/**
	 * �Ƴ�
	 *
	 * @param mac
	 * @param type
	 */
	void delete(String mac, Integer type);

	/**
	 * ����������ѯ
	 * @param mac MAC��ַ
	 * @param type ���� ����Ϊ�գ�Ϊ��ʱ��ʾ����������
	 * @return
	 */
	List<ThirdPartyStrategy> query(String mac, Integer type);
}
