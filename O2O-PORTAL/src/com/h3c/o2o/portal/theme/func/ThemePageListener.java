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

package com.h3c.o2o.portal.theme.func;

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
import com.h3c.o2o.portal.entity.ThemePage;
import com.h3c.o2o.portal.entity.ThemeTemplate;
import com.h3c.oasis.o2oserver.bootstrap.ServerContext;

import net.sf.json.JSONObject;

/**
 * ���ڴ�����ҳ��ķ�����ɾ����Ϣ��
 *
 * @author ykf2685
 *
 */
public class ThemePageListener {

    private Log log = LogFactory.getLog(getClass());

    private ThemePageMgr portalThemePageMgr;
    
    private HttpOrHttpsMgr httpOrHttps;

    /** ���ݻ���ӿ� */
    private CacheApiMgr cacheApiMgr;

    private static String INSTALL_PATH = System.getProperty("o2o.home") + File.separator + "web" + File.separator + "apps" + File.separator + "portal" + File.separator + "theme";

    private static String BASE_PAGE_PATH = INSTALL_PATH + File.separator + "basePage";

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
            Long subId = StringUtils.isBlank(obj.getString("subId")) ?
                -999L : Long.parseLong(obj.getString("subId"));
            String operate = StringUtils.isBlank(obj.getString("operate")) ?
                "" : obj.getString("operate");
            if(null == this.httpOrHttps){
            	this.httpOrHttps = (HttpOrHttpsMgr)getMgr("httpOrHttps");
            }
            String urlPre ="https://" + FuncUtil.getPortalDomain() + "/o2o/uam/theme";
            List<String> urlList = (List<String>)obj.get("urlList");
            // ˵����������
            if(-999L == subId || StringUtils.isEmpty(urlPre) || StringUtils.isEmpty(operate)) {
                throw new PortalException(PortalErrorCodes.PARAMETER_ILLEGAL);
            }
            // ȡ��Ԥע�����Բ�ѯMgr
            if (null == portalThemePageMgr) {
                portalThemePageMgr = (ThemePageMgr) getMgr("portalThemePageMgr");
            }
            if (null == cacheApiMgr) {
                cacheApiMgr = (CacheApiMgr) getMgr("cacheApiMgr");
            }
            // У���¼�Ƿ����
            ThemePage page = portalThemePageMgr.queryById(subId);
            if(null == page) {
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

            if(OPERATE_DRAW.equalsIgnoreCase(operate)) {
                try {
                    // ����ǻ�������ʱ�򷢲�����
                    copyFile(page.getLastModTime(),
                    		BASE_PAGE_PATH + File.separator + page.getFileName(),
                    		INSTALL_PATH + File.separator + page.getPathName() + File.separator + page.getFileName(),
                    		page.getPageHtml());
                } catch (Exception e) {
                   log.warn(null, e);
                   // ��������ļ�ʧ�ܣ���ɾ�����ļ�
                   File file = new File(INSTALL_PATH + File.separator + page.getPathName() + File.separator + page.getFileName());
                   if(file.exists() && file.length() == 0) {
                      file.delete();
                   }
                   throw e;
                }
            } else if(OPERATE_ADD.equalsIgnoreCase(operate)){
            	try {
                    // ���������ģ��
            		ThemeTemplate theme = page.getTemplate();
            		List<ThemePage> pages = theme.getPages();
            		for(ThemePage pp : pages){
            			copyFile(pp.getLastModTime(),
                        		BASE_PAGE_PATH + File.separator + pp.getFileName(),
                        		INSTALL_PATH + File.separator + pp.getPathName() + File.separator + pp.getFileName(),
                        		pp.getPageHtml());
            		}
                } catch (Exception e) {
                   log.warn(null, e);
                   // ��������ļ�ʧ�ܣ���ɾ�����ļ�
                   File file = new File(INSTALL_PATH + File.separator + page.getPathName() + File.separator + page.getFileName());
                   if(file.exists() && file.length() == 0) {
                      file.delete();
                   }
                   throw e;
                }
            }else if(OPERATE_DELETE.equalsIgnoreCase(operate)){
                // �����ɾ������
                deleteFile(new File(INSTALL_PATH + File.separator + page.getPathName()));
            }
            result.put("errorCode", 0);
            // У��ģ���Ƿ��ѻ��棬���������»���
            if (cacheApiMgr.getThemeTemplateCacheMgr().get(page.getTemplate().getId()) != null){
				cacheApiMgr.getThemeTemplateCacheMgr().put(
						page.getTemplate().getId(), page.getTemplate());
				if (log.isDebugEnabled()) {
					log.debug("Refresh cached template: "
							+ page.getTemplate().toString());
				}
            }
        } catch (Exception e) {
            log.warn(null, e);
            result.put("errorCode", 50010);
        }

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

    public ThemePageMgr getPortalThemePageMgr() {
        return portalThemePageMgr;
    }

    public void setPortalThemePageMgr(ThemePageMgr portalThemePageMgr) {
        this.portalThemePageMgr = portalThemePageMgr;
    }

	public HttpOrHttpsMgr getHttpOrHttps() {
		return httpOrHttps;
	}

	public void setHttpOrHttps(HttpOrHttpsMgr httpOrHttps) {
		this.httpOrHttps = httpOrHttps;
	}
}
