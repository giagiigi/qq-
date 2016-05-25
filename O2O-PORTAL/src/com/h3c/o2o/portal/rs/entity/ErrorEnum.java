/*
 * Copyright (c) 2015, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC V300R002
 * Module Name :
 * Date Created: 2015-7-18
 * Creator     : j09980
 * Description :
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 * YYYY-MM-DD  zhangshan        XXXX project, new code file.
 *
 *------------------------------------------------------------------------------
 */
package com.h3c.o2o.portal.rs.entity;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * <ol>
 * <li>SUCCESS 0x0000   �ɹ�;</li>
   <li>USER_NAME_EXISTS 0x0001    ͬ���û��Ѵ���;</li>
   <li>USER_NAME_ILLEAGAL 0x0002   �û������Ϸ�;</li>
   <li>USER_NO_EXISTS 0x0003   �û�������;</li>
   <li>PASSWORD_ERROR 0x0004   �û�ԭ�������;</li>
   <li>PASSWORD_INVALID 0x0005 �û����벻����Ҫ��;</li>
   <li>USER_INVALID 0x0006 �û���ʧЧ;</li>
   <li>USER_LOGIN_OUT 0x0007   �û�������;</li>
   <li>NAME_OR_PASSWORD_WRONG 0x0008   �û������������;</li>
   <li>SMS_CODE_OUTTIME 0x0009 ������֤���ѹ���;</li>
   <li>SMS_CODE_CHECK_FAILD 0x000a ������֤��У��ʧ��;</li>
   <li>BLACKLIST_NONEXISTENT_THIS_USER 0x000b  ���������޴��û�;</li>
   <li>WHITELIST_NONEXISTENT_THIS_USER 0x000c  ���������޴��û�;</li>
   <li>USER_EXIST_BLACKLIST 0x000d �û����ں�������;</li>
   <li>USER_EXIST_WHITELIST 0x000e �û����ڰ�������;</li>
   <li>PHONE_NO_ERROR 0x0010 �Ƿ��ĵ绰����;</li>
   <li>MAC_ILLEGAL 0x0011 �Ƿ���mac��ַ;</li>
   <li>UNKNOWN_ERROR 0xFFFF    ϵͳδ֪����  </li>
   </ol>
 * @author j09980
 */
public enum ErrorEnum {
    SUCCESS(0x0000, "success"),
    USER_NAME_EXISTS(0x0001, "user of same name already exist"),
    USER_NAME_ILLEAGAL(0x0002, "user name is illeagal"),
    USER_NO_EXISTS(0x0003, "user is not exists"),
    PASSWORD_ERROR(0x0004, "user password is error"),
    PASSWORD_INVALID(0x0005, "password is Invalid"),
    USER_INVALID(0x0006, "user is invalid"),
    USER_LOGIN_OUT(0x0007, "user is login out"),
    NAME_OR_PASSWORD_WRONG(0x0008, "user name or password is wrong"),
    SMS_CODE_OUTTIME(0x0009, "sms code is out time"),
    SMS_CODE_CHECK_FAILD(0x000a, "sms code check faild"),
    BLACKLIST_NONEXISTENT_THIS_USER(0x000b, "blacklist is non-existent this user"),
    WHITELIST_NONEXISTENT_THIS_USER(0x000c, "whitelist is non-existent this user"),
    USER_EXIST_BLACKLIST(0x000d, "user is in blacklist"),
    USER_EXIST_WHITELIST(0x000e, "user is in whitelist"),
    PHONE_NO_ERROR(0x0010, "phone number is illegal."),
    MAC_ILLEGAL(0x0011, "mac is illegal: %s."),
    UNKNOWN_ERROR(0xFFFF, "system error");

    private int errorCode;

    private String errorMsg;

    private static ConcurrentMap<Integer, ErrorInfo> MAP = new ConcurrentHashMap<Integer, ErrorInfo>();

    private ErrorEnum(int errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public ErrorInfo getErrorInfo() {
        return MAP.get(Integer.valueOf(errorCode));
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public static ErrorInfo getErrorInfo(int errorCode) {
        if (!MAP.containsKey(Integer.valueOf(errorCode))) {
            throw new IllegalArgumentException(
                "Error code is illegal! error code is " + errorCode);
        }
        return MAP.get(Integer.valueOf(errorCode));
    }

    static {
        ErrorEnum[] values = ErrorEnum.values();
        for (ErrorEnum errorEnum : values) {
            ErrorInfo errorInfo = new ErrorInfo();
            errorInfo.setError_code(String.format("%#06x", errorEnum.errorCode));
            errorInfo.setError_msg(errorEnum.errorMsg);
            MAP.put(errorEnum.errorCode, errorInfo);
        }
    }

}