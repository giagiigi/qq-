/*
 * Copyright (c) 2015, Hanliwei H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC V300R002
 * Module Name :
 * Date Created: 2015-10-22
 * Creator     :kf6090
 * Description :
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 * YYYY-MM-DD  hanliwei        XXXX project, new code file.
 *
 *------------------------------------------------------------------------------
 */
package com.h3c.o2o.portal.rs.func;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.h3c.o2o.portal.PortalException;
import com.h3c.o2o.portal.rs.entity.AccessDetailList;
import com.h3c.o2o.portal.rs.entity.AddUser;
import com.h3c.o2o.portal.rs.entity.AuthMac;
import com.h3c.o2o.portal.rs.entity.AuthResult;
import com.h3c.o2o.portal.rs.entity.AuthUser;
import com.h3c.o2o.portal.rs.entity.ErrorEnum;
import com.h3c.o2o.portal.rs.entity.ErrorInfo;
import com.h3c.o2o.portal.rs.entity.Mac;
import com.h3c.o2o.portal.rs.entity.MacList;
import com.h3c.o2o.portal.rs.entity.ModifyUser;
import com.h3c.o2o.portal.rs.entity.OnlineUserList;
import com.h3c.o2o.portal.rs.entity.Parameter;
import com.h3c.o2o.portal.rs.entity.PhoneCode;
import com.h3c.o2o.portal.rs.entity.RestRequest;
import com.h3c.o2o.portal.rs.entity.SmsCode;

/**
 * @author hkf6090
 *
 */
public class ThirdPartyListener {

	/** 日志对象。 */
	private Log log = LogFactory.getLog(getClass());

	private ThirdPartyAuthMgr thirdPartyAuthMgr;

	public String createUser(String req) {
    	Gson gson = new Gson();
		RestRequest request = gson.fromJson(req, RestRequest.class);
		AddUser user = gson.fromJson(request.getBody(), AddUser.class);
    	ErrorInfo errorInfo = this.thirdPartyAuthMgr.createUser(user);
        return gson.toJson(errorInfo);
    }
    /**
     * 修改方法，一般只用来修改密码
     *
     * */
    public String modifyUser(String req) {
    	Gson gson = new Gson();
		RestRequest request = gson.fromJson(req, RestRequest.class);
		ModifyUser user = gson.fromJson(request.getBody(), ModifyUser.class);
    	ErrorInfo errorInfo = this.thirdPartyAuthMgr.modifyUser(user);
        return gson.toJson(errorInfo);
    }
    /**
     * error_code取值0x0000、0x0008，expired_in取值为整数，单位为秒，
     * redirect_url为认证成功后，需重定向到此url。
     * <p>session_timeout为当前用户上网时长（单位秒，表示不限时），不携带将取系统默认时长。
     * */
    public String authUser(String req) {
    	Gson gson = new Gson();
    	RestRequest request = gson.fromJson(req, RestRequest.class);
    	AuthUser user = gson.fromJson(request.getBody(), AuthUser.class);
    	AuthResult authResult = this.thirdPartyAuthMgr.authUser(user);
        return gson.toJson(authResult);
     }
    /**
     * <p>error_code取值0x000d，expired_in取值为整数，单位为秒。
     * <p>redirect_url为认证成功后，需重定向到此url。
     * <p>session_timeout为当前用户上网时长（单位秒，表示不限时），不携带将取系统默认时长。
     * @param authMac
     * @return
     */
    public String authMac(String req) {
    	Gson gson = new Gson();
    	RestRequest request = gson.fromJson(req, RestRequest.class);
    	AuthMac authMac = gson.fromJson(request.getBody(), AuthMac.class);
    	AuthResult authResult = this.thirdPartyAuthMgr.authMac(authMac);
        return gson.toJson(authResult);
     }

    public String verifySmsCode(String req) {
    	Gson gson = new Gson();
    	RestRequest request = gson.fromJson(req, RestRequest.class);
    	PhoneCode code = gson.fromJson(request.getBody(), PhoneCode.class);
    	ErrorInfo errorInfo = this.thirdPartyAuthMgr.verifySmsCode(code);
        return gson.toJson(errorInfo);
    }
    /**
     * 获取在线用户列表
     *
     * off_set为偏移量，limit为记录数，如off_set=10&limit=50表示从第10条记录起取50条记录。
     *             如果不指定off_set和limit，则取所有记录.
     * */

    public String getOnlines( String req) {
    	Gson gson =new Gson();
    	RestRequest request = gson.fromJson(req, RestRequest.class);
    	List<Parameter> parameters = request.getParameters();
    	Map<String, String> map = new HashMap<String, String>();
    	for (Parameter parameter : parameters) {
              map.put(parameter.getName(), parameter.getValue());
		}
    	OnlineUserList onlineUserList = this.thirdPartyAuthMgr.getOnlines(map.get("identifier"), Integer.parseInt(map.get("offset")),Integer.parseInt(map.get("limit")));
        return gson.toJson(onlineUserList);
    }

    public String getAccessDetails(String req) {
    	Gson gson =new Gson();
    	RestRequest request = gson.fromJson(req, RestRequest.class);
    	List<Parameter> parameters = request.getParameters();
    	Map<String, String> map = new HashMap<String, String>();
    	for (Parameter parameter : parameters) {
              map.put(parameter.getName(), parameter.getValue());
		}
    	AccessDetailList accessDetailList = this.thirdPartyAuthMgr.getAccessDetails(map.get("identifier"), Integer.parseInt(map.get("offset")),Integer.parseInt(map.get("limit")));
        return gson.toJson(accessDetailList);
    }

    public String getSmsCode(String req) {
    	Gson gson =new Gson();
    	RestRequest request = gson.fromJson(req, RestRequest.class);
    	List<Parameter> parameters = request.getParameters();
    	Map<String, String> map = new HashMap<String, String>();
    	for (Parameter parameter : parameters) {
              map.put(parameter.getName(), parameter.getValue());
		}
        SmsCode sc=null;
        try {
            sc = this.thirdPartyAuthMgr.getSmsCode(map.get("phone_number"));
        } catch (PortalException e) {
            ErrorInfo info = ErrorEnum.getErrorInfo(e.getErrorCode());
            return gson.toJson(info);
        }
        return gson.toJson(sc);
    }
    /**
     * 加入黑名单
     * */
	public String addToBlackList(String req){
		Gson gson = new Gson();
		RestRequest request = gson.fromJson(req, RestRequest.class);
		MacList list = gson.fromJson(request.getBody(), MacList.class);

    	if (log.isDebugEnabled()) {
    		if (list == null) {
    			log.debug("Mac list is blank.");
    		} else {
    			log.debug(list);
    		}
    	}
		ErrorInfo result = null;
		try {
			result = this.thirdPartyAuthMgr.addToBlacklist(list);
		} catch (PortalException e) {
			log.warn("Add blackList error.", e);
			ErrorInfo errorInfo = new ErrorInfo(String.format("%#06x", e.getErrorCode()),
					e.getErrorMsg());
			return gson.toJson(errorInfo);
		} catch (Exception e) {
			log.warn("Add blackList error.", e);
			ErrorInfo errorInfo = ErrorEnum.getErrorInfo(ErrorEnum.UNKNOWN_ERROR
					.getErrorCode());
			return gson.toJson(errorInfo);
		}
        return gson.toJson(result);
    }
	 /**
     * 加入白名单
     * */
    public String addToWhitelist(String req) {
		Gson gson = new Gson();
		RestRequest request = gson.fromJson(req, RestRequest.class);
		MacList list = gson.fromJson(request.getBody(), MacList.class);
    	if (log.isDebugEnabled()) {
    		if (list == null) {
    			log.debug("Mac list is blank.");
    		} else {
    			log.debug(list);
    		}
    	}
		ErrorInfo result = null;
		try {
			result = this.thirdPartyAuthMgr.addToWhitelist(list);
		} catch (PortalException e) {
			log.warn("Add whiteList error.", e);
			ErrorInfo errorInfo =  new ErrorInfo(String.format("%#06x", e.getErrorCode()),
					e.getErrorMsg());
			return gson.toJson(errorInfo);
		} catch (Exception e) {
			log.warn("Add whiteList error.", e);
			ErrorInfo errorInfo =ErrorEnum.getErrorInfo(ErrorEnum.UNKNOWN_ERROR
					.getErrorCode());
			return gson.toJson(errorInfo);
		}
        return gson.toJson(result);
    }

    /**
     * 移除黑名单
     * */
    public String removeFromBlacklist(String req) {
		Gson gson = new Gson();
		RestRequest request = gson.fromJson(req, RestRequest.class);
		MacList list = gson.fromJson(request.getBody(), MacList.class);
    	if (log.isDebugEnabled()) {
    		if (list == null) {
    			log.debug("Mac list is blank.");
    		} else {
    			log.debug(list);
    		}
    	}
		ErrorInfo result = null;
		try {
			result = this.thirdPartyAuthMgr.removeFromBlacklist(list);
		} catch (PortalException e) {
			log.warn("Del blackList error.", e);
			ErrorInfo errorInfo = new ErrorInfo(String.format("%#06x", e.getErrorCode()),
					e.getErrorMsg());
			return gson.toJson(errorInfo);
		} catch (Exception e) {
			log.warn("Del blackList error.", e);
			ErrorInfo errorInfo =ErrorEnum.getErrorInfo(ErrorEnum.UNKNOWN_ERROR
					.getErrorCode());
			return gson.toJson(errorInfo);
		}
        return gson.toJson(result);
    }
    /**
     * 移除白名单
     * */
    public String removeFromWhitelist(String req) {
		Gson gson = new Gson();
		RestRequest request = gson.fromJson(req, RestRequest.class);
		MacList list = gson.fromJson(request.getBody(), MacList.class);

    	if (log.isDebugEnabled()) {
    		if (list == null) {
    			log.debug("Mac list is blank.");
    		} else {
    			log.debug(list);
    		}
    	}
		ErrorInfo result = null;
		try {
			result = this.thirdPartyAuthMgr.removeFromWhitelist(list);
		} catch (PortalException e) {
			log.warn("Del whiteList error.", e);
			ErrorInfo errorInfo = new ErrorInfo(String.format("%#06x", e.getErrorCode()),
					e.getErrorMsg());
			return gson.toJson(errorInfo);
		} catch (Exception e) {
			log.warn("Del whiteList error.", e);
			ErrorInfo errorInfo = ErrorEnum.getErrorInfo(ErrorEnum.UNKNOWN_ERROR
					.getErrorCode());
			return gson.toJson(errorInfo);
		}
		return gson.toJson(result);
    }

    /**
     * 用户下线
     * */
    public String loginout(String req) {
    	Gson gson = new Gson();
		RestRequest request = gson.fromJson(req, RestRequest.class);
		Mac mac = gson.fromJson(request.getBody(), Mac.class);
    	ErrorInfo errorInfo = this.thirdPartyAuthMgr.loginOut(mac);
        return gson.toJson(errorInfo);
    }

	public void setThirdPartyAuthMgr(ThirdPartyAuthMgr thirdPartyAuthMgr) {
		this.thirdPartyAuthMgr = thirdPartyAuthMgr;
	}
}
