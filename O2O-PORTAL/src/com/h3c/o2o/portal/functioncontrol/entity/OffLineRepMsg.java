package com.h3c.o2o.portal.functioncontrol.entity;

import com.h3c.o2o.mng.rabbitmq.entity.CommandResponse;
import com.h3c.o2o.portal.rs.entity.ErrorInfo;
/**
 * ǿ��������Ӧ
 * @author heqiao
 *
 */
public class OffLineRepMsg extends ErrorInfo{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 7546380964780828639L;
	
	private CommandResponse respData;

	public CommandResponse getRespData() {
		return respData;
	}

	public void setRespData(CommandResponse respData) {
		this.respData = respData;
	}

}
