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
 * ΢����Wifi����<br>
 * �ṩ΢��Sever�ӿڵ�rabbitmq����<br>
 * ��֮ǰ�ļ�����ͬ���������JSON�ִ�<br>
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
	 * ע��dispatcher
	 */
	private MsgDispatcher4JsonMsg dispatcher;

	/**
	 * ����RabbitMQ��Ϣ�ķ���
	 * @param reqStr
	 * @return
	 */
	public String handleMessage(String reqStr) {
		WxWifiOptResponse4Json retJsonObj = null; //���ض���
		try{
			// ����JSON
			WxWifiOptRequest4Json reqJsonObj = parseRequestJson(reqStr);
			// ����
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
		// תJSON
		return WifiUtils.toJson(retJsonObj);
	}
	
	/**
	 * ��������
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
