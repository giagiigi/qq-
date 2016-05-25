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
 * 缓存接口管理接口
 *
 * @author dkf5133
 */
public interface CacheApiMgr {

    /**
     * 初始化数据
     *
     */
    void init();

    /**
     * 清除数据
     *
     */
    void destroy();

    /**
     * 获取认证配置缓存接口
     *
     * @return
     */
    CacheMgr<Long, AuthCfg> getAuthCfgCacheMgr();

    /**
     * 获取注册用户缓存接口
     *
     * @return
     */
    CacheMgr<RegistUserCacheKey, RegistUser> getRegistUserCacheMgr();

    /**
     * 获取模板缓存接口
     *
     * @return
     */
    CacheMgr<Long, ThemeTemplate> getThemeTemplateCacheMgr();
   
    /**
     * 获取黑名单缓存接口
     *
     * @return
     */
    CacheMgr<Long, BlackList> getBlackListCacheMgr();

    /**
     * 获取在线用户缓存接口
     *
     * @return
     */
    CacheMgr<String, OnlineUser> getOnlineUserCacheMgr();

    /**
     * 获取发布管理缓存接口
     * @return
     */
    CacheMgr<PublishMngCacheKey, PublishMng> getPublishMngCacheMgr();

    /**
     * 获取发布管理参数缓存接口
     * @return
     */
    CacheMgr<Long, ArrayList<PublishMngPara>> getPublishParaCacheMgr();

    /**
     * 获得店铺认证配置参数
     *
     * @param storeId 店铺ID
     * @param ssid 接入ssid
     * @param paramType 参数：URL_AFTER_AUTH、ONLINE_MAX_TIME、IDLE_CUT_TIME、IDLE_CUT_FLOW
     * @return
     */
    String getConfigureParam(Long storeId, String ssid, String paramType);

    /**
     * 根据模板ID和页面类型，查找页面路径
     * @param template 模板
     * @param pageType 页面类型
     * @return
     */
    String getThemePageURI(ThemeTemplate template, int pageType);

    /**
     * 获得模板信息
     *
     * @param storeId 店铺ID，不能为空
     * @param ssid 接入ssid
     * @param templateId 模板ID，允许为空。为空时返回店铺当前启用的模板
     * @return 模板对象
     */
    ThemeTemplate getThemeTemplate(Long storeId, String ssid, Long templateId);
    
    /**
     * 获得店铺认证信息
     * @param storeId 店铺ID
     * @param ssid 接入ssid
     * @return 认证配置
     */
    AuthCfg getAuthCfg(Long storeId, String ssid);

    /**
     * 获得发布管理参数
     * @param storeId 店铺ID
     * @param paraName 参数名称
     * @return
     * @throws PortalException if there isn't store for {@code storeId}
     */
    String getPublishPara(long storeId, String paraName);

    /**
     * 获得店铺所属商户ID(从发布管理中获取)。此方法仅用于短信认证和一键登录（固定
     * 账号登录不需要；第三方认证需要根据店铺关联查询）
     * @param storeId 店铺ID
     * @param ssid 接入ssid
     * @return
     */
    long getOwnerId(long storeId, String ssid);
    
    /**
     * 获得店铺可用的发布，如果指定ssid没有可用的发布，则查找不区分ssid的发布
     * @param storeId
     * @param ssid
     * @return
     */
    public PublishMng getPublishMng(long storeId, String ssid);

    /**
     *
     * 判断用户是否为黑名单用户
     *
     * @param user
     * @return
     */
    boolean isBlackUser(RegistUser user);
}
