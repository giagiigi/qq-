/*
 * Copyright (c) 2003-2005 Huawei-3Com Technologies Co., Ltd. All rights
 * reserved. <http://www.huawei-3com.com/>
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
 * 仅用于校验shopId
 *
 * @author heqiao
 */
@Entity
@AccessType("field")
@Table(name = "TBL_WSM_SHOP")
public class Shop implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -5386153439038151076L;

	/** 店铺ID */
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/** 店铺名称 */
	@Column(name = "NAME")
	private String name;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}