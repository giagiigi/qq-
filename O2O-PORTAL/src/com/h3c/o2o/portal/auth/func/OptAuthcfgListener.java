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

package com.h3c.o2o.portal.auth.func;

import java.io.IOException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.h3c.o2o.auth.rabbitmq.MqRequest;
import com.h3c.o2o.auth.rabbitmq.MqResponse;
import com.h3c.o2o.portal.cache.api.CacheApiMgr;
import com.h3c.o2o.portal.entity.AuthCfg;

/**
 * 认证配置内存优化消息。
 *
 * @author ykf2685
 *
 */
public class OptAuthcfgListener {

    /** 数据缓存接口 */
    private CacheApiMgr cacheApiMgr;

    /** 认证配置接口 */
    private AuthCfgMgr authCfgMgr;

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
                    AuthCfg config = authCfgMgr.findAuthConfig(ids.get(0));
                    if (config != null) {
						if (cacheApiMgr.getAuthCfgCacheMgr().getFromCache(
								ids.get(0)) != null) {
							// 更新缓存
	                        cacheApiMgr.getAuthCfgCacheMgr().put(
	                            config.getId(), config);
						} else {
							// 如果缓存中没有，说明该认证配置没有被使用，不需要更新缓存
						}
                    }
                    break;
                case OPER_TYPE_DELETE:
					for (Long id : ids) {
						cacheApiMgr.getAuthCfgCacheMgr().remove(id);
					}
                    break;
                default:
                    result.setSuccess(false);
                    break;
            }
            if (log.isTraceEnabled()) {
                for (Long key : cacheApiMgr
                    .getAuthCfgCacheMgr().getKeys()) {
                    log.trace("key: "
                        + key.toString()
                        + " ~ value: "
                        + cacheApiMgr.getAuthCfgCacheMgr().get(key)
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

    public void setAuthCfgMgr(AuthCfgMgr authCfgMgr) {
        this.authCfgMgr = authCfgMgr;
    }

}
