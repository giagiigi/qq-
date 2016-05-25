package com.h3c.o2o.portal.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.netty.handler.codec.http.QueryStringDecoder;
import org.springframework.util.StringUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * ��������
 * 
 * @author heqiao
 *
 */
public class WifiUtils {
	/**
	 * Log
	 */
	private static Log log = LogFactory.getLog(WifiUtils.class);

	/**
	 * ��˵Gson.toJson() ��Gson.fromJson()��֧�ֲ�����
	 */
	private static Gson gson = new Gson();

	/**
     * URLdecode�ַ���
     * @param enCodedStr
     * @return
     */
    public static String safeDecode(String enCodedStr){
    	if(!StringUtils.hasText(enCodedStr)){
    		return enCodedStr;
    	}
    	String deCodedStr = null;
    	try {
			deCodedStr = URLDecoder.decode(enCodedStr, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.warn(null, e);
		}
    	return deCodedStr;
    }
    
    /**
     * URLencode�ַ���
     * @param str
     * @return
     */
    public static String safeEncode(String str){
    	if(!StringUtils.hasText(str)){
    		return str;
    	}
    	String enCodedStr = null;
    	try {
    		enCodedStr = URLEncoder.encode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.warn(null, e);
		}
    	return enCodedStr;
    }
	
	/**
	 * ��URI��ȡ��һ���������޷��ؿմ�.
	 * 
	 * @param decoder
	 * @param paraName
	 * @return String
	 * @author heqiao
	 */
	public static String getUriPara(QueryStringDecoder decoder, String paraName) {
		List<String> paras = decoder.getParameters().get(paraName);
		if (null == paras || paras.isEmpty()) {
			return null;
		}
		if (StringUtils.hasText(paras.get(0))) {
			try {
				return URLDecoder.decode(paras.get(0), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				log.warn(null, e);
				return paras.get(0);
			}
		}
		return paras.get(0);
	}

	/**
	 * �Ƿ�Ϊ��Ҫת���SQL�ַ�
	 * 
	 * @param c
	 * @return boolean
	 * @author heqiao
	 */
	public static boolean isEscapeChar(char c) {
		switch (c) {
		case ']':
		case '(':
		case '"':
		case '%':
		case '|':
		case '&':
		case '@':
		case '_':
		case '*':
		case '[':
		case ')':
		case '-':
		case '\'':
		case '\\':
			return true;
		default:
			return false;
		}
	}

	/**
	 * SQL�ַ����ļ�ת�崦��<br>
	 * 
	 * @return String
	 * @author heqiao
	 */
	public static String escapeStr(String searchKey, String escape) {
		if (!StringUtils.hasText(searchKey)) {
			return "";
		}
		StringBuilder strBuilder = new StringBuilder();
		for (char c : searchKey.toCharArray()) {
			if (isEscapeChar(c)) {
				strBuilder.append(escape);
			}
			strBuilder.append(c);
		}
		return strBuilder.toString();
	}

	/**
	 * Id�Ƿ�Ϸ�
	 * 
	 * @param id
	 * @return
	 */
	public static boolean isValidId(Long id) {
		return null != id && id > 0L;
	}

	/**
	 * ����Object��equals����
	 * 
	 * @param o1
	 * @param o2
	 * @return
	 * @author heiqao
	 */
	public static boolean equalsAcceptNull(Object o1, Object o2) {
		return ObjectUtils.equals(o1, o2);
	}

	/**
	 * ����Get������ȡEntity����
	 * 
	 * @param obj
	 * @param propertyName
	 * @return
	 * @throws Exception
	 */
	public static Object getObjectProperty(Object obj, String propertyName) throws Exception {
		// get����
		String methodName = "get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
		Method m = null;
		Object prop = null;
		try {
			m = obj.getClass().getMethod(methodName);
			prop = m.invoke(obj, (Object[]) null);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			log.warn("GetObjectProperty Failed!", e);
			throw new Exception("GetObjectProperty failed!", e);
		}
		return prop;
	}

	/**
	 * ����ʵ��Set����
	 * 
	 * @throws Exception
	 */
	public static void callSetMethod(Object entity, Class<?> c, String propertyName, Object... setArgs)
			throws Exception {
		// set����
		String methodName = "set" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
		// get setArgs Type
		Class<?>[] argsType = new Class<?>[null == setArgs ? 0 : setArgs.length];
		for (int i = 0; null != setArgs && i < setArgs.length; i++) {
			argsType[i] = setArgs[i].getClass();
		}
		try {
			Method m = c.getMethod(methodName, argsType);
			m.invoke(entity, setArgs);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			log.warn("CallSetMethod Failed!", e);
			throw new Exception("CallSetMethod failed!", e);
		}
	}

	/**
	 * ����תJSON
	 * 
	 * @param o
	 * @return
	 */
	public static String toJson(Object o) {
		return gson.toJson(o);
	}

	/**
	 * JSONת����
	 * 
	 * @param json
	 * @param c
	 * @return
	 * @throws JsonSyntaxException
	 */
	public static <T> T fromJson(String json, Class<T> c) throws JsonSyntaxException {
		return gson.fromJson(json, c);
	}

	/**
	 * ��ȡͼƬ�ļ���Ӧ�Ķ���������
	 * 
	 * @param filePath
	 * @return
	 */
	public static byte[] getImageBinary(String filePath) {
		File f = new File(filePath);
		BufferedImage bi;
		try {
			bi = ImageIO.read(f);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(bi, "jpg", baos);
			byte[] bytes = baos.toByteArray();
			return bytes;
		} catch (IOException e) {
			log.warn("Error occured when getImageBinary!", e);
			return null;
		}
	}
	
}
