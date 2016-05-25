/*
 * Copyright (c) 2015, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC V300R002
 * Module Name :
 * Date Created: 2015-6-26
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
package com.h3c.o2o.portal.protocol.func;

import com.h3c.o2o.portal.login.entity.LoginReq;
import com.h3c.o2o.portal.login.entity.WeixinConnectWifiPara;
import com.h3c.o2o.portal.protocol.entity.AccessToken;
import com.h3c.o2o.portal.protocol.entity.LoginParam;
import com.h3c.o2o.portal.protocol.entity.OnlineUserInfo;
import com.h3c.o2o.portal.protocol.entity.TokenReq;
import com.h3c.o2o.portal.protocol.entity.TpLoginInfo;
import com.h3c.o2o.portal.protocol.entity.UserInfo;
import com.h3c.o2o.portal.protocol.entity.UserStatus;

/**
 * O2OЭ�鴦��ӿ�
 *
 * @author j09980
 */
public interface ProtocolMgr {

    /**
     * ��¼�ض����Ĵ���
     *
     * @param param ��¼����
     * @return �ض���URL��ַ��storeId
     */
    String loginRedirect(LoginParam param, String ua);
    
    /**
     * 
     * ����app�ض���ֱ����֤�ɹ�������ע���û�������Ĭ��ģ��template01��
     * ����¼�ɹ�ҳ�桯 
     *
     * @param param �ض��������userurl�а���authtype=lvzhouapp
     * @param ua
     * @return
     */
    String appRedirect(LoginParam param, String ua);
    
    String tpLoginVerify(TpLoginInfo info);

    /**
     * ��ȡAccessToken
     *
     * @return AccessToken
     */
    AccessToken getAccessToken(TokenReq tokenReq);

    /**
     * ��ȡ�û���Ϣ
     *
     * @return �û���Ϣ
     */
    UserInfo getUserInfo(String token, String remoteHostIp);

    /**
     * �ϴ������û��б�
     *
     * @param onlineUserInfo �����û��б�
     * @return �û�״̬
     */
    UserStatus uploadOnlines(OnlineUserInfo onlineUserInfo);

    /**
     * �û����߲���
     */
    void userOffline(String ip);
    
    /**
     * �Ƿ����������΢����Wifi
     * @param param
     * @return
     */
    public boolean isOnlyEnabledWxWifi(LoginParam param);
    
    /**
     * ��ȡ΢����Wifi����
     * @param loginInfo
     * @return
     */
    public WeixinConnectWifiPara getWxWifiPara(LoginReq loginInfo);
}
