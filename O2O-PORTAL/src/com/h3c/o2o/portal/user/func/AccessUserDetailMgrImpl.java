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
package com.h3c.o2o.portal.user.func;

import java.util.List;

import com.h3c.o2o.portal.entity.AccessDetail;
import com.h3c.o2o.portal.entity.OnlineUser;
import com.h3c.o2o.portal.user.dao.AccessUserDetailDao;

/**
 * �û�����Mgrʵ����
 *
 * @author j09980
 */
public class AccessUserDetailMgrImpl implements AccessUserDetailMgr {

	private AccessUserDetailDao accessUserDetailDao;


	@Override
	public void saveAccessDetail(AccessDetail detail) {
		accessUserDetailDao.saveAccessDetail(detail);
	}

	@Override
	public void createAccessDetail(OnlineUser user) {
		//����������ϸ��
        AccessDetail accessDetail = new AccessDetail();
        accessDetail.setStoreId(user.getStoreId());
        accessDetail.setUserId(user.getUserId());
        accessDetail.setOwnerId(user.getOwnerId());
        accessDetail.setUserName(user.getUserName());
        accessDetail.setUserType(user.getUserType());
        accessDetail.setAccessStartTime(user.getAccessStartTime());
        long time = System.currentTimeMillis();
        accessDetail.setAccessEndTime(time);
        accessDetail
            .setAccessDuration((time - user.getAccessStartTime()) / 1000);
        accessDetail.setAccessAcIp(user.getAccessAcIp());
        accessDetail.setAccessSsid(user.getAccessSsid());
        accessDetail.setInputBytes(user.getInputBytes());
        accessDetail.setOutputBytes(user.getOutputBytes());
        accessDetail.setUserIp(user.getUserIp());
        accessDetail.setUserMac(user.getUserMac());
        accessDetail.setDevManufacturer(user.getDevManufacturer());
        accessDetail.setDevOsType(user.getDevOsType());
        accessDetail.setMobileNo(user.getMobileNo());
        // ����
        saveAccessDetail(accessDetail);
	}

	@Override
	public AccessDetail queryAccessDetail(Long storeId, String ssid, String usermac,
			long accessStartTime) {
		return accessUserDetailDao.queryAccessDetail(storeId, ssid, usermac, accessStartTime);
	}


	@Override
	public Long queryAccessStartTime(long userId) {
		return accessUserDetailDao.queryAccessStartTime(userId);
	}

	@Override
	public List<AccessDetail> queryAccessUser(String identifier, int offset,
			int limit) {

		return this.accessUserDetailDao.queryAccessUser(identifier, offset, limit);
	}

	public void setAccessUserDetailDao(AccessUserDetailDao accessUserDetailDao) {
		this.accessUserDetailDao = accessUserDetailDao;
	}

}
