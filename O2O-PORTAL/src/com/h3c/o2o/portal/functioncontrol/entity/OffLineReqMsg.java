package com.h3c.o2o.portal.functioncontrol.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.h3c.o2o.portal.util.WifiUtils;

/**
 * 强制用户下线请求
 * @author heqiao
 *
 */
public class OffLineReqMsg implements Serializable{
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -6560068079073469223L;
	
	// 强制下线接口的routingkey和exchange
	public static final String OFFLINE_EXCHANGE_NAME = "o2owsm.deploy.portal.cfg";
	public static final String OFFLINE_ROUNTING_KEY = "o2owsm.deploy.portal.cfg";
	// 强制下线操作码48
	public static final Integer OFFLINT_TYPE = 48;
	
	private Integer optType;
	private String uuid;
	private List<IP> offlineList;
	
	public Integer getOptType() {
		return optType;
	}

	public void setOptType(Integer optType) {
		this.optType = optType;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public List<String> getOfflineList() {
		List<String> ipList = new ArrayList<String>();
		for(int i=0; null!=offlineList && i<offlineList.size(); i++){
			ipList.add(offlineList.get(i).getIp());
		}
		return ipList;
	}

	public void setOfflineList(List<String> ipList) {
		this.offlineList = new ArrayList<IP>();
		for(int i=0; null!=ipList && i<ipList.size(); i++){
			IP ip = new IP();
			ip.setIp(ipList.get(i));
			offlineList.add(ip);
		}
	}

	@Override
	public String toString() {
		return "OffLineMsg [optType=" + optType + ", uuid=" + uuid + ", offlineList=" + offlineList + "]";
	}

	public String toOffLineMsg(){
		return WifiUtils.toJson(this);
	}

	/**
	 * the IP :)
	 * @author heqiao
	 *
	 */
	private class IP{
		String ip;
		public String getIp() {
			return ip;
		}
		public void setIp(String ip) {
			this.ip = ip;
		}
	}
	
	/**
	 * Test!
	 */
	public static void main(String[] args){
		OffLineReqMsg offline = new OffLineReqMsg();
		offline.setOptType(OffLineReqMsg.OFFLINT_TYPE);
		offline.setUuid(UUID.randomUUID().toString());
		
		List<String> ipList = new ArrayList<String>();
		ipList.add("192.168.1.1");
		ipList.add("192.168.1.1");
		ipList.add("192.168.1.1");
		offline.setOfflineList(ipList);
		
		System.out.println(offline.toOffLineMsg());
	}
}
