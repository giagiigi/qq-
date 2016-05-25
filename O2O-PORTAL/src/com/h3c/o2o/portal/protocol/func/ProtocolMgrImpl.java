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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.h3c.o2o.portal.PortalErrorCodes;
import com.h3c.o2o.portal.PortalException;
import com.h3c.o2o.portal.authuser.func.AuthUserMgr;
import com.h3c.o2o.portal.cache.api.CacheApiMgr;
import com.h3c.o2o.portal.cache.key.RegistUserCacheKey;
import com.h3c.o2o.portal.common.CommonUtils;
import com.h3c.o2o.portal.common.FuncUtil;
import com.h3c.o2o.portal.common.HttpOrHttpsMgr;
import com.h3c.o2o.portal.entity.AccessDetail;
import com.h3c.o2o.portal.entity.AuthCfg;
import com.h3c.o2o.portal.entity.AuthParam;
import com.h3c.o2o.portal.entity.AuthUser;
import com.h3c.o2o.portal.entity.OnlineUser;
import com.h3c.o2o.portal.entity.PublishMng;
import com.h3c.o2o.portal.entity.RegistUser;
import com.h3c.o2o.portal.entity.ThemePage;
import com.h3c.o2o.portal.entity.ThemeTemplate;
import com.h3c.o2o.portal.entity.UserAgent;
import com.h3c.o2o.portal.haihaiconfig.func.HaihaiAppMgr;
import com.h3c.o2o.portal.login.entity.LoginReq;
import com.h3c.o2o.portal.login.entity.WeixinConnectWifiPara;
import com.h3c.o2o.portal.login.func.LoginMgr;
import com.h3c.o2o.portal.protocol.dao.ProtocolDao;
import com.h3c.o2o.portal.protocol.entity.AccessToken;
import com.h3c.o2o.portal.protocol.entity.CodeInfo;
import com.h3c.o2o.portal.protocol.entity.LoginParam;
import com.h3c.o2o.portal.protocol.entity.Offline;
import com.h3c.o2o.portal.protocol.entity.Online;
import com.h3c.o2o.portal.protocol.entity.OnlineUserInfo;
import com.h3c.o2o.portal.protocol.entity.State;
import com.h3c.o2o.portal.protocol.entity.TokenInfo;
import com.h3c.o2o.portal.protocol.entity.TokenReq;
import com.h3c.o2o.portal.protocol.entity.TpLoginInfo;
import com.h3c.o2o.portal.protocol.entity.User;
import com.h3c.o2o.portal.protocol.entity.UserInfo;
import com.h3c.o2o.portal.protocol.entity.UserStatus;
import com.h3c.o2o.portal.user.func.AccessUserDetailMgr;
import com.h3c.o2o.portal.user.func.OnlineUserMgr;
import com.h3c.o2o.portal.user.func.RegistUserMgr;
import com.h3c.o2o.portal.useragent.func.UserAgentMgr;
import com.h3c.o2o.portal.util.WifiUtils;
import com.h3c.oasis.o2oserver.bootstrap.ServerContext;
import com.h3c.oasis.o2oserver.util.StringManager;

/**
 * O2OЭ�鴦��ʵ����
 *
 * @author j09980
 */
public class ProtocolMgrImpl implements ProtocolMgr {

	/**
	 * Ψһʵ��--��servlet��ʹ��
	 */
	private static ProtocolMgrImpl INSTANCE;

	/** ��¼����ʱ��־���� */
	private Log log = LogFactory.getLog(getClass());

	private ProtocolDao protocolDao;

	private UserAgentMgr userAgentMgr;

	private TokenMgr tokenMgr;

	/** ��Դ���ʶ��� */
	private static StringManager sm = StringManager.getManager("com.h3c.o2o.portal");

	private RegistUserMgr userMgr;

	private OnlineUserMgr onlineUserMgr;

	private AccessUserDetailMgr accessUserDetailMgr;

	private HttpOrHttpsMgr httpOrHttps;

	@SuppressWarnings("unused")
	private HaihaiAppMgr haihaiAppMgr;

	/** ���ݻ���ӿ� */
	private CacheApiMgr cacheApiMgr;

	private OfflineCallback callback;

	private LoginMgr loginMgr;

	/**
	 * ��ȡ��������֤�û���Ϣ
	 */
	private AuthUserMgr authUserMgr;
	
	/**
	 * ���û������һ����֤���Ƿ�����һ����֤ҳ��
	 */
	private Boolean skipQuickLogin = true;
	
	private String skipQuickLoginInAuth = "1";
	
	/**
	 * �������ֻ������΢����WiFi���Ƿ�ֱ����ת������΢�ſͻ���ҳ��
	 */
	private Boolean skipLoginIfOnlyWxWifi = false;
	
	private String skipLoginIfOnlyWxWifiInAuth = "1";
	
	/** ΢�Ź��ں���֤·����д��spring�����ļ��� */
	private String weChatAuthUrl = "/weixin/redirect";
	
	/**
	 * ��ȡΨһʵ��
	 *
	 * @return ʵ��
	 */
	public static final ProtocolMgr get() {
		return INSTANCE;
	}

	/**
	 * ��ʼ��
	 */
	public void init() {
		INSTANCE = this;
	}

	/**
	 * ��ȡURLencode֮���State
	 * 
	 * @param storeId
	 * @param ownerId
	 * @param accountId
	 * @param ssid
	 * @param userMac
	 * @return
	 */
	private String getStateEncodedStr(Long storeId, Long ownerId, Long accountId, String ssid, String userMac,
			String userIp) {
		State st = new State();
		st.setAccountId(accountId);
		st.setStoreId(storeId);
		st.setOwnerId(ownerId);
		st.setSsid(ssid);
		st.setUsermac(userMac);
		st.setUserIp(null != userIp ? userIp.trim() : "");
		try {
			String stStr = State.toState(st);
			if(null != stStr && stStr.length() > 128){
				log.warn("State is toooo long! state = " + st);
			}
			return stStr;
		} catch (Exception e) {
			log.warn(null, e);
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String loginRedirect(LoginParam param, String ua) {
		log.info("ua:" + ua);
		log.info(param);

		// ��ȡstoreid(ʵ���ϱ�����nas_id������)
		Long storeId = param.getNasId();

		// �޸�֪ʱ��
		String noSensationTimeStr = cacheApiMgr.getConfigureParam(storeId, param.getSsid(), AuthCfg.NO_SENSATION_TIME);
		if (log.isDebugEnabled()) {
			log.debug("noSensationTime: " + noSensationTimeStr);
		}

		// ��ȡURL
		String url = null;
		// �Ƚ����޸�֪��¼
		if (noSensationTimeStr != null && !noSensationTimeStr.trim().isEmpty()) {
			// ���ѱ����ڣ����ʹ���ҳ��
			url = noSensationLogin(noSensationTimeStr, storeId, param);
		}

		// ģ��ID
		ThemeTemplate template = cacheApiMgr.getThemeTemplate(storeId, param.getSsid(), null);
		Long templateId = template.getId();
		log.info("templateId" + templateId);
		// ����޸�֪��¼ʧ��
		if (url == null) {
			// �����΢�����󣬽���΢����֤
			if (isWeChatRequest(ua)) {
				String weixinServerDomain = FuncUtil.getWeixinServerDomain();
				PublishMng pubMng = cacheApiMgr.getPublishMng(storeId, param.getSsid());
				checkAuthCfg4WechatOauth(storeId, param.getSsid());
				url = httpOrHttps.getHttpOrHttps() + "://" + weixinServerDomain + weChatAuthUrl + "?state="
						+ getStateEncodedStr(storeId, pubMng.getOwnerId(), pubMng.getWeixinAccountId(), param.getSsid(),
								param.getUsermac(), param.getUserip());
			} else {
				// ��ת����¼ҳ��
				url = getRedirectUrl(template, param);
				tokenMgr.removeUserUrl(param.getUsermac());
			}
		} else {
			// �޸�֪��¼����ʾ�κ�ģ��ҳ�棬��֤�ɹ�����ת��ԭ���û����ʵ�ҳ��
			tokenMgr.putUserUrl(param.getUsermac(), param.getUserurl());
		}

		log.info("url:" + url);

		tokenMgr.putRedirectUri(storeId, param.getRedirect_uri());
		tokenMgr.putTemplateId(param.getUsermac(), templateId);
		if (ua != null) {
			tokenMgr.putUserAgent(param.getUsermac(), ua);
		}
		tokenMgr.putSsid(param.getUsermac(), param.getSsid());
		return url;
	}

	/**
	 * ΢��Oauth��֤У�鷢������<br>
	 * Ҫ����Ҫ����΢��Oauth��֤<br>
	 * 
	 * @param storeId
	 * @param ssid
	 */
	private void checkAuthCfg4WechatOauth(Long storeId, String ssid) throws PortalException {
		AuthCfg authCfg = cacheApiMgr.getAuthCfg(storeId, ssid);
		if (authCfg.getIsEnableWinxin().intValue() != 1) {
			// ΢��Oauth��֤δ����
			throw new PortalException(PortalErrorCodes.WECHAT_OAUTH_DISABLED, sm.getString("errorCode.60020"));
		}
	}

	/**
	 * �Ƿ����������΢����Wifi
	 * 
	 * @param param
	 * @return
	 */
	public boolean isOnlyEnabledWxWifi(LoginParam param) {
		AuthCfg authCfg = cacheApiMgr.getAuthCfg(param.getNasId(), param.getSsid());
		return isOnlyEnabledWxWifi(authCfg);
	}

	/**
	 * �Ƿ����������΢����Wifi
	 * 
	 * @param authCfg
	 * @return
	 */
	private boolean isOnlyEnabledWxWifi(AuthCfg authCfg) {
		Integer enabled = Integer.valueOf(1);
		return null != authCfg && enabled.equals(authCfg.getIsWeixinConnectWifi())
				&& !enabled.equals(authCfg.getIsEnableWinxin()) && !enabled.equals(authCfg.getIsEnableAccount())
				&& !enabled.equals(authCfg.getIsEnableAli()) && !enabled.equals(authCfg.getIsEnableQQ())
				&& !enabled.equals(authCfg.getIsEnableSms()) && !enabled.equals(authCfg.getIsEnableWinxin());
	}

	/**
	 * ��װһ��LoginReq
	 * 
	 * @param param
	 * @param template
	 * @return
	 */
	private LoginReq packLoginInfo(LoginParam param, ThemeTemplate template) {
		LoginReq req = new LoginReq();
		req.setBssid("");
		req.setNasId(param.getNasId());
		req.setRedirect_uri(param.getRedirect_uri());
		req.setSsid(param.getSsid());
		req.setTemplateId(null != template ? template.getId() : 0L);
		req.setUserip(param.getUserip());
		req.setUsermac(param.getUsermac());
		req.setUserurl(param.getUserurl());
		return req;
	}

	/**
	 * ��ȡ΢����Wifi�ĵ�¼ҳ��URL
	 * 
	 * @param req
	 * @return
	 */
	private String getWifiLoginUrl(LoginReq req) {
		String prefix = "";
		int port = FuncUtil.getPortalServerPort();
		if ("https".equals(httpOrHttps.getHttpOrHttps())) {
			prefix = "https://" + FuncUtil.getPortalDomain() + (443 != port ? ":" + port : "");
		} else {
			prefix = "http://" + FuncUtil.getPortalDomain() + (80 != port ? ":" + port : "");
		}
		String suffix = "/portal/wifilogin.jsp?" + "nas_id=" + req.getNasId() + "&redirect_uri=" + req.getRedirect_uri()
				+ "&ssid=" + WifiUtils.safeEncode(req.getSsid()) + "&templateId=" + req.getTemplateId() + "&userip="
				+ req.getUserip() + "&usermac=" + req.getUsermac() + "&userurl=" + req.getUserurl();
		return prefix + suffix;
	}

	/**
	 * ��ȡ��¼ҳ���ַ
	 * 
	 * @param template
	 * @param param
	 * @return
	 */
	private String getRedirectUrl(ThemeTemplate template, LoginParam param) {
		String url = null;
		AuthCfg authCfg = cacheApiMgr.getAuthCfg(param.getNasId(), param.getSsid());
		List<AuthParam> authParamList = authCfg.getAuthParamList();
		boolean skipQuickLogin = getSkipQuickLogin();
		boolean skipLoginIfOnlyWxWifi = getSkipLoginIfOnlyWxWifi();
		try {
			for (AuthParam authParam : authParamList) {
				if("SKIP_QUICK_LOGIN".equals(authParam.getAuthParamName())){
					if(skipQuickLoginInAuth.equals(authParam.getAuthParamValue())){
						skipQuickLogin = true;
					}else{
						skipQuickLogin = false;
					}
				}
				if("SKIP_LOGIN_ONLY_WXWIFI".equals(authParam.getAuthParamName())){
					if(skipLoginIfOnlyWxWifiInAuth.equals(authParam.getAuthParamValue())){
						skipLoginIfOnlyWxWifi = true;
					}else{
						skipLoginIfOnlyWxWifi = false;
					}
				}
			}
		} catch (Exception e) {
			log.debug("get skip param error we will use default param");
		}
		if (skipQuickLogin && AuthCfg.AUTH_TYPE_QUICKlOGIN != authCfg.getAuthType()) {
			// ���û������һ����¼����ֱ����ת����¼ҳ��
			url = cacheApiMgr.getThemePageURI(template, ThemePage.PAGE_TYPE_LOGIN);
			// �������������΢����Wifi������ת��������΢����Wifi��¼ҳ
			if (skipLoginIfOnlyWxWifi && isOnlyEnabledWxWifi(authCfg)) {
				LoginReq loginInfo = packLoginInfo(param, template);
				return getWifiLoginUrl(loginInfo);
			}
		} else {
			// �������һ����¼��ת����������ҳ��
			url = cacheApiMgr.getThemePageURI(template, ThemePage.PAGE_TYPE_QUICKLOGIN);
		}
		url += "?templateId=" + template.getId() + param.toParamString();
		return url;
	}

	/**
	 * ��ȡ΢����Wifi����
	 * 
	 * @param loginInfo
	 * @return
	 */
	public WeixinConnectWifiPara getWxWifiPara(LoginReq loginInfo) {
		AuthCfg authCfg = cacheApiMgr.getAuthCfg(loginInfo.getNasId(), loginInfo.getSsid());
		return this.getLoginMgr().getWxWifiPara(authCfg, loginInfo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.h3c.o2o.portal.protocol.func.ProtocolMgr#appRedirect(com.h3c.o2o.
	 * portal.protocol.entity.LoginParam, java.lang.String)
	 */
	@Override
	public String appRedirect(LoginParam param, String ua) {
		// ��ȡstoreid(ʵ���ϱ�����nas_id������)
		Long storeId = param.getNasId();

		// ���ɻ���key
		RegistUserCacheKey key = new RegistUserCacheKey(storeId, param.getUsermac(), RegistUser.USER_TYPE_LZ_APP);
		// ��ѯ�û��Ƿ����
		RegistUser user = cacheApiMgr.getRegistUserCacheMgr().get(key);

		if (user == null) {
			Long ownerId = protocolDao.queryUserId(storeId);
			if (ownerId == null) {
				throw new PortalException(PortalErrorCodes.NOT_EXISTS_STORE, sm.getString("errorCode.71006"));
			}
			// ��������ڣ� �����û�
			user = createAppUser(param, key, ownerId);
		}
		// ������Ȩ��
		String code = tokenMgr.generateCode(param.getUsermac(), param.getUserip(), param.getNasId(), param.getSsid(),
				RegistUser.USER_TYPE_LZ_APP, user);

		tokenMgr.putRedirectUri(storeId, param.getRedirect_uri());
		if (ua != null) {
			tokenMgr.putUserAgent(param.getUsermac(), ua);
		}
		tokenMgr.putSsid(param.getUsermac(), param.getSsid());
		return FuncUtil.getLoginRedirectURI(param.getRedirect_uri(), code, param.getUserip(), false );
	}

	/**
	 * 
	 * ��������app�û�
	 *
	 * @param param
	 * @param key
	 * @param ownerId
	 * @return
	 */
	private RegistUser createAppUser(LoginParam param, RegistUserCacheKey key, Long ownerId) {
		RegistUser user = new RegistUser();
		user.setStoreId(param.getNasId());
		user.setUserName(param.getUsermac());
		user.setUserPassword(CommonUtils.encryptData("h3clzpt"));
		user.setUserType(RegistUser.USER_TYPE_LZ_APP);
		user.setLoginTime(System.currentTimeMillis());
		user.setOwnerId(ownerId);

		// ����
		userMgr.saveRegistUser(user);
		cacheApiMgr.getRegistUserCacheMgr().put(key, user);
		return user;
	}

	/**
	 * �ж������Ƿ�����΢�������
	 * 
	 * @param ua
	 *            user agent
	 * @return
	 */
	private boolean isWeChatRequest(String ua) {
		if (StringUtils.isNotBlank(ua)) {
			return ua.matches("^.*MicroMessenger.*$");
		} else {
			return false;
		}
	}

	/**
	 * �޸�֪��¼
	 * 
	 * @param noSensationTimeStr
	 *            �޸�֪ʱ�䣨�죩
	 * @param storeId
	 *            ����ID
	 * @param param
	 *            ��¼����
	 * @return ��¼�ɹ�ҳ��url������������޸�֪��¼����null
	 */
	private String noSensationLogin(String noSensationTimeStr, Long storeId, LoginParam param) {
		Integer noSensationTime = Integer.valueOf(noSensationTimeStr.trim());
		// �����Ϊ��
		if (noSensationTime != null && noSensationTime > 0) {
			Calendar c = Calendar.getInstance();
			// �����޸�֪ʱ��Ϊ��Ȼ�죬
			c.add(Calendar.DAY_OF_YEAR, -noSensationTime);
			c.set(Calendar.HOUR_OF_DAY, 23);
			c.set(Calendar.MINUTE, 59);
			c.set(Calendar.SECOND, 59);
			c.set(Calendar.MILLISECOND, 999);

			// 1���̶��˺��ǿ���ɾ���ģ��ӽ�����ϸ�в�ѯ���û�������û������ڣ���������½����
			// 2���������û���¼ʱ��ע���û����б������Ϣ�Ǿɵģ��ݲ���ȡ������Ϣ
			// 3�����ʹ��ͬһ��MAC��ַ���ӽ�����ϸ���в�ѯ��������¼��ʹ�����һ�ν���ļ�¼
			AccessDetail accessDetail = accessUserDetailMgr.queryAccessDetail(storeId, param.getSsid(),
					param.getUsermac(), c.getTimeInMillis());
			if (accessDetail != null) {
				RegistUser registUser = cacheApiMgr.getRegistUserCacheMgr().get(new RegistUserCacheKey(
						accessDetail.getStoreId(), accessDetail.getUserName(), accessDetail.getUserType()));
				if (registUser != null) {
					// �ж��Ƿ�Ϊ�������û�
					if (cacheApiMgr.isBlackUser(registUser)) {
						throw new PortalException(PortalErrorCodes.NO_SENSE_LOGIN_BLACK_USER,
								sm.getString("errorCode.60012"));
					}
					// �޸�֪��¼ʱ��Ҫ�����û�����ID���û�MAC��IP��ַ
					registUser.setStoreId(param.getNasId());
					registUser.setUserMac(param.getUsermac());
					registUser.setUserIp(param.getUserip());
					String code = tokenMgr.generateCode(param.getUsermac(), param.getUserip(), param.getNasId(),
							param.getSsid(), registUser.getUserType(), registUser);
					return FuncUtil.getLoginRedirectURI(param.getRedirect_uri(), code, param.getUserip(),
							RegistUser.USER_TYPE_WX_WIFI == registUser.getUserType().intValue());
				}
			}
		}
		return null;
	}

	/**
	 * ��������¼��֤
	 * 
	 * @param openId
	 * @param type
	 *            ��������֤���ͣ���ע���û����Ͷ�Ӧ��4��֧������֤��5��QQ��֤��6��΢�Ź��ں���֤
	 * @param storeId
	 * @param extInfo
	 */
	@Override
	public String tpLoginVerify(TpLoginInfo info) {
		if (log.isDebugEnabled()) {
			log.debug("login paras: " + info);
		}
		RegistUser user = cacheApiMgr.getRegistUserCacheMgr()
				.get(new RegistUserCacheKey(info.getStoreId(), info.getOpenId(), info.getType()));
		// �ж��Ƿ�Ϊ�������û�
		if (null != user && cacheApiMgr.isBlackUser(user)) {
			throw new PortalException(PortalErrorCodes.BLACK_USER, sm.getString("errorCode.60003"));
		}
		// ����code
		String code = tokenMgr.generateCode(info.getUserMac(), info.getUserIp(), info.getStoreId(), info.getSsid(),
				info.getType(), null);
		tokenMgr.putTpLoginInfo(code, info);

		// ��ȡ�ض���URI
		String redirectURIHeader = null;
		boolean isWxWiFi = RegistUser.USER_TYPE_WX_WIFI == info.getType().intValue();
		if (isWxWiFi) {
			redirectURIHeader = getWxWiFiRedirectUri(info, code);
		} else {
			redirectURIHeader = tokenMgr.getRedirectUri(info.getStoreId());
		}
		String redirectURL = FuncUtil.getLoginRedirectURI(redirectURIHeader, code, info.getUserIp(), isWxWiFi);
		if (StringUtils.isBlank(redirectURIHeader)) {
			// �ض���URI����Ϊ��
			throw new PortalException(PortalErrorCodes.REDIRECT_URI_NULL, sm.getString("errorCode.60013"));
		} else if (log.isDebugEnabled()) {
			log.debug("Redirect URL: " + redirectURL);
		}

		return redirectURL;
	}

	/**
	 * ����΢����WiFi�ض���URi
	 * 
	 * @return
	 */
	private String getWxWiFiRedirectUri(TpLoginInfo info, String code) {
		String redirectURIHeader = null;
		try {
			tokenMgr.putSsid(URLDecoder.decode(info.getUserMac(), "UTF-8"), URLDecoder.decode(info.getSsid(), "UTF-8"));
			redirectURIHeader = URLDecoder.decode(info.getRedirect_uri(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.warn("Unsupported Encoding", e);
		}
		if (StringUtils.isBlank(redirectURIHeader)) {
			// �ض���URI����Ϊ��
			throw new PortalException(PortalErrorCodes.REDIRECT_URI_NULL, sm.getString("errorCode.60013"));
		}
		return redirectURIHeader;
	}
	
	/**
	 * ����ע���û�
	 * 
	 * @return ע���û����������������û��ͽ�����ϸ
	 */
	private RegistUser createTPUser(TpLoginInfo tpLoginInfo, RegistUserCacheKey key) {
		RegistUser user = new RegistUser();
		user.setStoreId(tpLoginInfo.getStoreId());
		user.setUserName(tpLoginInfo.getOpenId());
		// ����Ϊ��h3c����ƽ̨���ļ�д
		user.setUserPassword(CommonUtils.encryptData("h3clzpt"));
		user.setUserTokenId(tpLoginInfo.getOpenId());
		user.setUserExtInfo(tpLoginInfo.getExtInfo());

		user.setUserType(tpLoginInfo.getType());
		user.setLoginTime(System.currentTimeMillis());
		user.setOwnerId(tpLoginInfo.getOwnerId());
		
		// ��������֤�����IP��MAC��ַ
		user.setUserIp(tpLoginInfo.getUserIp());
		user.setUserMac(tpLoginInfo.getUserMac());

		// ����
		userMgr.saveRegistUser(user);
		cacheApiMgr.getRegistUserCacheMgr().put(key, user);
		return user;
	}

	/**
	 * �������޸ĵ������û�
	 * 
	 * @param tpLoginInfo
	 *            �û���Ϣ
	 * @param loginType
	 *            �û�����
	 * @return
	 */
	private RegistUser createOrUpdateTPUser(TpLoginInfo tpLoginInfo) {
		// ���ɻ���key
		RegistUserCacheKey key = new RegistUserCacheKey(tpLoginInfo.getStoreId(), tpLoginInfo.getOpenId(),
				tpLoginInfo.getType());
		// ��ѯ�û�
		RegistUser user = cacheApiMgr.getRegistUserCacheMgr().get(key);
		// ���û������ڣ������û��������޸��û���չ��Ϣ
		if (user == null) {
			user = createTPUser(tpLoginInfo, key);
		} else {
			// �����û�IP��MAC
			user.setUserMac(tpLoginInfo.getUserMac());
			user.setUserIp(tpLoginInfo.getUserIp());
			user.setUserExtInfo(tpLoginInfo.getExtInfo());
			userMgr.updateRegistUser(user);
			cacheApiMgr.getRegistUserCacheMgr().put(key, user);
		}
		return user;
	}

	/**
	 * ������߸��������û�
	 * 
	 * @param user
	 *            ע���û�
	 * @param param
	 * @param loginType
	 */
	private void saveOrUpdateOnlineUser(RegistUser user, long accessStartTime, String remoteHostIp, long ownerId) {
		// �������߱�
		OnlineUser onlineUser = new OnlineUser();
		onlineUser.setStoreId(user.getStoreId());
		onlineUser.setUserId(user.getId());
		onlineUser.setOwnerId(ownerId);
		onlineUser.setUserName(user.getUserName());
		onlineUser.setUserType(user.getUserType());
		onlineUser.setAccessStartTime(accessStartTime);
		onlineUser.setAccessAcIp(FuncUtil.convertHostIpToLong(remoteHostIp));
		onlineUser.setAccessSsid(tokenMgr.getSsid(user.getUserMac()));
		if (log.isDebugEnabled()) {
			log.debug("User login ip: " + user.getUserIp());
			log.debug("User login ip(converted): " + FuncUtil.convertHostIpToLong(user.getUserIp()));
		}
		onlineUser.setUserIp(FuncUtil.convertHostIpToLong(user.getUserIp()));
		onlineUser.setUserMac(user.getUserMac());
		onlineUser.setDevManufacturer(user.getDevManufacturer());
		onlineUser.setDevOsType(user.getDevOsType());
		onlineUser.setMobileNo(user.getPhoneNumber());

		// ������߸��������û�
		OnlineUser oldOnLineUser = cacheApiMgr.getOnlineUserCacheMgr().get(user.getUserMac());
		if (null != oldOnLineUser) {
			// ����Ѿ���¼����ֱ�Ӹ�����������
			onlineUser.setId(oldOnLineUser.getId());
			onlineUserMgr.updateOnlineUser(onlineUser);
			cacheApiMgr.getOnlineUserCacheMgr().put(user.getUserMac(), onlineUser);
			if (log.isDebugEnabled()) {
				StringBuilder sbuilder = new StringBuilder();
				sbuilder.append("Update old onLineUser:\n");
				sbuilder.append("Old onLineUser: " + oldOnLineUser + "\n");
				sbuilder.append("New onLineUser: " + onlineUser);
				log.debug(sbuilder.toString());
			}
		} else {
			// ����
			onlineUserMgr.saveOnlineUser(onlineUser);
		}

		// ����ssid����
		tokenMgr.removeSsid(user.getUserMac());
		cacheApiMgr.getOnlineUserCacheMgr().put(user.getUserMac(), onlineUser);
	}

	/**
	 * {@inheritDoc}�� ����token������code���û���Ϣֻ��ͨ��token��ȡ
	 */
	@Override
	public AccessToken getAccessToken(TokenReq tokenReq) {
		CodeInfo info = tokenMgr.getCodeInfo(tokenReq.getCode(), true);
		// У��code��Ч��
		if (info == null) {
			if (log.isDebugEnabled()) {
				log.debug("code not exist! code=" + tokenReq.getCode());
			}
			// �׳��쳣
			throw new PortalException(PortalErrorCodes.CODE_EXPIRE, sm.getString("errorCode.60001"));
		} else if (info != null
				&& ((System.currentTimeMillis() - info.getCreateTime())
						/ 1000) > info.getExpire_in()) {
			if (log.isDebugEnabled()) {
				log.debug("code is expired! code=" + tokenReq.getCode());
			}
			// ����code
			tokenMgr.removeCode(tokenReq.getCode());
			// �׳��쳣
			throw new PortalException(PortalErrorCodes.CODE_EXPIRE, sm.getString("errorCode.60001"));
		}

		RegistUser user = null;
		if (info.getType() == RegistUser.USER_TYPE_ONEBTN || info.getType() == RegistUser.USER_TYPE_MESSAGE
				|| info.getType() == RegistUser.USER_TYPE_LZ_APP
				|| (info.getType() == RegistUser.USER_TYPE_ACCOUNT && null != info.getRegistUser())
				|| tokenMgr.getUserUrl(tokenReq.getUserMac()) != null) {
			// һ����¼��app��֤��������֤�͹̶��˺���֤�Լ��޸�֪��֤�ڻ�ȡcodeʱ������user����ֱ��ȡ��
			user = info.getRegistUser();
		} else {
			// QQ��΢����֤����������֤������Ϊ�̶��˺ţ�����δ�����û���
			TpLoginInfo tpLoginInfo = tokenMgr.getTpLoginInfo(tokenReq.getCode(), true);
			// �������޸��û�
			user = createOrUpdateTPUser(tpLoginInfo);
		}
		// ��ʱ���ԣ����������û��ͽ�����ϸʱʹ��
		user.setUserMac(tokenReq.getUserMac());
		user.setUserIp(tokenReq.getUserIp());
		UserAgent agent = userAgentMgr.parseUserAgent(tokenMgr.getUserAgent(user.getUserMac()));
		user.setDevManufacturer(agent.getTerminalVendor());
		user.setDevOsType(agent.getTerminalOs());

		// ʹ�ú�����code
		tokenMgr.removeCode(tokenReq.getCode());
		// ����UserAgent����
		tokenMgr.removeUserAgent(user.getUserMac());
		// ����TpLoginInfo����
		tokenMgr.removeTpLoginInfo(tokenReq.getCode());
		// ����token
		return tokenMgr.generateToken(user);
	}

	/**
	 * ��ȡ�û��ǳƣ�������ԵĻ�<br>
	 * ĳЩ��������֤�ܹ��õ��û��ǳ�<br>
	 * 
	 * @param regiestUser
	 */
	private String getSutiableUserName(RegistUser regiestUser) {
		Assert.notNull(regiestUser, "A RegistUser is required!");
		String userName = regiestUser.getUserName();
		boolean isThirdPartAuth = RegistUser.USER_TYPE_WX_WIFI == regiestUser.getUserType().intValue()
				|| RegistUser.USER_TYPE_WECHATPUBLICER == regiestUser.getUserType().intValue();
		if (isThirdPartAuth) {
			List<AuthUser> authUsers = getAuthUserMgr().queryByOpenIdAndUserType(regiestUser.getUserName(), null);
			if (null != authUsers && authUsers.size() > 0) {
				String nickName = authUsers.get(0).getNickname();
				if (StringUtils.isNotBlank(nickName)) {
					// ���ǳ�
					userName = nickName;
				} else {
					// û�й�ע���ںţ��޷���ȡ�ǳ�
					userName = regiestUser.getUserMac();
				}
			} else {
				// û�е�������֤�û���Ϣ
				userName = regiestUser.getUserMac();
			}
		} 
		return userName;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserInfo getUserInfo(String token, String remoteHostIp) {
		UserInfo result = new UserInfo();
		// У�鲢���token
		TokenInfo tokenInfo = validateToken(token);
		Long storeId = tokenInfo.getRegistUser().getStoreId();
		// ȡ���û���Ϣ
		RegistUser regiestUser = tokenInfo.getRegistUser();
		result.setUsername(getSutiableUserName(regiestUser));
		result.setAuth_type(regiestUser.getUserType());
		
		// ����ʣ������ʱ����0��ʾ���ޣ�
		boolean isThirdParty = regiestUser.getUserType().intValue() == RegistUser.USER_TYPE_ACCOUNT
				&& regiestUser.getUserTokenId() != null;
		String ssid = tokenMgr.getSsid(regiestUser.getUserMac());
		// ��������޵�������֤
		if (isThirdParty) {
			result.setSessionTimeOut(tokenMgr.getThirdPartyParam(regiestUser.getId()).getSessionTimeout());
			result.setLogin_url(tokenMgr.getThirdPartyParam(regiestUser.getId()).getSuccessUrl());
			// ��������
			tokenMgr.removeThirdPartyParam(regiestUser.getId());
		} else {
			String configueTime = cacheApiMgr.getConfigureParam(storeId, ssid, AuthCfg.STORE_CONFIGUE_TIME);
			if (configueTime != null && !configueTime.equals("0")) {
				result.setSessionTimeOut(Integer.valueOf(configueTime));
			} else {
				result.setSessionTimeOut(0);
			}

			// ��֤�ɹ�ҳ��·��
			result.setLogin_url(getLoginSuccessUri(storeId, ssid, regiestUser.getUserMac()));
		}
		// �����ж�ʱ�������ӣ��������ж��������ֽڣ�
		String cutTime = cacheApiMgr.getConfigureParam(storeId, ssid, AuthCfg.IDLE_CUT_TIME);
		if (log.isDebugEnabled()) {
			log.debug("Store param: IDLE_CUT_TIME ~ " + cutTime);
		}
		result.setIdleCutTime(Integer.valueOf(cutTime));
		String cutFlow = cacheApiMgr.getConfigureParam(storeId, ssid, AuthCfg.IDLE_CUT_FLOW);
		if (log.isDebugEnabled()) {
			log.debug("Store param: IDLE_CUT_FLOW ~ " + cutFlow);
		}
		result.setIdleCutFlow(Integer.valueOf(cutFlow));

		// ���������û�
		createOnlineUser(regiestUser, remoteHostIp, storeId, ssid);
		return result;
	}

	/**
	 * 
	 * У��token
	 *
	 * @param token
	 * @return
	 */
	private TokenInfo validateToken(String token) {
		TokenInfo tokenInfo = tokenMgr.getTokenInfo(token, true);
		if (tokenInfo == null) {
			if (log.isDebugEnabled()) {
				log.debug("Token is cleared: " + token);
			}
			throw new PortalException(PortalErrorCodes.TOKEN_EXPIRE, sm.getString("errorCode.60002"));
		}
		// �ж�token�Ƿ���Ч
		if (((System.currentTimeMillis() - tokenInfo.getTokenCreateTime()) / 1000) > tokenInfo.getAccessToken()
				.getExpire_in()) {
			if (log.isDebugEnabled()) {
				log.debug("Token is expired: " + token);
			}
			// ����token
			tokenMgr.removeToken(token);
			// �׳��쳣
			throw new PortalException(PortalErrorCodes.TOKEN_EXPIRE, sm.getString("errorCode.60002"));
		}
		return tokenInfo;
	}

	/**
	 * 
	 * ���������û�
	 *
	 * @param regiestUser
	 *            ע���û�
	 * @param remoteHostIp
	 *            ap�豸ip��ַ
	 * @param storeId
	 *            ����id
	 */
	private void createOnlineUser(RegistUser regiestUser, String remoteHostIp, Long storeId, String ssid) {
		Long ownerId = null;
		if (regiestUser.getUserType() == RegistUser.USER_TYPE_LZ_APP) {
			ownerId = protocolDao.queryUserId(storeId);
		} else {
			ownerId = cacheApiMgr.getOwnerId(storeId, ssid);
		}
		// ���û�е�¼,���ߵ�¼�û��ĵ�¼��ʽ�������Լ�SSID�в���ͬ�ģ���������߱�
		saveOrUpdateOnlineUser(regiestUser, System.currentTimeMillis(), remoteHostIp, ownerId);
	}

	/**
	 * ��ȡ��¼�ɹ�ҳ�档�޸�֪��¼ʱ�������û���֤ǰ���ʵ�ҳ�档���򣬷��ص�¼�ɹ� ģ��ҳ�档
	 * 
	 * @param storeId
	 *            ����ID
	 * @param ssid
	 *            ����ssid
	 * @param userMac
	 *            �ն�MAC��ַ
	 * @return
	 */
	private String getLoginSuccessUri(long storeId, String ssid, String userMac) {
		// �û��ض���ǰ���ʵ�ҳ��
		String resultUri = tokenMgr.getUserUrl(userMac);
		// ���û�л���userUri��˵�������޸�֪��¼���ص�¼�ɹ�ģ��ҳ��
		// �����portalҳ�棬���ص�¼�ɹ�ģ��ҳ��
		if (resultUri == null || resultUri.matches("^.*/portal/theme/\\d*/.*\\.jsp.*$")) {
			Long templateId = tokenMgr.getTemplateId(userMac);

			ThemeTemplate template = cacheApiMgr.getThemeTemplate(storeId, ssid, templateId);
			// ���ģ�巢���仯��ҳ�����Ҳ����һ��仯
			templateId = template.getId();
			StringBuilder builder;
			try {
				builder = new StringBuilder().append(httpOrHttps.getHttpOrHttps() + "://")
						.append(FuncUtil.getPortalDomain()).append(":").append(FuncUtil.getPortalServerPort())
						.append("/portal/")
						.append(cacheApiMgr.getThemePageURI(template, ThemePage.PAGE_TYPE_LOGINSUCCESS))
						.append("?templateId=").append(templateId).append("&userMac=").append(userMac)
						.append("&nas_id=").append(storeId).append("&ssid=")
						.append(URLEncoder.encode(URLEncoder.encode(ssid, "utf-8"), "utf-8"));
			} catch (UnsupportedEncodingException e) {
				throw new PortalException("Unsupported encode.");
			}
			resultUri = builder.toString();
			// ʹ�ú�����
			tokenMgr.removeTemplateId(userMac);
		} else {
			// �����û���֤ǰ���ʵ�ҳ��
		}
		tokenMgr.removeUserUrl(userMac);
		return resultUri;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserStatus uploadOnlines(OnlineUserInfo onlineUserInfo) {
		UserStatus status = new UserStatus();
		if (null == onlineUserInfo) {
			return status;
		}
		for (User user : onlineUserInfo.getUsers()) {
			// У���û��Ƿ���������߱�
			OnlineUser onlineUser = cacheApiMgr.getOnlineUserCacheMgr().get(user.getMac());

			if (onlineUser != null) {
				if (log.isDebugEnabled()) {
					log.debug("OnlineUser: " + onlineUser.toString());
				}
				// ��ѯ������֤�����е��������ʱ��
				String configueTime = cacheApiMgr.getConfigureParam(onlineUser.getStoreId(), onlineUser.getAccessSsid(),
						AuthCfg.STORE_CONFIGUE_TIME);
				if (log.isDebugEnabled()) {
					log.debug("configueTime: " + configueTime);
				}
				// ����ʱ������Ϊ�ջ�0��ʱ����ʾ�����ƽ���ʱ��
				if (configueTime != null && !configueTime.equals("0")) {
					// ʣ�����ʱ��
					long sessionTimeout = Long.valueOf(configueTime) - user.getSessionTime();
					if (log.isDebugEnabled()) {
						log.debug("The rest of access time: " + sessionTimeout);
					}
					if (sessionTimeout > 0) {
						// ���������б�
						updateOnlineUserInfo(user, status, sessionTimeout);
					} else {
						// ������볬ʱ�����������б�
						updateOfflineUserInfo(user, onlineUser, status);
					}
				} else {
					// ���������б�0��ʾ�����ƽ���ʱ��
					updateOnlineUserInfo(user, status, 0);
				}
			} else {
				if (log.isDebugEnabled()) {
					log.debug("User is already offline: " + user.toString());
				}
				// ������߱��в����ڸ��û������������б�
				updateOfflineUserInfo(user, null, status);
			}
		}

		return status;
	}

	@Override
	public void userOffline(String mac) {
		// У���û��Ƿ���������߱�
		OnlineUser onlineUser = cacheApiMgr.getOnlineUserCacheMgr().get(mac);

		if (onlineUser != null) {
			if (log.isDebugEnabled()) {
				log.debug("OnlineUser: " + onlineUser.toString());
			}
			// ɾ�������û�
			onlineUserMgr.deleteOnlineUser(onlineUser);
			// ���»���
			cacheApiMgr.getOnlineUserCacheMgr().remove(mac);
			// ����������ϸ
			accessUserDetailMgr.createAccessDetail(onlineUser);
		} else {
			log.warn("user already offline with mac:" + mac);
		}
		OfflineCallback callback = getOfflineCallbak();
		if (callback != null) {
			log.info("Third party offline callback.");
			callback.callbak(mac);
		} else {
			log.warn("No Third party offline callback found!");
		}
	}

	/**
	 * ��ȡ���������߻ص��ӿ�
	 *
	 * @return �ص��ӿ�
	 */
	private OfflineCallback getOfflineCallbak() {
		if (callback != null) {
			return callback;
		}
		AbstractApplicationContext ctx = ServerContext.getRootAppContext();
		Map<String, OfflineCallback> beans = BeanFactoryUtils.beansOfTypeIncludingAncestors(ctx, OfflineCallback.class);
		for (Entry<String, OfflineCallback> entry : beans.entrySet()) {
			OfflineCallback value = entry.getValue();
			if (value != null) {
				callback = value;
				break;
			}
		}
		return callback;
	}

	/**
	 * �޸������û���Ϣ
	 * 
	 * @param user
	 *            �û�������Ϣ
	 * @param status
	 *            ���ر�������
	 * @param sessionTimeout
	 *            ʣ�����ʱ��
	 */
	private void updateOnlineUserInfo(User user, UserStatus status, long sessionTimeout) {
		// ���������б�
		status.getOnlines().add(new Online(user.getIp(), sessionTimeout));
		// �޸������û���
		onlineUserMgr.updateOnlineInfo(user);
		// ���»�������
		OnlineUser cacheUser = cacheApiMgr.getOnlineUserCacheMgr().get(user.getMac());
		cacheUser.setAccessDuration(user.getSessionTime());
		cacheUser.setInputBytes(user.getInputbytes());
		cacheUser.setOutputBytes(user.getOutputbytes());
	}

	/**
	 * �޸������û���Ϣ
	 * 
	 * @param user
	 *            �û�������Ϣ
	 * @param status
	 *            ���ر�������
	 */
	@Transactional
	private void updateOfflineUserInfo(User user, OnlineUser onlineUser, UserStatus status) {
		// ���������б�
		status.getOfflines().add(new Offline(user.getIp()));
		if (onlineUser != null) {
			// ɾ�������û�
			onlineUserMgr.deleteOnlineUser(onlineUser);
			// ���»���
			cacheApiMgr.getOnlineUserCacheMgr().remove(user.getMac());
			// �޸Ľ�����ϸ��
			onlineUser.setAccessDuration(user.getSessionTime());
			onlineUser.setInputBytes(user.getInputbytes());
			onlineUser.setOutputBytes(user.getOutputbytes());
			accessUserDetailMgr.createAccessDetail(onlineUser);
		}
	}

	public void setProtocolDao(ProtocolDao protocolDao) {
		this.protocolDao = protocolDao;
	}

	public void setUserAgentMgr(UserAgentMgr userAgentMgr) {
		this.userAgentMgr = userAgentMgr;
	}

	public void setTokenMgr(TokenMgr tokenMgr) {
		this.tokenMgr = tokenMgr;
	}

	public void setUserMgr(RegistUserMgr userMgr) {
		this.userMgr = userMgr;
	}

	public void setOnlineUserMgr(OnlineUserMgr onlineUserMgr) {
		this.onlineUserMgr = onlineUserMgr;
	}

	public void setAccessUserDetailMgr(AccessUserDetailMgr accessUserDetailMgr) {
		this.accessUserDetailMgr = accessUserDetailMgr;
	}

	public void setCacheApiMgr(CacheApiMgr cacheApiMgr) {
		this.cacheApiMgr = cacheApiMgr;
	}

	public void setWeChatAuthUrl(String weChatAuthUrl) {
		this.weChatAuthUrl = weChatAuthUrl;
	}

	public HttpOrHttpsMgr getHttpOrHttps() {
		return httpOrHttps;
	}

	public void setHttpOrHttps(HttpOrHttpsMgr httpOrHttps) {
		this.httpOrHttps = httpOrHttps;
	}

	public void setHaihaiAppMgr(HaihaiAppMgr haihaiAppMgr) {
		this.haihaiAppMgr = haihaiAppMgr;
	}

	public LoginMgr getLoginMgr() {
		return loginMgr;
	}

	public void setLoginMgr(LoginMgr loginMgr) {
		this.loginMgr = loginMgr;
	}

	public Boolean getSkipQuickLogin() {
		return skipQuickLogin;
	}

	public void setSkipQuickLogin(Boolean skipQuickLogin) {
		this.skipQuickLogin = skipQuickLogin;
	}

	public Boolean getSkipLoginIfOnlyWxWifi() {
		return skipLoginIfOnlyWxWifi;
	}

	public void setSkipLoginIfOnlyWxWifi(Boolean skipLoginIfOnlyWxWifi) {
		this.skipLoginIfOnlyWxWifi = skipLoginIfOnlyWxWifi;
	}

	public AuthUserMgr getAuthUserMgr() {
		return authUserMgr;
	}

	public void setAuthUserMgr(AuthUserMgr authUserMgr) {
		this.authUserMgr = authUserMgr;
	}
	
	public String getSkipQuickLoginInAuth() {
		return skipQuickLoginInAuth;
	}

	public void setSkipQuickLoginInAuth(String skipQuickLoginInAuth) {
		this.skipQuickLoginInAuth = skipQuickLoginInAuth;
	}

	public String getSkipLoginIfOnlyWxWifiInAuth() {
		return skipLoginIfOnlyWxWifiInAuth;
	}

	public void setSkipLoginIfOnlyWxWifiInAuth(String skipLoginIfOnlyWxWifiInAuth) {
		this.skipLoginIfOnlyWxWifiInAuth = skipLoginIfOnlyWxWifiInAuth;
	}
	
}
