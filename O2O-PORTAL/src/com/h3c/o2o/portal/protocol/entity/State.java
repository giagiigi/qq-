package com.h3c.o2o.portal.protocol.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Base64;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.h3c.o2o.portal.common.FuncUtil;

/**
 * Oauth认证里的State字段格式<br>
 * 
 * @author heqiao
 *
 */
public class State implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -2771190515379717815L;

	private Long ownerId;
	private Long storeId;
	private Long accountId;
	private String userIp;
	private String usermac;
	private String ssid;
	
	private static Log log = LogFactory.getLog(State.class);
	private static final String SPLIT = "&";
		
	
	/**
	 * 手动解析URI参数
	 * 
	 * @param paraMap
	 * @param paraName
	 */
	private static String[] parsState(String decodedUri){
		Assert.notNull(decodedUri, "Uri must not be null!");
		// split by &
		String[] values = decodedUri.split("" + SPLIT);
		return values;
	}
	
	/**
	 * State对象转化为JSON字符串，URLencode之后的形式
	 * 
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static String toState(State st) throws Exception {
		Assert.notNull(st, "A State Object is required!");
		String stateStr = st.getOwnerId() + SPLIT + st.getStoreId()
			+ SPLIT + st.getAccountId() + SPLIT + st.getUsermac()
			+ SPLIT + st.getSsid() + SPLIT + FuncUtil.convertHostIpToLong(st.getUserIp());
		String ret = null;
		try {
			ret = URLEncoder.encode(toBase64(stateStr), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.warn(null, e);
		}
		return null != ret ? ret.trim() : null;
	}

	/**
	 * json字串转为State对象
	 * 
	 * @param jsonStr
	 * @param isUrlEncoded
	 * @return
	 */
	public static State fromState(String stateStr) throws Exception{
		if (!StringUtils.hasText(stateStr)) {
			return null;
		}
		String decodeJsonStr = null;
		try {
			decodeJsonStr = URLDecoder.decode(stateStr, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.warn(null, e);
		}
		State ret = new State();
		String rawStr = fromBase64(decodeJsonStr);
		String[] values = parsState(rawStr);
		if(null == values || values.length != 6){
			throw new Exception("Wrong state: " + rawStr);
		}
		ret.setOwnerId(Long.valueOf(values[0]));
		ret.setStoreId(Long.valueOf(values[1]));
		ret.setAccountId(Long.valueOf(values[2]));
		ret.setUsermac(values[3]);
		ret.setSsid(values[4]);
		ret.setUserIp(FuncUtil.convertLongToHostIp(Long.valueOf(values[5])));
		return ret;
	}

	/**
	 * 由Base64转为String
	 * @param base64Str
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	private static String fromBase64(String base64Str) throws UnsupportedEncodingException{
		if(StringUtils.hasText(base64Str)){
			byte[] decodedBytes = Base64.getUrlDecoder().decode(base64Str.getBytes("UTF-8"));
			return new String(decodedBytes, "UTF-8");
		} else {
			return "";
		}
	}
	
	/**
	 * 转成Base64编码
	 * @param src
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private static String toBase64(String src) throws UnsupportedEncodingException{
		if(StringUtils.hasText(src)){
			byte[] encodedBytes = Base64.getUrlEncoder().encode(src.getBytes("UTF-8"));
			return new String(encodedBytes, "UTF-8");
		} else {
			return "";
		}
	}
	
	// getter and setter
	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public String getUsermac() {
		return usermac;
	}

	public void setUsermac(String usermac) {
		this.usermac = usermac;
	}

	public String getSsid() {
		return ssid;
	}

	public void setSsid(String ssid) {
		this.ssid = ssid;
	}
	
	public String getUserIp() {
		return userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}

	@Override
	public String toString() {
		return "State [ownerId=" + ownerId + ", storeId=" + storeId + ", accountId=" + accountId + ", userIp=" + userIp
				+ ", usermac=" + usermac + ", ssid=" + ssid + "]";
	}

	/**
	 * Test!
	 * @param args
	 */
	public static void main(String args[]){
		State st = new State();
		st.setAccountId(123213L);
		st.setOwnerId(3232323L);
		st.setSsid("12345678901234@#@#@#2456789wifi5678901234");
		st.setStoreId(2232323L);
		st.setUsermac("D0-A6-37-0E-77-3C");
		st.setUserIp("192.168.0.11");
		String stStr = null;
		System.out.println("State : " + st);
		try {
			stStr = State.toState(st);
			System.out.println("State Str: " + stStr + "\nState Len: " + stStr.length());
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			State stAgain = State.fromState(stStr);
			System.out.println("State : " + stAgain);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
