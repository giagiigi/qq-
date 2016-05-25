package com.h3c.o2o.portal.haihaiconfig.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.h3c.o2o.portal.entity.HaihaiPlatform;
import com.h3c.oasis.o2oserver.common.CommonDaoSupport;

public class HaihaiAppDaoImpl extends CommonDaoSupport implements HaihaiAppDao {

    private Log log = LogFactory.getLog(getClass());

    /**
     * 
     * 根据微信id查询app信息
     *
     * @param appId
     * @return
     */
    @Override
    public List<HaihaiPlatform> queryAppConfigByAppWeixinId(Long storeId) {
        final String sql = "select *  from tbl_uam_haihai_platform where weixin_id in"
            + "(select ACCOUNT_ID from TBL_UAM_WIFI_SHOP where NASID=:storeId)";

        List<HaihaiPlatform> haihaiAppList = (List<HaihaiPlatform>) getHibernateTemplate()
            .execute(new HibernateCallback<List<HaihaiPlatform>>() {
                @SuppressWarnings("unchecked")
                @Override
                public List<HaihaiPlatform> doInHibernate(Session session)
                    throws HibernateException, SQLException {
                    return (List<HaihaiPlatform>) session.createSQLQuery(sql)
                        .addEntity(HaihaiPlatform.class)
                        .setParameter("storeId", storeId).list();
                }
            });

        if (null != haihaiAppList && haihaiAppList.size() > 1) {
            log.warn("query app config by nasId: " + storeId);
        }
        return haihaiAppList;
    }

}
