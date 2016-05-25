package com.h3c.o2o.portal.wxwifi.func;

import java.util.Map;

import com.h3c.o2o.portal.entity.WXWifiTemplate;
import com.h3c.oasis.o2oserver.common.ListResult;
import com.h3c.oasis.o2oserver.common.QueryInfo;

/**
 * �����ӱ��߼���
 *
 * @author hkf6496
 *
 */
public interface WXWifiMgr {

    /**
     * ͨ����ѯ�����б�
     *
     */
    public ListResult<WXWifiTemplate> queryWifiTemplateList(QueryInfo queryInfo);

    /**
     * ͨ��id��ѯ������ϸ��Ϣ
     *
     * @param id ΢��wifiģ��id
     * @return ΢��wifiģ����ϸ��Ϣʵ��
     */
    public WXWifiTemplate queryWXWifiTemplate(Long id);
    
    /**
     * У��΢��wifiģ���Ƿ񱻷���ģ��ʹ�ã����ʹ�ã�����ɾ����
     *
     * @return
     */
    public boolean isExistPublish(long wxwifiId);

    /**
     * ͨ��name��ѯ����
     * @param name
     * @return
     */
    public WXWifiTemplate queryWXWifiTemplateByName(Long ownerId, String wifiName);

    /**
     * ͨ������id��ѯ����΢��wifiģ��
     *
     * @param id ΢��wifiģ��id
     * @return ΢��wifiģ��ʵ��
     */
    public WXWifiTemplate queryWXWifiTemplateByOwnerId(Long ownerId);

    /**
     * �ж�ͬ�������Ƿ�����ͬ��ģ��
     *
     */
    public Boolean isHaveSameWXWifiTemplateName(String wifiName, Long ownerId);

    /**
     * ����sql���ɾ��
     *
     * @param sql ԭ��sql
     */
    public void deleteCallback(final String sql);
    
    //public List<WifiShopInfo> queryWifiShopByTemplateID(Long id);     
    

    /**
     * �����ļ�
     *
     * @param operate �������ͣ�draw/delete
     * @param subId �ӱ�id
     * @return ���ں����������ǣ��ಿ�������
     * @throws Exception
     */
    public Map<String, Integer> publishFile(String operate, Long subId) throws Exception;

    /**
     * ɾ��ָ��΢��wifiģ��
     */
    public void deleteWXWifiTemplate(WXWifiTemplate wxWifiTemplate) throws Exception;

    /**
     * ��������΢��wifiģ��
     *
     * @param theme
     */
    public void saveOrUpdateWXWifiTemplate(WXWifiTemplate wxWifiTemplate)throws Exception;

}
