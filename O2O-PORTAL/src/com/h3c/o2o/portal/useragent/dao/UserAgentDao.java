package com.h3c.o2o.portal.useragent.dao;

import java.util.List;

import com.h3c.o2o.portal.entity.UserAgent;

/**
 * �����ӱ��߼���
 *
 * @author ykf2685
 *
 */
public interface UserAgentDao {

    /**
     * ͨ��id��ѯ������ϸ��Ϣ
     *
     * @param id ����id
     * @return ������ϸ��Ϣʵ��
     */
    public UserAgent readById(Long id);

    /**
     * ��ѯ������ϸ��Ϣ
     *
     * @return ������ϸ��Ϣʵ��
     */
    public List<UserAgent> readAll();
}
