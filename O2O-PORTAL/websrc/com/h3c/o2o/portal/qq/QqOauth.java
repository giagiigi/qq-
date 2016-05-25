/*
 * Copyright (c) 2007-2015, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC V700R001
 * Module Name :
 * Date Created: 2015-7-15
 * Creator     : ykf2685
 * Description :
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 * 2015-7-15  ykf2685             O2O-PORTAL project, new code file.
 *
 *------------------------------------------------------------------------------
 */

package com.h3c.o2o.portal.qq;

import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.h3c.o2o.portal.cache.api.CacheApiMgr;
import com.h3c.o2o.portal.common.FuncUtil;
import com.h3c.o2o.portal.common.HttpOrHttpsMgr;
import com.h3c.o2o.portal.protocol.func.ProtocolMgr;
import com.h3c.oasis.o2oserver.bootstrap.ServerContext;
import com.qq.connect.QQConnect;
import com.qq.connect.QQConnectException;
import com.qq.connect.javabeans.AccessToken;
import com.qq.connect.utils.QQConnectConfig;
import com.qq.connect.utils.http.PostParameter;

public class QqOauth extends QQConnect {
    private static final long serialVersionUID = -7860508274941797293L;

    private static Log log = LogFactory.getLog(QqOauth.class);

    private CacheApiMgr cacheApiMgr;
    
    private ProtocolMgr protocolMgr;
    
    private HttpOrHttpsMgr httpOrHttps;

	public String[] extractionAuthCodeFromUrl(String url)
        throws QQConnectException {

        if (url == null) {
            throw new QQConnectException("you pass a null String object");
        }
        Matcher m = Pattern.compile("code=(\\w+)&state=(\\w+)&?").matcher(url);
        String authCode = "";
        String state = "";
        if (m.find()) {
            authCode = m.group(1);
            state = m.group(2);
        }

        return new String[] { authCode, state };
    }

	public AccessToken getAccessTokenByRequest(ServletRequest request)
        throws QQConnectException {

        String queryString = ((HttpServletRequest) request).getQueryString();
        if(log.isDebugEnabled()){
        	log.debug("queryString is : " + queryString);
        }
        if (queryString == null) {
            return new AccessToken();
        }
        String state = (String) ((HttpServletRequest) request).getSession()
            .getAttribute("qq_connect_state");
        if(log.isDebugEnabled()){
        	log.debug("get state is " + state);
        }
        if ((state == null) || (state.equals(""))) {
            return new AccessToken();
        }

        String[] authCodeAndState = extractionAuthCodeFromUrl(queryString);
        String returnState = authCodeAndState[1];
        String returnAuthCode = authCodeAndState[0];
        if(log.isDebugEnabled()){
        	log.debug("returnState" + returnState + ", returnAuthCode" + returnAuthCode);
        }
        if (httpOrHttps == null) {
        	httpOrHttps = (HttpOrHttpsMgr) getMgr("httpOrHttps");
        }
        //String redirect_url = "http://" + FuncUtil.getPortalDomain() + ":" + FuncUtil.getPortalServerPort() + "/portal/qqAfterLogin";
        String redirect_url = httpOrHttps.getHttpOrHttps() + "://" + FuncUtil.getPortalDomain() + "/portal/qqAfterLogin";
        if(log.isDebugEnabled()){
        	log.debug("get redirect_url is " + redirect_url);
        }

        AccessToken accessTokenObj = null;

        if ((returnState.equals("")) || (returnAuthCode.equals(""))) {
            accessTokenObj = new AccessToken();
            if(log.isDebugEnabled()){
            	log.debug("returnState is null or returnAuthCode is null");
            }
        } else if (!state.equals(returnState)) {
            accessTokenObj = new AccessToken();
            if(log.isDebugEnabled()){
            	log.debug("state is not equal returnState");
            }
        } else accessTokenObj = new AccessToken(this.client.post(
            QQConnectConfig.getValue("accessTokenURL"),
            new PostParameter[] {
                new PostParameter("client_id", QQConnectConfig
                    .getValue("app_ID")),
                new PostParameter("client_secret", QQConnectConfig
                    .getValue("app_KEY")),
                new PostParameter("grant_type", "authorization_code"),
                new PostParameter("code", returnAuthCode),
                new PostParameter("redirect_uri", redirect_url) }, Boolean
                .valueOf(false)));

        return accessTokenObj;
    }

	public AccessToken getAccessTokenByRequestTest(ServletRequest request)
        throws QQConnectException {

        String queryString = ((HttpServletRequest) request).getQueryString();
        log.info("queryString is : " + queryString);
        if (queryString == null) {
            return new AccessToken();
        }
        String state = (String) ((HttpServletRequest) request).getSession()
            .getAttribute("qq_connect_state");
        log.info("get state is " + state);
        if ((state == null) || (state.equals(""))) {
            return new AccessToken();
        }

        String[] authCodeAndState = extractionAuthCodeFromUrl(queryString);
        String returnState = authCodeAndState[1];
        String returnAuthCode = authCodeAndState[0];
        log.info("returnState" + returnState + ", returnAuthCode" + returnAuthCode);
        if (httpOrHttps == null) {
        	httpOrHttps = (HttpOrHttpsMgr) getMgr("httpOrHttps");
        }
        //String redirect_url = "http://" + FuncUtil.getPortalDomain() + ":" + FuncUtil.getPortalServerPort() + "/portal/qqAfterLogin";
        String redirect_url = httpOrHttps.getHttpOrHttps() + "://" + FuncUtil.getPortalDomain() + "/portal/userLogin";
        log.info("get redirect_url is " + redirect_url);

        AccessToken accessTokenObj = null;

        if ((returnState.equals("")) || (returnAuthCode.equals(""))) {
            accessTokenObj = new AccessToken();
            log.info("returnState is null or returnAuthCode is null");
        } else if (!state.equals(returnState)) {
            accessTokenObj = new AccessToken();
            log.info("state is not equal returnState");
        } else accessTokenObj = new AccessToken(this.client.post(
            QQConnectConfig.getValue("accessTokenURL"),
            new PostParameter[] {
                new PostParameter("client_id", QQConnectConfig
                    .getValue("app_ID")),
                new PostParameter("client_secret", QQConnectConfig
                    .getValue("app_KEY")),
                new PostParameter("grant_type", "authorization_code"),
                new PostParameter("code", returnAuthCode),
                new PostParameter("redirect_uri", redirect_url) }, Boolean
                .valueOf(false)));

        return accessTokenObj;
    }

    @SuppressWarnings("deprecation")
	public String getAuthorizeURL(ServletRequest request)
        throws QQConnectException {
    	if (cacheApiMgr == null) {
        	cacheApiMgr = (CacheApiMgr) getMgr("cacheApiMgr");
        }
    	if(protocolMgr == null){
    		protocolMgr = (ProtocolMgr)getMgr("protocolMgr");
    	}
    	// 锟斤拷取storeId
        String storeId = request.getParameterMap().get("nas_id")[0];
        String ownerId = String.valueOf(cacheApiMgr.getOwnerId(Long.valueOf(storeId), null));
        /*String ownerId = String.valueOf(cacheApiMgr.getOwnerId(
				Long.valueOf(storeId), null));*/
        // 将店铺ID和商户ID用短拼到一起
        String state = storeId + "_" + ownerId;
        ((HttpServletRequest) request).getSession().setAttribute(
            "qq_connect_state", state);
        String scope = QQConnectConfig.getValue("scope");
        if (httpOrHttps == null) {
        	httpOrHttps = (HttpOrHttpsMgr) getMgr("httpOrHttps");
        }
        //String redirect_url = "http://" + FuncUtil.getPortalDomain() + ":" + FuncUtil.getPortalServerPort() + "/portal/qqAfterLogin";
        String redirect_url = httpOrHttps.getHttpOrHttps() + "://" + FuncUtil.getPortalDomain() + "/portal/qqAfterLogin";
        if(log.isDebugEnabled()){
        	log.debug("get redirect_url is " + redirect_url);
        }
        redirect_url = URLEncoder.encode(redirect_url);
        if(log.isDebugEnabled()){
        	log.debug("get encode redirect_url is " + redirect_url);
        }
        return QQConnectConfig.getValue("authorizeURL").trim() + "?client_id="
            + QQConnectConfig.getValue("app_ID").trim() + "&redirect_uri="
            + redirect_url + "&response_type=code" + "&state=" + state
            + "&scope=" + scope;
    }

    /**
     * 获取MGR接口。
     * @param request HTTP request请求
     * @param mgrName MGR名称
     * @return bean MGR实体
     */
    private Object getMgr(String mgrName) {
        return ServerContext.getRootAppContext().getBean(mgrName);
    }
    
    public ProtocolMgr getProtocolMgr() {
		return protocolMgr;
	}

	public void setProtocolMgr(ProtocolMgr protocolMgr) {
		this.protocolMgr = protocolMgr;
	}

	public HttpOrHttpsMgr getHttpOrHttps() {
		return httpOrHttps;
	}

	public void setHttpOrHttps(HttpOrHttpsMgr httpOrHttps) {
		this.httpOrHttps = httpOrHttps;
	}

}
