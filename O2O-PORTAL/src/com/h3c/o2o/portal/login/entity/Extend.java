package com.h3c.o2o.portal.login.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.h3c.o2o.portal.util.WifiUtils;


/**
 * extend字段结构<br>
 * 
 * @author heqiao
 *
 */
public class Extend implements Serializable {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -109422164139236615L;

	public static final String NASID = "nas_id";
	public static final String USERMAC = "usermac";
	public static final String USERIP = "userip";
	public static final String SSID = "ssid";
	public static final String REDIRECT_URI = "redirect_uri";

	private static final char URL_AND = '&';
	private static final char URL_EQU = '=';

	private String nasid;
	private String usermac;
	private String userip;
	private String ssid;
	private String redirect_uri;

	/**
	 * 手动解析URI参数
	 * 
	 * @param paraMap
	 * @param paraName
	 */
	private static Map<String, String> parsUri(String decodedUri) throws Exception{
		Assert.notNull(decodedUri, "Uri must not be null!");
		Map<String, String> retMap = new HashMap<String, String>();
		// split by &
		String[] nameValues = decodedUri.split("" + URL_AND);
		for(int i=0; null!=nameValues && i<nameValues.length; i++){
			// split by =
			String[] nameAndValue = nameValues[i].split("" + URL_EQU);
			retMap.put(nameAndValue[0], nameAndValue.length < 2 ? null : nameAndValue[1]);
		}
		return retMap;
	}

	/**
	 * String to Extend<br>
	 * 没办法，手动解析
	 * @param extendStr
	 * @return
	 * @throws Exception
	 */
	public static Extend fromExtend(String extendUri) throws Exception {
		String decodedStr = fromBase64(extendUri);
		Map<String, String> uriParas = parsUri(decodedStr);
		Extend ret = new Extend();
		ret.setNasid(uriParas.get(Extend.NASID));
		ret.setSsid(WifiUtils.safeDecode(uriParas.get(Extend.SSID)));
		ret.setUserip(uriParas.get(Extend.USERIP));
		ret.setUsermac(uriParas.get(Extend.USERMAC));
		// decode url
		ret.setRedirect_uri(WifiUtils.safeDecode(uriParas.get(Extend.REDIRECT_URI)));
		return ret;
	}

	/**
	 * Extend to String
	 * 
	 * @param extend
	 * @return
	 * @throws Exception
	 */
	public static String toExtend(Extend extend) throws Exception {
		Assert.notNull(extend, "Extend is required!");
		StringBuilder sbuilder = new StringBuilder();
		sbuilder.append(Extend.NASID + "=" + extend.getNasid() + "&")
				.append(Extend.SSID + "=" + WifiUtils.safeEncode(extend.getSsid()) + "&")
				.append(Extend.USERIP + "=" + extend.getUserip() + "&")
				.append(Extend.USERMAC + "=" + extend.getUsermac()+ "&")
				.append(Extend.REDIRECT_URI + "=" + WifiUtils.safeEncode(extend.getRedirect_uri()));
		return toBase64(sbuilder.toString());
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
	
	@Override
	public String toString() {
		return "Extend [nasid=" + nasid + ", usermac=" + usermac + ", userip=" + userip + ", ssid=" + ssid
				+ ", redirect_uri=" + redirect_uri + "]";
	}

	// getter and setter
	public String getNasid() {
		return nasid;
	}

	public void setNasid(String nasid) {
		this.nasid = nasid;
	}

	public String getUsermac() {
		return usermac;
	}

	public void setUsermac(String usermac) {
		this.usermac = usermac;
	}

	public String getUserip() {
		return userip;
	}

	public void setUserip(String userip) {
		this.userip = userip;
	}

	public String getSsid() {
		return ssid;
	}

	public void setSsid(String ssid) {
		this.ssid = ssid;
	}

	public String getRedirect_uri() {
		return redirect_uri;
	}

	public void setRedirect_uri(String redirect_uri) {
		this.redirect_uri = redirect_uri;
	}

}
