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
package com.h3c.o2o.portal.user.dao;

import java.util.List;

import com.h3c.o2o.portal.entity.OnlineUser;
import com.h3c.o2o.portal.protocol.entity.User;

/**
 * add description of types here
 *
 * @author wkf4532
 */
public interface OnlineUserDao {

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
     * ��ƫ�����Ĳ�ѯ�����û�
     * @param identifier ������Ψһ��ʾ
     * @param offset ƫ����
     * @param limit ��¼��
     *
     * */
    List<OnlineUser> queryOnlineUser(String identifier,int offset, int limit);

    OnlineUser queryOnlineUser(String mac);
    
    /**
     * ͨ��IP��ַ������ID��ѯ�û�
     * @param ip
     * @return
     */
    public List<OnlineUser> queryOnlineUser(Long ip, Long storeId);

}
