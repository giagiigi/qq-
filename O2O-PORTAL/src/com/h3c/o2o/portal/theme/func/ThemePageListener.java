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
 * 用于处理定制页面的发布，删除消息。
 *
 * @author ykf2685
 *
 */
public class ThemePageListener {

    private Log log = LogFactory.getLog(getClass());

    private ThemePageMgr portalThemePageMgr;
    
    private HttpOrHttpsMgr httpOrHttps;

    /** 数据缓存接口 */
    private CacheApiMgr cacheApiMgr;

    private static String INSTALL_PATH = System.getProperty("o2o.home") + File.separator + "web" + File.separator + "apps" + File.separator + "portal" + File.separator + "theme";

    private static String BASE_PAGE_PATH = INSTALL_PATH + File.separator + "basePage";

    private final String OPERATE_DRAW = "draw";

    private final String OPERATE_DELETE = "delete";

    private final String OPERATE_ADD = "add";


    /**
     * 根据操作类型分发请求，执行页面生成，向请求该sevlet的一方打印执行结果。
     *
     * @throws IOException IO异常
     */
    @SuppressWarnings({ "unchecked", "deprecation" })
    public String handleMessage(Object body) {

        log.info("call from foreground");

        JSONObject result = new JSONObject();
        try {
            // 将资料解码
            String reqBody = (String)body;
            JSONObject obj = JSONObject.fromObject(URLDecoder.decode(reqBody, HTTP.UTF_8));

            // 解析参数
            Long subId = StringUtils.isBlank(obj.getString("subId")) ?
                -999L : Long.parseLong(obj.getString("subId"));
            String operate = StringUtils.isBlank(obj.getString("operate")) ?
                "" : obj.getString("operate");
            if(null == this.httpOrHttps){
            	this.httpOrHttps = (HttpOrHttpsMgr)getMgr("httpOrHttps");
            }
            String urlPre ="https://" + FuncUtil.getPortalDomain() + "/o2o/uam/theme";
            List<String> urlList = (List<String>)obj.get("urlList");
            // 说明参数有误
            if(-999L == subId || StringUtils.isEmpty(urlPre) || StringUtils.isEmpty(operate)) {
                throw new PortalException(PortalErrorCodes.PARAMETER_ILLEGAL);
            }
            // 取得预注册属性查询Mgr
            if (null == portalThemePageMgr) {
                portalThemePageMgr = (ThemePageMgr) getMgr("portalThemePageMgr");
            }
            if (null == cacheApiMgr) {
                cacheApiMgr = (CacheApiMgr) getMgr("cacheApiMgr");
            }
            // 校验记录是否存在
            ThemePage page = portalThemePageMgr.queryById(subId);
            if(null == page) {
                throw new PortalException(PortalErrorCodes.ITEM_NOT_EXIST);
            }

            if(null != urlList && urlList.size() > 0) {
                for(String url : urlList) {
                    // 如果下载文件失败，则结束
                    if(!HttpDownload.downLoad(urlPre + "/" + url.replace("\\", "/"),
                        INSTALL_PATH + File.separator + url)) {
                        throw new PortalException(PortalErrorCodes.HTTP_DOWN_ERROR);
                    }
                }
            }

            if(OPERATE_DRAW.equalsIgnoreCase(operate)) {
                try {
                    // 如果是画布绘制时候发布数据
                    copyFile(page.getLastModTime(),
                    		BASE_PAGE_PATH + File.separator + page.getFileName(),
                    		INSTALL_PATH + File.separator + page.getPathName() + File.separator + page.getFileName(),
                    		page.getPageHtml());
                } catch (Exception e) {
                   log.warn(null, e);
                   // 如果复制文件失败，则删除该文件
                   File file = new File(INSTALL_PATH + File.separator + page.getPathName() + File.separator + page.getFileName());
                   if(file.exists() && file.length() == 0) {
                      file.delete();
                   }
                   throw e;
                }
            } else if(OPERATE_ADD.equalsIgnoreCase(operate)){
            	try {
                    // 如果是增加模板
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
                   // 如果复制文件失败，则删除该文件
                   File file = new File(INSTALL_PATH + File.separator + page.getPathName() + File.separator + page.getFileName());
                   if(file.exists() && file.length() == 0) {
                      file.delete();
                   }
                   throw e;
                }
            }else if(OPERATE_DELETE.equalsIgnoreCase(operate)){
                // 如果是删除数据
                deleteFile(new File(INSTALL_PATH + File.separator + page.getPathName()));
            }
            result.put("errorCode", 0);
            // 校验模板是否已缓存，如果是则更新缓存
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
     * 复制文件，并替换文件内容。
     *
     * @throws Exception
     */
    private void copyFile(long fileLastModified, String sourceFilePath,
        String desFilePath, String replaceContent) throws Exception {
        try {
            File sourceFile = new File(sourceFilePath);
            File desFile = new File(desFilePath);
            // 如果文件存在，则校验文件的修改时间
            if(desFile.exists()) {
                long lastModified = Long.valueOf(readModTime(desFile));
                // 如果数据库的时间大于文件的修改时间，则删除文件，重新生成文件
                if(lastModified < fileLastModified) {
                    // 先删除文件
                    if(desFile.delete()) {
                        // 重新创建文件
                        desFile = new File(desFilePath);
                        desFile.createNewFile();
                    }
                }
            } else {
                // 如果文件不存在，则创建文件以及目录
               if(!desFile.getParentFile().exists()){
                    desFile.getParentFile().mkdirs();
                }
                if(!desFile.exists()){
                    desFile.createNewFile();
                }
            }
            // 生成文件
            makeFile(fileLastModified,sourceFile,desFile,replaceContent);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 读取文件中首行最后修改时间。
     *
     * @param file 文件
     * @return 时间格式的字符串
     * @throws Exception
     */
    private String readModTime(File file) throws Exception {
        InputStream is = null;
        String line = "0";
        BufferedReader reader = null;
        try {
            is = new FileInputStream(file);
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            // 只读取一行
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
     * 根据文件内容生成文件。
     *
     * @param fileLastModified 文件创建时间
     * @param sourceFilePath 源文件
     * @param desFilePath 目标文件
     * @param replaceContent 文件替换内容
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
                // 如果是第一行，则添加文件创建的时间
                if(firstFlag) {
                    out.write("<!--" + fileLastModified + "-->");
                    out.write("\r\n");
                    out.write(sLine);
                    out.write("\r\n");
                    firstFlag = false;
                    continue;
                }
                // 把文件中的字符替换成真正的文件内容
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
     * 删除文件。
     *
     * @param destFile 目标文件
     * @throws Exception
     */
    private void deleteFile(File destFile) throws Exception {
        /**
         * 删除文件夹下的所有文件
         * @param oldPath
         */
        if (destFile.isDirectory()) {
            // 删除文件夹下文件
            File[] files = destFile.listFiles();
            if (files != null) {
	            for (File file : files) {
	                deleteFile(file);
	            }
            }
            // 删除文件夹
            destFile.delete();
        } else {
            destFile.delete();
        }
    }

    /**
     * 获取MGR接口。
     * @param request HTTP request请求
     * @param mgrName MGR名称
     * @return bean MGR实体
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
