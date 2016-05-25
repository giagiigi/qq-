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
import com.h3c.o2o.portal.entity.AdvertisementTime;
import com.h3c.oasis.o2oserver.bootstrap.ServerContext;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class AdCountServlet extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /** 日志对象。 */
    private Log log = LogFactory.getLog(getClass());
    
    // 点击次数为1
    private final int CLICK_TIME_ZERO = 0;

    private AdStatistictMgr adStatistictMgr;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        AdvertisementTime adTime = new AdvertisementTime();

        // 广告所在页面数据
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
        
        String adString = req.getParameter("adJson");
        
        JSONArray array = JSONArray.fromObject(adString);
        for (Object arr :array) {
           JSONObject object = JSONObject.fromObject(arr);
           long visitTime = Long.parseLong(object.get("visitTime").toString());
           long adId = Long.parseLong(object.get("adId").toString());
           int adPu = Integer.parseInt(object.get("uv").toString());
     
           adTime.setAdvertisementId(adId);
           adTime.setVisitTime(visitTime);
           adTime.setAdPu(adPu);
           adTime.setClick(CLICK_TIME_ZERO);
           adTime.setStoreId(storeId);
           // TODO
           adTime.setMac("");
           // 保存广告展示次数
           adStatistictMgr.saveAdCount(adTime);
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
