/*
 * Copyright (c) 2007-2015, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC V700R001
 * Module Name :
 * Date Created: 2015年9月29日
 * Creator     : dkf5133
 * Description :
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 * 2015年9月29日  dkf5133         O2O-PORTAL project, new code file.
 *
 *------------------------------------------------------------------------------
 */

package com.h3c.o2o.portal.rs.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.AccessType;

@Entity
@Table(name = "TBL_UAM_TP_STRATEGY")
@AccessType("field")
public class ThirdPartyStrategy implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -1294261836009023958L;

	public static final int STRATEGY_TYPE_BLACK_LIST = 1;

	public static final int STRATEGY_TYPE_WHITE_LIST = 2;

	/** ID */
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /** MAC地址 */
    @Column(name = "MAC")
    private String mac;

    /** 类型, 1：黑名单；2：白名单 */
    @Column(name = "TYPE")
    private Integer type;

    /** 创建时间 */
    @Column(name = "CREATE_TIME")
    private Long createTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "ThirdPartyBlackList [id=" + id + ", mac=" + mac + ", type="
				+ type + ", createTime=" + createTime + "]";
	}

}
