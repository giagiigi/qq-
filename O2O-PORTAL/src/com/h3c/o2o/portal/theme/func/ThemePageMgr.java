package com.h3c.o2o.portal.theme.func;

import java.util.List;

import com.h3c.o2o.portal.entity.ThemePage;
import com.h3c.o2o.portal.entity.ThemeTemplate;

/**
 * 主题子表逻辑。
 *
 * @author ykf2685
 *
 */
public interface ThemePageMgr {

    /**
     * 通过id查询主题详细信息
     *
     * @param id 主题id
     * @return 主题详细信息实体
     */
    ThemePage queryById(Long id);

    /**
     * 查询模板
     * @param templateId 模板ID
     * @return
     */
    public ThemeTemplate findTemplateById(long templateId);

    /**
     * 查询模板用作缓存，包括ID、storeId、isEnable、ssid
     * @param templateId 模板ID
     * @return
     */
    public ThemeTemplate findTemplateForCache(long templateId);

    /**
     * 获得模板页面信息
     *
     * @param template 模板
     * @param pageType 页面类型
     * @return 页面
     */
    ThemePage getThemePage(ThemeTemplate template, int pageType);

    /**
     * 获得模板页面信息
     *
     * @param template 模板
     * @param pageType 页面类型
     * @return 模板下的所有页面集合。属性包括：id,pageType,pathName,fileName,pageCfg
     */
    List<ThemePage> findThemePagesForCache(ThemeTemplate temp);

}
