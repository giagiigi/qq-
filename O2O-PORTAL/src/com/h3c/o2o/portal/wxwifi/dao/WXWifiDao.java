package com.h3c.o2o.portal.wxwifi.dao;

import java.util.List;

import com.h3c.o2o.portal.entity.WXWifiTemplate;
import com.h3c.oasis.o2oserver.common.ListResult;
import com.h3c.oasis.o2oserver.common.QueryInfo;

/**
 * ΢��wifiģ�塣
 *
 * @author hkf6496
 *
 */
public interface WXWifiDao {

    /**
     * ͨ��id��ѯ������ϸ��Ϣ
     *
     * @param id ΢��wifiģ��id
     * @return ΢��wifiģ����ϸ��Ϣʵ��
     */
    public WXWifiTemplate readWXWifiTemplate(Long id);

    /**
     * У��΢��wifiģ���Ƿ񱻷���ģ��ʹ�ã����ʹ�ã�����ɾ����
     *
     * @return
     */
    public boolean isExistPublish(long wifiId);

    /**
     * ͨ��name��ѯ΢��wifiģ��
     * @param wifiName
     * @return
     */
    public WXWifiTemplate readWXWifiTemplate(Long ownerId, String wifiName);

    /**
     * ͨ������id��ѯ����΢��wifiģ��
     *
     * @param id ΢��wifiģ��id
     * @return ΢��wifiģ����ϸ��Ϣʵ��
     */
    public List<WXWifiTemplate> readWXWifiTemplateByOwnerId(Long ownerId);

    /**
     * ��������΢��wifiģ��
     *
     * @param theme
     */
    public void saveOrUpdateWXWifiTemplate(WXWifiTemplate wXWifiTemplate);
    /**
     * 
     * add description of methods here 
     *
     * @return
     */
    //public List<WifiShopInfo> readWifiShopByTemplateID(Long id);
    /**
     * ��ҳ��ѯ�û�������־
     * @param queryInfo ��ѯ����
     * @return ��ǰҳ��ļ�¼
     */
    public ListResult<WXWifiTemplate> readByqueryInfo(QueryInfo queryInfo);

    /**
     * ɾ��ָ��΢��wifiģ��
     */
    public void deleteWXWifiTemplate(WXWifiTemplate wXWifiTemplate);

    /**
     * �ж�ͬ�������Ƿ�����ͬ��΢��wifiģ��
     *
     */
    public List<WXWifiTemplate> isHaveSameWXWifiTemplate(String wifiName, Long ownerId);

    /**
     * ����sql���ɾ��
     *
     * @param sql ԭ��sql
     */
    public void deleteCallback(final String sql);

}
