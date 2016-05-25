package com.h3c.o2o.portal.rabbitmq.handler;

import com.h3c.o2o.portal.login.entity.WxWifiOptResponse4Json;
import com.h3c.o2o.portal.rabbitmq.msg.MqMsgType4Json;

/**
 * ������JSON������RabbitMq��Ϣ��Handler�ӿ�
 * 
 * @author heqiao
 *
 */
public interface CommonMqJsonHandler {
	/**
	 * ֧�ֵ���Ϣ����
	 * 
	 * @return
	 */
	public MqMsgType4Json[] getSuportedMsgType();

	/**
	 * ������Ϣ
	 * 
	 * @return
	 */
	public WxWifiOptResponse4Json handleMessage(MqMsgType4Json type, String reqData) throws Exception;
}
