package com.h3c.o2o.portal.rabbitmq.handler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.h3c.o2o.portal.PortalErrorCodes;
import com.h3c.o2o.portal.functioncontrol.entity.OffLineRepMsg;
import com.h3c.o2o.portal.functioncontrol.func.FunctionControlMgrImpl;
import com.h3c.o2o.portal.login.entity.WxWifiOptResponse4Json;
import com.h3c.o2o.portal.rabbitmq.msg.MqMsgType4Json;
import com.h3c.o2o.portal.rabbitmq.msg.OffLineReqHolder;
import com.h3c.o2o.portal.rs.entity.ErrorInfo;
import com.h3c.o2o.portal.util.WifiUtils;

/**
 * 管理在线用户的rabbitMq消息接口<br>
 * 
 * @author heqiao
 *
 */
public class OnlineUserHandler implements CommonMqJsonHandler {

	/**
	 * Log
	 */
	private Log log = LogFactory.getLog(getClass());

	@Override
	public MqMsgType4Json[] getSuportedMsgType() {
		return new MqMsgType4Json[] { MqMsgType4Json.KICKOUT_USER_BYMAC };
	}

	@Override
	public WxWifiOptResponse4Json handleMessage(MqMsgType4Json type, String reqData) throws Exception {
		WxWifiOptResponse4Json response = new WxWifiOptResponse4Json();
		switch (type) {
		case KICKOUT_USER_BYMAC:
			String macStr = reqData;
			ErrorInfo errorInfo = FunctionControlMgrImpl.getInstance().logoutByMac(macStr);
			if (log.isDebugEnabled()) {
				log.debug("logoutByMac : " + macStr + " ret: [code=" + errorInfo.getError_code() + " msg="
						+ errorInfo.getError_msg() + "]");
			}
			if ("success".equals(errorInfo.getError_msg())) {
				response.setErrcode(PortalErrorCodes.ERROR_CODE_OK);
			} else {
				response.setErrcode(errorInfo.getError_code());
			}
			response.setErrmsg(errorInfo.getError_msg());
			break;
		case KICKOUT_USER_BYIPLIST:
			String ipListAndStoreId = reqData;
			OffLineReqHolder req = WifiUtils.fromJson(ipListAndStoreId, OffLineReqHolder.class);
			OffLineRepMsg rep = FunctionControlMgrImpl.getInstance().logoutByIpList(req.getStoreId(), req.getIpList());
			if (null == rep) {
				response.setErrcode(PortalErrorCodes.ERROR_CODE_ERROR);
				response.setErrmsg("Empty data returned!");
			} else if (PortalErrorCodes.ERROR_CODE_OK.equals(rep.getError_code())) {
				response.setErrcode(PortalErrorCodes.ERROR_CODE_OK);
				response.setErrmsg(rep.getError_msg());
				response.setRepData(WifiUtils.toJson(rep));
			} else {
				response.setErrcode(rep.getError_code());
				response.setErrmsg(rep.getError_msg());
				response.setRepData(WifiUtils.toJson(rep));
			}
			break;
		default:
			throw new Exception("MqMsgType4Json Not Support: [ type=" + type + " ]");
		}
		response.setMsgType(type.name());
		return response;
	}

}
