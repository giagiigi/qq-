/*
 * Copyright (c) 2007-2014, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC V700R001
 * Module Name :
 * Date Created: 2014-11-18��
 * Creator     : x10668
 * Description :
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 * 2014-11-18��  x10668             imc-sample1 project, new code file.
 *
 *------------------------------------------------------------------------------
 */

package com.h3c.o2o.portal.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * �����ļ�����������
 *
 * @author ykf2685
 *
 */
public class HttpDownload{
    
    private static final Log log = LogFactory.getLog(HttpDownload.class);

    /**
     *  ʹ��HTTP���ص���Դ���Ϊ�ļ������̲߳�����
     *
     * @param sourceFilePath   ����url
     * @param descFilePath     ����url
     */
    public static boolean downLoad(String resourceFilePath, String descFilePath) {
        URL url = null;
        HttpURLConnection conn = null;
        try {
            url = new URL(resourceFilePath);
            log.info(resourceFilePath);
            // �ж��Ƿ�ɹ�����http�������򷵻�
            conn = (HttpURLConnection) url.openConnection();
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                log.warn("Http connection fail: "+resourceFilePath);
                return false;
            }
            File localFile = new File(descFilePath);
            // ��������·��
            if(!localFile.getParentFile().exists()){
                localFile.getParentFile().mkdirs();
            }
            if(!localFile.exists()){
                localFile.createNewFile();
            } else {
               if(localFile.delete()) {
                   localFile = new File(descFilePath);
                   localFile.createNewFile();
               }
            }

            // ���������ļ���
            FileOutputStream o = new FileOutputStream(localFile);
            // ��������
            InputStream input = conn.getInputStream();
            byte[] b = new byte[1024];
            int len;
            while((len = input.read(b)) != -1){
                o.write(b , 0, len);
            }
            o.flush();
            o.close();
        } catch (Exception e) {
            log.warn(null, e);
            return false;
        }finally{
            if(conn != null){
                try {
                    conn.disconnect();
                } catch (Exception e) {
                    log.warn(null, e);
                }
            }
        }
        return true;
    }
}
