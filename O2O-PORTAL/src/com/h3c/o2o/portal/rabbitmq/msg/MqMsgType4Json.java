package com.h3c.o2o.portal.rabbitmq.msg;

/**
 * rabbitmq��Ϣ����<br>
 * JSON�ִ�����<br>
 * 
 * @author heqiao
 *
 */
public enum MqMsgType4Json {
	/**
	 * ��ȡĬ�Ϲ��ں�
	 */
	GET_DEFAULT_ACCOUNT,
	
	/**
	 * �����ŵ�����¼�
	 */
	HANDLE_SHOP_CHECKED,

	/**
	 * ����΢�Ų˵�
	 */
	PUBLISH_WEIXIN_MENU,
	
	/**
	 * ��ȡ��¼�ض���URL
	 */
	GET_LONGIN_VERIFY_REDIRECTURL,
	
	/**
	 * ͨ��Mac��ַ���ߵ������û�
	 */
	KICKOUT_USER_BYMAC,
	
	/**
	 * ͨ��IP��ַ�б��ߵ������û�
	 */
	KICKOUT_USER_BYIPLIST; 
	
}
