/*
 * Copyright (c) 2016, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC V300R002
 * Module Name : 
 * Date Created: 2016��2��2��
 * Creator     : donglicong
 * Description : 
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 * 2016��2��2��  donglicong        O2O-PORTAL project, new code file.
 * 
 *------------------------------------------------------------------------------
 */
package com.h3c.o2o.portal.login.entity;

import java.io.Serializable;
/**
 * RabbitMq��Ӧ<br>
 * ��JSON������
 * @author heqiao
 *
 */
public class WxWifiOptResponse4Json implements Serializable{
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 4666461945473314022L;
    
    /**
     * ��������
     */
    private String msgType;
    
    /**
     * ������
     */
    private String errcode;
    
    /**
     * ������Ϣ
     */
    private String errmsg;
    
    /**
     * ��������
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
