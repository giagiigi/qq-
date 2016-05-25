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
 * 公共属性值定义。
 *
 * @author ykf2685
 *
 */
public class PortalConstant {

    /** 内存消息类型-增加 */
    public final static Integer OPTTYPE_ADD = 1;

    /** 内存消息类型-修改*/
    public final static Integer OPTTYPE_MODIFY = 2;

    /** 内存消息类型-删除 */
    public final static Integer OPTTYPE_DELETE = 3;
    
    /** 获取公众号exchnage名字 */
    public final static String WXWIFI_DEFAULT_ACCOUNT_EXCHANGE = "o2ouam.weixinwifi.json";

    /** 获取公众号routing-key */
    public final static String WXWIFI_DEFAULT_ACCOUNT_EXCHANGE_KEY = "optimize.producer.weixinwifi.json";

    /** 发送短信exchnage名字 */
    public final static String SENDMESSAGE_EXCHANGE = "o2oportal.sendmessage";

    /** 发送短信routing-key */
    public final static String SENDMESSAGE_EXCHANGE_KEY = "o2oportal.sendmessage";

    /** 发送前台进行用户强制下线的exchange */
    public final static String LOGINOUT_EXCHANGE = "o2oportal.loginout";

    /** 发送前台进行用户强制下线的routing-key */
    public final static String LOGINOUT_EXCHANGE_KEY = "o2oportal.producer.loginout";

    /** 查询设备类型是否是小小贝exchnage */
    public final static String QUERY_DEV_XXB_EXC = "o2o.wsm.exchange.isxxb.dev";

    /** 查询设备类型是否是小小贝routing-key */
    public final static String QUERY_DEV_XXB_KEY = "o2o.wsm.queue.isxxb.dev";

}
