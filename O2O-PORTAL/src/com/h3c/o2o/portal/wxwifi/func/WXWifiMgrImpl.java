package com.h3c.o2o.portal.wxwifi.func;

import java.io.Serializable;
import java.util.Map;

import com.h3c.o2o.portal.entity.WXWifiTemplate;
import com.h3c.o2o.portal.wxwifi.dao.WXWifiDao;
import com.h3c.oasis.o2oserver.common.ListResult;
import com.h3c.oasis.o2oserver.common.QueryInfo;

/**
 * 主题子表逻辑。
 *
 * @author hkf6496
 *
 */
public class WXWifiMgrImpl implements Serializable,WXWifiMgr {

    /**
     * 序列化id
     */
    private static final long serialVersionUID = 7580616882138768082L;

    private WXWifiDao portalWXWifiDao;


    @Override
    public ListResult<WXWifiTemplate> queryWifiTemplateList(
        QueryInfo queryInfo) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public WXWifiTemplate queryWXWifiTemplate(Long id) {
        // TODO Auto-generated method stub
        return this.portalWXWifiDao.readWXWifiTemplate(id);
    }

    @Override
    public boolean isExistPublish(long wxwifiId) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public WXWifiTemplate queryWXWifiTemplateByName(Long ownerId,
        String wifiName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public WXWifiTemplate queryWXWifiTemplateByOwnerId(Long ownerId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Boolean isHaveSameWXWifiTemplateName(String wifiName, Long ownerId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void deleteCallback(String sql) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Map<String, Integer> publishFile(String operate, Long subId)
        throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void deleteWXWifiTemplate(WXWifiTemplate wxWifiTemplate)
        throws Exception {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void saveOrUpdateWXWifiTemplate(WXWifiTemplate wxWifiTemplate)
        throws Exception {
        // TODO Auto-generated method stub
        
    }

    public WXWifiDao getPortalWXWifiDao() {
        return portalWXWifiDao;
    }

    public void setPortalWXWifiDao(WXWifiDao portalWXWifiDao) {
        this.portalWXWifiDao = portalWXWifiDao;
    }
    
}
