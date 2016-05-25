/*
 * Copyright (c) 2007-2015, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC V700R001
 * Module Name :
 * Date Created: 2015-7-14
 * Creator     : ykf2685
 * Description :
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 * 2015-7-14  ykf2685             O2O-PORTAL project, new code file.
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

/**
 * qq��¼ǰ����
 *
 * @author ykf2685
 *
 */
public class QqLoginServlet extends HttpServlet {

    /**
     * ����id
     */
    private static final long serialVersionUID = 1L;

    /** log */
    private static Log log = LogFactory.getLog(QqAfterLoginServlet.class);

    /**
     * DOGET ����
     *
     * @param req request�������
     * @param resp response���ض���
     * @throws IOException IO�쳣
     * @throws ServletException Servlet�쳣
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    /**
     * DOGET ����
     *
     * @param req request�������
     * @param resp response���ض���
     * @throws IOException IO�쳣
     * @throws ServletException Servlet�쳣
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=utf-8");

        try {
        	// ��ȡstoreId
            String storeId = request.getParameterMap().get("nas_id")[0];
            if(log.isDebugEnabled()){
            	log.debug("get storeId is " + storeId);
            }

            String url = (new QqOauth()).getAuthorizeURL(request);
            if(log.isDebugEnabled()){
            	log.debug("qq redirect url is " + url);
            }

         // �ض���url
            response.getWriter().write(url);
        } catch (Exception e) {
            log.warn("", e);
            response.getWriter().write("error");
        }
    }


}
