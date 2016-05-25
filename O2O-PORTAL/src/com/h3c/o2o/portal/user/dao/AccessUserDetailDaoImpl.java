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

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.h3c.o2o.portal.entity.AccessDetail;
import com.h3c.o2o.portal.entity.RegistUser;

/**
 * 用户Dao实现类
 * 
 * @author j09980
 */
public class AccessUserDetailDaoImpl extends HibernateDaoSupport implements
    AccessUserDetailDao {

    public void updateRegistUser(RegistUser user) {
        getHibernateTemplate().merge(user);
    }

    @Override
    public void saveAccessDetail(AccessDetail detail) {
        getHibernateTemplate().save(detail);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public AccessDetail queryAccessDetail(final Long storeId, String ssid,
        final String usermac, final long accessStartTime) {
        final String sql = "select * from TBL_UAM_ACCESS_DTL "
            + " where STORE_ID=:storeId and USER_MAC=:usermac"
            + " and ACCESS_START_TIME>:accessStartTime and ACCESS_END_TIME is not null"
            + " order by ACCESS_START_TIME desc";
        List<AccessDetail> list = (List<AccessDetail>) getHibernateTemplate()
            .execute(new HibernateCallback() {
                @Override
                public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {
                    return session.createSQLQuery(sql)
                        .addEntity(AccessDetail.class)
                        .setParameter("storeId", storeId)
                        .setParameter("usermac", usermac)
                        .setParameter("accessStartTime", accessStartTime)
                        .list();
                }
            });
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Long queryAccessStartTime(final long userId) {
        final String sql = "select ACCESS_START_TIME from TBL_UAM_ONLINE_USER "
            + " where USER_ID=:userId";
        List<Object> list = (List<Object>) getHibernateTemplate().execute(
            new HibernateCallback() {
                @Override
                public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {
                    return session.createSQLQuery(sql)
                        .setParameter("userId", userId).list();
                }
            });
        if (list != null && list.size() > 0) {
            return ((BigDecimal) list.get(0)).longValue();
        }
        return null;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public List<AccessDetail> queryAccessUser(final String identifier,
        final int offset, final int limit) {
        final String sql = "select * from TBL_UAM_ACCESS_DTL a where exists "
            + "(select r.id from TBL_UAM_REGIST_USER r where a.USER_ID = r.id and r.USER_TOKEN_ID =:identifier)";

        return (List<AccessDetail>) getHibernateTemplate().execute(
            new HibernateCallback() {
                @Override
                public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {
                    return session.createSQLQuery(sql)
                        .addEntity(AccessDetail.class)
                        .setString("identifier", identifier)
                        .setFirstResult(offset).setMaxResults(limit).list();
                }
            });
    }

}
