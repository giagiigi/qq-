/*
 * Copyright (c) 2007-2015, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC V700R001
 * Module Name :
 * Date Created: 2015-7-7
 * Creator     : ykf2685
 * Description :
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 * 2015-7-7  ykf2685             O2O-PORTAL project, new code file.
 *
 *------------------------------------------------------------------------------
 */

package com.h3c.o2o.portal.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.h3c.o2o.portal.entity.RegistUser;
import com.h3c.o2o.portal.protocol.entity.TpLoginInfo;
import com.h3c.o2o.portal.protocol.func.ProtocolMgr;
import com.h3c.o2o.portal.qq.QqOauth;
import com.h3c.oasis.o2oserver.bootstrap.ServerContext;
import com.qq.connect.api.OpenID;
import com.qq.connect.javabeans.AccessToken;

import net.sf.json.JSONObject;

/**
 * qq登录后的处理逻辑。
 *
 * @author ykf2685
 *
 */
public class QqAfterLoginServlet extends HttpServlet {

    /**
     * 序列化id
     */
    private static final long serialVersionUID = 1L;

    private ProtocolMgr protocolMgr;

    private Log log = LogFactory.getLog(getClass());

    /**
     * DOGET 方法
     *
     * @param req request请求对象
     * @param resp response返回对象
     * @throws IOException IO异常
     * @throws ServletException Servlet异常
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        this.doPost(req, resp);
    }

    /**
     * DOPOST 方法
     *
     * @param req request请求对象
     * @param resp response返回对象
     * @throws IOException IO异常
     * @th
     * rows ServletException Servlet异常
     */
    @Override
    protected void doPost(HttpServletRequest request,
        HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=utf-8");

        try {
        	// 获取storeId
        	String queryString = ((HttpServletRequest) request).getQueryString();
        	String returnState[] = new QqOauth().extractionAuthCodeFromUrl(queryString);
        	// 店铺ID和商户ID以逗号分隔保存,@see com.h3c.o2o.portal.qq.QqOauth.getAuthorizeURL
        	String[] storeAndOwner = returnState[1].split("_");
        	String storeId = storeAndOwner[0];
        	String ownerId = storeAndOwner[1];
            if(log.isDebugEnabled()){
            	log.debug("get storeId is " + storeId);
            }

            AccessToken accessTokenObj = (new QqOauth())
                .getAccessTokenByRequest(request);

            String accessToken = null, openID = null;
            long tokenExpireIn = 0L;

            if (null == accessTokenObj
                || accessTokenObj.getAccessToken().equals("")) {
                log.warn("qq innernet error.");
            } else {
                accessToken = accessTokenObj.getAccessToken();
                tokenExpireIn = accessTokenObj.getExpireIn();
             // 利用获取到的accessToken 去获取当前用的openid
                OpenID openIDObj = new OpenID(accessToken);
                openID = openIDObj.getUserOpenID();

                // 设置用户名称
                JSONObject json = new JSONObject();
                json.put("accessToken", accessToken);
                json.put("tokenExpireIn", tokenExpireIn);
                request.setAttribute("userExtInfo", json.toString());

                if(log.isDebugEnabled()){
                	log.debug("param info : openID:" + openID
                			+ ", storeId:" + storeId
                			+ ", ownerId:" + ownerId
                            + ", userExtInfo:" + json.toString());
                }

             // 调用qq登录逻辑
                if (null == protocolMgr) {
                    protocolMgr = (ProtocolMgr) getMgr("protocolMgr");
                }
                TpLoginInfo info = new TpLoginInfo();
                info.setOpenId(openID);
                info.setType(RegistUser.USER_TYPE_QQ);
                info.setStoreId(Long.valueOf(storeId));
                info.setExtInfo(json.toString());
                info.setOwnerId(Long.valueOf(ownerId));
                String url = protocolMgr.tpLoginVerify(info);
                if(log.isDebugEnabled()){
                	log.debug("tpLoginVerify url : " + url);
                }
                response.sendRedirect(url);
            }
        } catch (Exception e) {
            log.warn("", e);
        }
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

}
