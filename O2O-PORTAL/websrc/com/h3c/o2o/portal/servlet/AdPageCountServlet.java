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
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.h3c.o2o.portal.admanage.func.AdCountMgr;
import com.h3c.o2o.portal.admanage.func.AdStatistictMgr;
import com.h3c.o2o.portal.entity.Advertisement;
import com.h3c.o2o.portal.entity.AdvertisementPageTime;
import com.h3c.oasis.o2oserver.bootstrap.ServerContext;

import net.sf.json.JSONObject;

public class AdPageCountServlet extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /** ��־���� */
    private Log log = LogFactory.getLog(getClass());
    
    private AdCountMgr adCountMgr;

    private AdStatistictMgr adStatistictMgr;
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            this.doPost(req, resp);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        AdvertisementPageTime adpt = new AdvertisementPageTime();

        // �������ҳ������
        long visitTime = Long.parseLong(req.getParameter("visitTime"));
        int pageType = Integer.parseInt(req.getParameter("pageType"));
        long templateId = Long.parseLong(req.getParameter("templateId"));
        long storeId = Long.parseLong(req.getParameter("storeId"));
        if (log.isDebugEnabled()) {
            log.debug("Current URI: " + req.getRequestURI());
            for (String key : req.getParameterMap().keySet()) {
                log.debug("Parameter: " + key + " ~ value: "
                    + req.getParameter(key));
            }
        }
        
        adpt.setTypeId(pageType);
        adpt.setTempId(templateId);
        adpt.setVisitTime(visitTime);
        adpt.setStoreId(storeId);
        
        if (null == adCountMgr) {
            adCountMgr = (AdCountMgr) getMgr("adCountMgr");
        }

        // ����������ҳ��ͳ������
        adCountMgr.saveAdCount(adpt);

        if (null == adStatistictMgr) {
            adStatistictMgr = (AdStatistictMgr) getMgr("adStatistictMgr");
        }
        // ȡ�ù���б�
        List<Advertisement>  adList = adStatistictMgr.queryAdList(templateId, pageType);
        
        JSONObject result = new JSONObject();
        result.put("adList", adList);
        resp.setContentType("text/json; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(result.toString());
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
