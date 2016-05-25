package com.h3c.o2o.portal.rabbitmq.msg;

import java.io.Serializable;
import java.util.List;

/**
 * 通过IP地址列表下线请求
 * 
 * @author heqiao
 *
 */
public class OffLineReqHolder implements Serializable {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 236897056964523159L;
	List<String> ipList;
	Long storeId;

	public List<String> getIpList() {
		return ipList;
	}

	public void setIpList(List<String> ipList) {
		this.ipList = ipList;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}
}
