package com.h3c.o2o.portal.rabbitmq.handler;

import com.h3c.o2o.portal.login.entity.WxWifiOptResponse4Json;
import com.h3c.o2o.portal.rabbitmq.msg.MqMsgType4Json;

/**
 * 处理用JSON交互的RabbitMq消息的Handler接口
 * 
 * @author heqiao
 *
 */
public interface CommonMqJsonHandler {
	/**
	 * 支持的消息类型
	 * 
	 * @return
	 */
	public MqMsgType4Json[] getSuportedMsgType();

	/**
	 * 处理消息
	 * 
	 * @return
	 */
	public WxWifiOptResponse4Json handleMessage(MqMsgType4Json type, String reqData) throws Exception;
}
