/*
 * Copyright (c) 2007-2015, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC V700R001
 * Module Name :
 * Date Created: 2015-8-12
 * Creator     : dkf5133
 * Description :
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 * 2015-8-12  dkf5133             O2O-PORTAL project, new code file.
 *
 *------------------------------------------------------------------------------
 */

package com.h3c.o2o.portal.blacklist.func;

import java.io.IOException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.h3c.o2o.auth.rabbitmq.MqRequest;
import com.h3c.o2o.auth.rabbitmq.MqResponse;
import com.h3c.o2o.portal.cache.api.CacheApiMgr;
import com.h3c.o2o.portal.entity.BlackList;

/**
 * 黑名单内存优化消息。
 *
 * @author dkf5133
 *
 */
public class OptBlackListListener {

    /** 数据缓存接口 */
    private CacheApiMgr cacheApiMgr;

    /** 黑名单接口 */
    private BlackListMgr blackListMgr;

    /** 操作类型：增加 */
    private final int OPER_TYPE_ADD = 1;

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
                    for (Long id : ids) {
                        log.info("Add blacklist, current id: " + id);
                        // 查询数据
                        BlackList blackList = blackListMgr.findBlackList(id);
                        log.info("blacklist: " + blackList);
                        if (blackList != null) {
                            // 更新缓存
                            cacheApiMgr.getBlackListCacheMgr().put(
                                blackList.getUserId(), blackList);
                        }
                    }
                    break;
                case OPER_TYPE_DELETE:
                    for (Long id : ids) {
                        cacheApiMgr.getBlackListCacheMgr().removeByRelation(id);
                    }
                    break;
                default:
                    result.setSuccess(false);
                    break;
            }
            if (log.isTraceEnabled()) {
                for (Long key : cacheApiMgr
                    .getBlackListCacheMgr().getKeys()) {
                    log.trace("key: "
                        + key.toString()
                        + " ~ value: "
                        + cacheApiMgr.getBlackListCacheMgr().get(key)
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

    public void setBlackListMgr(BlackListMgr blackListMgr) {
        this.blackListMgr = blackListMgr;
    }

}
