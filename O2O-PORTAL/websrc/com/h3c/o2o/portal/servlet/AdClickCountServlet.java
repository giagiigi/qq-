/*
 * Copyright (c) 2007-2015, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC V700R001
 * Module Name :
 * Date Created: 2015年10月28日
 * Creator     : dkf5133
 * Description :
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 * 2015年10月28日  dkf5133             O2O-UAM-UI project, new code file.
 *
 *------------------------------------------------------------------------------
 */

package com.h3c.o2o.portal.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.h3c.o2o.portal.admanage.func.AdStatistictMgr;
import com.h3c.o2o.portal.entity.Advertisement;
import com.h3c.o2o.portal.entity.AdvertisementTime;
import com.h3c.oasis.o2oserver.bootstrap.ServerContext;

public class AdClickCountServlet extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /** 记录运行时日志对象。 */
    private Log log = LogFactory.getLog(getClass());
    
    // 点击次数为1
    private final int CLICK_TIME_ONE = 1;

    private AdStatistictMgr adStatistictMgr;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        AdvertisementTime adTime = new AdvertisementTime();

        // 广告所在页面数据
        long clickTime = Long.parseLong(req.getParameter("clickTime"));
        int pageType = Integer.parseInt(req.getParameter("pageType"));
        long templateId = Long.parseLong(req.getParameter("templateId"));
        long adId =  Long.parseLong(req.getParameter("adId"));
        int adPu = Integer.parseInt(req.getParameter("uv"));
        long storeId = Long.parseLong(req.getParameter("storeId"));
        
        if (log.isDebugEnabled()) {
            log.debug("Current URI: " + req.getRequestURI());
            for (String key : req.getParameterMap().keySet()) {
                log.debug("Parameter: " + key + " ~ value: "
                    + req.getParameter(key));
            }
        }

        if (null == adStatistictMgr) {
            adStatistictMgr = (AdStatistictMgr) getMgr("adStatistictMgr");
        }

        // 取得广告Id
        Advertisement ad = adStatistictMgr.findAdById(adId);
        
        if (null == ad) {
            // 数据库中没有
            log.warn("there isn't ad wehre templateId = " + templateId + "imageId="
                    + adId + "pageType" + pageType + " in the database.");
            return;
        }
        adTime.setAdvertisementId(ad.getId());
        adTime.setClickTime(clickTime);
        adTime.setVisitTime(clickTime);
        adTime.setAdPu(adPu);
        adTime.setClick(CLICK_TIME_ONE);
        adTime.setStoreId(storeId);
        // TODO
        adTime.setMac("");
        adStatistictMgr.saveAdCount(adTime);
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
