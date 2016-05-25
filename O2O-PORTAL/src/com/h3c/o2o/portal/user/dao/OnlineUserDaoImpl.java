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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.h3c.o2o.portal.entity.OnlineUser;
import com.h3c.o2o.portal.protocol.entity.User;

/**
 * 在线用户Dao实现类
 *
 * @author j09980
 */
public class OnlineUserDaoImpl extends HibernateDaoSupport implements
    OnlineUserDao {

	/**
	 * Log
	 */
	private Log log = LogFactory.getLog(getClass());
	
    @Override
    public void saveOnlineUser(OnlineUser user) {
        getHibernateTemplate().save(user);
    }

    @Override
    public void deleteOnlineUser(OnlineUser user) {
        getHibernateTemplate().delete(user);
    }

    @Override
    public void updateOnlineUser(OnlineUser user) {
        getHibernateTemplate().update(user);
    }
    
	@Override
	public void updateOnlineInfo(final User user) {
		final String sql = "update TBL_UAM_ONLINE_USER set ACCESS_DURATION=:sessionTime, INPUT_BYTES=:inputbytes, OUTPUT_BYTES=:outputbytes where USER_MAC=:mac";
		getHibernateTemplate().execute(new HibernateCallback<Object>() {
			@Override
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				session.createSQLQuery(sql)
						.setParameter("sessionTime", user.getSessionTime())
						.setParameter("inputbytes", user.getInputbytes())
						.setParameter("outputbytes", user.getOutputbytes())
						.setParameter("mac", user.getMac()).executeUpdate();
				return null;
			}
		});

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<OnlineUser> queryAllOnlineUser() {
		final String hql = "select * from TBL_UAM_ONLINE_USER order by ACCESS_START_TIME";
		List<OnlineUser> users = (List<OnlineUser>) getHibernateTemplate()
				.execute(new HibernateCallback() {
					@Override
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						return session.createSQLQuery(hql)
								.addEntity(OnlineUser.class).list();
					}
				});
		return users;
	}

    /**
     * {@inheritDoc}
     * **/
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public List<OnlineUser> queryOnlineUser(final String identifier,
        final int offset, final int limit) {
        final String sql = "select * from TBL_UAM_ONLINE_USER o where exists "
            + "(select r.id from TBL_UAM_REGIST_USER r where o.USER_ID = r.id and r.USER_TOKEN_ID =:identifier)";

        return (List<OnlineUser>) getHibernateTemplate().execute(
            new HibernateCallback() {
                @Override
                public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {
                    return session.createSQLQuery(sql)
                        .addEntity(OnlineUser.class)
                        .setString("identifier", identifier)
                        .setFirstResult(offset).setMaxResults(limit).list();
                }
            });
    }

    @SuppressWarnings("unchecked")
    @Override
    public OnlineUser queryOnlineUser(String mac) {
        List<OnlineUser> list = getHibernateTemplate().find(
            "from OnlineUser o where o.userMac=?", mac);
        return list != null && list.size() > 0 ? list.get(0) : null;
    }
    
    @SuppressWarnings("unchecked")
	@Override
    public List<OnlineUser> queryOnlineUser(Long ip, Long storeId) {
		List<OnlineUser> list = null;
        if(null == storeId){
        	list = getHibernateTemplate().find(
                    "from OnlineUser o where o.userIp=?", ip);
        } else {
        	list = getHibernateTemplate().find(
                    "from OnlineUser o where o.userIp=? and o.storeId=? ", ip, storeId);
        }
        return list;
    }

	@Override
	public void deleteOnlineUserByMac(String mac) {
		Integer deleteNum = getHibernateTemplate().execute(new HibernateCallback<Integer>() {

			@Override
			public Integer doInHibernate(Session session) throws HibernateException, SQLException {
				String hql = "delete from OnlineUser onlineUser where onlineUser.userMac = :userMac ";
				Query query = session.createQuery(hql);
				query.setParameter("userMac", mac);
				return query.executeUpdate();
			}
		});
		if (log.isDebugEnabled()) {
			log.debug(deleteNum + " OnlineUser was delete, mac: " + mac);
		}
	}
	
	@Override
	public void deleteOnlineUserByStoreIdAndIp(Long storeId, Long ip) {
		Integer deleteNum = 0;
		if (null != storeId && storeId.longValue() >= 0 && null != ip && ip.longValue() != 0) {
			deleteNum = getHibernateTemplate().execute(new HibernateCallback<Integer>() {

				@Override
				public Integer doInHibernate(Session session) throws HibernateException, SQLException {
					String hql = "delete from OnlineUser onlineUser where onlineUser.storeId = :storeId and onlineUser.userIp= :userIp";
					Query query = session.createQuery(hql);
					query.setParameter("storeId", storeId);
					query.setParameter("userIp", ip);
					return query.executeUpdate();
				}
			});
		}
		if (log.isDebugEnabled()) {
			log.debug(deleteNum + " OnlineUser was delete, storeId: " + storeId + " ip:" + ip);
		}
	}
}
