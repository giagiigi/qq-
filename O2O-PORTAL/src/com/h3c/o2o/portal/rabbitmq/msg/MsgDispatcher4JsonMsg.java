package com.h3c.o2o.portal.rabbitmq.msg;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.h3c.o2o.portal.PortalErrorCodes;
import com.h3c.o2o.portal.login.entity.WxWifiOptRequest4Json;
import com.h3c.o2o.portal.login.entity.WxWifiOptResponse4Json;
import com.h3c.o2o.portal.rabbitmq.handler.CommonMqJsonHandler;

/**
 * 处理JSON交互的Dispatcher
 * 
 * @author heqiao
 *
 */
public class MsgDispatcher4JsonMsg implements InitializingBean {
	/**
	 * 处理rabbitMq消息的Handler注入
	 */
	private List<CommonMqJsonHandler> handlers;

	/**
	 * Log
	 */
	private Log log = LogFactory.getLog(getClass());

	/**
	 * Dispatch Map
	 */
	private static Map<MqMsgType4Json, CommonMqJsonHandler> dispatchMap = new Hashtable<MqMsgType4Json, CommonMqJsonHandler>();

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(handlers, "Handlers are required!");
		// 初始化dispatchMap
		for (CommonMqJsonHandler handler : handlers) {
			MqMsgType4Json[] supportedTypes = handler.getSuportedMsgType();
			for (int i = 0; null != supportedTypes && i < supportedTypes.length; i++) {
				dispatchMap.put(supportedTypes[i], handler);
			}
		}
	}

	/**
	 * 獲取Handler
	 * 
	 * @param msgType
	 * @return
	 */
	public CommonMqJsonHandler getHandler(MqMsgType4Json msgType) {
		return dispatchMap.get(msgType);
	}

	/**
	 * dispatch
	 * 
	 * @param request
	 * @return
	 */
	public WxWifiOptResponse4Json dispatch(WxWifiOptRequest4Json request) {
		WxWifiOptResponse4Json response = new WxWifiOptResponse4Json();
		response.setMsgType(request.getMsgType());
		MqMsgType4Json type = null;
		try {
			type = null != request ? MqMsgType4Json.valueOf(request.getMsgType()) : null;
		} catch (IllegalArgumentException e) {
			log.warn("Wrong msgType: " + request.getMsgType(), e);
		}
		if (null == type) {
			// 处理类型为空的情况
			log.warn("Not MqMsgType match found for request:" + request);
			response.setErrcode(PortalErrorCodes.ERROR_CODE_ERROR);
			response.setErrmsg("Not MqMsgType match found for request: " + request);
			response.setRepData("");
			return response;
		} else {
			// 获取一个Handler
			CommonMqJsonHandler handler = getHandler(type);
			try {
				response = handler.handleMessage(type, request.getReqData());
			} catch (Exception e) {
				log.warn("Error Occured when handle message.", e);
				response.setErrcode("-1");
				response.setErrmsg(e.getMessage());
				response.setRepData("");
			}
			return response;
		}
	}

	// getter and setter
	public List<CommonMqJsonHandler> getHandlers() {
		return handlers;
	}

	public void setHandlers(List<CommonMqJsonHandler> handlers) {
		this.handlers = handlers;
	}
}
