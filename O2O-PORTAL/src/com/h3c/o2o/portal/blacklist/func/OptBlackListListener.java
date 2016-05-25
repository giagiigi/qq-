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
 * �������ڴ��Ż���Ϣ��
 *
 * @author dkf5133
 *
 */
public class OptBlackListListener {

    /** ���ݻ���ӿ� */
    private CacheApiMgr cacheApiMgr;

    /** �������ӿ� */
    private BlackListMgr blackListMgr;

    /** �������ͣ����� */
    private final int OPER_TYPE_ADD = 1;

    /** �������ͣ�ɾ����֧������ɾ���� */
    private final int OPER_TYPE_DELETE = 3;

    private Log log = LogFactory.getLog(getClass());

    /**
     * ���ݲ������ͷַ�����ִ��ҳ�����ɣ��������sevlet��һ����ӡִ�н����
     *
     * @throws IOException IO�쳣
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
                    for (Long id : ids) {
                        log.info("Add blacklist, current id: " + id);
                        // ��ѯ����
                        BlackList blackList = blackListMgr.findBlackList(id);
                        log.info("blacklist: " + blackList);
                        if (blackList != null) {
                            // ���»���
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
