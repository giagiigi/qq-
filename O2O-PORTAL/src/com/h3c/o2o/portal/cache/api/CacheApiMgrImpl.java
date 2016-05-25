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
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.h3c.o2o.portal.PortalErrorCodes;
import com.h3c.o2o.portal.PortalException;
import com.h3c.o2o.portal.cache.CacheMgr;
import com.h3c.o2o.portal.cache.key.PublishMngCacheKey;
import com.h3c.o2o.portal.cache.key.RegistUserCacheKey;
import com.h3c.o2o.portal.entity.AuthCfg;
import com.h3c.o2o.portal.entity.AuthParam;
import com.h3c.o2o.portal.entity.BlackList;
import com.h3c.o2o.portal.entity.OnlineUser;
import com.h3c.o2o.portal.entity.PublishMng;
import com.h3c.o2o.portal.entity.PublishMngPara;
import com.h3c.o2o.portal.entity.RegistUser;
import com.h3c.o2o.portal.entity.ThemePage;
import com.h3c.o2o.portal.entity.ThemeTemplate;
import com.h3c.o2o.portal.theme.func.ThemePageMgr;
import com.h3c.o2o.portal.user.func.AccessUserDetailMgr;
import com.h3c.o2o.portal.user.func.OnlineUserMgr;
import com.h3c.oasis.o2oserver.util.StringManager;

/**
 * 缓存接口管理实现类
 *
 * @author dkf5133
 */
public class CacheApiMgrImpl implements CacheApiMgr {

	/** 资源访问对象。 */
    private static StringManager sm = StringManager
        .getManager("com.h3c.o2o.portal");

    private Log log = LogFactory.getLog(getClass());

    /** 认证配置缓存接口。主键：店铺ID */
    private CacheMgr<Long, AuthCfg> authCfgCacheMgr;

    /** 注册用户缓存接口。主键：com.h3c.o2o.portal.user.entity.RegistUserCacheKey */
    private CacheMgr<RegistUserCacheKey, RegistUser> registUserCacheMgr;

    /** 模板缓存接口。主键：店铺ID */
    private CacheMgr<Long, ThemeTemplate> themeTemplateCacheMgr;
    
    /** 黑名单缓存接口。主键：用户ID */
    private CacheMgr<Long, BlackList> blackListCacheMgr;

    /** 在线用户缓存接口。主键：用户MAC */
    private CacheMgr<String, OnlineUser> onlineUserCacheMgr;

    /** 发布管理缓存接口。主键：storeId + ssid */
    private CacheMgr<PublishMngCacheKey, PublishMng> publishMngCacheMgr;

    /** 发布管理参数缓存接口。主键：storeId */
    private CacheMgr<Long, ArrayList<PublishMngPara>> publishParaCacheMgr;

    /** 内部使用，不提供对外接口 */
    private ThemePageMgr themePageMgr;

    /** 内部使用，不提供对外接口 */
    private OnlineUserMgr onlineUserMgr;

    /** 内部使用，不提供对外接口 */
    private AccessUserDetailMgr accessUserDetailMgr;

    /** 遍历Token与Code间隔-单位秒 */
    private int interval = 10 * 60;
    
    /** 设备在线用户报文默认心跳间隔 */
    private long defaultInterval = 3 * 60;

    /**
     * 认证配置和主题模板不需要全部缓存，因此不做初始化。第一次查询后再做缓存
     * @see com.h3c.o2o.portal.cache.api.CacheApiMgr#init()
     *
     */
    @Override
    public void init() {
        registUserCacheMgr.init();
        blackListCacheMgr.init();
        onlineUserCacheMgr.init();
        publishMngCacheMgr.init();
        publishParaCacheMgr.init();
        new CheckOnlineUserHeartBeat().start();
    }

    /* (non-Javadoc)
     * @see com.h3c.o2o.portal.cache.api.CacheApiMgr#destroty()
     */
    @Override
    public void destroy() {
        authCfgCacheMgr.clear();
        registUserCacheMgr.clear();
        themeTemplateCacheMgr.clear();
        blackListCacheMgr.clear();
        onlineUserCacheMgr.clear();
        publishMngCacheMgr.clear();
        publishParaCacheMgr.clear();
    }

    /* (non-Javadoc)
     * @see com.h3c.o2o.portal.cache.api.CacheApiMgr#getAuthCfgCacheMgr()
     */
    @Override
    public CacheMgr<Long, AuthCfg> getAuthCfgCacheMgr() {
        return authCfgCacheMgr;
    }

    /* (non-Javadoc)
     * @see com.h3c.o2o.portal.cache.api.CacheApiMgr#getRegistUserCacheMgr()
     */
    @Override
    public CacheMgr<RegistUserCacheKey, RegistUser> getRegistUserCacheMgr() {
        return registUserCacheMgr;
    }

    /* (non-Javadoc)
     * @see com.h3c.o2o.portal.cache.api.CacheApiMgr#getThemeTemplateCacheMgr()
     */
    @Override
    public CacheMgr<Long, ThemeTemplate> getThemeTemplateCacheMgr() {
        return themeTemplateCacheMgr;
    }

    /* (non-Javadoc)
     * @see com.h3c.o2o.portal.cache.api.CacheApiMgr#getBlackListCacheMgr()
     */
    @Override
    public CacheMgr<Long, BlackList> getBlackListCacheMgr() {
        return blackListCacheMgr;
    }

    @Override
	public CacheMgr<String, OnlineUser> getOnlineUserCacheMgr() {
		return onlineUserCacheMgr;
	}

    @Override
	public CacheMgr<PublishMngCacheKey, PublishMng> getPublishMngCacheMgr() {
		return publishMngCacheMgr;
	}

    @Override
	public CacheMgr<Long, ArrayList<PublishMngPara>> getPublishParaCacheMgr() {
		return publishParaCacheMgr;
	}

	@Override
	public String getConfigureParam(Long storeId, String ssid, String paramType) {
		AuthCfg config = getAuthCfg(storeId, ssid);
		for (AuthParam param : config.getAuthParamList()) {
			if (param.getAuthParamName().equals(paramType)) {
				return param.getAuthParamValue();
			}
		}
		return null;
	}

	@Override
	public String getThemePageURI(ThemeTemplate template, int pageType) {
		ThemePage page = themePageMgr.getThemePage(template, pageType);
        String URI = null;
        if (page != null) {
            URI = "theme/" + page.getPathName() + "/" + page.getFileName();
        }
        return URI;
	}

    /* (non-Javadoc)
     * @see com.h3c.o2o.portal.theme.func.ThemePageMgr#getThemeTemplate(java.lang.Long, java.lang.Long)
     */
	@Override
	public ThemeTemplate getThemeTemplate(Long storeId, String ssid,
			Long userTemplateId) {
		ThemeTemplate result = null;
		// 首先查找用户使用的模板
		if (userTemplateId != null) {
            result = themeTemplateCacheMgr.get(userTemplateId);
		}
		// 如果用户模板不存在，查找店铺启用的模板
		if (result == null) {
			if (log.isDebugEnabled() && userTemplateId != null) {
				log.debug("Can't find the template that user is using, id:" + userTemplateId);
			}
			long storeThemeId = getPublishMng(storeId, ssid)
					.getThemeTemplateId();
			result = themeTemplateCacheMgr
					.get(storeThemeId);
		}

		// 如果没有可用的模板，抛出异常
		if (result == null) {
			log.warn("There isn't template can be used.");
			throw new PortalException("There isn't template can be used.");
		}  else {
			if (log.isDebugEnabled()) {
				log.debug("Using template: " + result.toString());
			}
		}
		return result;
	}

	/* (non-Javadoc)
     * @see com.h3c.o2o.portal.cache.api.CacheApiMgr#getAuthCfg(Long storeId, String ssid)
     */
	@Override
	public AuthCfg getAuthCfg(Long storeId, String ssid) {
		// 获得启用的发布
		PublishMng publish = getPublishMng(storeId, ssid);
		long cachedAuthcfgId = publish.getAuthCfgId();
		AuthCfg authcfg = authCfgCacheMgr.get(cachedAuthcfgId);
		// 如果没有可用的配置，抛出异常
		if (authcfg == null) {
			log.warn("There isn't auth config for storeId: " + storeId + "; ssid: " + ssid);
            throw new PortalException(PortalErrorCodes.AUTH_CFG_NOT_EXISTS,
                sm.getString("o2o.portal.quickLogin.store.notConfigued"));
		} else {
			if (log.isDebugEnabled()) {
				log.debug("Using auth config for storeId: " + storeId
						+ "; ssid: " + ssid + ": " + authcfg.toString());
			}
		}
		return authcfg;
	}

	@Override
	public String getPublishPara(long storeId, String paraName) {
		// 获得店铺下的所有参数
		List<PublishMngPara> paras = publishParaCacheMgr.get(storeId);

		String result = null;
		// 循环参数列表，如果名称与指定参数名称相同，返回该参数
		for (PublishMngPara item : paras) {
			if (item.getParam_name().equals(paraName)) {
				result = item.getParam_value();
				break;
			}
		}
		if (result == null) {
			log.info("There isn't parameter '" + paraName + "' for store: " + storeId);
			log.info("Using default value for '"
					+ PublishMngPara.REPORT_INTERVAL_PARA_NAME + "':"
					+ PublishMngPara.DEFAUL_INTERVAL);
			return String.valueOf(PublishMngPara.DEFAUL_INTERVAL);
		}
		return result;
	}

	@Override
	public long getOwnerId(long storeId, String ssid) {
		return getPublishMng(storeId, ssid).getOwnerId();
	}

	/**
	 * 获得店铺可用的发布，如果指定ssid没有可用的发布，则查找不区分ssid的发布
	 * @param storeId
	 * @param ssid
	 * @return
	 */
	@Override
	public PublishMng getPublishMng(long storeId, String ssid) {
		// 获得启用的发布
		PublishMng publish = publishMngCacheMgr.get(
		    new PublishMngCacheKey(storeId, ssid));
		// 如果没有找到指定ssid的发布，使用不区分ssid的配置
		if (ssid != null && publish == null) {
			publish = publishMngCacheMgr
					.get(new PublishMngCacheKey(storeId, null));
		}
		// 如果没有可用的发布，抛出异常
		if (publish == null) {
			if (log.isDebugEnabled()) {
				log.debug("There isn't publishMng for storeId: " + storeId);
			}
            throw new PortalException(
                PortalErrorCodes.PUBLISH_MNG_NOT_EXISTS,
                sm.getString("o2o.portal.quickLogin.store.no.publish.management"));
		}
		if (log.isDebugEnabled()) {
			log.debug("Using publishMng: " + publish);
		}
		return publish;
	}

    /* (non-Javadoc)
     * @see com.h3c.o2o.portal.cache.api.CacheApiMgr#isBlackUser(com.h3c.o2o.portal.entity.RegistUser)
     */
    @Override
    public boolean isBlackUser(RegistUser user) {
        return null != blackListCacheMgr.get(user.getId());
    }

    class CheckOnlineUserHeartBeat extends Thread {

    	public CheckOnlineUserHeartBeat() {
    		setName("CheckOnlineUserHeartBeat-Thread");
    		setDaemon(true);
		}

		@Override
		public void run() {
			for (;;) {
				try {
					for (String key : onlineUserCacheMgr.getKeys()) {
						OnlineUser onlineUser = onlineUserCacheMgr.get(key);
						if (onlineUser != null) {
							long lastBeatTime = onlineUser.getAccessStartTime() + getAccessDuration(onlineUser) * 1000;
							if (log.isDebugEnabled()) {
			                    log.debug("User:'" + onlineUser.getUserName() + "' LastBeatTime:" + lastBeatTime);
			                    log.debug("User:'" + onlineUser.getUserName() + "' 3 times BeatInterval:" + getBeatInterval(onlineUser));
			                    log.debug("Current time:" + System.currentTimeMillis());
			                }
							// 超时三分钟下线
							if (lastBeatTime + getBeatInterval(onlineUser) < System.currentTimeMillis()) {
								// 用户下线
								onlineUserMgr.deleteOnlineUser(onlineUser);
								// 创建接入明细
								accessUserDetailMgr.createAccessDetail(onlineUser);
								// 移除缓存
								onlineUserCacheMgr.remove(key);
							    log.info("User '" + onlineUser.getUserName() + "' offline for reason: overtime.");
							}
						}
					}
					TimeUnit.SECONDS.sleep(interval);
				} catch (Exception e) {
					log.warn("Check online user state error.", e);

					try {
						TimeUnit.SECONDS.sleep(interval);
					} catch (InterruptedException e1) {
						log.warn("Thread sleep error. Thread quit.");
						break;
					}
				}
			}

		}

		private long getBeatInterval(OnlineUser onlineUser) {
			// 获得用户信息上传时间间隔，单位：秒
			String intervalStr = null;
			try {
				intervalStr = getPublishPara(onlineUser.getStoreId(),
						PublishMngPara.REPORT_INTERVAL_PARA_NAME);
			} catch (Exception e) {
				log.warn("Get interval parameter error.", e);
				log.info("Using default value for '" 
						+ PublishMngPara.REPORT_INTERVAL_PARA_NAME + "'"
						+ defaultInterval);
			}
			if (StringUtils.isNotBlank(intervalStr)) {
				defaultInterval = Long.valueOf(intervalStr);
			}
			if (log.isDebugEnabled()) {
                log.debug("Final beat interval:" + defaultInterval);
            }
			// 转毫秒
			return 3 * defaultInterval * 1000;
		}

		private long getAccessDuration(OnlineUser onlineUser) {
			if (onlineUser.getAccessDuration() == null) {
				return 0;
			} else
				return onlineUser.getAccessDuration();
		}
	}

    public void setAuthCfgCacheMgr(CacheMgr<Long, AuthCfg> authCfgCacheMgr) {
        this.authCfgCacheMgr = authCfgCacheMgr;
    }

    public void setRegistUserCacheMgr(CacheMgr<RegistUserCacheKey, RegistUser> registUserCacheMgr) {
        this.registUserCacheMgr = registUserCacheMgr;
    }

    public void setThemeTemplateCacheMgr(CacheMgr<Long, ThemeTemplate> themeTemplateCacheMgr) {
        this.themeTemplateCacheMgr = themeTemplateCacheMgr;
    }

    public void setThemePageMgr(ThemePageMgr themePageMgr) {
        this.themePageMgr = themePageMgr;
    }

    public void setBlackListCacheMgr(CacheMgr<Long, BlackList> blackListCacheMgr) {
        this.blackListCacheMgr = blackListCacheMgr;
    }

	public void setOnlineUserCacheMgr(CacheMgr<String, OnlineUser> onlineUserCacheMgr) {
		this.onlineUserCacheMgr = onlineUserCacheMgr;
	}

	public void setPublishMngCacheMgr(CacheMgr<PublishMngCacheKey, PublishMng> publishMngCacheMgr) {
		this.publishMngCacheMgr = publishMngCacheMgr;
	}

	public void setPublishParaCacheMgr(CacheMgr<Long, ArrayList<PublishMngPara>> publishParaCacheMgr) {
		this.publishParaCacheMgr = publishParaCacheMgr;
	}

	public void setOnlineUserMgr(OnlineUserMgr onlineUserMgr) {
		this.onlineUserMgr = onlineUserMgr;
	}

	public void setAccessUserDetailMgr(AccessUserDetailMgr accessUserDetailMgr) {
		this.accessUserDetailMgr = accessUserDetailMgr;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public void setDefaultInterval(long defaultInterval) {
		this.defaultInterval = defaultInterval;
	}

}
