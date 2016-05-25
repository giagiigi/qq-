package com.h3c.o2o.portal.theme.func;

import java.util.List;

import com.h3c.o2o.portal.entity.ThemePage;
import com.h3c.o2o.portal.entity.ThemeTemplate;

/**
 * �����ӱ��߼���
 *
 * @author ykf2685
 *
 */
public interface ThemePageMgr {

    /**
     * ͨ��id��ѯ������ϸ��Ϣ
     *
     * @param id ����id
     * @return ������ϸ��Ϣʵ��
     */
    ThemePage queryById(Long id);

    /**
     * ��ѯģ��
     * @param templateId ģ��ID
     * @return
     */
    public ThemeTemplate findTemplateById(long templateId);

    /**
     * ��ѯģ���������棬����ID��storeId��isEnable��ssid
     * @param templateId ģ��ID
     * @return
     */
    public ThemeTemplate findTemplateForCache(long templateId);

    /**
     * ���ģ��ҳ����Ϣ
     *
     * @param template ģ��
     * @param pageType ҳ������
     * @return ҳ��
     */
    ThemePage getThemePage(ThemeTemplate template, int pageType);

    /**
     * ���ģ��ҳ����Ϣ
     *
     * @param template ģ��
     * @param pageType ҳ������
     * @return ģ���µ�����ҳ�漯�ϡ����԰�����id,pageType,pathName,fileName,pageCfg
     */
    List<ThemePage> findThemePagesForCache(ThemeTemplate temp);

}
