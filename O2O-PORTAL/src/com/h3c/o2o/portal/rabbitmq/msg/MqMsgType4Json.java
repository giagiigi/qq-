package com.h3c.o2o.portal.rabbitmq.msg;

/**
 * rabbitmq消息类型<br>
 * JSON字串交互<br>
 * 
 * @author heqiao
 *
 */
public enum MqMsgType4Json {
	/**
	 * 获取默认公众号
	 */
	GET_DEFAULT_ACCOUNT,
	
	/**
	 * 处理门店审核事件
	 */
	HANDLE_SHOP_CHECKED,

	/**
	 * 发布微信菜单
	 */
	PUBLISH_WEIXIN_MENU,
	
	/**
	 * 获取登录重定向URL
	 */
	GET_LONGIN_VERIFY_REDIRECTURL,
	
	/**
	 * 通过Mac地址，踢掉在线用户
	 */
	KICKOUT_USER_BYMAC,
	
	/**
	 * 通过IP地址列表，踢掉在线用户
	 */
	KICKOUT_USER_BYIPLIST; 
	
}
