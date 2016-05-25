package com.h3c.o2o.portal.rabbitmq.handler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.h3c.o2o.portal.PortalErrorCodes;
import com.h3c.o2o.portal.login.entity.WxWifiOptResponse4Json;
import com.h3c.o2o.portal.protocol.entity.TpLoginInfo;
import com.h3c.o2o.portal.protocol.func.ProtocolMgrImpl;
import com.h3c.o2o.portal.rabbitmq.msg.MqMsgType4Json;
import com.h3c.o2o.portal.util.WifiUtils;

/**
 * ��ȡ��¼��֤�ض���URL����<br>
 * Ϊ�˼���һ��΢�ſͻ��˵��ض��������ṩrabbitMQ�ķ�ʽ��ȡ�ض���URL�Ľӿڡ�<br>
 * 
 * @author heqiao
 *
 */
public class LoginVerifyRedirectHandler implements CommonMqJsonHandler {

	/**
	 * Log
	 */
	private Log log = LogFactory.getLog(getClass());
	
	@Override
	public MqMsgType4Json[] getSuportedMsgType() {
		return new MqMsgType4Json[] { MqMsgType4Json.GET_LONGIN_VERIFY_REDIRECTURL };
	}

	@Override
	public WxWifiOptResponse4Json handleMessage(MqMsgType4Json type, String reqData) throws Exception {
		WxWifiOptResponse4Json response = new WxWifiOptResponse4Json();
		switch (type) {
			case GET_LONGIN_VERIFY_REDIRECTURL:
				TpLoginInfo loginInfo = WifiUtils.fromJson(reqData, TpLoginInfo.class);
				String redirectURI = ProtocolMgrImpl.get().tpLoginVerify(loginInfo);
				if(log.isDebugEnabled()){
					log.debug("get redirectURI: " + redirectURI);
				}
				response.setRepData(redirectURI);
				break;
			default:
				throw new Exception("MqMsgType4Json Not Support: [ type=" + type + " ]");
		}
		response.setErrcode(PortalErrorCodes.ERROR_CODE_OK);
		response.setErrmsg("");
		response.setMsgType(type.name());
		return response;
	}
	
}
