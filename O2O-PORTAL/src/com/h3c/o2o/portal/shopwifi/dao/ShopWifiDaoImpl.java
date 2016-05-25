/*
 * Copyright (c) 2016, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC V300R002
 * Module Name : 
 * Date Created: 2016年1月15日
 * Creator     : donglicong
 * Description : 
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 * 2016年1月15日  donglicong        O2O-PORTAL project, new code file.
 * 
 *------------------------------------------------------------------------------
 */
package com.h3c.o2o.portal.shopwifi.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.h3c.o2o.portal.entity.ShopWifiInfo;
import com.h3c.oasis.o2oserver.common.CommonDaoSupport;

/**
 * add description of types here
 *
 * @author donglicong
 */
public class ShopWifiDaoImpl extends CommonDaoSupport implements ShopWifiDao {

	/**
	 * Log
	 */
	private Log log = LogFactory.getLog(getClass());
	
    /* (non-Javadoc)
     * @see com.h3c.o2o.portal.shopwifi.dao.ShopWifiDao#queryShopWifiInfo(long)
     */
    @Override
    public ShopWifiInfo queryShopWifiInfo(long nasId) {
        final String sql = "select * from TBL_UAM_WIFI_SHOP_INFO where SID in (select SID from TBL_UAM_WIFI_SHOP where NASID=:nasId)";
        return getHibernateTemplate()
            .execute(new HibernateCallback<ShopWifiInfo>() {

                @Override
                public ShopWifiInfo doInHibernate(Session session)
                    throws HibernateException, SQLException {
                	SQLQuery query = session.createSQLQuery(sql).addEntity(ShopWifiInfo.class);
                	query.setParameter("nasId", nasId);
                	@SuppressWarnings("unchecked")
					List<ShopWifiInfo> shopInfoList = query.list();
                	if(null != shopInfoList && shopInfoList.size() > 1){
                		log.warn("Multiple WifiShopInfo found by nasId: " + nasId);
                	}
                    return (null != shopInfoList && shopInfoList.size() > 0) ? shopInfoList.get(0) : null;
                }
            });
    }

}
