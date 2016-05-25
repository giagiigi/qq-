/*
 * Copyright (c) 2007-2015, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC V700R001
 * Module Name :
 * Date Created: 2015��10��20��
 * Creator     : dkf5133
 * Description :
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 * 2015��10��20��  dkf5133             O2O-PORTAL-UI project, new code file.
 *
 *------------------------------------------------------------------------------
 */

package com.h3c.o2o.portal.publishMng.func;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.h3c.o2o.auth.rabbitmq.MqRequest;
import com.h3c.o2o.auth.rabbitmq.MqResponse;
import com.h3c.o2o.portal.cache.api.CacheApiMgr;
import com.h3c.o2o.portal.cache.key.PublishMngCacheKey;
import com.h3c.o2o.portal.entity.PublishMng;

public class OptPublishMngListener {

	/** �������ͣ����� */
    private final int OPER_TYPE_ADD = 1;

    /** �������ͣ��޸� */
    private final int OPER_TYPE_MODIFY = 2;

    /** �������ͣ�ɾ����֧������ɾ���� */
    private final int OPER_TYPE_DELETE = 3;

    private Log log = LogFactory.getLog(getClass());

    private PublishMngMgr publishMngMgr;

    /** ���ݻ���ӿ� */
    private CacheApiMgr cacheApiMgr;

    /**
     * ���ݲ������ͷַ�����ִ��ҳ�����ɣ��������sevlet��һ����ӡִ�н����
     */
    public MqResponse handleMessage(MqRequest body) {
        log.info("call from foreground, ids: " + body.getIds());

        MqResponse result = new MqResponse();

        try {
            // �����Ͻ���
            // ��������(1:add,2:modify,3:delete(֧������ɾ��))
            Integer type = body.getType();
            List<Long> ids = body.getIds();
            switch (type) {
                case OPER_TYPE_ADD:
                case OPER_TYPE_MODIFY:
                    // ��ѯ����
                	PublishMng publish = publishMngMgr.findPublishMng(ids.get(0));
					if (publish != null) {
						PublishMngCacheKey key = new PublishMngCacheKey(
								publish.getStoreId(), publish.getSsidName());
						// ���»���
						cacheApiMgr.getPublishMngCacheMgr().put(key, publish);
					}
                    break;
                case OPER_TYPE_DELETE:
                    for (Long id : ids) {
                        cacheApiMgr.getPublishMngCacheMgr().removeByRelation(id);
                    }
                    break;
                default:
                    result.setSuccess(false);
                    break;
            }
            if (log.isTraceEnabled()) {
                for (PublishMngCacheKey key : cacheApiMgr
                    .getPublishMngCacheMgr().getKeys()) {
                    log.trace("key: "
                        + key.toString()
                        + " ~ value: "
                        + cacheApiMgr.getPublishMngCacheMgr().get(key)
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

	public void setPublishMngMgr(PublishMngMgr publishMngMgr) {
		this.publishMngMgr = publishMngMgr;
	}

	public void setCacheApiMgr(CacheApiMgr cacheApiMgr) {
		this.cacheApiMgr = cacheApiMgr;
	}

}