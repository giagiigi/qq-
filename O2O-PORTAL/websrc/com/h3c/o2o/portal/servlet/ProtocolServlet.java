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
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.h3c.o2o.portal.PortalErrorCodes;
import com.h3c.o2o.portal.PortalException;
import com.h3c.o2o.portal.common.FuncUtil;
import com.h3c.o2o.portal.common.HttpOrHttpsMgr;
import com.h3c.o2o.portal.entity.ShopWifiInfo;
import com.h3c.o2o.portal.protocol.entity.AccessToken;
import com.h3c.o2o.portal.protocol.entity.ErrorInfo;
import com.h3c.o2o.portal.protocol.entity.LoginParam;
import com.h3c.o2o.portal.protocol.entity.OfflineByMac;
import com.h3c.o2o.portal.protocol.entity.OnlineUserInfo;
import com.h3c.o2o.portal.protocol.entity.TokenReq;
import com.h3c.o2o.portal.protocol.entity.UserInfo;
import com.h3c.o2o.portal.protocol.entity.UserStatus;
import com.h3c.o2o.portal.protocol.func.ProtocolMgrImpl;
import com.h3c.o2o.portal.shopwifi.func.ShopWifiMgr;
import com.h3c.oasis.o2oserver.bootstrap.ServerContext;
import com.h3c.oasis.o2oserver.util.StringManager;

/**
 * @author dkf5133
 *
 */
public class ProtocolServlet extends HttpServlet {

    /**
     * ���л�ID
     */
    private static final long serialVersionUID = 6261785606389448581L;

    /** ��Դ���ʶ��� */
    private static StringManager sm = StringManager
        .getManager("com.h3c.o2o.portal");

    /** �������ͣ���½�ض��� */
    private static final String OPERATE_TYPE_LOGIN = "code";

    /** �������ͣ���ȡtoken */
    private static final String OPERATE_TYPE_ACCESS_TOKEN = "access_token";

    /** �������ͣ���ȡ�û���Ϣ */
    private static final String OPERATE_TYPE_USER_INFO = "userinfo";

    /** �������ͣ����߱��� */
    private static final String OPERATE_TYPE_ONLINES = "onlines";

    /** �������ͣ����߱��� */
    private static final String OPERATE_TYPE_OFFLINE = "offline";
    
    /** �������ͣ��̼���ҳ��ת */
    private static final String OPERATE_TYPE_HOMEPAGE = "shop";

    /** ��־���� */
    private Log log = LogFactory.getLog(getClass());
    
    private HttpOrHttpsMgr httpOrHttps;
    
    // �յ���authUrl�����󣬵ȴ�3s
    private static Long WRONGREQUESTWAITTIME = 3000L;

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res)
        throws IOException, ServletException {
        this.doPost(req, res);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res)
        throws IOException, ServletException {
        String requestURI = req.getRequestURI();
        if (log.isDebugEnabled()) {
            log.debug("Current URI: " + requestURI + (req.getQueryString() == null ? "" : "?" + req.getQueryString()));
        }
        String type = "";

        if (requestURI.matches("^.*/onlines$")) {
            // ���ͣ�onlines���Ĵ���
            type = OPERATE_TYPE_ONLINES;
        } else if (requestURI.matches("^.*/offline$")) {
            // ���ͣ�offline���Ĵ���
            type = OPERATE_TYPE_OFFLINE;
        } else {
            if (log.isDebugEnabled()) {
                log.debug("Current URI: " + req.getRequestURI());

                for (String key : req.getParameterMap().keySet()) {
                    log.debug("Parameter: " + key + " ~ value: "
                        + req.getParameter(key));
                }
            }
            // ��ȡ��������
        	type = req.getParameter("response_type");
        }
        // ���ദ��
        if (OPERATE_TYPE_LOGIN.equals(type)) {
        	long startTime = 0L;
        	if(log.isDebugEnabled()){
        		startTime = System.currentTimeMillis();
        	}
            operateTypeLogin(req, res);
            if(log.isDebugEnabled()){
            	long endTime = System.currentTimeMillis();
                log.debug("=== operateTypeLogin spend: " + (endTime - startTime) + " milliseconds. ===");
            }
        } else if (OPERATE_TYPE_ACCESS_TOKEN.equals(type)) {
            operateTypeToken(req, res);
        } else if (OPERATE_TYPE_USER_INFO.equals(type)) {
            operateTypeUserInfo(req, res);
        } else if (OPERATE_TYPE_ONLINES.equals(type)) {
            operateTypeOnlines(req, res);
        } else if (OPERATE_TYPE_OFFLINE.equals(type)) {
            operateTypeOffline(req, res);
        } else if (OPERATE_TYPE_HOMEPAGE.equals(type)) {
        	operateTypeHomePageRedirect(req, res);
        } else {
        	String authUrl = req.getParameter("authUrl");
            if(StringUtils.isNotBlank(authUrl)) {
            	// �յ���authUrl�����󣬵ȴ�3s
            	try {
        			Thread.sleep(WRONGREQUESTWAITTIME);
        		} catch (InterruptedException e) {
        			log.warn(null, e);
        		}
            	String info = "Wrong response_type with authUrl recieved, wait 3s.";
            	log.info(info);
            	// ֱ�ӷ���200
            	res.getWriter().write(info);
            } else {
            	res.sendRedirect(FuncUtil
                        .getErrorPageURI(PortalErrorCodes.OPERATION_NOT_SUPPORT));
                    log.warn("There is not 'response_typ' parameter in request.");
            }
        }
    }

    private void operateTypeLogin(HttpServletRequest req,
        HttpServletResponse res)
        throws ServletException, IOException {
        String ua = req.getHeader("User-Agent");
        String url = "";
        try {
            // У�鲢��װ����
            LoginParam param = validateLoginParam(req);
            // ����app��֤��userurl�а���authtype=lvzhouapp��ʶ
            String userurl = param.getUserurl();
            if (userurl != null && userurl.contains("authtype=lvzhouapp")) {
                url = ProtocolMgrImpl.get().appRedirect(param, ua);
            } else {
                url = ProtocolMgrImpl.get().loginRedirect(param, ua);
            }
            // �ض���
            if (log.isDebugEnabled()) {
                log.debug("Redirect URL is:" + url);
            }
            res.sendRedirect(url);
        } catch (PortalException e) {
            log.warn(null, e);
            if (-1 != e.getErrorCode()) {
                res.sendRedirect(FuncUtil.getErrorPageURI(e.getErrorCode()));
            } else {
                res.getWriter().write(e.getErrorMsg());
            }
        } catch (Exception e) {
            log.warn(null, e);
            res.getWriter().write(e.getMessage());
        }
    }

    private LoginParam validateLoginParam(HttpServletRequest req)
        throws UnsupportedEncodingException {
        LoginParam param = new LoginParam();
        // �ض���URI
        if (StringUtils.isBlank(req.getParameter("redirect_uri"))) {
            throw new PortalException(PortalErrorCodes.REDIRECT_URI_NULL,
                sm.getString("errorCode.60013"));
        } else {
            param.setRedirect_uri(req.getParameter("redirect_uri"));
        }
        // ����ID
        if (StringUtils.isBlank(req.getParameter("nas_id"))) {
            throw new PortalException(PortalErrorCodes.STORE_ID_NULL,
                sm.getString("errorCode.60014"));
        } else {
            param.setNasId(Long.valueOf(req.getParameter("nas_id")));
        }
        // �û�MAC
        if (StringUtils.isBlank(req.getParameter("usermac"))) {
            throw new PortalException(PortalErrorCodes.USER_MAC_NULL,
                sm.getString("errorCode.60015"));
        } else {
            param.setUsermac(req.getParameter("usermac"));
        }
        // �û�IP
        if (StringUtils.isBlank(req.getParameter("userip"))) {
            throw new PortalException(PortalErrorCodes.USER_IP_NULL,
                sm.getString("errorCode.60016"));
        } else {
            param.setUserip(req.getParameter("userip"));
        }
        // ssid���豸��urlencode�����һ�Σ��õ���ʵ��SSID
        if (StringUtils.isNotBlank(req.getParameter("ssid"))) {
            param.setSsid(URLDecoder.decode(req.getParameter("ssid"), "utf-8"));
        }

        if (StringUtils.isNotBlank(req.getParameter("userurl"))) {
        	String userUrl = null;
        	try{
        		userUrl = req.getParameter("userurl");
        		param.setUserurl(URLDecoder.decode(userUrl, "utf-8"));
        	} catch(Exception e){
        		log.warn("Wrong userUrl: " + userUrl, e);
        		// ���ڴ����UserUrl��Ĭ�ϸ�����������ַ.
        		param.setUserurl("http://www.h3c.com.cn/");
        	}
        }
        return param;
    }

    private void operateTypeToken(HttpServletRequest req,
        HttpServletResponse res)
        throws ServletException, IOException {
    	GsonBuilder gb =new GsonBuilder();
    	gb.disableHtmlEscaping();
        try {
            TokenReq tokenReq = new TokenReq();
            tokenReq.setCode(req.getParameter("code"));
            tokenReq.setUserIp(req.getParameter("userip"));
            tokenReq.setUserMac(req.getParameter("usermac"));
            AccessToken tokenInfo = ProtocolMgrImpl.get().getAccessToken(
                tokenReq);
            String json = gb.create().toJson(tokenInfo);
            logResponse(json);
            res.getWriter().write(json);
        } catch (PortalException e) {
            log.warn(null, e);
            // ����ʧ������
            ErrorInfo info = new ErrorInfo();
            info.setErrcode(e.getErrorCode());
            info.setErrmsg(e.getErrorMsg());
            info.setErrurl(FuncUtil.getErrorPageURI(e.getErrorCode()));
            res.getWriter().write(gb.create().toJson(info));
        } catch (Exception e) {
            log.warn(null, e);
            // ����ʧ������
            ErrorInfo info = new ErrorInfo();
            info.setErrcode(PortalErrorCodes.INTERNAL_SERVER_ERROR);
            info.setErrmsg("Internal server error.");
            info.setErrurl(FuncUtil.getErrorPageURI(PortalErrorCodes.INTERNAL_SERVER_ERROR));
            res.getWriter().write(gb.create().toJson(info));
        }
    }

    private void operateTypeUserInfo(HttpServletRequest req,
        HttpServletResponse res)
        throws ServletException, IOException {
    	GsonBuilder gb =new GsonBuilder();
    	gb.disableHtmlEscaping();
        try {
            UserInfo user = ProtocolMgrImpl.get().getUserInfo(
            		req.getParameter("access_token"), FuncUtil.getIpAddr(req));
            res.setCharacterEncoding("UTF-8");
            res.getWriter().write(gb.create().toJson(user));
        } catch (PortalException e) {
            log.warn(null, e);
            // ����ʧ������
            ErrorInfo info = new ErrorInfo();
            info.setErrcode(e.getErrorCode());
            info.setErrmsg(e.getErrorMsg());
            info.setErrurl(FuncUtil.getErrorPageURI(e.getErrorCode()));
            String json = gb.create().toJson(info);
            logResponse(json);
            res.getWriter().write(json);
        } catch (Exception e) {
            log.warn(null, e);
            // ����ʧ������
            ErrorInfo info = new ErrorInfo();
            info.setErrcode(PortalErrorCodes.INTERNAL_SERVER_ERROR);
            info.setErrmsg("Internal server error.");
            info.setErrurl(FuncUtil.getErrorPageURI(PortalErrorCodes.INTERNAL_SERVER_ERROR));
            res.getWriter().write(gb.create().toJson(info));
        }
    }

    private void logResponse(String json) {
        if (log.isDebugEnabled()) {
            log.debug("Response:" + json);
        }
    }

	private void operateTypeOnlines(HttpServletRequest req,
        HttpServletResponse res)
        throws ServletException, IOException {

    	ServletInputStream stream = req.getInputStream();
        Gson gson = new Gson();
        // ��������ʵ��
        UserStatus status = null;
        Reader reader = null;
        try {
            // ���л�
            reader = new InputStreamReader(stream);
            OnlineUserInfo onlineUserInfo = gson.fromJson(reader,
                OnlineUserInfo.class);
            if (log.isDebugEnabled()) {
                log.debug("OnlineUserInfo: " + onlineUserInfo);
            }
            status = ProtocolMgrImpl.get().uploadOnlines(onlineUserInfo);
            if (log.isDebugEnabled()) {
                log.debug("UserStatus: " + status);
            }
            String json = gson.toJson(status);
            logResponse(json);
            res.getWriter().write(json);
        } catch (Exception e) {
            log.warn("Process onlines protocol error. ", e);
            res.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    //ignore
                }
            }
            if (stream != null) {
                try {
                    stream.close();
                } catch (Exception e) {
                    //ignore
                }
            }
        }
    }

    private void operateTypeOffline(HttpServletRequest req,
            HttpServletResponse res) throws IOException {
        ServletInputStream stream = req.getInputStream();
        Gson gson = new Gson();
        Reader reader = null;
        try {
            // ���л�
            reader = new InputStreamReader(stream);
            OfflineByMac off = gson.fromJson(reader, OfflineByMac.class);
            if (log.isDebugEnabled()) {
                log.debug("Offline params: " + off);
            }
            if(null == off){
            	throw new PortalException("Mac is empty!");
            }
            // �û�����
            ProtocolMgrImpl.get().userOffline(off.getMac());
            res.getWriter().write("success");
        } catch (Exception e) {
            log.warn("Process offline protocol error. ", e);
            res.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    //ignore
                }
            }
            if (stream != null) {
                try {
                    stream.close();
                } catch (Exception e) {
                    //ignore
                }
            }
        }
    }
    
	/**
	 * �����̼���ҳ����ת302��ת
	 * 
	 * @param req
	 * @param res
	 */
	private void operateTypeHomePageRedirect(HttpServletRequest req, HttpServletResponse res) throws IOException {
		try {
			String nasidStr = req.getParameter("nas_id");
			if (StringUtils.isBlank(nasidStr)) {
				throw new Exception("Empty nasId.");
			}
			Long nasid = null;
			try {
				nasid = Long.valueOf(nasidStr);
			} catch (NumberFormatException e) {
				log.warn(null, e);
				throw new Exception("Wrong nasId: " + nasidStr);
			}
			ShopWifiMgr shopWifiMgr = (ShopWifiMgr) getMgr("shopWifiMgr");
			ShopWifiInfo shopWifiInfo = shopWifiMgr.queryShopWifiInfo(nasid);
			if (null == shopWifiInfo) {
				throw new Exception("No ShopWifiInfo found by nasId: " + nasidStr);
			} else if (StringUtils.isBlank(shopWifiInfo.getHomepage_url())) {
				throw new Exception(sm.getString("o2o.portal.homepage.url.notset", shopWifiInfo.getShopName()));
			} else {
				if (log.isDebugEnabled()) {
					log.debug("Redirect to: " + shopWifiInfo.getHomepage_url());
				}
				res.sendRedirect(shopWifiInfo.getHomepage_url());
			}
		} catch (Exception e) {
			log.warn(null, e);
			if(null == httpOrHttps){
				httpOrHttps = (HttpOrHttpsMgr)getMgr("httpOrHttps");
			}
			StringBuilder sbuilder = new StringBuilder().append(httpOrHttps.getHttpOrHttps() + "://").append(FuncUtil.getPortalDomain())
					.append(":").append(FuncUtil.getPortalServerPort()).append("/portal/ErrorPage.jsp?errorHint=");
			String errorhint = "Error:" + e.getMessage();
			try {
				res.sendRedirect(sbuilder.append(URLEncoder.encode(errorhint, "UTF-8")).toString());
			} catch (UnsupportedEncodingException e1) {
				log.warn(null, e1);
			}
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

	public HttpOrHttpsMgr getHttpOrHttps() {
		return httpOrHttps;
	}

	public void setHttpOrHttps(HttpOrHttpsMgr httpOrHttps) {
		this.httpOrHttps = httpOrHttps;
	}

}
