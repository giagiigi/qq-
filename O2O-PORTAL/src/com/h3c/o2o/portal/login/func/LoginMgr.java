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
     * ��ȡͼƬ��Ϣ
     *
     * @param pageReq
     * @return
     */
	public JSONObject getPageImageInfo(PageImageReq pageReq);

	/**
	 * һ����֤
	 *
	 * @param req
	 * @param res
	 * @throws IOException
	 */
	public String quickLogin(LoginReq loginReq);

	/**
	 * ������֤
	 *
	 */
	public String smsLogin(SmsLoginReq loginReq);

	/**
	 * �̶��˺���֤
	 *
	 */
	public String accountLogin(SmsLoginReq loginReq);

	/**
	 * �ض�����ҳ
	 *
	 * @param req
	 * @param res
	 * @throws IOException
	 */
	public String redirectToHome(HomePageReq homePageReq);

	/**
	 * ���Ͷ���У����
	 *
	 * @param phoneNO �绰����
	 * @param storeId ����ID
	 * @param ssid ����ssid
	 * @throws Exception
	 */
	public void sendAuthMessage(String phoneNO, Long storeId, String ssid);

	/**
	 * ��ȡ������֤����
	 *
	 * @param confPara
	 * @return
	 */
	public AuthCfgResp getAuthConfig(LoginReq confPara);
	
	/**
	 * ��ȡ΢����Wifi����
	 * @param authconf
	 * @param confPara
	 * @return
	 */
	public WeixinConnectWifiPara getWxWifiPara(AuthCfg authconf, LoginReq confPara);
}
