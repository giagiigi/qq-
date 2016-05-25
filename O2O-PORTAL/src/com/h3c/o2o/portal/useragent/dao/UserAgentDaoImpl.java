package com.h3c.o2o.portal.useragent.dao;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.h3c.o2o.portal.entity.UserAgent;

/**
 * 主题子表逻辑。
 *
 * @author ykf2685
 *
 */
public class UserAgentDaoImpl extends HibernateDaoSupport implements UserAgentDao {

	/**
     * 通过id查询主题详细信息
     *
     * @param id 主题id
     * @return 主题详细信息实体
     */
    public UserAgent readById(Long id) {
        return getHibernateTemplate().get(UserAgent.class, id);
    }

    /**
     * 查询主题详细信息
     *
     * @return 主题详细信息实体
     */
    @SuppressWarnings("unchecked")
    public List<UserAgent> readAll(){
        return getHibernateTemplate().find("from UserAgent");
    }
}
