package com.h3c.o2o.portal.useragent.dao;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.h3c.o2o.portal.entity.UserAgent;

/**
 * �����ӱ��߼���
 *
 * @author ykf2685
 *
 */
public class UserAgentDaoImpl extends HibernateDaoSupport implements UserAgentDao {

	/**
     * ͨ��id��ѯ������ϸ��Ϣ
     *
     * @param id ����id
     * @return ������ϸ��Ϣʵ��
     */
    public UserAgent readById(Long id) {
        return getHibernateTemplate().get(UserAgent.class, id);
    }

    /**
     * ��ѯ������ϸ��Ϣ
     *
     * @return ������ϸ��Ϣʵ��
     */
    @SuppressWarnings("unchecked")
    public List<UserAgent> readAll(){
        return getHibernateTemplate().find("from UserAgent");
    }
}
