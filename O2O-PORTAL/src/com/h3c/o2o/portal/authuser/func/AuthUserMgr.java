package com.h3c.o2o.portal.authuser.func;

import java.io.Serializable;
import java.util.List;

import com.h3c.o2o.portal.entity.AuthUser;

/**
 * ��ȡ��֤�û��Ľӿ�
 * @author heqiao
 *
 */
public interface AuthUserMgr extends Serializable{
	/**
	 * ͨ��MAC��ַ��ѯ��֤�û�
	 * @param mac
	 * @return
	 */
	public AuthUser queryByMac(String mac);
	
	/**
	 * ͨ��OpenId����֤��ʽ��ѯ��֤�û�
	 * @param openId
	 * @param userType ����Ϊ��
	 * @return
	 */
	public List<AuthUser> queryByOpenIdAndUserType(String openId, Integer userType);
	
	/**
	 * ������߸���һ����֤�û�
	 * 
	 * @param user
	 */
	public void saveOrUpdateAuthUser(AuthUser user);
	
}
