/*
 * Copyright (c) 2016, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC V300R002
 * Module Name : 
 * Date Created: 2016年2月15日
 * Creator     : donglicong
 * Description : 
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 * 2016年2月15日  donglicong        O2O-PORTAL project, new code file.
 * 
 *------------------------------------------------------------------------------
 */
package com.h3c.o2o.portal.functioncontrol.entity;

/**
 * add description of types here
 *
 * @author donglicong
 */
public class XxbDevMq {

    private Long shopId;
    
    private Boolean isXxbDev;

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Boolean getIsXxbDev() {
        return isXxbDev;
    }

    public void setIsXxbDev(Boolean isXxbDev) {
        this.isXxbDev = isXxbDev;
    }

    @Override
    public String toString() {
        return "XxbDevMq [shopId=" + shopId + ", isXxbDev=" + isXxbDev + "]";
    }
    
}
