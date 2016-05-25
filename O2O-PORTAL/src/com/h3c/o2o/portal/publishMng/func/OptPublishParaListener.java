/*
 * Copyright (c) 2007-2015, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC V700R001
 * Module Name :
 * Date Created: 2015年10月20日
 * Creator     : dkf5133
 * Description :
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 * 2015年10月20日  dkf5133             O2O-PORTAL-UI project, new code file.
 *
 *------------------------------------------------------------------------------
 */

package com.h3c.o2o.portal.publishMng.func;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.h3c.o2o.auth.rabbitmq.MqRequest;
import com.h3c.o2o.auth.rabbitmq.MqResponse;
import com.h3c.o2o.portal.cache.api.CacheApiMgr;
import com.h3c.o2o.portal.entity.PublishMngPara;

public class OptPublishParaListener {

	/** 操作类型：增加 */
    private final int OPER_TYPE_ADD = 1;

    /** 操作类型：修改 */
    private final int OPER_TYPE_MODIFY = 2;

    /** 操作类型：删除（支持批量删除） */
    private final int OPER_TYPE_DELETE = 3;

    private Log log = LogFactory.getLog(getClass());

    private PublishParaMgr publishParaMgr;

    /** 数据缓存接口 */
    private CacheApiMgr cacheApiMgr;

    /**
     * 根据操作类型分发请求，执行页面生成，向请求该sevlet的一方打印执行结果。
     */
    public MqResponse handleMessage(MqRequest body) {
        log.info("call from foreground, ids: " + body.getIds() + ", type: " + body.getType());

        MqResponse result = new MqResponse();

        try {
            // 将资料解码
            // 解析参数(1:add,2:modify,3:delete(支持批量删除))
            Integer type = body.getType();
            // 店铺ID
            List<Long> ids = body.getIds();
            switch (type) {
                case OPER_TYPE_ADD:
                case OPER_TYPE_MODIFY:
                    // 查询数据
                	List<PublishMngPara> paras = publishParaMgr
						.findPubParamByStoreId((long) ids.get(0),
						PublishMngPara.getParaNames());
					if (paras != null && paras.size() > 0) {
						// 更新缓存
						cacheApiMgr.getPublishParaCacheMgr().put((long) ids.get(0),
							new ArrayList<PublishMngPara>(paras));
					}
                    break;
                case OPER_TYPE_DELETE:
                    for (Long id : ids) {
                        cacheApiMgr.getPublishParaCacheMgr().remove(id);
                    }
                    break;
                default:
                    result.setSuccess(false);
                    break;
            }
            if (log.isTraceEnabled()) {
                for (Long key : cacheApiMgr
                    .getPublishParaCacheMgr().getKeys()) {
                    log.trace("key: "
                        + key.toString()
                        + " ~ value: "
                        + cacheApiMgr.getPublishParaCacheMgr().get(key)
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

	public void setPublishParaMgr(PublishParaMgr publishParaMgr) {
		this.publishParaMgr = publishParaMgr;
	}

	public void setCacheApiMgr(CacheApiMgr cacheApiMgr) {
		this.cacheApiMgr = cacheApiMgr;
	}

}
