/*
 * Copyright (c) 2016, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC V300R002
 * Module Name : 
 * Date Created: 2016年2月2日
 * Creator     : donglicong
 * Description : 
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 * 2016年2月2日  donglicong        O2O-PORTAL project, new code file.
 * 
 *------------------------------------------------------------------------------
 */
package com.h3c.o2o.portal.protocol.dao;

import java.math.BigInteger;
import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.h3c.oasis.o2oserver.common.CommonDaoSupport;

/**
 * add description of types here
 *
 * @author donglicong
 */
public class ProtocolDaoImpl extends CommonDaoSupport implements ProtocolDao {

    /* (non-Javadoc)
     * @see com.h3c.o2o.portal.protocol.dao.ProtocolDao#queryUserId(long)
     */
    @Override
    public Long queryUserId(final long shopId) {
        final String sql = "select USER_ID from TBL_WSM_USER_SHOP where SHOP_ID=:shopId";
        return getHibernateTemplate().execute(new HibernateCallback<Long>() {

            @Override
            public Long doInHibernate(Session session)
                throws HibernateException, SQLException {
                BigInteger userId = (BigInteger) session.createSQLQuery(sql)
                    .setParameter("shopId", shopId).uniqueResult();
                if (userId == null) {
                    return null;
                }
                return userId.longValue();
            }
        });
    }

}
