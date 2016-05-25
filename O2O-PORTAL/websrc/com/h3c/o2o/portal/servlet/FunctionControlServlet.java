/*
 * Copyright (c) 2007-2015, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC V700R001
 * Module Name :
 * Date Created: 2015��10��28��
 * Creator     : dkf5133
 * Description :
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 * 2015��10��28��  dkf5133             O2O-UAM-UI project, new code file.
 *
 *------------------------------------------------------------------------------
 */

package com.h3c.o2o.portal.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.h3c.o2o.portal.functioncontrol.entity.FunctionControl;
import com.h3c.o2o.portal.functioncontrol.func.FunctionControlMgrImpl;

public class FunctionControlServlet extends HttpServlet {

	/**
	 *
	 */
	private static final long serialVersionUID = 4343210986008181885L;
	/** ��¼����ʱ��־���� */
    private Log log = LogFactory.getLog(getClass());

	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        this.doPost(req, resp);
    }

	@Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
	    FunctionControl fc = new FunctionControl();
	    resp.setContentType("text/json; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
	    try {
	        // ����QQ
    	    fc.setEnableQQ(FunctionControlMgrImpl.getInstance().getQQAuthEnable());
    	    // ����΢����wifi
    	    long storeId;
    		if (StringUtils.isNotBlank(req.getParameter("storeId"))) {
    		    storeId = Long.valueOf(req.getParameter("storeId"));
    		    fc.setXXBDev(FunctionControlMgrImpl.getInstance().isXXBDevice(storeId));
    		    if (log.isDebugEnabled()) {
    		        log.debug("Store id:" + storeId);
    		    }
    		} else {
    		    fc.setXXBDev(false);
    		    if (log.isDebugEnabled()) {
                    log.debug("Store id is null");
                }
    		}
		} catch (NumberFormatException e) {
		    log.warn("Store id must be digital.", e);
		    fc.setError("Store id must be digital.");
		} catch (Exception e) {
			log.warn("Internal server error.", e);
			fc.setError("Internal server error.");
		}
	    // ��Ӧ��Ϣ
        try {
            resp.getWriter().write(new Gson().toJson(fc));
        } catch (IOException e) {
            log.warn("Internal server error.", e);
        }
    }
}
