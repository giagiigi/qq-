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

import com.h3c.o2o.portal.entity.RegistUser;

/**
 * �û�����Mgr
 * 
 * @author j09980
 */
public interface RegistUserMgr {

	void saveRegistUser(RegistUser user);

	void updateRegistUser(RegistUser user);

	/**
	 * ��ѯע���û�
	 * 
	 * @param id
	 * @return
	 */
	RegistUser queryRegistUser(Long id);

    /**
     * ��ѯע���û�
     * 
     * @param storeId ����ID
     * @param userName �û���
     * @param userType �û�����
     * @return
     */
    RegistUser queryRegistUser(Long storeId, String userName, Integer userType);
    
    /**
     * ��ѯ����ע���û�
     *
     * @return
     */
    List<RegistUser> findAllRegistUser();

	/**
	 * ��ѯע���û�
	 * 
	 * @param identifier
	 *            �������û���Ψһ��ʶ
	 * @return
	 */
	RegistUser queryRegistUser(String userName, String identifier,
			Integer userType);
}
