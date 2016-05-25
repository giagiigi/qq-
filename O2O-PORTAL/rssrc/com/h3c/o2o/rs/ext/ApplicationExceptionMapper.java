/*
 * Copyright (c) 2007-2011, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 */

package com.h3c.o2o.rs.ext;

import java.io.UnsupportedEncodingException;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.h3c.oasis.o2oserver.common.ApplicationException;

/**
 * �� {@link ApplicationException} �Զ�ת��Ϊ 409 (Conflict)��
 */
@Component
@Provider
public class ApplicationExceptionMapper implements ExceptionMapper<ApplicationException> {

    /** ��־��¼���� */
    private static Log log = LogFactory.getLog(ApplicationExceptionMapper.class);

    /**
     * �� {@link ApplicationException} ת��Ϊ 409 (Conflict)�������ô�����ʹ�����Ϣ��
     *
     * @param exception {@link ApplicationException}��
     * @return ת����Ļ�Ӧ��Ϣ��
     */
    public Response toResponse(ApplicationException exception) {
        if (log.isDebugEnabled()) {
            log.debug(exception.getMessage(), exception);
        }
        return Response
            .status(Status.CONFLICT)
            .header("Error-Code", exception.getErrorCode())
            .header("Error-Message", encode(exception.getErrorMsg()))
            .header("Exception-Message", encode(exception.getMessage())).build();
    }

    /**
     * �Դ�����Ϣ����ʹ�� ISO-8859-1 ���б��롣
     *
     * @param str ������Ϣ�����ݡ�
     * @return ���������ݡ�
     */
    private String encode(String str) {
        try {
            return str == null ? null : new String(str.getBytes(), "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            log.error(null, e);
            return str;
        }
    }
}
