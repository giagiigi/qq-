package com.h3c.o2o.portal.useragent.func;

import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.h3c.o2o.portal.entity.UserAgent;
import com.h3c.oasis.o2oserver.bootstrap.ServerContext;

public class UserAgentMgrImplTest {

    private UserAgentMgr servie;

    @Before
    public void setUp() throws Exception {
        System.out.println("setUp()...");
        servie = (UserAgentMgr) ServerContext
            .getRootAppContext().getBean("portalUserAgentMgr");
    }

    @After
    public void tearDown() throws Exception {
        System.out.println("tearDown()...");
    }

    @Test
    public void testEcho() throws IOException {
        String uaStr = "ua:Mozilla/5.0 (Linux; U; Android 4.2.2; zh-cn; vivo Y17W Build/JDQ39) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30";
        UserAgent ua = servie.parseUserAgent(uaStr);
        System.out.println(ua.toString());
        Assert.assertTrue(ua.getTerminalOs() != null);
    }

    public void setServie(UserAgentMgr servie) {
        this.servie = servie;
    }

}
