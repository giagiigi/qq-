/*
 * Copyright (c) 2015, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC V300R002
 * Module Name :
 * Date Created: 2015-7-15
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
package com.h3c.o2o.rs;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
import com.h3c.o2o.portal.rs.entity.PhoneCode;
import com.h3c.o2o.portal.rs.entity.SmsCode;
import com.h3c.o2o.portal.rs.func.ThirdPartyAuthMgr;

/**
 * �������û���֤REST����
 *
 * @author j09980
 */

@Component
@Path("/")
@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
public class ThirdPartyAuthResource {

	/** ��־���� */
    private Log log = LogFactory.getLog(getClass());

    @Autowired(required = false)
    private ThirdPartyAuthMgr thirdPartyAuthMgr;

    @POST
    @Path("/createuser")
    public ErrorInfo createUser(AddUser user) {
        return this.thirdPartyAuthMgr.createUser(user);
    }

    /**
     * �޸ķ�����һ��ֻ�����޸�����
     *
     * */
    @POST
    @Path("/modifyuser")
    public ErrorInfo modifyUser(ModifyUser user) {
        return this.thirdPartyAuthMgr.modifyUser(user);
    }

    /**
     * error_codeȡֵ0x0000��0x0008��expired_inȡֵΪ��������λΪ�룬
     * redirect_urlΪ��֤�ɹ������ض��򵽴�url��
     * <p>session_timeoutΪ��ǰ�û�����ʱ������λ�룬��ʾ����ʱ������Я����ȡϵͳĬ��ʱ����
     * */
    @POST
    @Path("/auth")
    public AuthResult authUser(AuthUser user) {
       return this.thirdPartyAuthMgr.authUser(user);
    }

    /**
     * <p>error_codeȡֵ0x000d��expired_inȡֵΪ��������λΪ�롣
     * <p>redirect_urlΪ��֤�ɹ������ض��򵽴�url��
     * <p>session_timeoutΪ��ǰ�û�����ʱ������λ�룬��ʾ����ʱ������Я����ȡϵͳĬ��ʱ����
     * @param authMac
     * @return
     */
    @POST
    @Path("/authMac")
    public AuthResult authMac(AuthMac authMac) {
       return this.thirdPartyAuthMgr.authMac(authMac);
    }

    /**
     * ��ȡ�����û��б�
     *
     * off_setΪƫ������limitΪ��¼������off_set=10&limit=50��ʾ�ӵ�10����¼��ȡ50����¼��
     *             �����ָ��off_set��limit����ȡ���м�¼.
     * */
    @GET
    @Path("/online")
    public OnlineUserList getOnlines(
            @QueryParam("identifier") String identifier,
            @QueryParam("offset") int offset, @QueryParam("limit") int limit) {
        return this.thirdPartyAuthMgr.getOnlines(identifier, offset, limit);
    }

    @GET
    @Path("/accessdetail")
    public AccessDetailList getAccessDetails(
            @QueryParam("identifier") String identifier,
            @QueryParam("offset") int offset, @QueryParam("limit") int limit) {

        return this.thirdPartyAuthMgr.getAccessDetails(identifier, offset, limit);
    }

    @GET
    @Path("/smscode")
    public Response getSmsCode(@QueryParam("phone_number") String phoneNumber) {
        Response response;
        try {
            SmsCode sc = this.thirdPartyAuthMgr.getSmsCode(phoneNumber);
            response = Response.ok(sc).build();
        } catch (PortalException e) {
            ErrorInfo info = ErrorEnum.getErrorInfo(e.getErrorCode());
            response = Response.ok(info).build();
        }
        return response;
    }

    @POST
    @Path("/verifycode")
    public ErrorInfo verifySmsCode(PhoneCode code) {
        return this.thirdPartyAuthMgr.verifySmsCode(code);
    }

    /**
     * ���������
     * */
    @POST
    @Path("/addtoblacklist")
    public ErrorInfo addToBlacklist(MacList list) {
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
			return new ErrorInfo(String.format("%#06x", e.getErrorCode()),
					e.getErrorMsg());
		} catch (Exception e) {
			log.warn("Add blackList error.", e);
			return ErrorEnum.getErrorInfo(ErrorEnum.UNKNOWN_ERROR
					.getErrorCode());
		}
        return result;
    }

    /**
     * ���������
     * */
    @POST
    @Path("/addtowhitelist")
    public ErrorInfo addToWhitelist(MacList list) {
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
			return new ErrorInfo(String.format("%#06x", e.getErrorCode()),
					e.getErrorMsg());
		} catch (Exception e) {
			log.warn("Add whiteList error.", e);
			return ErrorEnum.getErrorInfo(ErrorEnum.UNKNOWN_ERROR
					.getErrorCode());
		}
        return result;
    }

    /**
     * �Ƴ�������
     * */
    @POST
    @Path("/removefromblacklist")
    public ErrorInfo removeFromBlacklist(MacList list) {
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
			return new ErrorInfo(String.format("%#06x", e.getErrorCode()),
					e.getErrorMsg());
		} catch (Exception e) {
			log.warn("Del blackList error.", e);
			return ErrorEnum.getErrorInfo(ErrorEnum.UNKNOWN_ERROR
					.getErrorCode());
		}
        return result;
    }

    /**
     * �Ƴ�������
     * */
    @POST
    @Path("/removefromwhitelist")
    public ErrorInfo removeFromWhitelist(MacList list) {
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
			return new ErrorInfo(String.format("%#06x", e.getErrorCode()),
					e.getErrorMsg());
		} catch (Exception e) {
			log.warn("Del whiteList error.", e);
			return ErrorEnum.getErrorInfo(ErrorEnum.UNKNOWN_ERROR
					.getErrorCode());
		}
		return result;
    }

    /**
     * �û�����
     * */
    @POST
    @Path("/loginout")
    public ErrorInfo loginout(Mac mac) {
        return this.thirdPartyAuthMgr.loginOut(mac);
    }
}
