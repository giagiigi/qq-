/*
 * Copyright (c) 2016, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC V300R002
 * Module Name : 
 * Date Created: 2016年2月2日
 * Creator     : donglicong
 * Description : 
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 * 2016年2月2日  donglicong        O2O-PORTAL project, new code file.
 * 
 *------------------------------------------------------------------------------
 */
package com.h3c.o2o.portal.login.entity;

import java.io.Serializable;
/**
 * RabbitMq响应<br>
 * 用JSON串交互
 * @author heqiao
 *
 */
public class WxWifiOptResponse4Json implements Serializable{
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 4666461945473314022L;
    
    /**
     * 请求类型
     */
    private String msgType;
    
    /**
     * 错误码
     */
    private String errcode;
    
    /**
     * 错误信息
     */
    private String errmsg;
    
    /**
     * 返回内容
     */
    private String repData;

    @Override
    public String toString() {
        return "WxWifiOptResponse4Json [msgType=" + msgType + ", errcode=" + errcode + ", errmsg=" + errmsg
                + ", repData=" + repData + "]";
    }

    // getter and setter
    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getRepData() {
        return repData;
    }

    public void setRepData(String repData) {
        this.repData = repData;
    }
}
