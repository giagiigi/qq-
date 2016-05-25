/*
 * Copyright (c) 2015, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC V300R002
 * Module Name :
 * Date Created: 2015-8-10
 * Creator     : dkf5133
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
package com.h3c.o2o.portal.protocol.func;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.GsonBuilder;
import com.h3c.o2o.portal.cache.api.CacheApiMgr;
import com.h3c.o2o.portal.cache.key.RegistUserCacheKey;
import com.h3c.o2o.portal.entity.AccessDetail;
import com.h3c.o2o.portal.entity.RegistUser;
import com.h3c.o2o.portal.protocol.entity.AccessToken;
import com.h3c.o2o.portal.protocol.entity.LoginParam;
import com.h3c.o2o.portal.protocol.entity.Offline;
import com.h3c.o2o.portal.protocol.entity.OnlineUserInfo;
import com.h3c.o2o.portal.protocol.entity.TokenReq;
import com.h3c.o2o.portal.protocol.entity.TpLoginInfo;
import com.h3c.o2o.portal.protocol.entity.User;
import com.h3c.o2o.portal.protocol.entity.UserStatus;
import com.h3c.o2o.portal.user.func.AccessUserDetailMgr;
import com.h3c.o2o.portal.user.func.RegistUserMgr;
import com.h3c.oasis.o2oserver.bootstrap.ServerContext;

/**
 * add description of types here
 *
 * @author dkf5133
 */
public class ProtocolMgrTest {

    private Log log = LogFactory.getLog(ProtocolMgrTest.class);

    private AccessUserDetailMgr accessUserDetailMgr = null;

    private RegistUserMgr userMgr;

    private CacheApiMgr cacheApiMgr;

    private TokenMgr tokenMgr;

    @Before
    public void setUp() throws Exception {
        System.out.println("setUp()...");

        accessUserDetailMgr = (AccessUserDetailMgr) ServerContext
        .getRootAppContext().getBean("accessUserDetailMgr");

        userMgr = (RegistUserMgr) ServerContext
            .getRootAppContext().getBean("userMgr");

        cacheApiMgr = (CacheApiMgr) ServerContext
            .getRootAppContext().getBean("cacheApiMgr");

        tokenMgr = (TokenMgr) ServerContext
            .getRootAppContext().getBean("tokenMgr");

    }

    @After
    public void tearDown() throws Exception {
        System.out.println("tearDown()...");
    }

    @Test
    public void testLoginRedirect() throws IOException {
        // 封装参数
        List<RegistUser> users = userMgr.findAllRegistUser();
        RegistUser user = null;
        if (users != null && users.size() > 0) {
            user = users.get(0);
        } else {
            user = new RegistUser();
            user.setStoreId(-1l);
        }
        LoginParam param = null;
        param = new LoginParam();
        param.setRedirect_uri("www.sina.com");
        param.setNasId(user.getStoreId());
        param.setSsid("ssid");
        param.setUsermac("aa:bb:cc:dd:ee:ff");
        param.setUserip("123456");
        param.setUserurl("userurl");

        String ua = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36";
        // 执行
        String url = ProtocolMgrImpl.get().loginRedirect(param, ua);

        log.info("--------->redirect url: " + url);

        // 判断执行结果
        AccessDetail detailFromDb = accessUserDetailMgr.queryAccessDetail(param.getNasId(),
            param.getSsid(), param.getUsermac(), System.currentTimeMillis());
        if (detailFromDb != null) {
            RegistUser registUser = cacheApiMgr.getRegistUserCacheMgr().get(
                new RegistUserCacheKey(detailFromDb.getStoreId(),
                    detailFromDb.getUserName(), detailFromDb.getUserType()));
            if (registUser != null) {
                Assert.assertTrue(url.matches("^.+code=.+&portal_server=http://.+$"));
                return;
            }
        }

        Assert.assertTrue(url.matches("^.+index.jsp\\?templateId=.+$"));
    }

    /**
     * 用户类型为一键登录、固定账号认证和短信认证时， 不涉及到内存化，因此不测试
     * 该方法只测试第三方用户
     *
     * @throws IOException
     */
    @Test
    public void testGetAccessToken() throws IOException {
        // 封装数据
        RegistUser user = new RegistUser();
        user.setStoreId(-1l);
        user.setUserType(RegistUser.USER_TYPE_QQ);
        user.setUserIp("123456");
        user.setUserMac("aa:bb:cc:dd:ee:ff");
        String ssid = "123123";
        
		String code = tokenMgr.generateCode(user.getUserMac(), user.getUserIp(), user.getStoreId(), ssid,
				RegistUser.USER_TYPE_QQ, user);

        TpLoginInfo info = new TpLoginInfo();
        info.setOpenId("openID");
        info.setType(RegistUser.USER_TYPE_QQ);
        info.setStoreId(Long.valueOf(-1));
        info.setExtInfo("json");
        info.setOwnerId(1L);

        tokenMgr.putTpLoginInfo(code, info);

        TokenReq tokenReq = new TokenReq();
        tokenReq.setCode(code);
        tokenReq.setUserIp(user.getUserIp());
        tokenReq.setUserMac(user.getUserMac());
        // 执行
        AccessToken tokenInfo = ProtocolMgrImpl.get().getAccessToken(
            tokenReq);
        // 判断执行结果
        GsonBuilder gb =new GsonBuilder();
        gb.disableHtmlEscaping();
        String tokenStr = gb.create().toJson(tokenInfo);
        Assert.assertTrue(tokenStr.matches(".*access_token.*expire\\_in.*"));
        log.info("--------->token info: " + tokenStr);
    }

    @Test
    public void testUploadOnlines() throws IOException {
        OnlineUserInfo onlineUserInfo = new OnlineUserInfo();
        User user = new User();
        user.setIp("365425254");
        user.setMac("aa:bb:cc:dd:ee:ff");
        user.setSessionTime(102401);
        List<User> users = new ArrayList<User>();
        users.add(user);
        onlineUserInfo.setUsers(users);

        UserStatus status = ProtocolMgrImpl.get().uploadOnlines(onlineUserInfo);
        log.info("UserStatus: " + status.toString());
    }

    @Test
    public void testUserOffline() throws IOException {
        Offline off = new Offline();
        off.setIp("365425254");
        // 序列化
        ProtocolMgrImpl.get().userOffline(off.getIp());
        Assert.assertTrue(true);
    }

}
