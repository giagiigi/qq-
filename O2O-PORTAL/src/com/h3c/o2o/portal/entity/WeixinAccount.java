/*
 * Copyright (c) 2016, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC V300R002
 * Module Name : 
 * Date Created: 2016��1��15��
 * Creator     : donglicong
 * Description : 
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 * 2016��1��15��  donglicong        O2O-PORTAL project, new code file.
 * 
 *------------------------------------------------------------------------------
 */
package com.h3c.o2o.portal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.AccessType;

/**
 * ΢�Ź��ں�ʵ��
 *
 * @author donglicong
 */
@Entity
@Table(name = "TBL_UAM_WEIXIN_ACCOUT")
@AccessType("field")
public class WeixinAccount {

    /** ����*/
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /** ���ں�ID */
    @Column(name = "APP_ID")
    private String appId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

}
