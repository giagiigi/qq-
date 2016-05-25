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

import com.h3c.o2o.portal.qq.QqOauth;
import com.qq.connect.api.OpenID;
import com.qq.connect.api.qzone.UserInfo;
import com.qq.connect.javabeans.AccessToken;
import com.qq.connect.javabeans.qzone.UserInfoBean;

/**
 * qq��¼��Ĵ����߼���
 *
 * @author ykf2685
 *
 */
public class UserLoginServlet extends HttpServlet {

    /**
     * ���л�id
     */
    private static final long serialVersionUID = 1L;

    private Log log = LogFactory.getLog(getClass());

    /**
     * DOGET ����
     *
     * @param req request�������
     * @param resp response���ض���
     * @throws IOException IO�쳣
     * @throws ServletException Servlet�쳣
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        this.doPost(req, resp);
    }

    /**
     * DOPOST ����
     *
     * @param req request�������
     * @param resp response���ض���
     * @throws IOException IO�쳣
     * @throws ServletException Servlet�쳣
     */
    @Override
    protected void doPost(HttpServletRequest request,
        HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=utf-8");

        try {
        	// ��ȡstoreId
        	String queryString = ((HttpServletRequest) request).getQueryString();
        	String returnState[] = new QqOauth().extractionAuthCodeFromUrl(queryString);
        	String storeId = returnState[1];
            log.info("get storeId is " + storeId);

            AccessToken accessTokenObj = (new QqOauth())
                .getAccessTokenByRequestTest(request);

            String accessToken = null, openID = null;
            if (null == accessTokenObj
                || accessTokenObj.getAccessToken().equals("")) {
                log.warn("qq innernet error.");
            }
            else {
                accessToken = accessTokenObj.getAccessToken();
                // ���û�ȡ����accessToken ȥ��ȡ��ǰ�õ�openid
                OpenID openIDObj = new OpenID(accessToken);
                openID = openIDObj.getUserOpenID();
                UserInfo userinfo = new UserInfo(accessToken, openID);
                UserInfoBean userinfobean = userinfo.getUserInfo();
                request.setAttribute("userinfobean",userinfobean);

/*              request.setAttribute("nickname","С����");
                request.setAttribute("gender","F");
                //request.setAttribute("avatar","");
                request.setAttribute("level",11);
                request.setAttribute("message","message");
                request.setAttribute("ret",12);
                request.setAttribute("vip",true);
                request.setAttribute("yellowYearVip",false);

                request.setAttribute("avatarURL30","avatarURL30");
                request.setAttribute("avatarURL50","avatarURL50");
                request.setAttribute("avatarURL100","avatarURL100");*/
                request.getRequestDispatcher("loginsuccess.jsp").forward(request, response);
            }
        } catch (Exception e) {
            log.warn("", e);
        }
    }

}
