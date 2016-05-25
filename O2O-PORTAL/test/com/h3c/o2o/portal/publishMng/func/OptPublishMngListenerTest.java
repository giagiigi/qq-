/*
 * Copyright (c) 2007-2015, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC V700R001
 * Module Name :
 * Date Created: 2015年10月20日
 * Creator     : dkf5133
 * Description :
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 * 2015年10月20日  dkf5133             O2O-PORTAL-UI project, new code file.
 *
 *------------------------------------------------------------------------------
 */

package com.h3c.o2o.portal.publishMng.func;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.h3c.o2o.auth.rabbitmq.MqRequest;
import com.h3c.o2o.auth.rabbitmq.MqResponse;
import com.h3c.o2o.portal.UamConstant;
import com.h3c.oasis.o2oserver.bootstrap.ServerContext;

public class OptPublishMngListenerTest {

	private Log log = LogFactory.getLog(getClass());

	private RabbitTemplate rabbitTemplate;

	OptPublishMngListener listener = null;

	@Before
    public void setUp() throws Exception {
        System.out.println("setUp()...");
        listener = (OptPublishMngListener) ServerContext
            .getRootAppContext().getBean("o2oportalOptPublishMngListener");
        rabbitTemplate = (RabbitTemplate) ServerContext
            .getRootAppContext().getBean("o2oportalRabbitTemplate");
    }

    @After
    public void tearDown() throws Exception {
        System.out.println("tearDown()...");
    }

    @Test
    public void testEcho() throws IOException {
    	log.info(ServerContext.getO2OHome());
        log.info("...");
        List<Long> ids = new ArrayList<Long>();
        ids.add(1l);
        ids.add(2l);
        ids.add(3l);
        ids.add(4l);

        MqRequest request = new MqRequest();
        // 删除
        request.setType(3);
        request.setIds(ids);
        MqResponse strResultDel = (MqResponse) rabbitTemplate.convertSendAndReceive(
            UamConstant.PUBLISH_MNG_OPT_EXCHANGE, UamConstant.PUBLISH_MNG_OPT_EXCHANGE_KEY, request);
        Assert.assertTrue(strResultDel.isSuccess());
        // 增加
        request.setType(1);
        request.setIds(ids);
        MqResponse strResultAdd = (MqResponse) rabbitTemplate.convertSendAndReceive(
            UamConstant.PUBLISH_MNG_OPT_EXCHANGE, UamConstant.PUBLISH_MNG_OPT_EXCHANGE_KEY, request);
        Assert.assertTrue(strResultAdd.isSuccess());
        // 修改
        request.setType(2);
        request.setIds(ids);
        MqResponse strResultMod = (MqResponse) rabbitTemplate.convertSendAndReceive(
            UamConstant.PUBLISH_MNG_OPT_EXCHANGE, UamConstant.PUBLISH_MNG_OPT_EXCHANGE_KEY, request);
        Assert.assertTrue(strResultMod.isSuccess());
    }
}
