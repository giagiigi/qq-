/*
 * Copyright (c) 2006, Hangzhou Huawei-3Com Technology Co., Ltd.
 * All rights reserved.
 * <http://www.huawei-3com.com/>
 */
package com.h3c.o2o.portal.blacklist.dao;

import java.util.List;

import com.h3c.o2o.portal.entity.BlackList;


/**
 * ���������ݷ��ʽӿڡ�
 * 
 * @author dkf5133
 *
 */
public interface BlackListDao {

    /**
     * ���ݺ�����ID��ѯ��������Ϣ��
     * @param id ������ID
     * @return ��������Ϣ
     */
    BlackList queryBlackList(Long id);
    
    /**
     * 
     * �����û�Id��ѯ��������Ϣ��
     *
     * @param userId �û�ID
     * @return ��������Ϣ
     */
    BlackList queryBlackListByUser(Long userId);
    
    /**
     * ��ѯ���к�������Ϣ
     *
     * @return ����������
     */
    List<BlackList> queryAllBlackList();
    
    /**
     * ���Ӻ�������һ���ǵ������ӿ�ʹ��
     * */
    void save(BlackList entity);
    
    /**
     * �����������һ���ǵ������ӿ�ʹ��
     * */
    void delete(BlackList entity);

}
