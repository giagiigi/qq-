/*
 * Copyright (c) 2007-2015, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC V700R001
 * Module Name :
 * Date Created: 2015��10��28��
 * Creator     : dkf5133
 * Description :
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 * 2015��10��28��  dkf5133             O2O-UAM-UI project, new code file.
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
	 * QQ��֤�����Ƿ��Ѵ�
	 * @return
	 */
	boolean getQQAuthEnable();
	
	/** �Ƿ���СС���豸 */
	boolean isXXBDevice(long storeId);
	
	/**
	 * ����Mac��ַ�ߵ��û�
	 * @param macStr
	 * @return
	 */
	public ErrorInfo logoutByMac(String macStr);
	
	/**
	 * ���ݳ���ID��IP��ַ�б��ߵ��û�
	 * @param macStr
	 * @return
	 */
	public OffLineRepMsg logoutByIpList(Long storeId, List<String> ipList);
	
	/**
	 * �ж��û��Ƿ��Ѿ�����<br>
	 * ��������׳��쳣������л��˳��������û���������<br>
	 * 
	 * @param userMac
	 * @param storeId
	 * @param ssid
	 * @param userType
	 * @throws PortalException
	 */
	public void onlineCheck(String userMac, String ip, Long storeId, String ssid, Integer userType) throws PortalException;
	
	
}
