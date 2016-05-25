/*
 * Copyright (c) 2016, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC V300R002
 * Module Name : 
 * Date Created: 2016年1月20日
 * Creator     : donglicong
 * Description : 
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 * 2016年1月20日  donglicong        O2O-PORTAL project, new code file.
 * 
 *------------------------------------------------------------------------------
 */
package com.h3c.o2o.portal.functioncontrol.entity;

/**
 * 功能控制实体
 *
 * @author donglicong
 */
public class FunctionControl {

    /** 是否启用QQ认证 */
    private boolean isEnableQQ;
    
    /** 是否启用QQ认证 */
    private boolean isXXBDev;
    
    private String error;

    public boolean isEnableQQ() {
        return isEnableQQ;
    }

    public boolean isXXBDev() {
        return isXXBDev;
    }

    public void setXXBDev(boolean isXXBDev) {
        this.isXXBDev = isXXBDev;
    }

    public void setEnableQQ(boolean isEnableQQ) {
        this.isEnableQQ = isEnableQQ;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "FunctionControl [isEnableQQ=" + isEnableQQ + ", isXXBDev="
            + isXXBDev + ", error=" + error + "]";
    }
    
}
