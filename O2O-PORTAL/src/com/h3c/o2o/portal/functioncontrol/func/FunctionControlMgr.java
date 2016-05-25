/*
 * Copyright (c) 2007-2015, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC V700R001
 * Module Name :
 * Date Created: 2015年10月28日
 * Creator     : dkf5133
 * Description :
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 * 2015年10月28日  dkf5133             O2O-UAM-UI project, new code file.
 *
 *------------------------------------------------------------------------------
 */

package com.h3c.o2o.portal.functioncontrol.func;

import java.util.List;

import com.h3c.o2o.portal.PortalException;
import com.h3c.o2o.portal.functioncontrol.entity.OffLineRepMsg;
import com.h3c.o2o.portal.rs.entity.ErrorInfo;

public interface FunctionControlMgr {

	/**
	 * QQ认证功能是否已打开
	 * @return
	 */
	boolean getQQAuthEnable();
	
	/** 是否是小小贝设备 */
	boolean isXXBDevice(long storeId);
	
	/**
	 * 根据Mac地址踢掉用户
	 * @param macStr
	 * @return
	 */
	public ErrorInfo logoutByMac(String macStr);
	
	/**
	 * 根据场所ID和IP地址列表踢掉用户
	 * @param macStr
	 * @return
	 */
	public OffLineRepMsg logoutByIpList(Long storeId, List<String> ipList);
	
	/**
	 * 判断用户是否已经在线<br>
	 * 如果在线抛出异常，如果切换了场所，旧用户先踢下线<br>
	 * 
	 * @param userMac
	 * @param storeId
	 * @param ssid
	 * @param userType
	 * @throws PortalException
	 */
	public void onlineCheck(String userMac, String ip, Long storeId, String ssid, Integer userType) throws PortalException;
	
	
}
