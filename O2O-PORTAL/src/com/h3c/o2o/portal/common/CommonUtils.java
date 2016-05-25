/*
 * Copyright (c) 2003-2006 Huawei-3Com Technologies Co., Ltd. All rights
 * reserved. <http://www.huawei-3com.com/>
 */
package com.h3c.o2o.portal.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.h3c.oasis.o2oserver.bootstrap.ServerContext;
import com.h3c.oasis.o2oserver.util.ServerAddressViewer;

/**
 * 公用工具类，用于提供静态实现的公共工具方法。
 */
public class CommonUtils {
    private static final byte[] PASSWORD_KEY = "liuan814".getBytes();;

    /** 日志记录对象 */
    private static Log log = LogFactory.getLog(CommonUtils.class);

    /** 保存 iMC 前台进程的 Web 服务端口号。 */
    private static int[] webServerPort = null;

    /** 服务器地址查询实体。 */
    private static ServerAddressViewer serverAddressViewer = null;

    /**
     * 获取 iMC 前台进程的 Web 服务端口号。
     *
     * @return 如果获取成功，返回长度为2的数组，两个元素分别是 HTTP 和 HTTPS 端口。
     * 如果获取失败，返回 <code>null</code>。
     */
	public static int[] getWebServerPort() {
		return getWebServerPortFromHttpProperties();
	}

	/**
     * 对XML特殊字符进行规避处理
     *
     * @param oStr 原始字符串
     * @return 处理后的字符串
     */
    public static String escapeString(String oStr) {
        StringBuffer sb = new StringBuffer();
        if (oStr != null) {
            for (int i = 0; i < oStr.length(); i++) {
                char c = oStr.charAt(i);
                if (c == '&') {
                    sb.append("&amp;");
                } else if (c == '<') {
                    sb.append("&lt;");
                } else if (c == '>') {
                    sb.append("&gt;");
                } else if (c == '"') {
                    sb.append("&#034;");
                } else if (c == '\'') {
                    sb.append("&#039;");
                } else {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }

    public static int[] getWebServerPortFromServerXml() {
    	queryWebServerPort();
		return webServerPort;
    }

	public static int[] getWebServerPortFromHttpProperties() {

		int[] webServerPort = null;
		Properties prop = new Properties();
		FileInputStream fis = null;
		try {
			// 从配置文件中读取服务器 Web 端口配置信息
			fis = new FileInputStream(new File(ServerContext.getO2OHome(),
					"conf/http.properties"));
			prop.load(fis);
			int httpPort = Integer.parseInt(prop.getProperty("o2o.http.port"));
			int httpsPort = Integer
					.parseInt(prop.getProperty("o2o.https.port"));
			webServerPort = new int[] { httpPort, httpsPort };
		} catch (Exception e) {
			log.error(null, e);
		} finally {
			IOUtils.closeQuietly(fis);
		}

		// 返回
		return webServerPort;
	}

    /**
     * 设置 iMC 前台进程的 Web 服务端口号。
     *
     * @param serverPortParaArray 长度为2的数组，两个元素分别是 HTTP 和 HTTPS 端口号。
     */
	public static void setWebServerPort(int[] serverPortParaArray) {

		File serverXmlFile = new File(ServerContext.getO2OHome(),
				"conf/server.xml");

		if (!serverXmlFile.canWrite()) {
			serverXmlFile.setWritable(true);
		}

		if (serverXmlFile != null && serverXmlFile.exists()) {

			Document document = null;

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(false);

			try {
				DocumentBuilder builder = dbf.newDocumentBuilder();
				document = builder.parse(serverXmlFile);
			} catch (ParserConfigurationException pe) {
				log.warn("setWebServerPort() Parse " + serverXmlFile.getPath()
						+ " get ParserConfigurationException: ", pe);
			} catch (SAXException saxe) {
				log.warn("setWebServerPort() Parse " + serverXmlFile.getPath()
						+ " get SAXException: ", saxe);
			} catch (IOException ioe) {
				log.warn("setWebServerPort() Parse " + serverXmlFile.getPath()
						+ " get IOException: ", ioe);
			}

			Element root = document.getDocumentElement();

			int connectorElementLength = root.getElementsByTagName("Connector")
					.getLength();

			int httpPort = serverPortParaArray[0];
			int httpsPort = serverPortParaArray[1];

			if (connectorElementLength > 0) {
				for (int i = 0; i < connectorElementLength; i++) {

					Node nameNode = root.getElementsByTagName("Connector")
							.item(i);

					if (nameNode.getAttributes().getNamedItem("protocol")
							.getNodeValue().equals("HTTP/1.1")
							|| nameNode
									.getAttributes()
									.getNamedItem("protocol")
									.getNodeValue()
									.equals("org.apache.coyote.http11.Http11NioProtocol")) {
						if (nameNode.getAttributes().getNamedItem("scheme") == null
								|| StringUtils.isBlank(nameNode.getAttributes()
										.getNamedItem("scheme").getNodeValue())) {
							Node portNode = nameNode.getAttributes()
									.getNamedItem("port");
							portNode.setNodeValue(String.valueOf(httpPort));
							nameNode.getAttributes().setNamedItem(portNode);
							Node redirectPortNode = nameNode.getAttributes()
									.getNamedItem("redirectPort");
							redirectPortNode.setNodeValue(String
									.valueOf(httpsPort));
							nameNode.getAttributes().setNamedItem(
									redirectPortNode);
						} else if (nameNode.getAttributes()
								.getNamedItem("scheme").getNodeValue()
								.equals("https")) {
							Node portNode = nameNode.getAttributes()
									.getNamedItem("port");
							portNode.setNodeValue(String.valueOf(httpsPort));
							nameNode.getAttributes().setNamedItem(portNode);
						}
					}

					if (nameNode.getAttributes().getNamedItem("protocol")
							.getNodeValue().equals("AJP/1.3")) {
						if (nameNode.getAttributes().getNamedItem(
								"redirectPort") != null) {
							Node redirectPortNode = nameNode.getAttributes()
									.getNamedItem("redirectPort");
							redirectPortNode.setNodeValue(String
									.valueOf(httpsPort));

							nameNode.getAttributes().setNamedItem(
									redirectPortNode);
						}
					}
				}
			}

			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer;
			try {
				transformer = tFactory.newTransformer();

				DOMSource source = new DOMSource(document);
				StreamResult result = new StreamResult(new File(
				    ServerContext.getO2OHome(), "conf/server.xml"));
				transformer.transform(source, result);

			} catch (TransformerConfigurationException tce) {
				log.warn("setWebServerPort() Parse " + serverXmlFile.getPath()
						+ " get TransformerConfigurationException: ", tce);
			} catch (TransformerException te) {
				log.warn("setWebServerPort() Parse " + serverXmlFile.getPath()
						+ " get TransformerException: ", te);
			}

		} else {
			log.warn("Can not find file - 'conf/server.xml'.");
		}
	}

	/**
     * 获取iMC前台进程的Web服务端口参数。
     *
     * @return 如果获取成功，返回长度为2的Map。Map的key为协议名称HTTP或HTTPS，value为对应协议的端口。
     */
	public static Map<String, Integer> queryWebServerPort() {

		Map<String, Integer> result = new HashMap<String, Integer>(2);
		result.put("HTTP", 8080);
		result.put("HTTPS", 8443);
		// result.put("AJP13", 8009);
		// result.put("AJP13S", 8019);

		File serverXmlFile = new File(ServerContext.getO2OHome(), "conf/server.xml");

		if (serverXmlFile != null && serverXmlFile.exists()) {

			Document document = null;

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(false);

			try {
				DocumentBuilder builder = dbf.newDocumentBuilder();
				document = builder.parse(serverXmlFile);
			} catch (ParserConfigurationException pe) {
				log.warn("getWebServerPort() Parse " + serverXmlFile.getPath()
						+ " get ParserConfigurationException: ", pe);
			} catch (SAXException saxe) {
				log.warn("getWebServerPort() Parse " + serverXmlFile.getPath()
						+ " get SAXException: ", saxe);
			} catch (IOException ioe) {
				log.warn("getWebServerPort() Parse " + serverXmlFile.getPath()
						+ " get IOException: ", ioe);
			}

			Element root = document.getDocumentElement();

			int connectorElementLength = root.getElementsByTagName("Connector")
					.getLength();

			int httpPort = 8080;
			int httpsPort = 8443;
			int ajp13Port = 8009;
			int ajp13sPort = 8019;

			if (connectorElementLength > 0) {
				for (int i = 0; i < connectorElementLength; i++) {

					Node nameNode = root.getElementsByTagName("Connector")
							.item(i);

					if (nameNode.getAttributes().getNamedItem("protocol")
							.getNodeValue().equals("HTTP/1.1")
							|| nameNode
									.getAttributes()
									.getNamedItem("protocol")
									.getNodeValue()
									.equals("org.apache.coyote.http11.Http11NioProtocol")) {
						if (nameNode.getAttributes().getNamedItem("scheme") == null
								|| StringUtils.isBlank(nameNode.getAttributes()
										.getNamedItem("scheme").getNodeValue())) {
							httpPort = NumberUtils.createInteger(nameNode
									.getAttributes().getNamedItem("port")
									.getNodeValue());
							result.put("HTTP", httpPort);
						} else if (nameNode.getAttributes()
								.getNamedItem("scheme").getNodeValue()
								.equals("https")) {
							httpsPort = NumberUtils.createInteger(nameNode
									.getAttributes().getNamedItem("port")
									.getNodeValue());
							result.put("HTTPS", httpsPort);
						}
					} else if (nameNode.getAttributes()
							.getNamedItem("protocol").getNodeValue()
							.equals("AJP/1.3")) {
						if (nameNode.getAttributes().getNamedItem("scheme") == null
								|| StringUtils.isBlank(nameNode.getAttributes()
										.getNamedItem("scheme").getNodeValue())) {
							ajp13Port = NumberUtils.createInteger(nameNode
									.getAttributes().getNamedItem("port")
									.getNodeValue());
							result.put("AJP13", ajp13Port);
						} else if (nameNode.getAttributes()
								.getNamedItem("scheme").getNodeValue()
								.equals("https")) {
							ajp13sPort = NumberUtils.createInteger(nameNode
									.getAttributes().getNamedItem("port")
									.getNodeValue());
							result.put("AJP13S", ajp13sPort);
						}
					}
				}
			}

			webServerPort = new int[] { httpPort, httpsPort };

		} else {
			log.warn("Can not find file - 'conf/server.xml'.");
		}

		return result;
	}

    /**
     * 获取服务器地址查看实体。
     *
     * @return 服务器地址查看实体。
     */
    public static synchronized ServerAddressViewer getServerAddressViewer() {
        if (serverAddressViewer == null) {
            File fileOrPath = new File(ServerContext.getO2OHome(), "..");
            if (!new File(fileOrPath, ServerAddressViewer.FILE_NAME).exists()) {
                // 开发环境下使用
                fileOrPath = new File(ServerContext.getO2OHome(), "conf/server-addr.xml");
            }
            serverAddressViewer = new ServerAddressViewer(fileOrPath);
        }
        return serverAddressViewer;
    }

    /**
     * 返回 SQL 语句中使用的 Escape \ 语句，对不同的数据库需返回不同值。
     *
     * @return  SQL 语句中使用的 Escape \ 语句。
     */
    public static String getSqlEscapeBackslash() {
        if ("MySQL".equals(getServerAddressViewer().getDbType())) {
            return "escape '\\\\'";
        } else {
            return "escape '\\'";
        }
    }

    /**
     * 确认特定方法的调用者是否合法。
     *
     * @param prefix 调用者前缀，可以是 包名 或 包名+"."+类名。
     * @return 调用者是否合法。
     */
    public static boolean confirmCaller(String prefix) {
        Throwable dummyException = new Throwable();
        StackTraceElement locations[] = dummyException.getStackTrace();
        if (locations != null && locations.length > 2) {
            StackTraceElement caller = locations[2];
            String name = caller.getClassName() + "." + caller.getMethodName();
            return name.startsWith(prefix);
        }
        return false;
    }

    /**
     * 拷贝一个日期类型对象。<p>
     * 拷贝过程考虑到参数为 null 的情况。
     *
     * @param src 待拷贝的源日期对象
     * @return 拷贝后的值
     */
    public static Date cloneDate(Date src) {
        return src == null ? null : (Date) src.clone();
    }

    private static Cipher encryptDesCipher;
    private static Cipher decryptDesCipher;

    /**
     * 对字符串进行加密，主要用于在保存用户密码时，进行加密操作。
     *
     * @param plainText 文本明文。
     * @return 文本密文，该密文已经过Base64编码。
     */
    public static synchronized byte[] encryptData(byte[] plainText) {
        //        // 如果DES加密工具没有创建，则创建之
        try {
            if (encryptDesCipher == null) {
                DESKeySpec desKeySpec = new DESKeySpec(PASSWORD_KEY);
                SecretKeyFactory factory = SecretKeyFactory.getInstance("DES");
                SecretKey desSecretKey = factory.generateSecret(desKeySpec);
                encryptDesCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
                encryptDesCipher.init(Cipher.ENCRYPT_MODE, desSecretKey);
            }
            return Base64.encode(encryptDesCipher.doFinal(plainText));
        } catch (Exception e) {
            log.error(null, e);
            return null;
        }

    }

    /**
     * 对一个字符串解密，如果解密失败就返回字符串本身。
     * @param str 待解密字串
     * @return 解密后的串
     */
    public static String decryptStr(String str){

        if(StringUtils.isBlank(str)){
            return str;
        }

        byte[] bytes = CommonUtils.decryptData(StringUtils
            .trimToEmpty(str).getBytes());

        if(bytes == null){
            return str;
        }

        return new String(bytes);

    }

    /**
     * 对字符串数据进行解密操作。
     *
     * @param cryptoText 经过Base64编码的文本密文。
     * @return 文本明文。
     */
    public static synchronized byte[] decryptData(byte[] cryptoText) {
        //        // 如果DES解密工具没有创建，则创建之
        try {
            if (decryptDesCipher == null) {
                DESKeySpec desKeySpec = new DESKeySpec(PASSWORD_KEY);
                SecretKeyFactory factory = SecretKeyFactory.getInstance("DES");
                SecretKey desSecretKey = factory.generateSecret(desKeySpec);
                decryptDesCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
                decryptDesCipher.init(Cipher.DECRYPT_MODE, desSecretKey);
            }
            return decryptDesCipher.doFinal(Base64.decode(cryptoText));
        } catch (Throwable t) {
            return null;
        }
    }

    public static void main(String[] args) {
        String data = encryptData("00000");
        System.out.println(data);
        System.out.println(decryptData(data));
    }

    /**
     * 对字符串进行加密，主要用于在保存用户密码时，进行加密操作。
     *
     * @param plainText 文本明文。
     * @param charsetName 字符编码名称。
     * @return 文本密文。
     */
    public static String encryptData(String plainText, String charsetName) {
        try {
            return new String(encryptData(plainText.getBytes(charsetName)),
                charsetName);
        } catch (UnsupportedEncodingException e) {
            log.error(null, e);
            return null;
        }
    }

    /**
     * 对一个字符串加密，如果加密失败就返回字符串本身。
     *
     * @param str 待加密字串。
     *
     * @return 加密后的串。
     */
	public static String encryptStr(String str) {

		if (StringUtils.isBlank(str)) {
			return str;
		}

		byte[] bytes = CommonUtils.encryptData(StringUtils.trimToEmpty(str)
				.getBytes());

		if (bytes == null) {
			return str;
		}

		return new String(bytes);
	}

    /**
     * 对字符串数据进行解密操作。
     *
     * @param cryptoText 文本密文。
     * @param charsetName 字符编码名称。
     * @return 文本明文。
     */
    public static String decryptData(String cryptoText, String charsetName) {
        try {
            return new String(decryptData(cryptoText.getBytes(charsetName)),
                charsetName);
        } catch (UnsupportedEncodingException e) {
            log.error(null, e);
            return null;
        }
    }

    /**
     * 对字符串进行加密，主要用于在保存用户密码时，进行加密操作。
     * 该方法使用系统缺省字符编码。
     *
     * @param plainText 文本明文。
     * @return 文本密文。
     */
    public static String encryptData(String plainText) {
        return new String(encryptData(plainText.getBytes()));
    }

    /**
     * 对字符串数据进行解密操作。
     * 该方法使用系统缺省字符编码。
     *
     * @param cryptoText 文本密文。
     * @return 文本明文。
     */
    public static String decryptData(String cryptoText) {
        return new String(decryptData(cryptoText.getBytes()));
    }

    /**
     * 使用 RSA 非对称算法加密。
     *
     * @param bytes 明文。
     * @param key 加密密钥。
     * @return 密文。如果失败，返回 <code>null</code>。
     */
    public static byte[] encrypt(byte[] bytes, Key key) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return blockCipher(cipher, bytes, Cipher.ENCRYPT_MODE);
        } catch (Exception e) {
            log.error(null, e);
            return null;
        }
    }

    /**
     * 使用 RSA 非对称算法解密。
     *
     * @param bytes 密文。
     * @param key 解密密钥。
     * @return 明文。如果失败，返回 <code>null</code>。
     */
    public static byte[] decrypt(byte[] bytes, Key key) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, key);
            return blockCipher(cipher, bytes, Cipher.DECRYPT_MODE);
        } catch (Exception e) {
            log.error(null, e);
            return null;
        }
    }

    /**
     * 对Like条件的值进行转换，若其中没有通配符则前后将加上通配符 %
     *
     * @param oldCondition
     *            原始的查询条件值
     * @return 在SQL语句中可以使用的查询条件值
     */
    public static String transformLikeCondition(String oldCondition) {
        return transformLikeCondition(oldCondition, true);
    }

    /**
     * 对Like条件的值进行转换。对于条件中没有通配符，若autoAppend为true则前后将加上通配符 % ，若为false则不变。
     * <p>
     * <ul>
     * <li>将前面没有转义字符"\"的"*"和"?"分别转换为"%"和"_"； </li>
     * <li>将含有"%"和"_"的字符转换成"\%"和"\_" </li>
     * <li>若条件中含有"\*"和"\?"则将前面的转义字符"\"去掉，转换为"*"和"?"； </li>
     * <li>若条件中没有"*"和"?"，当autoAppend为true时则自动在条件前后加入"%"，若为false则不变； </li>
     * </ul>
     * </p>
     *
     * @param oldCondition
     *            原始的查询条件值
     * @param autoAppend
     *            对于条件中没有通配符，若autoAppend为true则前后将加上通配符 % ，若为false则不变。
     * @return 在SQL语句中可以使用的查询条件值
     */
    public static String transformLikeCondition(String oldCondition,
        boolean autoAppend) {
        if (oldCondition == null) {
            return null;
        }
        // 判断字符中是否有非ASCII码
        boolean isAsciiCondition = oldCondition.length() == oldCondition
            .getBytes().length;
        // 若条件中没有通配符则根据输入参数决定是否前后加上通配符
        if (autoAppend) {
            if (oldCondition.equals("\\")) {
                return "%\\\\%";
            }
            if (oldCondition.endsWith("\\")) {
            }
            // 如果没有 * 或 ? 则前后自动加上通配符 %
            else if (!StringUtils.contains(oldCondition, '*')
                && !StringUtils.contains(oldCondition, '?')) {
                oldCondition = "*" + oldCondition + "*";
            }
            // 如果有 * 或 ? 则需要判定其中是否有通配符
            else if (StringUtils.countMatches(oldCondition, "*") == StringUtils
                .countMatches(oldCondition, "\\*")
                && StringUtils.countMatches(oldCondition, "?") == StringUtils
                    .countMatches(oldCondition, "\\?")) {
                // 若全部为 \* 则表明其中没有通配符，前后自动加上通配符 %
                oldCondition = "*" + oldCondition + "*";
            }
        } else {
            if (oldCondition.equals("\\")) {
                return "\\\\";
            }
        }
        // 对"%"进行转义
        oldCondition = oldCondition.replaceAll("\\x25", "\\\\%");
        // 对"_"进行转义
        oldCondition = oldCondition.replaceAll("\\x5f", "\\\\_");
        // 对"*"和"?"进行通配转换（仅当该符号前没有"\"时）
        StringBuilder sb = new StringBuilder();
        char oldc = 0, c = 0;
        for (int i = 0; i < oldCondition.length(); i++) {
            c = oldCondition.charAt(i);
            if (c == '*' || c == '?') {
                if (oldc != '\\') {
                    // 如果前面没有转义符，则进行替换
                    sb.append(c == '*' ? (isAsciiCondition ? "%" : "%%")
                        :"_");
                } else {
                    // 如果前面有转移符，则删掉该转移符，不进行字符替换
                    sb.deleteCharAt(sb.length() - 1);
                    sb.append(c);
                }
            } else {
                sb.append(c);
            }
            oldc = c;
        }

        oldCondition = sb.toString();
        char newc = 0;
        if (oldCondition.length() > 0) {
            newc = oldCondition.charAt(0);
        }

        c = 0;
        sb = new StringBuilder();
        for (int i = 0; i < oldCondition.length(); i++) {
            c = oldCondition.charAt(i);
            if (i < oldCondition.length() - 1) {
                newc = oldCondition.charAt(i + 1);
            } else {
                newc = 0;
            }
            if (c == '\\') {
                if (newc != '%' && newc != '_') {
                    // 如果后面没有%或_，则进行替换
                    sb.append("\\\\");
                } else {
                    sb.append(c);
                }
            } else {
                sb.append(c);
            }
        }
        oldCondition = sb.toString();
        if (oldCondition.endsWith("\\")) {
            if (autoAppend) {
                oldCondition = "%" + oldCondition + "%";
            }
        }
        return oldCondition;

    }

    /**
     * 加解密内部处理使用。
     *
     * @param cipher Cypher.
     * @param bytes 明文/密文。
     * @param mode 加解密模式。
     * @return 结果。
     * @throws Exception 出现错误。
     */
    private static byte[] blockCipher(Cipher cipher, byte[] bytes, int mode)
        throws Exception {
        // string initialize 2 buffers.
        // scrambled will hold intermediate results
        byte[] scrambled = new byte[0];

        // toReturn will hold the total result
        byte[] toReturn = new byte[0];
        // if we encrypt we use 100 byte long blocks. Decryption requires 128
        // byte long blocks (because of RSA)
        int length = (mode == Cipher.ENCRYPT_MODE) ? 100 : 128;

        // another buffer. this one will hold the bytes that have to be modified in this step
        byte[] buffer = new byte[length];

        for (int i = 0; i < bytes.length; i++) {
            // if we filled our buffer array we have our block ready for de- or encryption
            if ((i > 0) && (i % length == 0)) {
                //execute the operation
                scrambled = cipher.doFinal(buffer);
                // add the result to our total result.
                toReturn = append(toReturn, scrambled);
                // here we calculate the length of the next buffer required
                int newlength = length;

                // if newlength would be longer than remaining bytes in the
                // bytes array we shorten it.
                if (i + length > bytes.length) {
                    newlength = bytes.length - i;
                }
                // clean the buffer array
                buffer = new byte[newlength];
            }
            // copy byte into our buffer.
            buffer[i % length] = bytes[i];
        }

        // this step is needed if we had a trailing buffer. should only happen
        // when encrypting. example: we encrypt 110 bytes. 100 bytes per run means
        // we "forgot" the last 10 bytes. they are in the buffer array
        scrambled = cipher.doFinal(buffer);

        // final step before we can return the modified data.
        toReturn = append(toReturn, scrambled);

        return toReturn;
    }

    /**
     * 内部使用处理方法。
     *
     * @param prefix 前缀字符。
     * @param suffix 后缀字符。
     * @return 结果。
     */
    private static byte[] append(byte[] prefix, byte[] suffix) {
        byte[] toReturn = new byte[prefix.length + suffix.length];
        for (int i = 0; i < prefix.length; i++) {
            toReturn[i] = prefix[i];
        }
        for (int i = 0; i < suffix.length; i++) {
            toReturn[i + prefix.length] = suffix[i];
        }
        return toReturn;
    }

	public static String getComponentIpAddress(String compId) {
		ServerAddressViewer viewer = getServerAddressViewer();
		for (ServerAddressViewer.ServerAddressConfig comp : viewer.getConfigs()) {
			if (comp.getCompId().equals(compId)) {
				return comp.getAddress();
			}
		}
		return null;
	}
}