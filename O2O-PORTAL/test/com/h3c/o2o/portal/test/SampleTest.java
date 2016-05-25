package com.h3c.o2o.portal.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.h3c.oasis.o2oserver.bootstrap.ServerContext;
import com.qq.connect.utils.RandomStatusGenerator;

/**
 * 单元测试样例，实际实现应该存放在对应包内。
 * @author l04133
 *
 */
public class SampleTest {

	private Log log = LogFactory.getLog(SampleTest.class);

	@Before
	public void setUp() throws Exception {
		System.out.println("setUp()...");
	}

	@After
	public void tearDown() throws Exception {
		System.out.println("tearDown()...");
	}

	@Test
	public void testEcho() {
		log.info(ServerContext.getO2OHome());
		System.out.println(RandomStatusGenerator.getUniqueState());
	}

}
