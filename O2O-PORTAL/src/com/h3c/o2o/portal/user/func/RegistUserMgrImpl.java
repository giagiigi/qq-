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

import org.apache.commons.lang.StringUtils;

import com.h3c.o2o.portal.PortalException;
import com.h3c.o2o.portal.common.FuncUtil;
import com.h3c.o2o.portal.entity.RegistUser;
import com.h3c.o2o.portal.user.dao.RegistUserDao;

/**
 * 用户管理Mgr实现类
 * 
 * @author j09980
 */
public class RegistUserMgrImpl implements RegistUserMgr {

	private RegistUserDao userDao;
	
	@Override
	public void saveRegistUser(RegistUser user) {
		// 入库前进行校验
		// 名称空值校验
		String userName = user.getUserName();
		if (StringUtils.isBlank(userName)) {
			throw new PortalException(0x0008, "user name or password is wrong");
		}
		userName = userName.trim().toLowerCase();
		// 名称合法性校验 校验用户名长度 用户名不能包含特殊字符
		if (userName.length() > 32 || !FuncUtil.isValidAccountName(userName)) {
			throw new PortalException(0x0002, "user name is illeagal");
		}

		// 校验用户名是否存在
		RegistUser tempUser = userDao.queryRegistUser(user.getStoreId(),
				userName, user.getUserType());
		if (tempUser != null) {
			throw new PortalException(0x0001, "user name already exist");
		}
		// 密码校验 还需提供校验项目 0x0005

		// 黑名单 白名单校验
		userDao.saveRegistUser(user);
	}

	@Override
	public void updateRegistUser(RegistUser user) {
		userDao.updateRegistUser(user);
	}

	@Override
	public RegistUser queryRegistUser(Long id) {
		return userDao.queryRegistUser(id);
	}

	@Override
	public RegistUser queryRegistUser(Long storeId, String userName,
			Integer userType) {
		return userDao.queryRegistUser(storeId, userName, userType);
	}
	
	/* (non-Javadoc)
     * @see com.h3c.o2o.portal.user.func.RegistUserMgr#findAllRegistUser()
     */
    @Override
    public List<RegistUser> findAllRegistUser() {
        return userDao.queryAllRegistUser();
    }

	@Override
	public RegistUser queryRegistUser(String userName, String identifier,
			Integer userType) {
		return this.userDao.queryRegistUser(userName, identifier, userType);
	}
	
	public void setUserDao(RegistUserDao userDao) {
        this.userDao = userDao;
    }
	
}
