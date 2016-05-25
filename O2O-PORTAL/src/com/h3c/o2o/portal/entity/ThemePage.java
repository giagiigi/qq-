/*
 * Copyright (c) 2007-2015, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC V700R001
 * Module Name :
 * Date Created: 2015-6-19
 * Creator     : ykf2685
 * Description :
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 * 2015-6-19  ykf2685             O2O-PORTAL project, new code file.
 *
 *------------------------------------------------------------------------------
 */

package com.h3c.o2o.portal.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.AccessType;

/**
 * 定制页面子表。
 *
 * @author ykf2685
 *
 */
@Entity
@Table(name = "TBL_UAM_THEME_PAGE")
@AccessType("field")
public class ThemePage implements Serializable{

    /**
     * 序列化id
     */
    private static final long serialVersionUID = 1L;

    public static int PAGE_TYPE_QUICKLOGIN = 1;
    public static int PAGE_TYPE_LOGIN = 2;
    public static int PAGE_TYPE_LOGINSUCCESS = 3;
    public static int PAGE_TYPE_HOME = 4;

    /**主键*/
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    /**页面类型*/
    @Column(name = "PAGE_TYPE", nullable = false)
    public Integer pageType;

    /** 主表*/
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TEMPLATE_ID")
    private ThemeTemplate template;

    /**路径*/
    @Column(name = "PATH_NAME", length = 256, nullable = false)
    public String pathName;

    /**文件名称*/
    @Column(name = "FILE_NAME", length = 256, nullable = false)
    public String fileName;

    /**页面内容*/
    @Column(name = "PAGE_HTML", length = 8000, nullable = false)
    public String pageHtml;

    /**页面配置信息*/
    @Column(name = "PAGE_CFG", length = 1024, nullable = true)
    public String pageCfg;

    /**最近一次修改时间*/
    @Column(name = "LAST_MOD_TIME", nullable = false)
    public Long lastModTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPageType() {
        return pageType;
    }

    public void setPageType(Integer pageType) {
        this.pageType = pageType;
    }

    public String getPathName() {
        return pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPageHtml() {
        return pageHtml;
    }

    public void setPageHtml(String pageHtml) {
        this.pageHtml = pageHtml;
    }

    public String getPageCfg() {
        return pageCfg;
    }

    public void setPageCfg(String pageCfg) {
        this.pageCfg = pageCfg;
    }

    public Long getLastModTime() {
        return lastModTime;
    }

    public void setLastModTime(Long lastModTime) {
        this.lastModTime = lastModTime;
    }

    public ThemeTemplate getTemplate() {
        return template;
    }

    public void setTemplate(ThemeTemplate template) {
        this.template = template;
    }

}
