package com.h3c.o2o.portal;

import com.h3c.oasis.o2oserver.common.ApplicationException;

/**
 * EMO����ʹ�õ��쳣��Ϣ�ࡣ
 *
 */
public class PortalException extends ApplicationException {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -4202865763567910674L;

    /** ������Ϣ��*/
    private String errorMsg;

    /**
     * ���췽����ʹ�ô���š�������Ϣ�ͷ�װ���쳣���ݴ�������
     *
     * @param errorCode ����š�
     * @param message ������Ϣ��
     * @param t ��װ���쳣���ݡ�
     */
    public PortalException(int errorCode, String message, Throwable t) {
        super(errorCode, message, t);
        errorMsg = message;
    }

    /**
     * ���췽����ʹ�ô���źʹ�����Ϣ��������
     *
     * @param errorCode ����š�
     * @param message ������Ϣ��
     */
    public PortalException(int errorCode, String message) {
        super(errorCode, message);
        errorMsg = message;
    }

    /**
     * ���췽����ʹ�ô���źͷ�װ���쳣���ݴ�������
     *
     * @param errorCode ����š�
     * @param t ��װ���쳣���ݡ�
     */
    public PortalException(int errorCode, Throwable t) {
        super(errorCode, t);
        errorMsg = null;
    }

    /**
     * ���췽����ʹ�ô���Ŵ�������
     *
     * @param errorCode ����š�
     */
    public PortalException(int errorCode) {
        super(errorCode);
        errorMsg = null;
    }

    /**
     * ���췽����ʹ�ô�����Ϣ�������󣬴�ʱ�Ĵ���������塣
     *
     * @param message ������Ϣ��
     */
    public PortalException(String message) {
        super(-1);
        errorMsg = message;
    }

    /**
     * ��ȡ�������Ӧ�Ĵ�����Ϣ��
     *
     * @return �������Ӧ�Ĵ�����Ϣ��
     */
    @Override
    public String getErrorMsg() {
        return errorMsg != null ? errorMsg : super.getErrorMsg();
    }
}
