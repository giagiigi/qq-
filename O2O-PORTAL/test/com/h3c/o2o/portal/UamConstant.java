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
public class UamConstant {

    /** 内存消息类型-增加 */
    public final static Integer OPTTYPE_ADD = 1;

    /** 内存消息类型-修改*/
    public final static Integer OPTTYPE_MODIFY = 2;

    /** 内存消息类型-删除 */
    public final static Integer OPTTYPE_DELETE = 3;

    /** 发布主题模板exchnage名字 */
    public final static String THEME_EXCHANGE = "o2ouam.theme";

    /** 发布主题模板routing-key */
    public final static String THEME_EXCHANGE_KEY = "theme.producer";

    /** 主题模板缓存exchnage名字 */
    public final static String THEME_OPT_EXCHANGE = "o2ouam.optimize.theme";

    /** 主题模板缓存routing-key */
    public final static String THEME_OPT_EXCHANGE_KEY = "optimize.producer.theme";

    /** 认证配置缓存exchnage名字 */
    public final static String AUTHCFG_OPT_EXCHANGE = "o2ouam.optimize.authcfg";

    /** 认证配置缓存routing-key */
    public final static String AUTHCFG_OPT_EXCHANGE_KEY = "optimize.producer.authcfg";

    /** 认证配置参数缓存exchnage名字 */
    public final static String AUTHPARAM_OPT_EXCHANGE = "o2ouam.optimize.authparam";

    /** 认证配置参数routing-key */
    public final static String AUTHPARAM_OPT_EXCHANGE_KEY = "optimize.producer.authparam";

    /** 微信消息缓存exchnage名字 */
    public final static String CUSTOMMESSAGE_OPT_EXCHANGE = "o2ouam.optimize.csmessage";

    /** 微信消息参数routing-key */
    public final static String CUSTOMMESSAGE_OPT_EXCHANGE_KEY = "optimize.producer.csmessage";

    /** 微信帐号缓存exchnage名字 */
    public final static String WXACCOUNT_OPT_EXCHANGE = "o2ouam.optimize.wxaccount";

    /** 微信帐号参数缓存routing-key */
    public final static String WXACCOUNT_OPT_EXCHANGE_KEY = "optimize.producer.wxaccount";

    /** 微信关键字和关键字自动回复缓存exchnage名字 */
    public final static String WXMESSAGE_OPT_EXCHANGE = "o2ouam.optimize.wxmessage";

    /** 微信关键字和关键字自动回复参数缓存routing-key */
    public final static String WXMESSAGE_OPT_EXCHANGE_KEY = "optimize.producer.wxmessage";

    /** 微信自定义菜单缓存exchnage名字 */
    public final static String WXMENU_OPT_EXCHANGE = "o2ouam.optimize.wxmenu";

    /** 微信自定义菜单缓存routing-key */
    public final static String WXMENU_OPT_EXCHANGE_KEY = "optimize.producer.wxmenu";

    /** 固定帐号缓存exchnage名字 */
    public final static String REGACCOUNT_OPT_EXCHANGE = "o2ouam.optimize.regaccount";

    /** 固定帐号缓存routing-key */
    public final static String REGACCOUNT_OPT_EXCHANGE_KEY = "optimize.producer.regaccount";

    /** 黑名单缓存exchnage名字 */
    public final static String BLACKLIST_OPT_EXCHANGE = "o2ouam.optimize.blacklist";

    /** 黑名单缓存routing-key */
    public final static String BLACKLIST_OPT_EXCHANGE_KEY = "optimize.producer.blacklist";

    /** 发布管理缓存exchnage名字 */
    public final static String PUBLISH_MNG_OPT_EXCHANGE = "o2ouam.publish";

    /** 发布管理缓存routing-key */
    public final static String PUBLISH_MNG_OPT_EXCHANGE_KEY = "optimize.producer.publish";

    /** 发布管理参数缓存routing-key */
    public final static String PUBLISH_PARA_OPT_EXCHANGE_KEY = "o2ouam.queue.optimize.publish.param";

}
