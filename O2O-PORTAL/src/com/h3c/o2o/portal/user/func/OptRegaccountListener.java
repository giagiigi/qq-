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

package com.h3c.o2o.portal.user.func;

import java.io.IOException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.h3c.o2o.auth.rabbitmq.MqRequest;
import com.h3c.o2o.auth.rabbitmq.MqResponse;
import com.h3c.o2o.portal.cache.api.CacheApiMgr;
import com.h3c.o2o.portal.cache.key.RegistUserCacheKey;
import com.h3c.o2o.portal.entity.RegistUser;

/**
 * �̶��ʺ��ڴ��Ż���Ϣ��
 *
 * @author ykf2685
 *
 */
public class OptRegaccountListener {

    /** ���ݻ���ӿ� */
    private CacheApiMgr cacheApiMgr;

    /** ע���û��ӿ� */
    private RegistUserMgr userMgr;

    /** �������ͣ����� */
    private final int OPER_TYPE_ADD = 1;

    /** �������ͣ��޸� */
    private final int OPER_TYPE_MODIFY = 2;

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
                case OPER_TYPE_MODIFY:
                    // ��ѯ����
                    RegistUser user = userMgr.queryRegistUser(ids.get(0));
                    if (user != null) {
                        RegistUserCacheKey key = new RegistUserCacheKey(
                            user.getStoreId(), user.getUserName(),
                            user.getUserType());
                        // ���»���
                        cacheApiMgr.getRegistUserCacheMgr().put(key, user);
                    }
                    break;
                case OPER_TYPE_DELETE:
                    for (Long id : ids) {
                        cacheApiMgr.getRegistUserCacheMgr().removeByRelation(id);
                    }
                    break;
                default:
                    result.setSuccess(false);
                    break;
            }
            if (log.isTraceEnabled()) {
                for (RegistUserCacheKey key : cacheApiMgr
                    .getRegistUserCacheMgr().getKeys()) {
                    log.trace("key: "
                        + key.toString()
                        + " ~ value: "
                        + cacheApiMgr.getRegistUserCacheMgr().get(key)
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

    public void setUserMgr(RegistUserMgr userMgr) {
        this.userMgr = userMgr;
    }

}