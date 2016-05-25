/*
 * Copyright (c) 2015, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC V300R002
 * Module Name :
 * Date Created: 2015-8-6
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
package com.h3c.o2o.portal.theme.func;

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
import com.h3c.o2o.portal.auth.func.OptAuthcfgListenerTest;
import com.h3c.oasis.o2oserver.bootstrap.ServerContext;

/**
 * add description of types here
 *
 * @author dkf5133
 */
public class OptThemeListenerTest {
private Log log = LogFactory.getLog(OptAuthcfgListenerTest.class);

    OptThemeListener listener = null;

    private RabbitTemplate rabbitTemplate;
    
    @Before
    public void setUp() throws Exception {
        System.out.println("setUp()...");
        listener = (OptThemeListener) ServerContext
            .getRootAppContext().getBean("o2oportalOptThemeListener");
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
        // É¾³ý
        request.setType(3);
        request.setIds(ids);
        MqResponse strResultDel = (MqResponse) rabbitTemplate.convertSendAndReceive(
            UamConstant.THEME_OPT_EXCHANGE, UamConstant.THEME_OPT_EXCHANGE_KEY, request);
        Assert.assertTrue(strResultDel.isSuccess());
        // Ôö¼Ó
        request.setType(1);
        request.setIds(ids);
        MqResponse strResultAdd = (MqResponse) rabbitTemplate.convertSendAndReceive(
            UamConstant.THEME_OPT_EXCHANGE, UamConstant.THEME_OPT_EXCHANGE_KEY, request);
        Assert.assertTrue(strResultAdd.isSuccess());
        // ÐÞ¸Ä
        request.setType(2);
        request.setIds(ids);
        MqResponse strResultMod = (MqResponse) rabbitTemplate.convertSendAndReceive(
            UamConstant.THEME_OPT_EXCHANGE, UamConstant.THEME_OPT_EXCHANGE_KEY, request);
        Assert.assertTrue(strResultMod.isSuccess());
    }
}
