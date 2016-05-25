/*
 * Copyright (c) 2007-2013, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC v7
 * Module Name : iMC
 * Date Created: 2015-6-29
 * Creator     : dkf5133
 * Description :
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 * 2015-6-29     dkf5133          iMC project, new code file.
 *
 *------------------------------------------------------------------------------
 */
package com.h3c.o2o.portal.message.func;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.h3c.o2o.mng.rabbitmq.entity.MsgRequest;
import com.h3c.o2o.mng.rabbitmq.entity.MsgResponse;
import com.h3c.o2o.portal.PortalConstant;
import com.h3c.o2o.portal.PortalException;
import com.h3c.o2o.portal.protocol.entity.AuthMessageCode;
import com.h3c.o2o.portal.protocol.func.TokenMgr;
import com.h3c.oasis.o2oserver.util.StringManager;

/**
 * @author dkf5133
 *
 */
public class MessageMgrImpl implements MessageMgr {

    /** ��Դ���ʶ��� */
    private static StringManager sm = StringManager
        .getManager("com.h3c.o2o.portal");

    private Log log = LogFactory.getLog(getClass());

    private DateFormat chineseFormater = new SimpleDateFormat(
        sm.getString("o2o.portal.date.chinese.format"));

    private static final String PHONE_NO_REGULAE = "^1\\d{10}$";

    /**�ֻ�����У��  ֻУ��ǰ��λ  13_+ 158+ 159+ 188+ 189+ */
    private static final String PHONE_NO = "^[1]([3][0-9]{1}|[5][0-2]{1}|59|58|81|[8][5-9]{1})[0-9]{8}$";

    private TokenMgr tokenMgr;
    
    private RabbitTemplate rabbitTemplate;

    private int expireIn = AuthMessageCode.MESSAGE_CODE_ALIVE_TIME;

    public void sendAuthMessage(String phoneNO, String authCfgMsgCont, String interval) {
        if (phoneNO.matches(PHONE_NO_REGULAE)) {
            // ��λ�����
            Integer authCode = ((Double)(Math.random() * 9000 + 1000)).intValue();
            String date = chineseFormater.format(new Date());
            // У���������
            String message = MessageFormat.format(authCfgMsgCont,
                date, authCode.toString(), interval);
            String[] phones = new String[] { phoneNO };
            // ��Ϣ����
            MsgRequest request = new MsgRequest();
            request.setPhoneNums(phones);
            request.setMessage(message);
            MsgResponse response = null;
            try {
                // ���ͣ�����ȡ���
                response = (MsgResponse)rabbitTemplate.convertSendAndReceive(
                    PortalConstant.SENDMESSAGE_EXCHANGE,
                    PortalConstant.SENDMESSAGE_EXCHANGE_KEY, request);
            } catch (Exception e) {
                throw new PortalException("Send message failed, error msg: "
                    + e.getMessage());
            }
            // ������0Ϊʧ��
            if (null == response || 0 != response.getRetCode()) {
                throw new PortalException("Send message failed.");
            }
            if (log.isDebugEnabled()) {
                log.debug("Send message successed, code: " + authCode);
            }
            AuthMessageCode amc = new AuthMessageCode(authCode.toString());
            amc.setExpire_in(expireIn);
            tokenMgr.putSmsCode(phoneNO, amc);
        } else {
            throw new PortalException(
                sm.getString("o2o.portal.phoneNo.error"));
        }
    }

    public AuthMessageCode getCode(String phoneNO){
        //�ж��Ƿ����ֻ���  �����򷵻ؿ�
        boolean flag = phoneNO.matches(PHONE_NO);
        if(!flag){
            return null;
        }
        // ��λ����� ������λ��֤��
        Integer authCode = ((Double)(Math.random() * 9000 + 1000)).intValue();
        AuthMessageCode amc = new AuthMessageCode(authCode.toString());
        amc.setExpire_in(expireIn);
        tokenMgr.putSmsCode(phoneNO, amc);
        return amc;
    }

    public void setTokenMgr(TokenMgr tokenMgr) {
        this.tokenMgr = tokenMgr;
    }

    public RabbitTemplate getRabbitTemplate() {
        return rabbitTemplate;
    }

    public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void setExpireIn(int expireIn) {
        this.expireIn = expireIn;
    }

}
