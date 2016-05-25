/*
 * Copyright (c) 2007-2015, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC V700R001
 * Module Name :
 * Date Created: 2015-7-10
 * Creator     : ykf2685
 * Description :
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 * 2015-7-10  ykf2685             O2O-PORTAL project, new code file.
 *
 *------------------------------------------------------------------------------
 */

package com.h3c.o2o.portal.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.AccessType;

/**
 * 指纹。
 *
 * @author ykf2685
 *
 */
@Entity
@AccessType("field")
@Table(name = "TBL_UAM_USERAGENT")
public class UserAgent implements Serializable {

    /**
     * 序列化ID
     */
    private static final long serialVersionUID = 4931565693909306482L;

    /**
     * 记录表的主键。

     */
    @Id
    @Column(name = "ID")
    private Long id;

    /**
     * USER AGENT 指纹
     */
    @Column(name = "NAME")
    private String name;

    /**
     * 厂商
     */
    @Column(name = "TERMINAL_VENDOR")
    private String terminalVendor;

    /**
     * 终端类型
     */
    @Column(name = "TERMINAL_TYPE")
    private String terminalType;

    /**
     * 操作系统类型
     */
    @Column(name = "TERMINAL_OS")
    private String terminalOs;

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

    public String getTerminalVendor() {
        return terminalVendor;
    }

    public void setTerminalVendor(String terminalVendor) {
        this.terminalVendor = terminalVendor;
    }

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    public String getTerminalOs() {
        return terminalOs;
    }

    public void setTerminalOs(String terminalOs) {
        this.terminalOs = terminalOs;
    }

    @Override
    public String toString() {
        return "UserAgent [id=" + id + ", name=" + name + ", terminalVendor="
            + terminalVendor + ", terminalType=" + terminalType
            + ", terminalOs=" + terminalOs + "]";
    }

}
