package com.h3c.o2o.portal.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.h3c.oasis.o2oserver.util.ServerAddressViewer.ServerAddressConfig;

public class FuncUtil {

	public static final String COMP_ID_WSM = "O2O-WSM";

	public static final String COMP_ID_UAM = "O2O-UAM";

	public static final String COMP_ID_PORTAL = "O2O-PORTAL";

	public static final String COMP_ID_WEIXIN = "O2O-WEIXIN";

	public static final String PORTAL_PORT = "O2OPORTAL_HTTP_PORT";
	
	public static final String PROTAL_PORT_HTTPS = "O2OPORTAL_HTTPS_PORT";

	public static final String WSM_PORT = "O2OWSM_SERVER_PORT";

	public static final String PORTAL_IP = "O2OPORTAL_SERVER_IP";

	public static final String PORTAL_DOMAIN = "O2OPORTAL_SERVER_DOMAIN";

	public static final String WEIXIN_DOMAIN = "O2OWEIXIN_SERVER_DOMAIN";

	public static final String WEIXIN_IP = "O2OWEIXIN_SERVER_IP";

	public static final String WEIXIN_PORT = "O2OWEIXIN_HTTP_PORT";
	
	public static final String WEIXIN_PORT_HTTPS = "O2OWEIXIN_HTTPS_PORT";

	/** 微信连wifi组件名 */
	public static final String COMP_ID_WEIXIN_WIFI = "O2O-WEIXIN-WIFI";

	/** 微信连wifi域名 */
	public static final String WEIXIN_WIFI_DOMAIN = "WEIXINWIFI_SERVER_DOMAIN";

	/** 微信连wifi端口号 */
	public static final String WEIXIN_WIFI_PORT = "WEIXINWIFI_HTTP_PORT";
	
	public static final String WEIXIN_WIFI_PORT_HTTPS = "WEIXINWIFI_HTTPS_PORT";

	private static MessageDigest md5;
	
	private static HttpOrHttpsMgr httpOrHttps;

	/**
	 * Log
	 */
	private static Log log = LogFactory.getLog(FuncUtil.class);
	
	/**
	 * 非法字符：#、+、/、?、%、&、=、*（半角英文*号）、'（半角英文引号）、@、\ 、"、[、]、(、)、
	 * <、>、`(数字键1左侧的逗号)、TAB 。 <br/>
	 * 具体如下：[#+/?%&=*'@\"[]()<>` ，注意最后一个（逗号前）是TAB键。
	 */
	public static final String ACCOUNT_NAME_PATTERN = "[^\\x23\\x2B\\x2F\\x3F\\x25\\x26\\x3D\\x2A"
			+ "\\x27\\x40\\x5C\\x22\\x5B\\x5D\\x28\\x29\\x3C\\x3E\\x60\\x09]+";

	/**
	 * md5
	 *
	 * @param originalStr
	 * @return
	 */
	public static String md5(String originalStr) {
		try {
			if (originalStr == null) {
				return null;
			}
			if (md5 == null) {
				try {
					md5 = MessageDigest.getInstance("MD5");
				} catch (Exception e) {
					return null;
				}
			}

			byte[] bytes = md5.digest(originalStr.getBytes());
			if (bytes == null) {
				return null;
			}
			return convertByteArrayToAsciiString(bytes);
		} catch (Exception e) {
			return null;
		}

	}

	public static String convertByteArrayToAsciiString(byte[] array) {
		if (array == null) {
			return null;
		}
		int value;
		char[] ch = new char[2];
		int len = array.length;
		StringBuffer retStr = new StringBuffer();
		for (int i = 0; i < len; i++) {
			value = (array[i]) & 0xff;
			ch[0] = (char) (value >>> 4);
			if (ch[0] > 9) {
				ch[0] += 'a' - 10;
			} else {
				ch[0] += '0';
			}
			ch[1] = (char) (value & 0x0f);
			if (ch[1] > 9) {
				ch[1] += 'a' - 10;
			} else {
				ch[1] += '0';
			}
			retStr.append(ch);
		}
		return new String(retStr);
	}

	/**
	 * 发送get请求
	 *
	 * @param url
	 * @return 资源
	 */
	public static String sendRequest(String url) {
		String str = "";
		URLConnection con;
		InputStream input = null;
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		try {
			con = (new URL(url)).openConnection();
			// 为了防止无限期等待，设置连接超时和获取响应超时时间
			con.setConnectTimeout(5000);
			con.setReadTimeout(50000);
			con.setDoOutput(false);
			input = con.getInputStream();
			byte[] buff = new byte[1024];
			int len = -1;
			while ((len = input.read(buff)) != -1) {
				buffer.write(buff, 0, len);
			}
			str = buffer.toString(); // 字符串格式的结果
		} catch (Exception e) {

		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
				}
			}
		}
		return str;
	}

	/**
	 * 将ip地址转换为整数字
	 *
	 */
	public static long convertHostIpToLong(String hostIp) {
		if (null == hostIp) {
			throw new NullPointerException("Host IP can not be null");
		}

		String[] parts = hostIp.split("\\x2e");
		if (parts.length != 4) {
			return 0L;
		}

		long ip = 0L;
		for (String part : parts) {
			try {
				int p = Integer.parseInt(part);
				if ((p < 0) || (p > 255)) {
					return 0L;
				}
				ip = ip << 8 | p;
			} catch (NumberFormatException nfe) {
				return 0L;
			}
		}

		return ip;
	}

	/**
	 * 取得微信域名
	 */
	public static String getPortalDomain() {
		return getResource(COMP_ID_PORTAL, PORTAL_DOMAIN);
	}

	private static String getResource(String compId, String customAddr) {
		ServerAddressConfig[] configs = CommonUtils.getServerAddressViewer().getConfigsByComponentId(compId);
		for (ServerAddressConfig config : configs) {
			String customAddress = config.getCustomAddress();
			String[] addresses = customAddress.split("&");
			for (String addr : addresses) {
				String[] parts = addr.split("=");
				if (parts.length == 2 && parts[0].equals(customAddr)) {
					return parts[1];
				}
			}
		}
		return null;
	}

	public static String getWsmServerIp() {
		String address = CommonUtils.getComponentIpAddress(COMP_ID_WSM);
		return address;
	}

	public static String getUamServerIp() {
		String address = CommonUtils.getComponentIpAddress(COMP_ID_UAM);
		return address;
	}

	public static int getWsmWebServerPort() {
		ServerAddressConfig[] configs = CommonUtils.getServerAddressViewer().getConfigsByComponentId(COMP_ID_WSM);
		for (ServerAddressConfig config : configs) {
			String customAddress = config.getCustomAddress();
			String[] addresses = customAddress.split("&");
			for (String addr : addresses) {
				String[] parts = addr.split("=");
				if (parts.length == 2 && parts[0].equals(WSM_PORT)) {
					return Integer.parseInt(parts[1]);
				}
			}
		}
		return 80;
	}

	public static String getWeixinServerIp() {
		return getResource(COMP_ID_WEIXIN, WEIXIN_IP);
	}

	public static String getWeixinServerDomain() {
		return getResource(COMP_ID_WEIXIN, WEIXIN_DOMAIN);
	}

	public static int getWeixinServerPort() {
		ServerAddressConfig[] configs = CommonUtils.getServerAddressViewer().getConfigsByComponentId(WEIXIN_IP);
		for (ServerAddressConfig config : configs) {
			String customAddress = config.getCustomAddress();
			String[] addresses = customAddress.split("&");
			for (String addr : addresses) {
				String[] parts = addr.split("=");
				if("https".equals(httpOrHttps.getHttpOrHttps())){
					if (parts.length == 2 && parts[0].equals(WEIXIN_PORT_HTTPS)) {
						return Integer.parseInt(parts[1]);
					}
				}else{
					if (parts.length == 2 && parts[0].equals(WEIXIN_PORT)) {
						return Integer.parseInt(parts[1]);
					}
				}
				
			}
		}
		return 80;
	}

	public static String getWeixinWifiDomain() {
		return getResource(COMP_ID_WEIXIN_WIFI, WEIXIN_WIFI_DOMAIN);
	}

	public static String getWeixinWifiPort() {
		if("https".equals(httpOrHttps.getHttpOrHttps())){
			return getResource(COMP_ID_WEIXIN_WIFI, WEIXIN_WIFI_PORT_HTTPS);
		}else{
			return getResource(COMP_ID_WEIXIN_WIFI, WEIXIN_WIFI_PORT);
		}
	}

	public static String getPortalServerIp() {
		ServerAddressConfig[] configs = CommonUtils.getServerAddressViewer().getConfigsByComponentId(COMP_ID_PORTAL);
		for (ServerAddressConfig config : configs) {
			String customAddress = config.getCustomAddress();
			String[] addresses = customAddress.split("&");
			for (String addr : addresses) {
				String[] parts = addr.split("=");
				if (parts.length == 2 && parts[0].equals(PORTAL_IP)) {
					return parts[1];
				}
			}
		}
		return null;
	}

	public static int getPortalServerPort() {
		ServerAddressConfig[] configs = CommonUtils.getServerAddressViewer().getConfigsByComponentId(COMP_ID_PORTAL);
		for (ServerAddressConfig config : configs) {
			String customAddress = config.getCustomAddress();
			String[] addresses = customAddress.split("&");
			for (String addr : addresses) {
				String[] parts = addr.split("=");
				if("https".equals(httpOrHttps.getHttpOrHttps())){
					if (parts.length == 2 && parts[0].equals(PROTAL_PORT_HTTPS)) {
						return Integer.parseInt(parts[1]);
					}
				}else{
					if (parts.length == 2 && parts[0].equals(PORTAL_PORT)) {
						return Integer.parseInt(parts[1]);
					}
				}
				
			}
		}
		return 8088;
	}

	/**
	 * 将整数转换为ip地址
	 *
	 */
	public static String convertLongToHostIp(long longIp) {
		return (longIp >> 24 & 0xFF) + "." + (longIp >> 16 & 0xFF) + "." + (longIp >> 8 & 0xFF) + "." + (longIp & 0xFF);
	}

	/**
	 * 判断用户名是否符合要求。<br>
	 * 不合法字符包括 <code>#+/?%&=*'@\\\"[]()<>`</code><br>
	 * 另外，也不能包含连续两个空格，不能为空。<br>
	 * 
	 * @param accountName
	 *            用户名字符串
	 * @return 用户名合法，返回true
	 */
	public static boolean isValidAccountName(String accountName) {
		if (accountName == null) {
			return false;
		}
		if (accountName.contains("  ") || "".equals(accountName.trim())) {
			// 包含两个和两个以上的空格，认为非法；字符串为空串，认为是非法
			return false;
		}
		Pattern pattern = Pattern.compile(ACCOUNT_NAME_PATTERN);
		return pattern.matcher(accountName.trim()).matches();
	}

	/**
	 * 获取登录认证成功后跳转URI
	 * 
	 * @param redirectURI
	 * @param code
	 * @param userIp
	 *            为了支持本地转发，需要将userIp回传给设备端
	 * @param isWxWiFi
	 *            微信连WiFi需要增加auth_type字段
	 * @return
	 */
	public static String getLoginRedirectURI(String redirectURI, String code, String userIp, boolean isWxWiFi) {
		String urlHeader = "";
		try {
			urlHeader = URLDecoder.decode(redirectURI, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.warn(null, e);
			urlHeader = redirectURI;
		}
		return urlHeader + "?code=" + code + "&userip=" + (StringUtils.isBlank(userIp) ? "" : userIp.trim())
				+ (isWxWiFi ? "&auth_type=weixin" : "") + "&portal_server=" + (httpOrHttps.getHttpOrHttps()) + "://"
				+ getPortalDomain() + ":" + getPortalServerPort() + "/portal/protocol";
	}

	/**
	 *
	 * 获取自定义错误页面
	 *
	 * @param errorCode
	 *            错误码
	 * @return 错误页面URI
	 */
	public static String getErrorPageURI(int errorCode) {
		String prefix = "";
		if("https".equals(httpOrHttps.getHttpOrHttps())){
			prefix = "https://" + FuncUtil.getPortalDomain() + ":" + FuncUtil.getPortalServerPort();
		}else{
			prefix = "http://" + FuncUtil.getPortalDomain() + ":" + FuncUtil.getPortalServerPort();
		}
		String suffix = "/portal/portalError.jsp?errorCode=" + errorCode;
		return prefix + suffix;
	}

	/**
	 * 获取当前网络ip
	 * 
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ipAddress = request.getHeader("x-forwarded-for");
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getRemoteAddr();
			if (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")) {
				// 根据网卡取本机配置的IP
				InetAddress inet = null;
				try {
					inet = InetAddress.getLocalHost();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				if (inet != null) {
					ipAddress = inet.getHostAddress();
				}
			}
		}
		// 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
		if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
															// = 15
			if (ipAddress.indexOf(",") > 0) {
				ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
			}
		}
		return ipAddress;
	}

	public HttpOrHttpsMgr getHttpOrHttps() {
		return httpOrHttps;
	}

	public void setHttpOrHttps(HttpOrHttpsMgr httpOrHttps) {
		FuncUtil.httpOrHttps = httpOrHttps;
	}
}
