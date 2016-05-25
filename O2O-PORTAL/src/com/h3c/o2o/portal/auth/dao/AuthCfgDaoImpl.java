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
package com.h3c.o2o.portal.auth.dao;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.h3c.o2o.portal.entity.AuthCfg;

/**
 * add description of types here
 *
 * @author j09980
 */
public class AuthCfgDaoImpl extends HibernateDaoSupport implements AuthCfgDao {

    @Override
    public AuthCfg findAuthConfig(final Long id) {
        final String hql = "select * from TBL_UAM_AUTH_CFG where ID=:id";
        List<AuthCfg> configs = getHibernateTemplate().execute(
            new HibernateCallback<List<AuthCfg>>() {
                @SuppressWarnings("unchecked")
                @Override
                public List<AuthCfg> doInHibernate(Session session)
                    throws HibernateException, SQLException {
                    return session.createSQLQuery(hql).addEntity(AuthCfg.class)
                        .setParameter("id", id).list();
                }
            });

        if (configs != null && configs.size() > 0) {
            return configs.get(0);
        }
        return null;
    }

}
