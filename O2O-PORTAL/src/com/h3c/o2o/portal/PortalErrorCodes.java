package com.h3c.o2o.portal;


/**
 * 公共错误码。
 *
 * @author ykf2685
 *
 */
public class PortalErrorCodes {

    /** 参数不合法 */
    public static Integer PARAMETER_ILLEGAL = 70001;

    /** 记录不存在 */
    public static Integer ITEM_NOT_EXIST = 70002;

    /** 下载文件失败 */
    public static Integer HTTP_DOWN_ERROR = 70003;

    /** 和无线通信参数不合法 */
    public static Integer MSG_PARAMETER_ILLEGAL = 71001;

    /** 和无线通信,操作失败 */
    public static Integer MSG_OPERATE_ERROR = 71002;

    /** 服务器内部错误 */
    public static Integer INTERNAL_SERVER_ERROR = 60000;
    
    /** 无效的授权码 */
    public static Integer CODE_EXPIRE = 60001;

    /** 无效的token*/
    public static Integer TOKEN_EXPIRE = 60002;

    /** 用户已被拉入黑名单，无法登录。*/
    public static Integer BLACK_USER = 60003;

    /** 您已被拉入黑名单，若要登录请联系管理员。*/
    public static Integer NO_SENSE_LOGIN_BLACK_USER = 60012;

    /** 重定向URI不能为空。*/
    public static Integer REDIRECT_URI_NULL = 60013;

    /** 场所ID不能为空。*/
    public static Integer STORE_ID_NULL = 60014;

    /** MAC地址不能为空。*/
    public static Integer USER_MAC_NULL = 60015;

    /** IP地址不能为空。*/
    public static Integer USER_IP_NULL = 60016;

    /** 未找到该店铺的发布管理信息。*/
    public static Integer PUBLISH_MNG_NOT_EXISTS = 60017;

    /**未找到该店铺的配置信息。*/
    public static Integer AUTH_CFG_NOT_EXISTS = 60018;

    /**不可识别的操作。*/
    public static Integer OPERATION_NOT_SUPPORT = 60019;
    
    /**微信公众号认证未启用，请尝试其他的联网方式。*/
    public static Integer WECHAT_OAUTH_DISABLED = 60020;
    
    /** 用户已经在线 */
    public static Integer USER_ALLREADY_ONLINE = 60021;
    
    /** 微信连Wifi配置错误，请联系管理员 */
    public static Integer WEIXIN_WIFI_CONFIG_ERROR = 60022;
    
    /** 您连接了错误的Wifi信号，请选择 */
    public static Integer WEIXIN_WIFI_WRONG_SSID = 60023;
    
    /** IP地址冲突。*/
    public static Integer USER_IP_CONFLICT = 60024;
    
    /**不存在的商户，请联系此商铺管理员。*/
    public static Integer NOT_EXISTS_STORE = 71006;
    
    /** 出错*/
    public static String ERROR_CODE_ERROR = "-1";
    public static String ERROR_CODE_OK = "0";
}
