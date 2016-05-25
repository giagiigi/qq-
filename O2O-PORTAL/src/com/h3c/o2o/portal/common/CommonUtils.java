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
 * ���ù����࣬�����ṩ��̬ʵ�ֵĹ������߷�����
 */
public class CommonUtils {
    private static final byte[] PASSWORD_KEY = "liuan814".getBytes();;

    /** ��־��¼���� */
    private static Log log = LogFactory.getLog(CommonUtils.class);

    /** ���� iMC ǰ̨���̵� Web ����˿ںš� */
    private static int[] webServerPort = null;

    /** ��������ַ��ѯʵ�塣 */
    private static ServerAddressViewer serverAddressViewer = null;

    /**
     * ��ȡ iMC ǰ̨���̵� Web ����˿ںš�
     *
     * @return �����ȡ�ɹ������س���Ϊ2�����飬����Ԫ�طֱ��� HTTP �� HTTPS �˿ڡ�
     * �����ȡʧ�ܣ����� <code>null</code>��
     */
	public static int[] getWebServerPort() {
		return getWebServerPortFromHttpProperties();
	}

	/**
     * ��XML�����ַ����й�ܴ���
     *
     * @param oStr ԭʼ�ַ���
     * @return �������ַ���
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
			// �������ļ��ж�ȡ������ Web �˿�������Ϣ
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

		// ����
		return webServerPort;
	}

    /**
     * ���� iMC ǰ̨���̵� Web ����˿ںš�
     *
     * @param serverPortParaArray ����Ϊ2�����飬����Ԫ�طֱ��� HTTP �� HTTPS �˿ںš�
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
     * ��ȡiMCǰ̨���̵�Web����˿ڲ�����
     *
     * @return �����ȡ�ɹ������س���Ϊ2��Map��Map��keyΪЭ������HTTP��HTTPS��valueΪ��ӦЭ��Ķ˿ڡ�
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
     * ��ȡ��������ַ�鿴ʵ�塣
     *
     * @return ��������ַ�鿴ʵ�塣
     */
    public static synchronized ServerAddressViewer getServerAddressViewer() {
        if (serverAddressViewer == null) {
            File fileOrPath = new File(ServerContext.getO2OHome(), "..");
            if (!new File(fileOrPath, ServerAddressViewer.FILE_NAME).exists()) {
                // ����������ʹ��
                fileOrPath = new File(ServerContext.getO2OHome(), "conf/server-addr.xml");
            }
            serverAddressViewer = new ServerAddressViewer(fileOrPath);
        }
        return serverAddressViewer;
    }

    /**
     * ���� SQL �����ʹ�õ� Escape \ ��䣬�Բ�ͬ�����ݿ��践�ز�ֵͬ��
     *
     * @return  SQL �����ʹ�õ� Escape \ ��䡣
     */
    public static String getSqlEscapeBackslash() {
        if ("MySQL".equals(getServerAddressViewer().getDbType())) {
            return "escape '\\\\'";
        } else {
            return "escape '\\'";
        }
    }

    /**
     * ȷ���ض������ĵ������Ƿ�Ϸ���
     *
     * @param prefix ������ǰ׺�������� ���� �� ����+"."+������
     * @return �������Ƿ�Ϸ���
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
     * ����һ���������Ͷ���<p>
     * �������̿��ǵ�����Ϊ null �������
     *
     * @param src ��������Դ���ڶ���
     * @return �������ֵ
     */
    public static Date cloneDate(Date src) {
        return src == null ? null : (Date) src.clone();
    }

    private static Cipher encryptDesCipher;
    private static Cipher decryptDesCipher;

    /**
     * ���ַ������м��ܣ���Ҫ�����ڱ����û�����ʱ�����м��ܲ�����
     *
     * @param plainText �ı����ġ�
     * @return �ı����ģ��������Ѿ���Base64���롣
     */
    public static synchronized byte[] encryptData(byte[] plainText) {
        //        // ���DES���ܹ���û�д������򴴽�֮
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
     * ��һ���ַ������ܣ��������ʧ�ܾͷ����ַ�������
     * @param str �������ִ�
     * @return ���ܺ�Ĵ�
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
     * ���ַ������ݽ��н��ܲ�����
     *
     * @param cryptoText ����Base64������ı����ġ�
     * @return �ı����ġ�
     */
    public static synchronized byte[] decryptData(byte[] cryptoText) {
        //        // ���DES���ܹ���û�д������򴴽�֮
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
     * ���ַ������м��ܣ���Ҫ�����ڱ����û�����ʱ�����м��ܲ�����
     *
     * @param plainText �ı����ġ�
     * @param charsetName �ַ��������ơ�
     * @return �ı����ġ�
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
     * ��һ���ַ������ܣ��������ʧ�ܾͷ����ַ�������
     *
     * @param str �������ִ���
     *
     * @return ���ܺ�Ĵ���
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
     * ���ַ������ݽ��н��ܲ�����
     *
     * @param cryptoText �ı����ġ�
     * @param charsetName �ַ��������ơ�
     * @return �ı����ġ�
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
     * ���ַ������м��ܣ���Ҫ�����ڱ����û�����ʱ�����м��ܲ�����
     * �÷���ʹ��ϵͳȱʡ�ַ����롣
     *
     * @param plainText �ı����ġ�
     * @return �ı����ġ�
     */
    public static String encryptData(String plainText) {
        return new String(encryptData(plainText.getBytes()));
    }

    /**
     * ���ַ������ݽ��н��ܲ�����
     * �÷���ʹ��ϵͳȱʡ�ַ����롣
     *
     * @param cryptoText �ı����ġ�
     * @return �ı����ġ�
     */
    public static String decryptData(String cryptoText) {
        return new String(decryptData(cryptoText.getBytes()));
    }

    /**
     * ʹ�� RSA �ǶԳ��㷨���ܡ�
     *
     * @param bytes ���ġ�
     * @param key ������Կ��
     * @return ���ġ����ʧ�ܣ����� <code>null</code>��
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
     * ʹ�� RSA �ǶԳ��㷨���ܡ�
     *
     * @param bytes ���ġ�
     * @param key ������Կ��
     * @return ���ġ����ʧ�ܣ����� <code>null</code>��
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
     * ��Like������ֵ����ת����������û��ͨ�����ǰ�󽫼���ͨ��� %
     *
     * @param oldCondition
     *            ԭʼ�Ĳ�ѯ����ֵ
     * @return ��SQL����п���ʹ�õĲ�ѯ����ֵ
     */
    public static String transformLikeCondition(String oldCondition) {
        return transformLikeCondition(oldCondition, true);
    }

    /**
     * ��Like������ֵ����ת��������������û��ͨ�������autoAppendΪtrue��ǰ�󽫼���ͨ��� % ����Ϊfalse�򲻱䡣
     * <p>
     * <ul>
     * <li>��ǰ��û��ת���ַ�"\"��"*"��"?"�ֱ�ת��Ϊ"%"��"_"�� </li>
     * <li>������"%"��"_"���ַ�ת����"\%"��"\_" </li>
     * <li>�������к���"\*"��"\?"��ǰ���ת���ַ�"\"ȥ����ת��Ϊ"*"��"?"�� </li>
     * <li>��������û��"*"��"?"����autoAppendΪtrueʱ���Զ�������ǰ�����"%"����Ϊfalse�򲻱䣻 </li>
     * </ul>
     * </p>
     *
     * @param oldCondition
     *            ԭʼ�Ĳ�ѯ����ֵ
     * @param autoAppend
     *            ����������û��ͨ�������autoAppendΪtrue��ǰ�󽫼���ͨ��� % ����Ϊfalse�򲻱䡣
     * @return ��SQL����п���ʹ�õĲ�ѯ����ֵ
     */
    public static String transformLikeCondition(String oldCondition,
        boolean autoAppend) {
        if (oldCondition == null) {
            return null;
        }
        // �ж��ַ����Ƿ��з�ASCII��
        boolean isAsciiCondition = oldCondition.length() == oldCondition
            .getBytes().length;
        // ��������û��ͨ��������������������Ƿ�ǰ�����ͨ���
        if (autoAppend) {
            if (oldCondition.equals("\\")) {
                return "%\\\\%";
            }
            if (oldCondition.endsWith("\\")) {
            }
            // ���û�� * �� ? ��ǰ���Զ�����ͨ��� %
            else if (!StringUtils.contains(oldCondition, '*')
                && !StringUtils.contains(oldCondition, '?')) {
                oldCondition = "*" + oldCondition + "*";
            }
            // ����� * �� ? ����Ҫ�ж������Ƿ���ͨ���
            else if (StringUtils.countMatches(oldCondition, "*") == StringUtils
                .countMatches(oldCondition, "\\*")
                && StringUtils.countMatches(oldCondition, "?") == StringUtils
                    .countMatches(oldCondition, "\\?")) {
                // ��ȫ��Ϊ \* ���������û��ͨ�����ǰ���Զ�����ͨ��� %
                oldCondition = "*" + oldCondition + "*";
            }
        } else {
            if (oldCondition.equals("\\")) {
                return "\\\\";
            }
        }
        // ��"%"����ת��
        oldCondition = oldCondition.replaceAll("\\x25", "\\\\%");
        // ��"_"����ת��
        oldCondition = oldCondition.replaceAll("\\x5f", "\\\\_");
        // ��"*"��"?"����ͨ��ת���������÷���ǰû��"\"ʱ��
        StringBuilder sb = new StringBuilder();
        char oldc = 0, c = 0;
        for (int i = 0; i < oldCondition.length(); i++) {
            c = oldCondition.charAt(i);
            if (c == '*' || c == '?') {
                if (oldc != '\\') {
                    // ���ǰ��û��ת�����������滻
                    sb.append(c == '*' ? (isAsciiCondition ? "%" : "%%")
                        :"_");
                } else {
                    // ���ǰ����ת�Ʒ�����ɾ����ת�Ʒ����������ַ��滻
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
                    // �������û��%��_��������滻
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
     * �ӽ����ڲ�����ʹ�á�
     *
     * @param cipher Cypher.
     * @param bytes ����/���ġ�
     * @param mode �ӽ���ģʽ��
     * @return �����
     * @throws Exception ���ִ���
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
     * �ڲ�ʹ�ô�������
     *
     * @param prefix ǰ׺�ַ���
     * @param suffix ��׺�ַ���
     * @return �����
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