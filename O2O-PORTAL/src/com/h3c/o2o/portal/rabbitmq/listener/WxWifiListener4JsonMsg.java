package com.h3c.o2o.portal.rabbitmq.listener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import com.h3c.o2o.portal.PortalErrorCodes;
import com.h3c.o2o.portal.login.entity.WxWifiOptRequest4Json;
import com.h3c.o2o.portal.login.entity.WxWifiOptResponse4Json;
import com.h3c.o2o.portal.rabbitmq.msg.MsgDispatcher4JsonMsg;
import com.h3c.o2o.portal.util.WifiUtils;

/**
 * 微信连Wifi监听<br>
 * 提供微信Sever接口的rabbitmq调用<br>
 * 与之前的监听不同，这里采用JSON字串<br>
 * 
 * @author heqiao
 *
 */
public class WxWifiListener4JsonMsg {
	/**
	 * Log
	 */
	private Log log = LogFactory.getLog(getClass());

	/**
	 * 注入dispatcher
	 */
	private MsgDispatcher4JsonMsg dispatcher;

	/**
	 * 处理RabbitMQ消息的方法
	 * @param reqStr
	 * @return
	 */
	public String handleMessage(String reqStr) {
		WxWifiOptResponse4Json retJsonObj = null; //返回对象
		try{
			// 解析JSON
			WxWifiOptRequest4Json reqJsonObj = parseRequestJson(reqStr);
			// 处理
			retJsonObj = this.getDispatcher().dispatch(reqJsonObj);
		} catch(Exception e){
			log.warn("Error occured when handle json msg.", e);
			retJsonObj = new WxWifiOptResponse4Json();
			retJsonObj.setErrcode(PortalErrorCodes.ERROR_CODE_ERROR);
			retJsonObj.setErrmsg(e.getMessage());
			retJsonObj.setRepData("");
		}
		if(log.isDebugEnabled()){
			log.debug("Req: " + reqStr);
			log.debug("Rep: " + retJsonObj);
		}
		// 转JSON
		return WifiUtils.toJson(retJsonObj);
	}
	
	/**
	 * 解析请求串
	 * @return
	 * @throws Exception 
	 */
	private WxWifiOptRequest4Json parseRequestJson(String reqStr) throws Exception{
		if(!StringUtils.hasText(reqStr)){
			throw new Exception("Request Json String is empty!");
		}
		WxWifiOptRequest4Json reqJsonObj = WifiUtils.fromJson(reqStr, WxWifiOptRequest4Json.class);
		return reqJsonObj;
	}
	
	// getter and setter
	public MsgDispatcher4JsonMsg getDispatcher() {
		return dispatcher;
	}

	public void setDispatcher(MsgDispatcher4JsonMsg dispatcher) {
		this.dispatcher = dispatcher;
	}
}
