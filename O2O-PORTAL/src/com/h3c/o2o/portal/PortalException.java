package com.h3c.o2o.portal;

import com.h3c.oasis.o2oserver.common.ApplicationException;

/**
 * EMO自助使用的异常信息类。
 *
 */
public class PortalException extends ApplicationException {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -4202865763567910674L;

    /** 错误信息。*/
    private String errorMsg;

    /**
     * 构造方法，使用错误号、错误信息和封装的异常数据创建对象。
     *
     * @param errorCode 错误号。
     * @param message 错误信息。
     * @param t 封装的异常数据。
     */
    public PortalException(int errorCode, String message, Throwable t) {
        super(errorCode, message, t);
        errorMsg = message;
    }

    /**
     * 构造方法，使用错误号和错误信息创建对象。
     *
     * @param errorCode 错误号。
     * @param message 错误信息。
     */
    public PortalException(int errorCode, String message) {
        super(errorCode, message);
        errorMsg = message;
    }

    /**
     * 构造方法，使用错误号和封装的异常数据创建对象。
     *
     * @param errorCode 错误号。
     * @param t 封装的异常数据。
     */
    public PortalException(int errorCode, Throwable t) {
        super(errorCode, t);
        errorMsg = null;
    }

    /**
     * 构造方法，使用错误号创建对象。
     *
     * @param errorCode 错误号。
     */
    public PortalException(int errorCode) {
        super(errorCode);
        errorMsg = null;
    }

    /**
     * 构造方法，使用错误信息创建对象，此时的错误号无意义。
     *
     * @param message 错误信息。
     */
    public PortalException(String message) {
        super(-1);
        errorMsg = message;
    }

    /**
     * 获取错误码对应的错误信息。
     *
     * @return 错误码对应的错误信息。
     */
    @Override
    public String getErrorMsg() {
        return errorMsg != null ? errorMsg : super.getErrorMsg();
    }
}
