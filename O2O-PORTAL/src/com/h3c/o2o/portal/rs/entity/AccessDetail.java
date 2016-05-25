/*
 * Copyright (c) 2015, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC V300R002
 * Module Name : 
 * Date Created: 2015-7-18
 * Creator     : j09980
 * Description : 
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 * YYYY-MM-DD  zhangshan        XXXX project, new code file.
 * 
 *------------------------------------------------------------------------------
 */
package com.h3c.o2o.portal.rs.entity;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * add description of types here
 *
 * @author j09980
 */
@XmlRootElement(name = "accessDetail")
@XmlAccessorType(XmlAccessType.FIELD)
public class AccessDetail implements Serializable {

    /**
     * add description of field here
     */
    private static final long serialVersionUID = 589577753619752538L;

    private String user_name;

    private long access_start_time;

    private long access_duration;
    
    private long access_end_time;

    private String access_ssid;

    private String user_ip;

    private String user_mac;

    private String phone_number;

    private String manufacturer;

    private String os_type;

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public long getAccess_start_time() {
        return access_start_time;
    }

    public void setAccess_start_time(long access_start_time) {
        this.access_start_time = access_start_time;
    }

    public long getAccess_duration() {
        return access_duration;
    }

    public void setAccess_duration(long access_duration) {
        this.access_duration = access_duration;
    }

    public String getAccess_ssid() {
        return access_ssid;
    }

    public void setAccess_ssid(String access_ssid) {
        this.access_ssid = access_ssid;
    }

    public String getUser_ip() {
        return user_ip;
    }

    public void setUser_ip(String user_ip) {
        this.user_ip = user_ip;
    }

    public String getUser_mac() {
        return user_mac;
    }

    public void setUser_mac(String user_mac) {
        this.user_mac = user_mac;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getOs_type() {
        return os_type;
    }

    public void setOs_type(String os_type) {
        this.os_type = os_type;
    }

    public long getAccess_end_time() {
        return access_end_time;
    }

    public void setAccess_end_time(long access_end_time) {
        this.access_end_time = access_end_time;
    }

}
