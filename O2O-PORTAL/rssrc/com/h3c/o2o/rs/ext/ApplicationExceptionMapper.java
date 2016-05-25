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
 * 将 {@link ApplicationException} 自动转换为 409 (Conflict)。
 */
@Component
@Provider
public class ApplicationExceptionMapper implements ExceptionMapper<ApplicationException> {

    /** 日志记录对象。 */
    private static Log log = LogFactory.getLog(ApplicationExceptionMapper.class);

    /**
     * 将 {@link ApplicationException} 转换为 409 (Conflict)，并设置错误码和错误信息。
     *
     * @param exception {@link ApplicationException}。
     * @return 转换后的回应消息。
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
     * 对错误消息内容使用 ISO-8859-1 进行编码。
     *
     * @param str 错误消息的内容。
     * @return 编码后的内容。
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
