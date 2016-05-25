/*
 * Copyright (c) 2016, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC V300R002
 * Module Name : 
 * Date Created: 2016年1月15日
 * Creator     : donglicong
 * Description : 
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 * 2016年1月15日  donglicong        O2O-PORTAL project, new code file.
 * 
 *------------------------------------------------------------------------------
 */
package com.h3c.o2o.portal.shopwifi.func;

import com.h3c.o2o.portal.entity.ShopWifiInfo;

/**
 * add description of types here
 *
 * @author donglicong
 */
public interface ShopWifiMgr {

    ShopWifiInfo queryShopWifiInfo(long nasId);
}
