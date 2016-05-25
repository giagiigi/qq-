package com.h3c.o2o.portal.wxwifi.dao;

import java.io.Serializable;
import java.util.List;

import com.h3c.o2o.portal.entity.WXWifiTemplate;
import com.h3c.oasis.o2oserver.common.CommonDaoSupport;
import com.h3c.oasis.o2oserver.common.ListResult;
import com.h3c.oasis.o2oserver.common.QueryInfo;

/**
 * 主题子表逻辑。
 *
 * @author ykf2685
 *
 */
public class WXWifiDaoImpl extends CommonDaoSupport implements Serializable, WXWifiDao {

    /**
     * add description of field here
     */
    private static final long serialVersionUID = 349140100702123981L;

    @Override
    public WXWifiTemplate readWXWifiTemplate(Long id) {
        return getHibernateTemplate().get(WXWifiTemplate.class, id);
    }

    @Override
    public boolean isExistPublish(long wifiId) {
        // TODO Auto-generated method stub
        return false;
    }
    @SuppressWarnings("unchecked")
    @Override
    public WXWifiTemplate readWXWifiTemplate(Long ownerId, String wifiName) {
        List<WXWifiTemplate> result = getHibernateTemplate().find(
            "from WXWifiTemplate where ownerId=? and templateName=?", ownerId,
            wifiName);
    return result != null && result.size() > 0 ? result.get(0) : null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<WXWifiTemplate> readWXWifiTemplateByOwnerId(Long ownerId) {
        List<WXWifiTemplate> list = getHibernateTemplate().find(
            "from WXWifiTemplate u where u.ownerId=?",ownerId);
        return null == list ? null : list;
    }

    @Override
    public void saveOrUpdateWXWifiTemplate(WXWifiTemplate wXWifiTemplate) {
        getHibernateTemplate().saveOrUpdate(wXWifiTemplate);
        
    }

    @SuppressWarnings("unchecked")
    @Override
    public ListResult<WXWifiTemplate> readByqueryInfo(QueryInfo queryInfo) {
        return (ListResult<WXWifiTemplate>) queryList(queryInfo);
    }

    @Override
    public void deleteWXWifiTemplate(WXWifiTemplate wXWifiTemplate) {
        getHibernateTemplate().delete(wXWifiTemplate);
        
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<WXWifiTemplate> isHaveSameWXWifiTemplate(String wifiName,
        Long ownerId) {
        List<WXWifiTemplate> list = getHibernateTemplate().find(
            "from WXWifiTemplate u where  u.templateName=? and u.ownerId=?",wifiName,ownerId);
        return null == list ? null : list;
    }

    @Override
    public void deleteCallback(String sql) {
        // TODO Auto-generated method stub
        
    }
    /*  @SuppressWarnings("unchecked")
    @Override
   public List<WifiShopInfo> readWifiShopByTemplateID(Long id) {
        
        List<WifiShopInfo> list = getHibernateTemplate().find(
            "from WifiShopInfo u where u.template_id=?",id.intValue());
        return list;
    }*/
    
}
