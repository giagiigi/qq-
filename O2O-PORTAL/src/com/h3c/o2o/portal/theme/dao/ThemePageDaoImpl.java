package com.h3c.o2o.portal.theme.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.h3c.o2o.portal.entity.ThemePage;
import com.h3c.o2o.portal.entity.ThemeTemplate;

/**
 * 主题子表逻辑。
 *
 * @author ykf2685
 *
 */
public class ThemePageDaoImpl extends HibernateDaoSupport implements
    ThemePageDao {

    /**
     * 通过id查询主题详细信息
     *
     * @param id 主题id
     * @return 主题详细信息实体
     */
    public ThemePage readById(Long id) {
        return getHibernateTemplate().get(ThemePage.class, id);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public ThemeTemplate findTemplateById(final long templateId) {
        final String hql = "select * from TBL_UAM_THEME_TEMPLATE where ID=:templateId";
        List<ThemeTemplate> templates = (List<ThemeTemplate>) getHibernateTemplate()
            .execute(new HibernateCallback() {
                @Override
                public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {
                    return session.createSQLQuery(hql)
                        .addEntity(ThemeTemplate.class)
                        .setParameter("templateId", templateId).list();
                }
            });

        if (templates != null && templates.size() > 0) {
            return templates.get(0);
        }
        return null;
    }

    /* (non-Javadoc)
     * @see com.h3c.o2o.portal.theme.dao.ThemePageDao#queryTemplateForCache(long)
     */
    @SuppressWarnings({ "unchecked" })
    @Override
    public ThemeTemplate queryTemplateForCache(final long templateId) {
        final String hql = "select id from ThemeTemplate where id=?";
        List<Object> templates = getHibernateTemplate().find(hql, templateId);
        if (templates != null && templates.size() > 0) {
            ThemeTemplate temp = new ThemeTemplate();
            temp.setId((Long) templates.get(0));
            return temp;
        }
        return null;
    }

    /* (non-Javadoc)
     * @see com.h3c.o2o.portal.theme.dao.ThemePageDao#queryThemePages(com.h3c.o2o.portal.entity.ThemeTemplate)
     */
    @SuppressWarnings({ "unchecked" })
    @Override
    public List<ThemePage> queryThemePagesForCache(ThemeTemplate temp) {
        final String hql = "select id,pageType,pathName,fileName,pageCfg"
        		+ " from ThemePage where template.id=" + temp.getId();
        List<Object> list = getHibernateTemplate().find(hql);

        List<ThemePage> result = new ArrayList<ThemePage>();
        for(int i = 0;i < list.size(); i++){
            Object[] arr = (Object[]) list.get(i);
            ThemePage page = new ThemePage();
            page.setId((Long) arr[0]);
            page.setPageType((Integer) arr[1]);
            page.setPathName((String) arr[2]);
            page.setFileName((String) arr[3]);
            page.setPageCfg((String) arr[4]);
            page.setTemplate(temp);
            result.add(page);
        }

        return result;
    }

}
