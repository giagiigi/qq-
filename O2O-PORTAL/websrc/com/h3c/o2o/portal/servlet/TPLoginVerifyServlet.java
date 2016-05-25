/*
 * Copyright (c) 2007-2013, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC v7
 * Module Name : iMC
 * Date Created: 2015-7-10
 * Creator     : dkf5133
 * Description :
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 * 2015-7-10     dkf5133          iMC project, new code file.
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

import com.h3c.o2o.portal.PortalException;
import com.h3c.o2o.portal.common.FuncUtil;
import com.h3c.o2o.portal.entity.RegistUser;
import com.h3c.o2o.portal.protocol.entity.TpLoginInfo;
import com.h3c.o2o.portal.protocol.func.ProtocolMgrImpl;
import com.h3c.o2o.portal.util.WifiUtils;

/**
 * 第三方登录回调接口
 * @author dkf5133
 *
 */
public class TPLoginVerifyServlet extends HttpServlet {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -8460349869994633635L;
    
    private Log log = LogFactory.getLog(getClass());

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res)
        throws IOException {
        this.doPost(req, res);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res)
        throws IOException {
        TpLoginInfo info = new TpLoginInfo();
        info.setOpenId(req.getParameter("openId"));
        int type = Integer.valueOf(req.getParameter("type"));
        info.setOwnerId(Long.valueOf(req.getParameter("ownerId")));
        info.setStoreId(Long.valueOf(req.getParameter("storeId")));
        info.setType(type);
        info.setRedirect_uri(WifiUtils.safeDecode(req.getParameter("redirect_uri")));
        info.setSsid(WifiUtils.safeDecode(req.getParameter("ssid")));
        info.setUserMac(WifiUtils.safeDecode(req.getParameter("usermac")));
        info.setUserIp(req.getParameter("userIp"));
        info.setExtInfo(WifiUtils.safeDecode(req.getParameter("extInfo")));
        
        try {
            res.sendRedirect(ProtocolMgrImpl.get().tpLoginVerify(info));
        } catch (PortalException e) {
        	log.warn(null, e);
        	if(type == RegistUser.USER_TYPE_WX_WIFI){
        		// 如果是微信连Wifi认证，返回400，而不是重定向到Error页面，否则会返回200，提示连接成功
        		res.sendError(HttpServletResponse.SC_BAD_REQUEST, "PortalException: [ code: " + e.getErrorCode() 
        			+ " msg: " + e.getErrorMsg());
        	} else {
        		res.sendRedirect(FuncUtil.getErrorPageURI(e.getErrorCode()));
        	}
        } catch (Exception e){
        	log.warn(null, e);
        	res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error msg: " + e.getMessage());
        }
    }

}
