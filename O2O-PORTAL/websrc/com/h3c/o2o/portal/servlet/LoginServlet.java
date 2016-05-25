/*
 * Copyright (c) 2007-2013, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC v7
 * Module Name : iMC
 * Date Created: 2015-6-18
 * Creator     : dkf5133
 * Description :
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 * 2015-6-18     dkf5133          iMC project, new code file.
 *
 *------------------------------------------------------------------------------
 */
package com.h3c.o2o.portal.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.GsonBuilder;
import com.h3c.o2o.portal.PortalException;
import com.h3c.o2o.portal.login.entity.AuthCfgResp;
import com.h3c.o2o.portal.login.entity.HomePageReq;
import com.h3c.o2o.portal.login.entity.LoginReq;
import com.h3c.o2o.portal.login.entity.PageImageReq;
import com.h3c.o2o.portal.login.entity.SmsLoginReq;
import com.h3c.o2o.portal.login.func.LoginMgr;
import com.h3c.o2o.portal.protocol.entity.AuthMessageCode;
import com.h3c.o2o.portal.util.WifiUtils;
import com.h3c.oasis.o2oserver.bootstrap.ServerContext;
import com.h3c.oasis.o2oserver.util.StringManager;

import net.sf.json.JSONObject;

/**
 * @author dkf5133
 *
 */
public class LoginServlet extends HttpServlet {

    /**
     * ���л�ID
     */
    private static final long serialVersionUID = -7377068109074590914L;

    /** �������ͣ��������� */
    private final int OPERATE_TYPE_QUICK_LOGIN = 1;

    /** �������ͣ���ȡУ���� */
    private final int OPERATE_TYPE_GET_AUTH_MESSAGE = 2;

    /** �������ͣ����ŵ�½ */
    private final int OPERATE_TYPE_SMS_LOGIN = 3;

    /** �������ͣ�������ҳ */
    private final int OPERATE_TYPE_HOME = 4;

    /** �������ͣ�����ͼƬ */
    private final int OPERATE_TYPE_INIT_IMAGE = 5;

    /** �������ͣ���½ҳ���ʼ������ */
    private final int OPERATE_TYPE_INIT_AUTHCONF = 6;

    /** �������ͣ��̶��˺ŵ�½ */
    private final int OPERATE_TYPE_ACCOUNT_LOGIN = 7;

    /** ��Դ���ʶ��� */
    private static StringManager sm = StringManager
        .getManager("com.h3c.o2o.portal");

    private LoginMgr service;

    /** ��־���� */
    private Log log = LogFactory.getLog(getClass());

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res)
        throws IOException {
        this.doPost(req, res);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res)
        throws IOException {
        if (null == service) {
            service = (LoginMgr) getMgr("portalLoginMgr");
        }
        if (log.isDebugEnabled()) {
            log.debug("Current URI: " + req.getRequestURI());

            for (String key : req.getParameterMap().keySet()) {
                log.debug("Parameter: " + key + " ~ value: "
                    + req.getParameter(key));
            }
        }

        String redirect_uri = "";
        try {
			int operateType = Integer.valueOf(req.getParameter("operateType"));
			// ���ｫssid����һ�Σ��Ա��ڵõ���ʵ��SSID
			String ssid = WifiUtils.safeDecode(req.getParameter("ssid"));
            switch (operateType) {
                case OPERATE_TYPE_QUICK_LOGIN:
                	LoginReq loginReq = new LoginReq();
                    loginReq.setNasId(Long.valueOf(req.getParameter("nas_id")));
                    loginReq.setRedirect_uri(req.getParameter("redirect_uri"));
                    loginReq.setSsid(ssid);
                    loginReq.setUserip(req.getParameter("userip"));
                    loginReq.setUsermac(req.getParameter("usermac"));
                    loginReq.setUserurl(req.getParameter("userurl"));
                    loginReq.setTemplateId(Long.valueOf(req.getParameter("templateId")));
                    redirect_uri = service.quickLogin(loginReq);
                    if (redirect_uri == null) {
                        throw new PortalException("Incorrect sotre config.");
                    }
                    // ��½�ض���
                    res.sendRedirect(redirect_uri);
                    break;
                case OPERATE_TYPE_GET_AUTH_MESSAGE:
				service.sendAuthMessage(req.getParameter("phoneNO"),
						Long.valueOf(req.getParameter("storeId")),
						ssid);
                    JSONObject resendTime = new JSONObject();
                    resendTime.put("resendTime",
                        AuthMessageCode.MESSAGE_CODE_ALIVE_TIME / 1000);
                    resendTime.put("btnVal",
                        sm.getString("o2o.portal.send.auth.message.btn.value"));
                    res.setContentType("text/json; charset=UTF-8");
                    res.setCharacterEncoding("UTF-8");
                    res.getWriter().write(resendTime.toString());
                    break;
                case OPERATE_TYPE_SMS_LOGIN:
                    SmsLoginReq smsLoginReq = new SmsLoginReq();
                    smsLoginReq.setNasId(Long.valueOf(req.getParameter("nas_id")));
                    smsLoginReq.setRedirect_uri(req.getParameter("redirect_uri"));
                    smsLoginReq.setSsid(ssid);
                    smsLoginReq.setUserip(req.getParameter("userip"));
                    smsLoginReq.setUsermac(req.getParameter("usermac"));
                    smsLoginReq.setUserurl(req.getParameter("userurl"));
                    smsLoginReq.setUserName(req.getParameter("userName"));
                    smsLoginReq.setPassword(req.getParameter("signature"));
                    redirect_uri = service.smsLogin(smsLoginReq);
                    res.setContentType("text/json; charset=UTF-8");
                    res.setCharacterEncoding("UTF-8");
                    // ��Ϊ��ajax���󣬷���URI���ɿͻ����ض���
                    res.getWriter().write("{\"redirect_uri\":\"" + redirect_uri + "\"}");
                    break;
                case OPERATE_TYPE_HOME:
                    // TODO У���û���¼״̬����ʱ�����ǣ�

                	HomePageReq homePageReq = new HomePageReq();
                	homePageReq.setNas_id(Long.valueOf(req.getParameter("nas_id")));
                	homePageReq.setSsid(ssid);
                	homePageReq.setTemplateId(Long.valueOf(req.getParameter("templateId")));
                    // �ض�����ҳ
                    redirect_uri = service
                        .redirectToHome(homePageReq);
                    res.sendRedirect(redirect_uri);
                    break;
                case OPERATE_TYPE_INIT_IMAGE:
                    PageImageReq pageReq = new PageImageReq();
                    pageReq.setNasId(Long.valueOf(req.getParameter("nas_id")));
                    pageReq.setSsid(ssid);
                    pageReq.setPageType(req.getParameter("pageType"));
                    pageReq.setTemplateId(req.getParameter("templateId"));
                    JSONObject imageConfig = service.getPageImageInfo(pageReq);
                    JSONObject result = new JSONObject();
                    result.put("image", imageConfig);
                    res.setContentType("text/json; charset=UTF-8");
                    res.setCharacterEncoding("UTF-8");
                    res.getWriter().write(result.toString());
                    break;
                case OPERATE_TYPE_INIT_AUTHCONF:
                    LoginReq confPara = new LoginReq();
                    confPara.setNasId(Long.valueOf(req.getParameter("nas_id")));
                    confPara.setSsid(ssid);
                    confPara.setUserip(req.getParameter("userip"));
                    confPara.setUsermac(req.getParameter("usermac"));
                    confPara.setBssid(req.getParameter("bssid"));
                    
                    AuthCfgResp authConfig = service.getAuthConfig(confPara);
                    GsonBuilder gb = new GsonBuilder();
                    gb.disableHtmlEscaping();
                    String json = gb.create().toJson(authConfig);
                    res.setContentType("text/json; charset=UTF-8");
                    res.setCharacterEncoding("UTF-8");
                    res.getWriter().write(json);
                    break;
                case OPERATE_TYPE_ACCOUNT_LOGIN:
                    SmsLoginReq accountLoginReq = new SmsLoginReq();
                    accountLoginReq.setNasId(Long.valueOf(req
                        .getParameter("nas_id")));
                    accountLoginReq.setRedirect_uri(req
                        .getParameter("redirect_uri"));
                    accountLoginReq.setSsid(ssid);
                    accountLoginReq.setUserip(req.getParameter("userip"));
                    accountLoginReq.setUsermac(req.getParameter("usermac"));
                    accountLoginReq.setUserurl(req.getParameter("userurl"));
                    accountLoginReq.setUserName(req.getParameter("userName"));
                    accountLoginReq.setPassword(req.getParameter("signature"));
                    redirect_uri = service.accountLogin(accountLoginReq);
                    res.setContentType("text/json; charset=UTF-8");
                    res.setCharacterEncoding("UTF-8");
                    // ��Ϊ��ajax���󣬷���URI���ɿͻ����ض���
                    res.getWriter().write("{\"redirect_uri\":\"" + redirect_uri + "\"}");
                    break;
                default:
                    break;
            }
        } catch (PortalException e) {
            log.warn(null, e);
            res.setContentType("text/json; charset=UTF-8");
            res.setCharacterEncoding("UTF-8");
            res.getWriter().write("{\"error\":\"" + e.getErrorMsg() + "\"}");
        } catch (Exception e) {
            log.warn(null, e);
            res.setContentType("text/json; charset=UTF-8");
            res.setCharacterEncoding("UTF-8");
            res.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }

    }

    /**
     * ��ȡMGR�ӿڡ�
     * @param request HTTP request����
     * @param mgrName MGR����
     * @return bean MGRʵ��
     */
    private Object getMgr(String mgrName) {
        return ServerContext.getRootAppContext().getBean(mgrName);
    }
}
