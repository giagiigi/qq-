package com.h3c.o2o.portal.test;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.h3c.o2o.portal.auth.func.OptAuthcfgListenerTest;
import com.h3c.o2o.portal.blacklist.func.OptBlackListListenerTest;
import com.h3c.o2o.portal.login.func.LoginMgrTest;
import com.h3c.o2o.portal.protocol.func.ProtocolMgrTest;
import com.h3c.o2o.portal.publishMng.func.OptPublishMngListenerTest;
import com.h3c.o2o.portal.publishMng.func.OptPublishParaListenerTest;
import com.h3c.o2o.portal.theme.func.OptThemeListenerTest;
import com.h3c.o2o.portal.user.func.OptRegaccountListenerTest;
import com.h3c.o2o.portal.useragent.func.UserAgentMgrImplTest;
import com.h3c.oasis.o2oserver.bootstrap.JunitLoader;

/**
 * 测试套件入口，添加新的TestCase只要在此标注中补充即可。
 * @author l04133
 *
 */
@RunWith(Suite.class)
@SuiteClasses({ SampleTest.class, OptAuthcfgListenerTest.class,
    OptThemeListenerTest.class, OptRegaccountListenerTest.class,
    LoginMgrTest.class, ProtocolMgrTest.class, OptBlackListListenerTest.class,
    OptPublishMngListenerTest.class, OptPublishParaListenerTest.class,
    UserAgentMgrImplTest.class})
public class AllTests {
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        JunitLoader.getInstance().start();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        JunitLoader.getInstance().stop();
    }
}
