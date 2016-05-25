/*
 * Copyright (c) 2007-2015, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC V700R001
 * Module Name :
 * Date Created: 2015-7-23
 * Creator     : ykf2685
 * Description :
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 * 2015-7-23  ykf2685             O2O-PORTAL project, new code file.
 *
 *------------------------------------------------------------------------------
 */

package com.h3c.o2o.portal.theme.func;

import java.io.IOException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.h3c.o2o.auth.rabbitmq.MqRequest;
import com.h3c.o2o.auth.rabbitmq.MqResponse;
import com.h3c.o2o.portal.cache.api.CacheApiMgr;
import com.h3c.o2o.portal.entity.ThemePage;
import com.h3c.o2o.portal.entity.ThemeTemplate;

/**
 * 主题模板内存优化消息。
 *
 * @author ykf2685
 *
 */
public class OptThemeListener {

    /** 数据缓存接口 */
    private CacheApiMgr cacheApiMgr;

    /** 主题模板接口 */
    private ThemePageMgr themePageMgr;

    /** 操作类型：增加 */
    private final int OPER_TYPE_ADD = 1;

    /** 操作类型：修改 */
    private final int OPER_TYPE_MODIFY = 2;

    /** 操作类型：删除（支持批量删除） */
    private final int OPER_TYPE_DELETE = 3;

    private Log log = LogFactory.getLog(getClass());

    /**
     * 根据操作类型分发请求，执行页面生成，向请求该sevlet的一方打印执行结果。
     *
     * @throws IOException IO异常
     */
    public MqResponse handleMessage(MqRequest body) {
        log.info("call from foreground, ids: " + body.getIds());

        MqResponse result = new MqResponse();

        try {
            // 将资料解码
            // 解析参数(1:add,2:modify,3:delete(支持批量删除))
            Integer type = body.getType();
            List<Long> ids = body.getIds();
            switch (type) {
                case OPER_TYPE_ADD:
                	result.setSuccess(false);
                    break;
                case OPER_TYPE_MODIFY:
                    // 查询数据
                    ThemeTemplate temp = themePageMgr.findTemplateForCache(ids
                        .get(0));
                    List<ThemePage> pages = null;
                    if (temp != null) {
                        pages = themePageMgr.findThemePagesForCache(temp);
                    }
                    if (pages != null && pages.size() > 0) {
                        temp.setPages(pages);
                        // 更新缓存
                        cacheApiMgr.getThemeTemplateCacheMgr().put(temp.getId(),
							temp);
                    }
                    break;
                case OPER_TYPE_DELETE:
                	// 已发布的模板不允许删除
                	result.setSuccess(false);
                    break;
                default:
                    result.setSuccess(false);
                    break;
            }
            if (log.isTraceEnabled()) {
                for (Long key : cacheApiMgr
                    .getThemeTemplateCacheMgr().getKeys()) {
                    log.trace("key: "
                        + key.toString()
                        + " ~ value: "
                        + cacheApiMgr.getThemeTemplateCacheMgr().get(key)
                            .toString());
                }
            }
            result.setSuccess(true);
        } catch (Exception e) {
            log.warn(null, e);
            result.setSuccess(false);
        }

        return result;
    }

    public void setCacheApiMgr(CacheApiMgr cacheApiMgr) {
        this.cacheApiMgr = cacheApiMgr;
    }

    public void setThemePageMgr(ThemePageMgr themePageMgr) {
        this.themePageMgr = themePageMgr;
    }

}
