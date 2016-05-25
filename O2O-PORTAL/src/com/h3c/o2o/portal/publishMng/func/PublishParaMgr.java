/*
 * Copyright (c) 2007-2015, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC V700R001
 * Module Name :
 * Date Created: 2015��10��31��
 * Creator     : dkf5133
 * Description :
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 * 2015��10��31��  dkf5133             O2O-PORTAL-UI project, new code file.
 *
 *------------------------------------------------------------------------------
 */

package com.h3c.o2o.portal.publishMng.func;

import java.util.List;

import com.h3c.o2o.portal.entity.PublishMngPara;

public interface PublishParaMgr {

	/**
	 * ��ѯ���з����������
	 * @return ���������б�Ĭ�ϰ�����id��������
	 */
	List<PublishMngPara> findAllPublishMngParam(List<String> paraNames);

	/**
	 * ����ID��ѯ�����������
	 * @param id
	 * @return
	 */
	PublishMngPara findPubParamById(long id);

	/**
	 * ���ݳ���ID��ѯ�����������
	 * @param storeId
	 * @return
	 */
	List<PublishMngPara> findPubParamByStoreId(long storeId, List<String> paraNames);

}
