/*
 * Copyright (c) 2007-2013, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC v7
 * Module Name : iMC
 * Date Created: 2015-6-23
 * Creator     : dkf5133
 * Description :
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 * 2015-6-23     dkf5133          iMC project, new code file.
 *
 *------------------------------------------------------------------------------
 */
package com.h3c.o2o.portal.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.AccessType;

/**
 * @author hkf6496
 *
 */
@Entity
@Table(name = "TBL_UAM_WXWIFI_TEMPLATE")
@AccessType("field")
public class WXWifiTemplate implements Serializable {
	/**
     * add description of field here
     */
    private static final long serialVersionUID = 7451166324942721459L;

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    /**主键*/
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**商户ID*/
    @Column(name = "OWNER_ID")
    private Long ownerId;

    /***/
    @Column(name = "SHOP_ID")
    private Long shopId;

    /**主题名称*/
    @Column(name = "TEMPLATE_NAME")
    private String templateName;

    /**路径*/
    @Column(name = "PATH_NAME")
    public String pathName;

    /**文件名称*/
    @Column(name = "FILE_NAME")
    public String fileName;

    /**页面内容*/
    @Column(name = "PAGE_HTML")
    public String pageHtml;

    /**页面配置信息*/
    @Column(name = "PAGE_CFG")
    public String pageCfg;

    /**描述*/
    @Column(name = "DESCRIPTION")
	private String description;

    /**创建时间*/
    @Column(name = "CREATETIME")
	private Long createTime;

    /**最近一次修改时间*/
    @Column(name = "LAST_MOD_TIME")
    public Long lastModTime;

    public Long getCreateTime() {
        return createTime;
    }

    public String getDescription() {
        return description;
    }

    public String getFileName() {
        return fileName;
    }

    public Long getId() {
        return id;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public String getPageCfg() {
        return pageCfg;
    }

    public String getPageHtml() {
        return pageHtml;
    }

    public String getPathName() {
        return pathName;
    }

    public Long getShopId() {
        return shopId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public void setPageCfg(String pageCfg) {
        this.pageCfg = pageCfg;
    }

    public void setPageHtml(String pageHtml) {
        this.pageHtml = pageHtml;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }
    
    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    
    public Long getLastModTime() {
        return lastModTime;
    }

    public void setLastModTime(Long lastModTime) {
        this.lastModTime = lastModTime;
    }

}
