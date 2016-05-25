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

/**
 * 用户管理Mgr
 *
 * @author j09980
 */
public interface AccessUserDetailMgr {


	void saveAccessDetail(AccessDetail detail);

	/**
	 * 根据在线用户信息创建接入明细
	 * @param user 在线用户
	 */
	void createAccessDetail(OnlineUser user);

	AccessDetail queryAccessDetail(Long storeId, String ssid, String usermac,
			long accessStartTime);

	/**
	 * 查询接入开始时间
	 *
	 * @param userId
	 * @return
	 */
	Long queryAccessStartTime(long userId);


	/**
     * 带偏移量  获取接入明细
     * @param identifier 第三方唯一标示
     * @param offset 偏移量
     * @param limit 记录数
     *
     * */
    List<AccessDetail> queryAccessUser(String identifier,int offset, int limit);
}
