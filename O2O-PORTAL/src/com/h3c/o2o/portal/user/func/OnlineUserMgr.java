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

import com.h3c.o2o.portal.entity.OnlineUser;
import com.h3c.o2o.portal.protocol.entity.User;

/**
 * �����û�����Mgr
 *
 * @author j09980
 */
public interface OnlineUserMgr {

	void saveOnlineUser(OnlineUser user);

	void deleteOnlineUser(OnlineUser user);

	void deleteOnlineUserByMac(String mac);
	
	void deleteOnlineUserByStoreIdAndIp(Long storeId, Long ip);
	
	void updateOnlineUser(OnlineUser user);
	
	/**
	 * �޸����߱���Ϣ����������ʱ�����ۼ������ֽ������ۼ������ֽ���
	 * @param user
	 */
	void updateOnlineInfo(User user);

	/**
	 * ��ѯȫ�������û�
	 * @return
	 */
	List<OnlineUser> queryAllOnlineUser();

	/**
	 * ��ѯ�����û�
	 * @param identifier �������û���Ψһ��ʶ
	 * @param offset ƫ����
	 * @param limit ��¼��
	 *
	 * */
	List<OnlineUser> queryOnlineUser(String identifier, int offset,
			int limit);

	/**
     * ��ѯע���û�
     *
     * @param mac
     * @return OnlineUser
     */
    OnlineUser queryOnlineUser(String mac);
    
    /**
     * ͨ��IP��ַ��ѯ�û�
     * @param ip
     * @return
     */
    public List<OnlineUser> queryOnlineUser(Long ip, Long storeId);
}
