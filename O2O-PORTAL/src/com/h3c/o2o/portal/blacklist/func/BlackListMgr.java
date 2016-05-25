/*
 * Copyright (c) 2006, Hangzhou Huawei-3Com Technology Co., Ltd.
 * All rights reserved.
 * <http://www.huawei-3com.com/>
 */
package com.h3c.o2o.portal.blacklist.func;

import java.util.List;

import com.h3c.o2o.portal.entity.BlackList;


/**
 * ������ҵ���߼�����ӿڡ�
 *
 * @author dkf5133
 *
 */
public interface BlackListMgr {

    /**
     * ���ݺ�����ID��ѯ��������Ϣ��
     * @param id ������ID
     * @return ��������Ϣ
     */
    BlackList findBlackList(Long id);

    /**
     *
     * �����û�Id��ѯ��������Ϣ��
     *
     * @param userId �û�ID
     * @return ��������Ϣ
     */
    BlackList findBlackListByUser(Long userId);

    /**
     * ��ѯ���к�������Ϣ
     *
     * @return ����������
     */
    List<BlackList> findAllBlackList();

    /**
     * �������Ӻ�������һ���ǵ������ӿ�ʹ��
     * @param list �����ӵĺ�����list
     * */
    void saveBlacklist(List<BlackList> list);

    /**
     * ���������������һ���ǵ������ӿ�ʹ��
     * @param list ������ĺ�����list
     * */
    void deleteBlacklist(List<BlackList> list);
}
