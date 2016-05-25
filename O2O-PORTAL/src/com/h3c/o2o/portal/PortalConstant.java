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
public class PortalConstant {

    /** �ڴ���Ϣ����-���� */
    public final static Integer OPTTYPE_ADD = 1;

    /** �ڴ���Ϣ����-�޸�*/
    public final static Integer OPTTYPE_MODIFY = 2;

    /** �ڴ���Ϣ����-ɾ�� */
    public final static Integer OPTTYPE_DELETE = 3;
    
    /** ��ȡ���ں�exchnage���� */
    public final static String WXWIFI_DEFAULT_ACCOUNT_EXCHANGE = "o2ouam.weixinwifi.json";

    /** ��ȡ���ں�routing-key */
    public final static String WXWIFI_DEFAULT_ACCOUNT_EXCHANGE_KEY = "optimize.producer.weixinwifi.json";

    /** ���Ͷ���exchnage���� */
    public final static String SENDMESSAGE_EXCHANGE = "o2oportal.sendmessage";

    /** ���Ͷ���routing-key */
    public final static String SENDMESSAGE_EXCHANGE_KEY = "o2oportal.sendmessage";

    /** ����ǰ̨�����û�ǿ�����ߵ�exchange */
    public final static String LOGINOUT_EXCHANGE = "o2oportal.loginout";

    /** ����ǰ̨�����û�ǿ�����ߵ�routing-key */
    public final static String LOGINOUT_EXCHANGE_KEY = "o2oportal.producer.loginout";

    /** ��ѯ�豸�����Ƿ���СС��exchnage */
    public final static String QUERY_DEV_XXB_EXC = "o2o.wsm.exchange.isxxb.dev";

    /** ��ѯ�豸�����Ƿ���СС��routing-key */
    public final static String QUERY_DEV_XXB_KEY = "o2o.wsm.queue.isxxb.dev";

}
