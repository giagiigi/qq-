/*
 * Copyright (c) 2007-2015, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC V700R001
 * Module Name :
 * Date Created: 2015-6-18
 * Creator     : ykf2685
 * Description :
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 * 2015-6-18  ykf2685             O2O-PORTAL project, new code file.
 *
 *------------------------------------------------------------------------------
 */

package com.h3c.o2o.portal.wxwifi.func;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URLDecoder;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.protocol.HTTP;

import com.h3c.o2o.portal.PortalErrorCodes;
import com.h3c.o2o.portal.PortalException;
import com.h3c.o2o.portal.cache.api.CacheApiMgr;
import com.h3c.o2o.portal.common.FuncUtil;
import com.h3c.o2o.portal.common.HttpDownload;
import com.h3c.o2o.portal.common.HttpOrHttpsMgr;
import com.h3c.o2o.portal.entity.WXWifiTemplate;
import com.h3c.oasis.o2oserver.bootstrap.ServerContext;

import net.sf.json.JSONObject;

/**
 * ���ڴ�����ҳ��ķ�����ɾ����Ϣ��
 *
 * @author hkf6496
 *
 */
public class WXWiFiListener {

    private Log log = LogFactory.getLog(getClass());
    
    private HttpOrHttpsMgr httpOrHttps;

	private WXWifiMgr portalWXWifiMgr;

    /** ���ݻ���ӿ� */
    private CacheApiMgr cacheApiMgr;

    private static String INSTALL_PATH = System.getProperty("o2o.home") + File.separator + "web" + File.separator + "apps" + File.separator + "portal" + File.separator + "home";

    private static String BASE_PAGE_PATH = INSTALL_PATH + File.separator + "basepage";

    private final String OPERATE_DRAW = "draw";

    private final String OPERATE_DELETE = "delete";

    private final String OPERATE_ADD = "add";


    /**
     * ���ݲ������ͷַ�����ִ��ҳ�����ɣ��������sevlet��һ����ӡִ�н����
     *
     * @throws IOException IO�쳣
     */
    @SuppressWarnings({ "unchecked", "deprecation" })
    public String handleMessage(Object body) {

        log.info("call from foreground");

        JSONObject result = new JSONObject();
        try {
            // �����Ͻ���
            String reqBody = (String)body;
            JSONObject obj = JSONObject.fromObject(URLDecoder.decode(reqBody, HTTP.UTF_8));

            // ��������
            Long templateId = StringUtils.isBlank(obj.getString("templateId")) ?
                -999L : Long.parseLong(obj.getString("templateId"));
            String operate = StringUtils.isBlank(obj.getString("operate")) ?
                "" : obj.getString("operate");
            if(null == httpOrHttps){
            	httpOrHttps = (HttpOrHttpsMgr)getMgr("httpOrHttps");
            }
            String urlPre ="https://" + FuncUtil.getPortalDomain()
                + "/o2o/uam/weixinwifi";
            List<String> urlList = (List<String>)obj.get("urlList");
            // ˵����������
            if(-999L == templateId || StringUtils.isEmpty(urlPre) || StringUtils.isEmpty(operate)) {
                throw new PortalException(PortalErrorCodes.PARAMETER_ILLEGAL);
            }
            // ȡ��Ԥע�����Բ�ѯMgr
            if (null == portalWXWifiMgr) {
                portalWXWifiMgr = (WXWifiMgr) getMgr("portalWXWifiMgr");
            }
            if (null == cacheApiMgr) {
                cacheApiMgr = (CacheApiMgr) getMgr("cacheApiMgr");
            }
            // У���¼�Ƿ����
            WXWifiTemplate home = portalWXWifiMgr.queryWXWifiTemplate(templateId);
            if(null == home) {
                throw new PortalException(PortalErrorCodes.ITEM_NOT_EXIST);
            }

            if(null != urlList && urlList.size() > 0) {
                for(String url : urlList) {
                    // ��������ļ�ʧ�ܣ������
                    if(!HttpDownload.downLoad(urlPre + "/" + url.replace("\\", "/"),
                        INSTALL_PATH + File.separator + url)) {
                        throw new PortalException(PortalErrorCodes.HTTP_DOWN_ERROR);
                    }
                }
            }
            /*if(null != urlList && urlList.size() > 0) {
                for(String url : urlList) {
                    // ��������ļ�ʧ�ܣ������
                    if(!HttpDownload.downLoad("http://172.27.12.43:80/o2o/uam/weixinwifi" + "/" + url.replace("\\", "/"),
                        INSTALL_PATH + File.separator + url)) {
                        throw new PortalException(PortalErrorCodes.HTTP_DOWN_ERROR);
                    }
                }
            }*/

            if(OPERATE_DRAW.equalsIgnoreCase(operate)) {
                try {
                    // ����ǻ�������ʱ�򷢲�����
                    copyFile(home.getLastModTime() ,
                    		BASE_PAGE_PATH + File.separator + home.getFileName(),
                    		INSTALL_PATH + File.separator + home.getPathName() + File.separator + home.getFileName(),
                    		home.getPageHtml());
                } catch (Exception e) {
                   log.warn(null, e);
                   // ��������ļ�ʧ�ܣ���ɾ�����ļ�
                   File file = new File(INSTALL_PATH + File.separator + home.getPathName() + File.separator + home.getFileName());
                   if(file.exists() && file.length() == 0) {
                      file.delete();
                   }
                   throw e;
                }
            } else if(OPERATE_ADD.equalsIgnoreCase(operate)){
            	try {
            			copyFile(home.getLastModTime(),
                        		BASE_PAGE_PATH + File.separator + home.getFileName(),
                        		INSTALL_PATH + File.separator + home.getPathName() + File.separator + home.getFileName(),
                        		home.getPageHtml());
                } catch (Exception e) {
                   log.warn(null, e);
                   // ��������ļ�ʧ�ܣ���ɾ�����ļ�
                   File file = new File(INSTALL_PATH + File.separator + home.getPathName() + File.separator + home.getFileName());
                   if(file.exists() && file.length() == 0) {
                      file.delete();
                   }
                   throw e;
                }
            }else if(OPERATE_DELETE.equalsIgnoreCase(operate)){
                // �����ɾ������
                deleteFile(new File(INSTALL_PATH + File.separator + home.getPathName()));
            }
            result.put("errorCode", 0);
            // У��ģ���Ƿ��ѻ��棬���������»���
            /*if (cacheApiMgr.getWxWiFiCacheMgr().get(home.getId()) != null){
				cacheApiMgr.getWxWiFiCacheMgr().put(
				    home.getId(), home);
				if (log.isDebugEnabled()) {
					log.debug("Refresh cached wxwifitemplate: "
							+ home.toString());
				}
            }*/
        } catch (Exception e) {
            log.warn(null, e);
            result.put("errorCode", 50010);
        }
        log.info("call success ");
        return result.toString();
    }

    /**
     * �����ļ������滻�ļ����ݡ�
     *
     * @throws Exception
     */
    private void copyFile(long fileLastModified, String sourceFilePath,
        String desFilePath, String replaceContent) throws Exception {
        try {
            File sourceFile = new File(sourceFilePath);
            File desFile = new File(desFilePath);
            // ����ļ����ڣ���У���ļ����޸�ʱ��
            if(desFile.exists()) {
                long lastModified = Long.valueOf(readModTime(desFile));
                // ������ݿ��ʱ������ļ����޸�ʱ�䣬��ɾ���ļ������������ļ�
                if(lastModified < fileLastModified) {
                    // ��ɾ���ļ�
                    if(desFile.delete()) {
                        // ���´����ļ�
                        desFile = new File(desFilePath);
                        desFile.createNewFile();
                    }
                }
            } else {
                // ����ļ������ڣ��򴴽��ļ��Լ�Ŀ¼
               if(!desFile.getParentFile().exists()){
                    desFile.getParentFile().mkdirs();
                }
                if(!desFile.exists()){
                    desFile.createNewFile();
                }
            }
            // �����ļ�
            makeFile(fileLastModified,sourceFile,desFile,replaceContent);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * ��ȡ�ļ�����������޸�ʱ�䡣
     *
     * @param file �ļ�
     * @return ʱ���ʽ���ַ���
     * @throws Exception
     */
    private String readModTime(File file) throws Exception {
        InputStream is = null;
        String line = "0";
        BufferedReader reader = null;
        try {
            is = new FileInputStream(file);
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            // ֻ��ȡһ��
            for (line = reader.readLine(); line != null; ) {
                line = line.trim();
                line = line.replace("<!--", "").replace("-->", "");
                break;
            }
        } catch (Exception e){
            log.warn(e);
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
                if (reader != null) {
                    reader.close();
                    reader = null;
                }
            } catch (IOException e) {
                throw e;
            }
        }
        return line;
    }

    /**
     * �����ļ����������ļ���
     *
     * @param fileLastModified �ļ�����ʱ��
     * @param sourceFilePath Դ�ļ�
     * @param desFilePath Ŀ���ļ�
     * @param replaceContent �ļ��滻����
     * @throws Exception
     */
    private void makeFile(long fileLastModified, File sourceFile,
        File desFile, String replaceContent) throws Exception {
        BufferedReader in = null;
        OutputStreamWriter out = null;
        try {
            FileInputStream fi = new FileInputStream(sourceFile);
            in = new BufferedReader(new InputStreamReader(fi,"UTF-8"));
            out = new OutputStreamWriter(new FileOutputStream(desFile), "UTF-8");

            String sLine = new String();
            boolean firstFlag = true;
            while ((sLine = in.readLine()) != null) {
                // ����ǵ�һ�У�������ļ�������ʱ��
                if(firstFlag) {
                    out.write("<!--" + fileLastModified + "-->");
                    out.write("\r\n");
                    out.write(sLine);
                    out.write("\r\n");
                    firstFlag = false;
                    continue;
                }
                // ���ļ��е��ַ��滻���������ļ�����
                if ("<!-- content -->".equalsIgnoreCase(sLine.trim())) {
                	out.write(new String(replaceContent));
                } else {
                    out.write(sLine);
                }
                out.write("\r\n");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (in != null) {
                    in.close();
                    in = null;
                }
                if (out != null) {
                    out.close();
                    out = null;
                }
            } catch (Exception e) {
                throw e;
            }
        }
    }

    /**
     * ɾ���ļ���
     *
     * @param destFile Ŀ���ļ�
     * @throws Exception
     */
    private void deleteFile(File destFile) throws Exception {
        /**
         * ɾ���ļ����µ������ļ�
         * @param oldPath
         */
        if (destFile.isDirectory()) {
            // ɾ���ļ������ļ�
            File[] files = destFile.listFiles();
            if (files != null) {
	            for (File file : files) {
	                deleteFile(file);
	            }
            }
            // ɾ���ļ���
            destFile.delete();
        } else {
            destFile.delete();
        }
    }

    /**
     * ��ȡMGR�ӿڡ�
     * @param request HTTP request����
     * @param mgrName MGR����
     * @return bean MGRʵ��
     */
    private Object getMgr(String mgrName) {
        return ServerContext.getRootAppContext().getBean(mgrName);
    }

    public WXWifiMgr getPortalWXWifiMgr() {
        return portalWXWifiMgr;
    }

    public void setPortalWXWifiMgr(WXWifiMgr portalWXWifiMgr) {
        this.portalWXWifiMgr = portalWXWifiMgr;
    }
    
    public HttpOrHttpsMgr getHttpOrHttps() {
		return httpOrHttps;
	}

	public void setHttpOrHttps(HttpOrHttpsMgr httpOrHttps) {
		this.httpOrHttps = httpOrHttps;
	}
}
