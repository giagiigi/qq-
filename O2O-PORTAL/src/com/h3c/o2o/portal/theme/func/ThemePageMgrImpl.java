package com.h3c.o2o.portal.theme.func;

import java.util.List;

import com.h3c.o2o.portal.entity.ThemePage;
import com.h3c.o2o.portal.entity.ThemeTemplate;
import com.h3c.o2o.portal.theme.dao.ThemePageDao;

/**
 * 主题子表逻辑。
 *
 * @author ykf2685
 *
 */
public class ThemePageMgrImpl implements ThemePageMgr {

    /** 数据访问层 */
    private ThemePageDao themePageDao;

    /**
     * 通过id查询主题详细信息
     *
     * @param id 主题id
     * @return 主题详细信息实体
     */
    public ThemePage queryById(Long id) {
        return themePageDao.readById(id);
    }

    @Override
    public ThemeTemplate findTemplateById(long templateId) {
        return themePageDao.findTemplateById(templateId);
    }

    /* (non-Javadoc)
     * @see com.h3c.o2o.portal.theme.func.ThemePageMgr#findTemplateForCache(long)
     */
    @Override
    public ThemeTemplate findTemplateForCache(long templateId) {
        return themePageDao.queryTemplateForCache(templateId);
    }

    /* (non-Javadoc)
     * @see com.h3c.o2o.portal.theme.func.ThemePageMgr#getThemePage(com.h3c.o2o.portal.entity.ThemeTemplate, int)
     */
    @Override
    public ThemePage getThemePage(ThemeTemplate template, int pageType) {
        ThemePage page = null;
        for (ThemePage item : template.getPages()) {
            if (item.getPageType() == pageType) {
                page = item;
                break;
            }
        }
        return page;
    }

    /* (non-Javadoc)
     * @see com.h3c.o2o.portal.theme.func.ThemePageMgr#getThemePages(com.h3c.o2o.portal.entity.ThemeTemplate)
     */
    @Override
    public List<ThemePage> findThemePagesForCache(ThemeTemplate temp) {
        return themePageDao.queryThemePagesForCache(temp);
    }

    public void setThemePageDao(ThemePageDao themePageDao) {
        this.themePageDao = themePageDao;
    }

}
