/*
 * Copyright (c) 2007-2013, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC v7
 * Module Name : iMC
 * Date Created: 2015-6-19
 * Creator     : dkf5133
 * Description :
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 * 2015-6-19     dkf5133          iMC project, new code file.
 *
 *------------------------------------------------------------------------------
 */
package com.h3c.o2o.portal.login.func;

import java.io.IOException;

import net.sf.json.JSONObject;

import com.h3c.o2o.portal.entity.AuthCfg;
import com.h3c.o2o.portal.login.entity.AuthCfgResp;
import com.h3c.o2o.portal.login.entity.HomePageReq;
import com.h3c.o2o.portal.login.entity.LoginReq;
import com.h3c.o2o.portal.login.entity.PageImageReq;
import com.h3c.o2o.portal.login.entity.SmsLoginReq;
import com.h3c.o2o.portal.login.entity.WeixinConnectWifiPara;

/**
 * @author dkf5133
 *
 */
public interface LoginMgr {

    /**
     * 获取图片信息
     *
     * @param pageReq
     * @return
     */
	public JSONObject getPageImageInfo(PageImageReq pageReq);

	/**
	 * 一键认证
	 *
	 * @param req
	 * @param res
	 * @throws IOException
	 */
	public String quickLogin(LoginReq loginReq);

	/**
	 * 短信认证
	 *
	 */
	public String smsLogin(SmsLoginReq loginReq);

	/**
	 * 固定账号认证
	 *
	 */
	public String accountLogin(SmsLoginReq loginReq);

	/**
	 * 重定向到主页
	 *
	 * @param req
	 * @param res
	 * @throws IOException
	 */
	public String redirectToHome(HomePageReq homePageReq);

	/**
	 * 发送短信校验码
	 *
	 * @param phoneNO 电话号码
	 * @param storeId 店铺ID
	 * @param ssid 接入ssid
	 * @throws Exception
	 */
	public void sendAuthMessage(String phoneNO, Long storeId, String ssid);

	/**
	 * 获取店铺认证配置
	 *
	 * @param confPara
	 * @return
	 */
	public AuthCfgResp getAuthConfig(LoginReq confPara);
	
	/**
	 * 获取微信连Wifi参数
	 * @param authconf
	 * @param confPara
	 * @return
	 */
	public WeixinConnectWifiPara getWxWifiPara(AuthCfg authconf, LoginReq confPara);
}
