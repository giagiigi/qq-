/*
 * Copyright (c) 2015, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC V300R002
 * Module Name :
 * Date Created: 2015-7-18
 * Creator     : j09980
 * Description :
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 * YYYY-MM-DD  zhangshan        XXXX project, new code file.
 *
 *------------------------------------------------------------------------------
 */
package com.h3c.o2o.portal.user.func;

import java.util.List;

import com.h3c.o2o.portal.entity.AccessDetail;
import com.h3c.o2o.portal.entity.OnlineUser;

/**
 * �û�����Mgr
 *
 * @author j09980
 */
public interface AccessUserDetailMgr {


	void saveAccessDetail(AccessDetail detail);

	/**
	 * ���������û���Ϣ����������ϸ
	 * @param user �����û�
	 */
	void createAccessDetail(OnlineUser user);

	AccessDetail queryAccessDetail(Long storeId, String ssid, String usermac,
			long accessStartTime);

	/**
	 * ��ѯ���뿪ʼʱ��
	 *
	 * @param userId
	 * @return
	 */
	Long queryAccessStartTime(long userId);


	/**
     * ��ƫ����  ��ȡ������ϸ
     * @param identifier ������Ψһ��ʾ
     * @param offset ƫ����
     * @param limit ��¼��
     *
     * */
    List<AccessDetail> queryAccessUser(String identifier,int offset, int limit);
}
