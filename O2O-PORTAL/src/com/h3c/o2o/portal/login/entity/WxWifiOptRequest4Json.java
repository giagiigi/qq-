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
 * RabbitMq请求<br>
 * 用JSON串交互<br>
 * @author heqiao
 *
 */
public class WxWifiOptRequest4Json implements Serializable{
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -6553890050458124998L;
    
    public static final String MSG_TYPE_DEFAULT_ACCOUNT = "GET_DEFAULT_ACCOUNT";

    /**
     * 请求类型
     */
    private String msgType;
    
    /**
     * 请求内容
     */
    private String reqData;
    
    @Override
    public String toString() {
        return "WxWifiOptRequest4Json [msgType=" + msgType + ", reqData=" + reqData + "]";
    }

    // getter and setter
    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getReqData() {
        return reqData;
    }

    public void setReqData(String reqData) {
        this.reqData = reqData;
    }
    
}
