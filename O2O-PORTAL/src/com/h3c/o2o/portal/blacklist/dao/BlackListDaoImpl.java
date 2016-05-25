/*
 * Copyright (c) 2006, Hangzhou Huawei-3Com Technology Co., Ltd.
 * All rights reserved.
 * <http://www.huawei-3com.com/>
 */
/**
 *
 */
package com.h3c.o2o.portal.blacklist.dao;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.h3c.o2o.portal.entity.BlackList;

/**
 * 黑名单数据访问实现。
 *
 * @author dkf5133
 *
 */
public class BlackListDaoImpl extends HibernateDaoSupport implements
    BlackListDao {

    @Override
    public BlackList queryBlackList(final Long id) {
        final String sql = "select * from TBL_UAM_BLACK_LIST where ID=:id";
        List<BlackList> list = getHibernateTemplate().execute(
            new HibernateCallback<List<BlackList>>() {
                @SuppressWarnings("unchecked")
                @Override
                public List<BlackList> doInHibernate(Session session)
                    throws HibernateException, SQLException {
                    return session.createSQLQuery(sql)
                        .addEntity(BlackList.class).setParameter("id", id)
                        .list();
                }
            });

        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /* (non-Javadoc)
     * @see com.h3c.o2o.portal.blacklist.dao.BlackListDao#queryBlackListByUser(java.lang.Long)
     */
    @Override
    public BlackList queryBlackListByUser(final Long userId) {
        final String sql = "select * from TBL_UAM_BLACK_LIST where USER_ID=:userId";
        List<BlackList> list = getHibernateTemplate().execute(
            new HibernateCallback<List<BlackList>>() {
                @SuppressWarnings("unchecked")
                @Override
                public List<BlackList> doInHibernate(Session session)
                    throws HibernateException, SQLException {
                    return session.createSQLQuery(sql)
                        .addEntity(BlackList.class).setParameter("userId", userId)
                        .list();
                }
            });

        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /* (non-Javadoc)
     * @see com.h3c.o2o.portal.blacklist.dao.BlackListDao#queryAllBlackList()
     */
    @Override
    public List<BlackList> queryAllBlackList() {
        final String sql = "select * from TBL_UAM_BLACK_LIST";
        List<BlackList> list = getHibernateTemplate().execute(
            new HibernateCallback<List<BlackList>>() {
                @SuppressWarnings("unchecked")
                @Override
                public List<BlackList> doInHibernate(Session session)
                    throws HibernateException, SQLException {
                    return session.createSQLQuery(sql)
                        .addEntity(BlackList.class).list();
                }
            });

        return list;
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public void save(BlackList entity) {
        getHibernateTemplate().save(entity);
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public void delete(BlackList entity) {
        getHibernateTemplate().delete(entity);
    }

}
