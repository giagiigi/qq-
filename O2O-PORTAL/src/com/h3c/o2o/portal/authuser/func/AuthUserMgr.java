package com.h3c.o2o.portal.authuser.func;

import java.io.Serializable;
import java.util.List;

import com.h3c.o2o.portal.entity.AuthUser;

/**
 * 获取认证用户的接口
 * @author heqiao
 *
 */
public interface AuthUserMgr extends Serializable{
	/**
	 * 通过MAC地址查询认证用户
	 * @param mac
	 * @return
	 */
	public AuthUser queryByMac(String mac);
	
	/**
	 * 通过OpenId和认证方式查询认证用户
	 * @param openId
	 * @param userType 可以为空
	 * @return
	 */
	public List<AuthUser> queryByOpenIdAndUserType(String openId, Integer userType);
	
	/**
	 * 保存或者更新一个认证用户
	 * 
	 * @param user
	 */
	public void saveOrUpdateAuthUser(AuthUser user);
	
}
