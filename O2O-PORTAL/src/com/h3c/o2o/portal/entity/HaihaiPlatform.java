package com.h3c.o2o.portal.entity;

/*
 * Copyright (c) 2007-2013, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC V500R003
 * Module Name : iMC
 * Date Created: 2015-6-20
 * Creator     : wkf4532
 * Description : MAM EMO ͼƬʵ��
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 * 2015-6-20   wkf4532          MAM project, new code file.
 *
 *------------------------------------------------------------------------------
 */

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.AccessType;

/**
 * ��������ʵ����
 * @author d12150
 *
 */
@Entity
@AccessType("field")
@Table(name = "TBL_UAM_HAIHAI_PLATFORM")
public class HaihaiPlatform implements Serializable {

    /**
     * ���л�id
     */
    private static final long serialVersionUID = 1L;

    /** ����*/
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /** appID*/
    @Column(name = "APP_ID")
    private Long appId;

    /** B��URL*/
    @Column(name = "BUSSINESS_URL")
    private String bussinessUrl;

    /** c��URL*/
    @Column(name = "CUSTOMER_URL")
    private String customerUrl;

    /** �̻�*/
    @Column(name = "MECHANT_ID")
    private Long mechantId;

    /** ΢��I���ں�*/
    @Column(name = "WEIXIN_ID")
    private Long weixinId;

    /** ����ƽ̨api��Ȩ�� */
    @Column(name = "ACCESSTOKEN")
    private String accesstoken;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getBussinessUrl() {
        return bussinessUrl;
    }

    public void setBussinessUrl(String bussinessUrl) {
        this.bussinessUrl = bussinessUrl;
    }

    public String getCustomerUrl() {
        return customerUrl;
    }

    public void setCustomerUrl(String customerUrl) {
        this.customerUrl = customerUrl;
    }

    public Long getMechantId() {
        return mechantId;
    }

    public void setMechantId(Long mechantId) {
        this.mechantId = mechantId;
    }

    public Long getWeixinId() {
        return weixinId;
    }

    public void setWeixinId(Long weixinId) {
        this.weixinId = weixinId;
    }

    public String getAccesstoken() {
        return accesstoken;
    }

    public void setAccesstoken(String accesstoken) {
        this.accesstoken = accesstoken;
    }
}