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

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.h3c.o2o.portal.entity.RegistUser;

/**
 * 用户Dao实现类
 * 
 * @author j09980
 */
public class RegistUserDaoImpl extends HibernateDaoSupport implements RegistUserDao {

	@Override
	public void saveRegistUser(RegistUser user) {
		getHibernateTemplate().save(user);
	}

	public void updateRegistUser(RegistUser user) {
		getHibernateTemplate().merge(user);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public RegistUser queryRegistUser(final Long id) {
		final String hql = "select * from TBL_UAM_REGIST_USER "
				+ " where ID=:id";
		List<RegistUser> users = (List<RegistUser>) getHibernateTemplate()
				.execute(new HibernateCallback() {
					@Override
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						return session.createSQLQuery(hql)
								.addEntity(RegistUser.class)
								.setParameter("id", id).list();
					}
				});

		if (users != null && users.size() == 1) {
			return users.get(0);
		}
		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public RegistUser queryRegistUser(final Long storeId,
			final String userName, final Integer userType) {
		final String hql = "select * from TBL_UAM_REGIST_USER "
				+ " where STORE_ID=:storeId and USER_NAME=:userName and USER_TYPE=:userType";
		List<RegistUser> users = (List<RegistUser>) getHibernateTemplate()
				.execute(new HibernateCallback() {
					@Override
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						return session.createSQLQuery(hql)
								.addEntity(RegistUser.class)
								.setParameter("storeId", storeId)
								.setParameter("userName", userName)
								.setParameter("userType", userType).list();
					}
				});

		if (users != null && users.size() == 1) {
			return users.get(0);
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 * */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public RegistUser queryRegistUser(final String userName,
			final String identifier, final Integer userType) {
		final String hql = "select * from TBL_UAM_REGIST_USER "
				+ " where USER_NAME=:userName and USER_TOKEN_ID=:identifier and USER_TYPE=:userType";
		List<RegistUser> users = (List<RegistUser>) getHibernateTemplate()
				.execute(new HibernateCallback() {
					@Override
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						return session.createSQLQuery(hql)
								.addEntity(RegistUser.class)
								.setParameter("identifier", identifier)
								.setParameter("userType", userType)
								.setParameter("userName", userName).list();
					}
				});

		if (users != null && users.size() == 1) {
			return users.get(0);
		}
		return null;
	}

    /* (non-Javadoc)
     * @see com.h3c.o2o.portal.user.dao.RegistUserDao#queryAllRegistUser()
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public List<RegistUser> queryAllRegistUser() {
        final String hql = "select * from TBL_UAM_REGIST_USER ";
        List<RegistUser> users = (List<RegistUser>) getHibernateTemplate()
            .execute(new HibernateCallback() {
                @Override
                public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {
                    return session.createSQLQuery(hql)
                        .addEntity(RegistUser.class).list();
                }
            });

        return users;
    }

}
