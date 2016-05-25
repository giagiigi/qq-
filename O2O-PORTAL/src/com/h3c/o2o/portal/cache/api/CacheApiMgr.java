/*
 * Copyright (c) 2015, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC V300R002
 * Module Name :
 * Date Created: 2015-8-3
 * Creator     : dkf5133
 * Description :
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 * YYYY-MM-DD  zhangshan        XXXX project, new code file.
 *
 *------------------------------------------------------------------------------
 */
package com.h3c.o2o.portal.cache.api;

import java.util.ArrayList;

import com.h3c.o2o.portal.PortalException;
import com.h3c.o2o.portal.cache.CacheMgr;
import com.h3c.o2o.portal.cache.key.PublishMngCacheKey;
import com.h3c.o2o.portal.cache.key.RegistUserCacheKey;
import com.h3c.o2o.portal.entity.AuthCfg;
import com.h3c.o2o.portal.entity.BlackList;
import com.h3c.o2o.portal.entity.OnlineUser;
import com.h3c.o2o.portal.entity.PublishMng;
import com.h3c.o2o.portal.entity.PublishMngPara;
import com.h3c.o2o.portal.entity.RegistUser;
import com.h3c.o2o.portal.entity.ThemeTemplate;

/**
 * ����ӿڹ���ӿ�
 *
 * @author dkf5133
 */
public interface CacheApiMgr {

    /**
     * ��ʼ������
     *
     */
    void init();

    /**
     * �������
     *
     */
    void destroy();

    /**
     * ��ȡ��֤���û���ӿ�
     *
     * @return
     */
    CacheMgr<Long, AuthCfg> getAuthCfgCacheMgr();

    /**
     * ��ȡע���û�����ӿ�
     *
     * @return
     */
    CacheMgr<RegistUserCacheKey, RegistUser> getRegistUserCacheMgr();

    /**
     * ��ȡģ�建��ӿ�
     *
     * @return
     */
    CacheMgr<Long, ThemeTemplate> getThemeTemplateCacheMgr();
   
    /**
     * ��ȡ����������ӿ�
     *
     * @return
     */
    CacheMgr<Long, BlackList> getBlackListCacheMgr();

    /**
     * ��ȡ�����û�����ӿ�
     *
     * @return
     */
    CacheMgr<String, OnlineUser> getOnlineUserCacheMgr();

    /**
     * ��ȡ����������ӿ�
     * @return
     */
    CacheMgr<PublishMngCacheKey, PublishMng> getPublishMngCacheMgr();

    /**
     * ��ȡ���������������ӿ�
     * @return
     */
    CacheMgr<Long, ArrayList<PublishMngPara>> getPublishParaCacheMgr();

    /**
     * ��õ�����֤���ò���
     *
     * @param storeId ����ID
     * @param ssid ����ssid
     * @param paramType ������URL_AFTER_AUTH��ONLINE_MAX_TIME��IDLE_CUT_TIME��IDLE_CUT_FLOW
     * @return
     */
    String getConfigureParam(Long storeId, String ssid, String paramType);

    /**
     * ����ģ��ID��ҳ�����ͣ�����ҳ��·��
     * @param template ģ��
     * @param pageType ҳ������
     * @return
     */
    String getThemePageURI(ThemeTemplate template, int pageType);

    /**
     * ���ģ����Ϣ
     *
     * @param storeId ����ID������Ϊ��
     * @param ssid ����ssid
     * @param templateId ģ��ID������Ϊ�ա�Ϊ��ʱ���ص��̵�ǰ���õ�ģ��
     * @return ģ�����
     */
    ThemeTemplate getThemeTemplate(Long storeId, String ssid, Long templateId);
    
    /**
     * ��õ�����֤��Ϣ
     * @param storeId ����ID
     * @param ssid ����ssid
     * @return ��֤����
     */
    AuthCfg getAuthCfg(Long storeId, String ssid);

    /**
     * ��÷����������
     * @param storeId ����ID
     * @param paraName ��������
     * @return
     * @throws PortalException if there isn't store for {@code storeId}
     */
    String getPublishPara(long storeId, String paraName);

    /**
     * ��õ��������̻�ID(�ӷ��������л�ȡ)���˷��������ڶ�����֤��һ����¼���̶�
     * �˺ŵ�¼����Ҫ����������֤��Ҫ���ݵ��̹�����ѯ��
     * @param storeId ����ID
     * @param ssid ����ssid
     * @return
     */
    long getOwnerId(long storeId, String ssid);
    
    /**
     * ��õ��̿��õķ��������ָ��ssidû�п��õķ���������Ҳ�����ssid�ķ���
     * @param storeId
     * @param ssid
     * @return
     */
    public PublishMng getPublishMng(long storeId, String ssid);

    /**
     *
     * �ж��û��Ƿ�Ϊ�������û�
     *
     * @param user
     * @return
     */
    boolean isBlackUser(RegistUser user);
}
