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
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OrderBy;

/**
 * @author dkf5133
 *
 */
@Entity
@Table(name = "TBL_UAM_THEME_TEMPLATE")
@AccessType("field")
public class ThemeTemplate implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -2467733184760029584L;

	public static int IS_ENABLE_TRUE = 1;
	public static int IS_ENABLE_FALSE = 0;

	/**主键*/
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**店铺ID*/
    @Column(name = "OWNER_ID", nullable = false)
    private Long ownerId;

    /**主题名称*/
    @Column(name = "THEME_NAME", length = 64, nullable = false)
    private String themeName;

    /**此处排序是按照类型排序的，方便获取每个定制页面的记录*/
    @SuppressWarnings("deprecation")
    @OneToMany(mappedBy = "template", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    @OrderBy(clause="pageType")
    private List<ThemePage> pages = new LinkedList<ThemePage>();

    /**描述*/
    @Column(name = "DESCRIPTION", length = 256)
	private String description;

    /**创建时间*/
    @Column(name = "CREATE_TIME", nullable = false)
	private Long createTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public String getThemeName() {
		return themeName;
	}

	public void setThemeName(String themeName) {
		this.themeName = themeName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

    public List<ThemePage> getPages() {
        return pages;
    }

    public void setPages(List<ThemePage> pages) {
        this.pages = pages;
    }

	@Override
	public String toString() {
		return "ThemeTemplate [id=" + id + ", ownerId=" + ownerId
				+ ", themeName=" + themeName + ", pages=" + pages
				+ ", description=" + description + ", createTime=" + createTime
				+ "]";
	}

}
