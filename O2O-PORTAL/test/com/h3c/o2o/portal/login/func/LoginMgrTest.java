/*
 * Copyright (c) 2015, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC V300R002
 * Module Name :
 * Date Created: 2015-8-11
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
package com.h3c.o2o.portal.login.func;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.GsonBuilder;
import com.h3c.o2o.portal.cache.api.CacheApiMgr;
import com.h3c.o2o.portal.cache.key.RegistUserCacheKey;
import com.h3c.o2o.portal.entity.AuthCfg;
import com.h3c.o2o.portal.entity.RegistUser;
import com.h3c.o2o.portal.login.entity.AuthCfgResp;
import com.h3c.o2o.portal.login.entity.HomePageReq;
import com.h3c.o2o.portal.login.entity.LoginReq;
import com.h3c.o2o.portal.login.entity.PageImageReq;
import com.h3c.o2o.portal.login.entity.SmsLoginReq;
import com.h3c.o2o.portal.protocol.entity.AuthMessageCode;
import com.h3c.o2o.portal.protocol.func.TokenMgr;
import com.h3c.oasis.o2oserver.bootstrap.JunitLoader;
import com.h3c.oasis.o2oserver.bootstrap.ServerContext;

import net.sf.json.JSONObject;

/**
 * add description of types here
 *
 * @author dkf5133
 */
public class LoginMgrTest {

    private Log log = LogFactory.getLog(LoginMgrTest.class);

    private LoginMgr service;

    private CacheApiMgr cacheApiMgr;

    private TokenMgr tokenMgr;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        JunitLoader.getInstance().start();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        JunitLoader.getInstance().stop();
    }
    
    @Before
    public void setUp() throws Exception {
        System.out.println("setUp()...");
        cacheApiMgr = (CacheApiMgr) ServerContext
            .getRootAppContext().getBean("cacheApiMgr");

        service = (LoginMgr) ServerContext
            .getRootAppContext().getBean("portalLoginMgr");

        tokenMgr = (TokenMgr) ServerContext
            .getRootAppContext().getBean("tokenMgr");
    }

    @After
    public void tearDown() throws Exception {
        System.out.println("tearDown()...");
    }

    @Test
    public void testGetPageImageInfo() {
        // 封装数据
        PageImageReq pageReq = new PageImageReq();
        pageReq.setNasId(Long.valueOf(-1));
        pageReq.setPageType("1");
        pageReq.setTemplateId("1");
        // 执行
        JSONObject imageConfig = service.getPageImageInfo(pageReq);
        log.info("--------->image info: " + imageConfig.toString());
        // 判断执行结果
        Assert.assertTrue(imageConfig.toString().matches(".*image100.*"));
    }

    @Test
    public void testGetAuthConfig() {
        // 取得店铺ID
        Iterator<Long> ite = cacheApiMgr.getAuthCfgCacheMgr().getKeys().iterator();

        Long storeId = null;
        if (ite.hasNext()) {
            storeId = ite.next();
        }
        LoginReq loginReq = new LoginReq();
        loginReq.setNasId(storeId);
        loginReq.setRedirect_uri("redirect_uri");
        loginReq.setSsid("ssid");
        loginReq.setTemplateId(1l);
        loginReq.setUserip("1.1.1.1");
        loginReq.setUsermac("aa:bb:cc:dd:ee:ff");
        loginReq.setUserurl("userurl");
        
        // 执行
        AuthCfgResp authConfig = null;
        if (storeId != null) {
            authConfig = service.getAuthConfig(loginReq);
        } else {
            log.info("--------->There is no data can be used for test. Please make data.");
            Assert.assertTrue(false);
            return;
        }

        // 判断执行结果
        GsonBuilder gb = new GsonBuilder();
        gb.disableHtmlEscaping();
        String json = gb.create().toJson(authConfig);

        log.info("--------->authConfig info: " + json);
        Assert.assertTrue(json.matches(".*phone2guding.*weixin.*qq.*"));
    }

    @Test
    public void testQuickLogin() {
        // 取得店铺ID
        Iterator<Long> ite = cacheApiMgr.getAuthCfgCacheMgr().getKeys().iterator();
        Long storeId = null;
        if (ite.hasNext()) {
            storeId = ite.next();
        } else {
            storeId = -1l;
        }

        LoginReq loginReq = new LoginReq();
        loginReq.setNasId(storeId);
        loginReq.setRedirect_uri("redirect_uri");
        loginReq.setSsid("ssid");
        loginReq.setTemplateId(1l);
        loginReq.setUserip("1.1.1.1");
        loginReq.setUsermac("aa:bb:cc:dd:ee:ff");
        loginReq.setUserurl("userurl");
        // 执行
        String url = service.quickLogin(loginReq);
        log.info("--------->url: " + url);
        // 判断执行结果
        AuthCfg config = null;
            config = (AuthCfg) cacheApiMgr.getAuthCfg(storeId, loginReq.getSsid());
        if (config.getAuthType().intValue() == AuthCfg.AUTH_TYPE_QUICKlOGIN) {
            Assert.assertTrue(url.matches("^.+code=.+&portal_server=http://.+$"));
        } else if (config.getAuthType().intValue() == AuthCfg.AUTH_TYPE_ACCOUNTLOGIN) {
            Assert.assertTrue(url.matches("^.+login.jsp\\?.+$"));
        }
    }

    @Test
    public void testSmsLogin() {
        // 取得店铺ID
        Iterator<Long> ite = cacheApiMgr.getAuthCfgCacheMgr().getKeys().iterator();
        Long storeId = null;
        if (ite.hasNext()) {
            storeId = ite.next();
        } else {
            storeId = -1l;
        }

        // 四位随机数
        Integer authCode = ((Double)(Math.random() * 9000 + 1000)).intValue();

        SmsLoginReq loginReq = new SmsLoginReq();
        loginReq.setNasId(storeId);
        loginReq.setRedirect_uri("redirect_uri");
        loginReq.setSsid("ssid");
        loginReq.setUserip("userip");
        loginReq.setUsermac("usermac");
        loginReq.setUserurl("userurl");
        loginReq.setUserName("13261891032");
        loginReq.setPassword(authCode.toString());

        // 生成授权码
        AuthMessageCode amc = new AuthMessageCode(authCode.toString());
        amc.setExpire_in(AuthMessageCode.MESSAGE_CODE_ALIVE_TIME);
        tokenMgr.putSmsCode(loginReq.getUserName(), amc);
        // 执行
        String redirect_uri = service.smsLogin(loginReq);
        log.info("--------->url: " + redirect_uri);
        // 判断执行结果
        Assert.assertTrue(redirect_uri.matches("^.+code=.+&portal_server=http://.+$"));
    }

    @Test
    public void testAccountLogin() {
        // 封装参数
        Iterator<RegistUserCacheKey> userIte = cacheApiMgr.getRegistUserCacheMgr().getKeys().iterator();
        RegistUser user = null;
        while (userIte.hasNext()) {
            RegistUser temp = cacheApiMgr.getRegistUserCacheMgr().get(
                userIte.next());
            if (temp.getUserType().intValue() == RegistUser.USER_TYPE_ACCOUNT) {
                user = temp;
                break;
            }
        }
        if (user == null){
            log.info("--------->There is no data can be used for test. Please make data.");
            Assert.assertTrue(false);
            return;
        }

        SmsLoginReq accountLoginReq = new SmsLoginReq();
        accountLoginReq.setNasId(user.getStoreId());
        accountLoginReq.setRedirect_uri("redirect_uri");
        accountLoginReq.setSsid("ssid");
        accountLoginReq.setUserip("userip");
        accountLoginReq.setUsermac("usermac");
        accountLoginReq.setUserurl("userurl");
        accountLoginReq.setUserName(user.getUserName());
        accountLoginReq.setPassword(user.getAndDecryptPassword());

        // 执行
        String redirect_uri = service.accountLogin(accountLoginReq);
        log.info("--------->url: " + redirect_uri);
        // 判断执行结果
        Assert.assertTrue(redirect_uri.matches("^.+code=.+&portal_server=http://.+$"));
    }

    @Test
    public void testRedirectToHome() {
        // 取得店铺ID
        Iterator<Long> ite = cacheApiMgr.getAuthCfgCacheMgr().getKeys().iterator();
        Long storeId = null;
        if (ite.hasNext()) {
            storeId = ite.next();
        } else {
            storeId = -1l;
        }
        Map<String, String[]> params = new HashMap<String, String[]>();
        params.put("nas_id", new String[]{storeId.toString()});

        HomePageReq homePageReq = new HomePageReq();
        homePageReq.setNas_id(storeId);
        homePageReq.setSsid("111");
        homePageReq.setTemplateId(1L);
        String url = service.redirectToHome(homePageReq);
        log.info("--------->url: " + url);

        String configUrl = cacheApiMgr.getConfigureParam(
            storeId, homePageReq.getSsid(), AuthCfg.STORE_CONFIGUE_URL);
        if (StringUtils.isNotBlank(url)) {
            Assert.assertTrue(url.equals(configUrl));
        } else {
            Assert.assertTrue(url.matches("^.+home.jsp\\?.+$"));
        }
    }
}
