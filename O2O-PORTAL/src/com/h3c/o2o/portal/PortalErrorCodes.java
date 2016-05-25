package com.h3c.o2o.portal;


/**
 * ���������롣
 *
 * @author ykf2685
 *
 */
public class PortalErrorCodes {

    /** �������Ϸ� */
    public static Integer PARAMETER_ILLEGAL = 70001;

    /** ��¼������ */
    public static Integer ITEM_NOT_EXIST = 70002;

    /** �����ļ�ʧ�� */
    public static Integer HTTP_DOWN_ERROR = 70003;

    /** ������ͨ�Ų������Ϸ� */
    public static Integer MSG_PARAMETER_ILLEGAL = 71001;

    /** ������ͨ��,����ʧ�� */
    public static Integer MSG_OPERATE_ERROR = 71002;

    /** �������ڲ����� */
    public static Integer INTERNAL_SERVER_ERROR = 60000;
    
    /** ��Ч����Ȩ�� */
    public static Integer CODE_EXPIRE = 60001;

    /** ��Ч��token*/
    public static Integer TOKEN_EXPIRE = 60002;

    /** �û��ѱ�������������޷���¼��*/
    public static Integer BLACK_USER = 60003;

    /** ���ѱ��������������Ҫ��¼����ϵ����Ա��*/
    public static Integer NO_SENSE_LOGIN_BLACK_USER = 60012;

    /** �ض���URI����Ϊ�ա�*/
    public static Integer REDIRECT_URI_NULL = 60013;

    /** ����ID����Ϊ�ա�*/
    public static Integer STORE_ID_NULL = 60014;

    /** MAC��ַ����Ϊ�ա�*/
    public static Integer USER_MAC_NULL = 60015;

    /** IP��ַ����Ϊ�ա�*/
    public static Integer USER_IP_NULL = 60016;

    /** δ�ҵ��õ��̵ķ���������Ϣ��*/
    public static Integer PUBLISH_MNG_NOT_EXISTS = 60017;

    /**δ�ҵ��õ��̵�������Ϣ��*/
    public static Integer AUTH_CFG_NOT_EXISTS = 60018;

    /**����ʶ��Ĳ�����*/
    public static Integer OPERATION_NOT_SUPPORT = 60019;
    
    /**΢�Ź��ں���֤δ���ã��볢��������������ʽ��*/
    public static Integer WECHAT_OAUTH_DISABLED = 60020;
    
    /** �û��Ѿ����� */
    public static Integer USER_ALLREADY_ONLINE = 60021;
    
    /** ΢����Wifi���ô�������ϵ����Ա */
    public static Integer WEIXIN_WIFI_CONFIG_ERROR = 60022;
    
    /** �������˴����Wifi�źţ���ѡ�� */
    public static Integer WEIXIN_WIFI_WRONG_SSID = 60023;
    
    /** IP��ַ��ͻ��*/
    public static Integer USER_IP_CONFLICT = 60024;
    
    /**�����ڵ��̻�������ϵ�����̹���Ա��*/
    public static Integer NOT_EXISTS_STORE = 71006;
    
    /** ����*/
    public static String ERROR_CODE_ERROR = "-1";
    public static String ERROR_CODE_OK = "0";
}
