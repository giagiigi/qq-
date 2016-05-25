package com.h3c.o2o.portal.useragent.func;

import java.util.List;

import com.h3c.o2o.portal.entity.UserAgent;

/**
 * �����ӱ��߼���
 *
 * @author ykf2685
 *
 */
public interface UserAgentMgr {

    /**
     * ͨ��id��ѯ������ϸ��Ϣ
     *
     * @param id ����id
     * @return ������ϸ��Ϣʵ��
     */
    public UserAgent queryById(Long id);


    /**
     * ��ѯ������ϸ��Ϣ
     *
     * @return ������ϸ��Ϣʵ��
     */
    public List<UserAgent> queryAll();
    
    /**
     * ����useragent��ȡ��Ϣ��
     *
     * @param userAgent
     * @return ����
     */
    public UserAgent parseUserAgent(String agent);
}
