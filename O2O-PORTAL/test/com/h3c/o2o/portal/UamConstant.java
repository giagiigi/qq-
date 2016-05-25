/*
 * Copyright (c) 2007-2015, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC V700R001
 * Module Name :
 * Date Created: 2015-7-23
 * Creator     : ykf2685
 * Description :
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 * 2015-7-23  ykf2685             O2O-UAM project, new code file.
 *
 *------------------------------------------------------------------------------
 */

package com.h3c.o2o.portal;

/**
 * ��������ֵ���塣
 *
 * @author ykf2685
 *
 */
public class UamConstant {

    /** �ڴ���Ϣ����-���� */
    public final static Integer OPTTYPE_ADD = 1;

    /** �ڴ���Ϣ����-�޸�*/
    public final static Integer OPTTYPE_MODIFY = 2;

    /** �ڴ���Ϣ����-ɾ�� */
    public final static Integer OPTTYPE_DELETE = 3;

    /** ��������ģ��exchnage���� */
    public final static String THEME_EXCHANGE = "o2ouam.theme";

    /** ��������ģ��routing-key */
    public final static String THEME_EXCHANGE_KEY = "theme.producer";

    /** ����ģ�建��exchnage���� */
    public final static String THEME_OPT_EXCHANGE = "o2ouam.optimize.theme";

    /** ����ģ�建��routing-key */
    public final static String THEME_OPT_EXCHANGE_KEY = "optimize.producer.theme";

    /** ��֤���û���exchnage���� */
    public final static String AUTHCFG_OPT_EXCHANGE = "o2ouam.optimize.authcfg";

    /** ��֤���û���routing-key */
    public final static String AUTHCFG_OPT_EXCHANGE_KEY = "optimize.producer.authcfg";

    /** ��֤���ò�������exchnage���� */
    public final static String AUTHPARAM_OPT_EXCHANGE = "o2ouam.optimize.authparam";

    /** ��֤���ò���routing-key */
    public final static String AUTHPARAM_OPT_EXCHANGE_KEY = "optimize.producer.authparam";

    /** ΢����Ϣ����exchnage���� */
    public final static String CUSTOMMESSAGE_OPT_EXCHANGE = "o2ouam.optimize.csmessage";

    /** ΢����Ϣ����routing-key */
    public final static String CUSTOMMESSAGE_OPT_EXCHANGE_KEY = "optimize.producer.csmessage";

    /** ΢���ʺŻ���exchnage���� */
    public final static String WXACCOUNT_OPT_EXCHANGE = "o2ouam.optimize.wxaccount";

    /** ΢���ʺŲ�������routing-key */
    public final static String WXACCOUNT_OPT_EXCHANGE_KEY = "optimize.producer.wxaccount";

    /** ΢�Źؼ��ֺ͹ؼ����Զ��ظ�����exchnage���� */
    public final static String WXMESSAGE_OPT_EXCHANGE = "o2ouam.optimize.wxmessage";

    /** ΢�Źؼ��ֺ͹ؼ����Զ��ظ���������routing-key */
    public final static String WXMESSAGE_OPT_EXCHANGE_KEY = "optimize.producer.wxmessage";

    /** ΢���Զ���˵�����exchnage���� */
    public final static String WXMENU_OPT_EXCHANGE = "o2ouam.optimize.wxmenu";

    /** ΢���Զ���˵�����routing-key */
    public final static String WXMENU_OPT_EXCHANGE_KEY = "optimize.producer.wxmenu";

    /** �̶��ʺŻ���exchnage���� */
    public final static String REGACCOUNT_OPT_EXCHANGE = "o2ouam.optimize.regaccount";

    /** �̶��ʺŻ���routing-key */
    public final static String REGACCOUNT_OPT_EXCHANGE_KEY = "optimize.producer.regaccount";

    /** ����������exchnage���� */
    public final static String BLACKLIST_OPT_EXCHANGE = "o2ouam.optimize.blacklist";

    /** ����������routing-key */
    public final static String BLACKLIST_OPT_EXCHANGE_KEY = "optimize.producer.blacklist";

    /** ����������exchnage���� */
    public final static String PUBLISH_MNG_OPT_EXCHANGE = "o2ouam.publish";

    /** ����������routing-key */
    public final static String PUBLISH_MNG_OPT_EXCHANGE_KEY = "optimize.producer.publish";

    /** ���������������routing-key */
    public final static String PUBLISH_PARA_OPT_EXCHANGE_KEY = "o2ouam.queue.optimize.publish.param";

}
