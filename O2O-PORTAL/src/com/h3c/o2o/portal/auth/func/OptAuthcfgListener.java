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
 * ��֤�����ڴ��Ż���Ϣ��
 *
 * @author ykf2685
 *
 */
public class OptAuthcfgListener {

    /** ���ݻ���ӿ� */
    private CacheApiMgr cacheApiMgr;

    /** ��֤���ýӿ� */
    private AuthCfgMgr authCfgMgr;

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
                	result.setSuccess(false);
                    break;
                case OPER_TYPE_MODIFY:
                    // ��ѯ����
                    AuthCfg config = authCfgMgr.findAuthConfig(ids.get(0));
                    if (config != null) {
						if (cacheApiMgr.getAuthCfgCacheMgr().getFromCache(
								ids.get(0)) != null) {
							// ���»���
	                        cacheApiMgr.getAuthCfgCacheMgr().put(
	                            config.getId(), config);
						} else {
							// ���������û�У�˵������֤����û�б�ʹ�ã�����Ҫ���»���
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
