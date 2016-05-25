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

	/** ΢����wifi����� */
	public static final String COMP_ID_WEIXIN_WIFI = "O2O-WEIXIN-WIFI";

	/** ΢����wifi���� */
	public static final String WEIXIN_WIFI_DOMAIN = "WEIXINWIFI_SERVER_DOMAIN";

	/** ΢����wifi�˿ں� */
	public static final String WEIXIN_WIFI_PORT = "WEIXINWIFI_HTTP_PORT";
	
	public static final String WEIXIN_WIFI_PORT_HTTPS = "WEIXINWIFI_HTTPS_PORT";

	private static MessageDigest md5;
	
	private static HttpOrHttpsMgr httpOrHttps;

	/**
	 * Log
	 */
	private static Log log = LogFactory.getLog(FuncUtil.class);
	
	/**
	 * �Ƿ��ַ���#��+��/��?��%��&��=��*�����Ӣ��*�ţ���'�����Ӣ�����ţ���@��\ ��"��[��]��(��)��
	 * <��>��`(���ּ�1���Ķ���)��TAB �� <br/>
	 * �������£�[#+/?%&=*'@\"[]()<>` ��ע�����һ��������ǰ����TAB����
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
	 * ����get����
	 *
	 * @param url
	 * @return ��Դ
	 */
	public static String sendRequest(String url) {
		String str = "";
		URLConnection con;
		InputStream input = null;
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		try {
			con = (new URL(url)).openConnection();
			// Ϊ�˷�ֹ�����ڵȴ����������ӳ�ʱ�ͻ�ȡ��Ӧ��ʱʱ��
			con.setConnectTimeout(5000);
			con.setReadTimeout(50000);
			con.setDoOutput(false);
			input = con.getInputStream();
			byte[] buff = new byte[1024];
			int len = -1;
			while ((len = input.read(buff)) != -1) {
				buffer.write(buff, 0, len);
			}
			str = buffer.toString(); // �ַ�����ʽ�Ľ��
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
	 * ��ip��ַת��Ϊ������
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
	 * ȡ��΢������
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
	 * ������ת��Ϊip��ַ
	 *
	 */
	public static String convertLongToHostIp(long longIp) {
		return (longIp >> 24 & 0xFF) + "." + (longIp >> 16 & 0xFF) + "." + (longIp >> 8 & 0xFF) + "." + (longIp & 0xFF);
	}

	/**
	 * �ж��û����Ƿ����Ҫ��<br>
	 * ���Ϸ��ַ����� <code>#+/?%&=*'@\\\"[]()<>`</code><br>
	 * ���⣬Ҳ���ܰ������������ո񣬲���Ϊ�ա�<br>
	 * 
	 * @param accountName
	 *            �û����ַ���
	 * @return �û����Ϸ�������true
	 */
	public static boolean isValidAccountName(String accountName) {
		if (accountName == null) {
			return false;
		}
		if (accountName.contains("  ") || "".equals(accountName.trim())) {
			// �����������������ϵĿո���Ϊ�Ƿ����ַ���Ϊ�մ�����Ϊ�ǷǷ�
			return false;
		}
		Pattern pattern = Pattern.compile(ACCOUNT_NAME_PATTERN);
		return pattern.matcher(accountName.trim()).matches();
	}

	/**
	 * ��ȡ��¼��֤�ɹ�����תURI
	 * 
	 * @param redirectURI
	 * @param code
	 * @param userIp
	 *            Ϊ��֧�ֱ���ת������Ҫ��userIp�ش����豸��
	 * @param isWxWiFi
	 *            ΢����WiFi��Ҫ����auth_type�ֶ�
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
	 * ��ȡ�Զ������ҳ��
	 *
	 * @param errorCode
	 *            ������
	 * @return ����ҳ��URI
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
	 * ��ȡ��ǰ����ip
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
				// ��������ȡ�������õ�IP
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
		// ����ͨ�����������������һ��IPΪ�ͻ�����ʵIP,���IP����','�ָ�
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
