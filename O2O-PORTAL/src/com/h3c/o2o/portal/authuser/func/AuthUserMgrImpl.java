package com.h3c.o2o.portal.authuser.func;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.h3c.o2o.portal.authuser.dao.AuthUserDao;
import com.h3c.o2o.portal.entity.AuthUser;

/**
 * 获取认证用户的接口
 * 
 * @author heqiao
 *
 */
public class AuthUserMgrImpl implements AuthUserMgr {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -4279975565107347369L;

	/**
	 * DAO
	 */
	private AuthUserDao dao;

	/**
	 * Log
	 */
	private Log log = LogFactory.getLog(getClass());

	public AuthUser queryByMac(String mac) {
		if (!StringUtils.hasText(mac)) {
			return null;
		}
		String hql = " from AuthUser where " + AuthUser.USER_MAC + " = ? ";
		List<AuthUser> list = getDao().queryByHql(hql, mac);
		if (null == list || list.isEmpty()) {
			return null;
		} else if (list.size() > 1) {
			log.warn("Multiple AuthUser found by usermac：" + mac);
		}
		return list.get(0);
	}

	public List<AuthUser> queryByOpenIdAndUserType(String openId, Integer userType) {
		if (!StringUtils.hasText(openId)) {
			return null;
		}
		List<AuthUser> list = null;
		StringBuilder hqlbuilder = new StringBuilder();
		hqlbuilder.append(" from AuthUser where " + AuthUser.OPEN_ID + " = ? ");
		if (null == userType) {
			list = getDao().queryByHql(hqlbuilder.toString(), openId);
		} else {
			hqlbuilder.append(" and " + AuthUser.USER_TYPE + " = ? ");
			list = getDao().queryByHql(hqlbuilder.toString(), openId, userType);
		}
		return list;
	}
	
	public void saveOrUpdateAuthUser(AuthUser user) {
		Assert.notNull(user, "A AuthUser object is required!");
		List<AuthUser> newEntities = new ArrayList<AuthUser>();
		// 通过MAC地址查询
		AuthUser oldUser = queryByMac(user.getUser_mac());
		if (null == oldUser) {
			// save
			newEntities.add(user);
			getDao().insert(newEntities, AuthUser.class, AuthUser.ID);
		} else {
			// update
			user.setId(oldUser.getId());
			user.setUser_mac(oldUser.getUser_mac());
			newEntities.add(user);
			getDao().update(newEntities);
		}
	}

	// getter and setter
	public AuthUserDao getDao() {
		return dao;
	}

	public void setDao(AuthUserDao dao) {
		this.dao = dao;
	}

}
