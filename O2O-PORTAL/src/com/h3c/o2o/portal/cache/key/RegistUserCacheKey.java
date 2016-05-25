/*
 * Copyright (c) 2015, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC V300R002
 * Module Name : 
 * Date Created: 2015-8-3
 * Creator     : dkf5133
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
package com.h3c.o2o.portal.cache.key;

import java.io.Serializable;

/**
 * 注册用户缓存数据key
 *
 * @author dkf5133
 */
public class RegistUserCacheKey implements Serializable {
    /**
     * add description of field here
     */
    private static final long serialVersionUID = -1435625944679843970L;
    
    /** constructor */
    public RegistUserCacheKey() {
        
    }
    
    /** constructor */
    public RegistUserCacheKey(Long storeId, String userName, Integer userType) {
        this.storeId = storeId;
        this.userName = userName;
        this.userType = userType;
    }
    
    /** 店铺ID */
    private Long storeId;
    /** 用户名称 */
    private String userName;
    /** 用户类型 */
    private Integer userType;
    
    public Long getStoreId() {
        return storeId;
    }
    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public Integer getUserType() {
        return userType;
    }
    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((storeId == null) ? 0 : storeId.hashCode());
        result = prime * result
            + ((userName == null) ? 0 : userName.hashCode());
        result = prime * result
            + ((userType == null) ? 0 : userType.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RegistUserCacheKey other = (RegistUserCacheKey) obj;
        if (storeId == null) {
            if (other.storeId != null)
                return false;
        } else if (!storeId.equals(other.storeId))
            return false;
        if (userName == null) {
            if (other.userName != null)
                return false;
        } else if (!userName.equals(other.userName))
            return false;
        if (userType == null) {
            if (other.userType != null)
                return false;
        } else if (!userType.equals(other.userType))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "RegistUserCacheKey [storeId=" + storeId + ", userName="
            + userName + ", userType=" + userType + "]";
    }
    
}
