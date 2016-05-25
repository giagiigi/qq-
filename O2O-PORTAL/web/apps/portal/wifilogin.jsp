<%@page import="org.apache.commons.logging.LogFactory"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	import="com.h3c.o2o.portal.login.entity.LoginReq,
	com.h3c.o2o.portal.protocol.func.ProtocolMgrImpl,
	org.apache.commons.lang.StringUtils,
	com.h3c.o2o.portal.entity.AuthCfg,
	org.apache.commons.logging.Log,
	org.apache.commons.logging.LogFactory,
	com.h3c.o2o.portal.login.entity.WeixinConnectWifiPara"
	pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";

	Log log = LogFactory.getLog(getClass());

	// 请求参数
	LoginReq req = null;
	// 拉起微信的参数
	WeixinConnectWifiPara wifiParas = null;
	String error = "";
	try {
		// 在请求中获取微信连Wi-Fi的参数
		req = new LoginReq();
		String nas_id = request.getParameter("nas_id");
		String redirect_uri = request.getParameter("redirect_uri");
		String ssid = request.getParameter("ssid");
		String templateId = request.getParameter("templateId");
		String userip = request.getParameter("userip");
		String usermac = request.getParameter("usermac");
		String userurl = request.getParameter("userurl");
		String bssid = "";
		req.setBssid(bssid);
		try {
			req.setNasId(Long.valueOf(nas_id));
		} catch (NumberFormatException e) {
			throw new Exception("错误的nasid: " + nas_id);
		}
		req.setRedirect_uri(redirect_uri);
		req.setSsid(ssid);
		try {
			req.setTemplateId(Long.valueOf(templateId));
		} catch (NumberFormatException e) {
			throw new Exception("错误的templateId: " + templateId);
		}
		req.setUserip(userip);
		req.setUsermac(usermac);
		req.setUserurl(userurl);

		// 获取微信连wifi参数
		wifiParas = ProtocolMgrImpl.get().getWxWifiPara(req);
		wifiParas.setBssid("");
		if (null == wifiParas && StringUtils.isNotBlank(wifiParas.getError())) {
			throw new Exception("出错了:" + (null != wifiParas ? wifiParas.getError() : "未知错误，请刷新页面。"));
		} else if (AuthCfg.IS_ENABLE_NO == wifiParas.getIsWeixinConnectWifi()) {
			throw new Exception("微信连Wi-Fi未启用，请联系管理员!");
		}
	} catch (Exception e) {
		log.warn("Error in wifilogin:", e);
		error = ("null".equals(e.getMessage()) || StringUtils.isBlank(e.getMessage()))
				? "未知错误，请刷新页面。"
				: e.getMessage();
	}
%>
<!DOCTYPE html>
<html>
<head lang="zh-CN">
<title>微信连Wi-Fi</title>
<base href="<%=basePath%>">
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="viewport"
	content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<meta name="viewport"
	content="width=device-width,height=device-height,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="format-detection" content="telephone=no">
<style type="text/css">
BODY {
	BACKGROUND: #FFFFFF;
	FONT-FAMILY: "微软雅黑"
}

.header_img {
	MARGIN-TOP: 20px;
	height: 150px;
}

.header {
	MARGIN: 0px auto;
	WIDTH: 50%;
	LINE-HEIGHT: 20px;
	FONT-WEIGHT: normal;
	FONT-SIZE: 18px;
	COLOR: #555;
	TEXT-ALIGN: center
}

.bg {
	BACKGROUND: url(img/errorBackground.jpg) #dad9d7 no-repeat center top;
	LEFT: 0px;
	OVERFLOW: hidden;
	WIDTH: 100%;
	POSITION: absolute;
	TOP: 0px;
	height: 100%
}

.content_words {
	FONT-SIZE: 14px;
	COLOR: #999;
	TEXT-ALIGN: center;
	word-wrap: break-word;
	margin: 20px;
}
</style>
<script type="text/javascript">
		/**
		 * 微信连Wi-Fi协议3.1供运营商portal呼起微信浏览器使用
		 */
		var loadIframe = null;
		var noResponse = null;
		var callUpTimestamp = 0;
		 
		function putNoResponse(ev){
			 clearTimeout(noResponse);
		}	
		
		 function errorJump()
		 {
			 var now = new Date().getTime();
			 if((now - callUpTimestamp) > 4*1000){
				 return;
			 }
			 alert('该浏览器不支持自动跳转微信请手动打开微信\n如果已跳转请忽略此提示');
		 }
		 
		 myHandler = function(error) {
			 errorJump();
		 };
		 
		 function createIframe(){
			 var iframe = document.createElement("iframe");
		     iframe.style.cssText = "display:none;width:0px;height:0px;";
		     document.body.appendChild(iframe);
		     loadIframe = iframe;
		 }
		//注册回调函数
		function jsonpCallback(result){  
			if(result && result.success){
				// 此Alert用于调试
			    // alert('WeChat will call up : ' + result.success + '  data:' + result.data);			    
			    var ua=navigator.userAgent;              
				if (ua.indexOf("iPhone") != -1 ||ua.indexOf("iPod")!=-1||ua.indexOf("iPad") != -1) {   //iPhone             
					document.location = result.data;
				}else{
					
					if('false'=='true'){
						alert('[强制]该浏览器不支持自动跳转微信请手动打开微信\n如果已跳转请忽略此提示');
						return;
					}
					
				    createIframe();
				    callUpTimestamp = new Date().getTime();
				    loadIframe.src=result.data;
					noResponse = setTimeout(function(){
						errorJump();
			      	},3000);
				}			    
			}else if(result && !result.success){
				if("Expired timestamp" == result.data){
					// 翻译时间戳过期提示
					alert("页面已过期，请刷新页面后重试。");
				} else {
					alert(result.data);
				}
			}
		}
		
		function Wechat_GotoRedirect(appId, extend, timestamp, sign, shopId, authUrl, mac, ssid, bssid){
			
			//将回调函数名称带到服务器端
			var url = "http://wifi.weixin.qq.com/operator/callWechatBrowser.xhtml?appId=" + appId 
																				+ "&extend=" + extend 
																				+ "&timestamp=" + timestamp 
																				+ "&sign=" + sign;	
			
			//如果sign后面的参数有值，则是新3.1发起的流程
			if(authUrl && shopId){
				
				
				url = "http://wifi.weixin.qq.com/operator/callWechat.xhtml?appId=" + appId 
																				+ "&extend=" + extend 
																				+ "&timestamp=" + timestamp 
																				+ "&sign=" + sign
																				+ "&shopId=" + shopId
																				+ "&authUrl=" + encodeURIComponent(authUrl)
																				+ "&mac=" + mac
																				+ "&ssid=" + ssid
																				+ "&bssid=" + bssid;
				
			}			
			
			//通过dom操作创建script节点实现异步请求  
			var script = document.createElement('script');  
			script.setAttribute('src', url);  
			document.getElementsByTagName('head')[0].appendChild(script);
		}
	</script>
</head>
<body>
	<script type="text/javascript">
		<%if (StringUtils.isBlank(error)) {%>
			Wechat_GotoRedirect('<%=wifiParas.getAppId()%>', '<%=wifiParas.getExtend()%>', '<%=wifiParas.getTimestamp()%>', 
					'<%=wifiParas.getSign()%>', '<%=wifiParas.getShopId()%>', '<%=wifiParas.getAuthUrl()%>',
					'<%=wifiParas.getUsermac()%>', '<%=req.getSsid()%>', '<%=wifiParas.getBssid()%>');
		<%} else {%>
			alert('<%=error%>
		');
	<%}%>
		document.addEventListener('visibilitychange', putNoResponse, false);
	</script>
	<DIV>
		<DIV class="header">
			<IMG class=header_img src="img/logo.png">
			<h5>欢迎使用微信连Wi-Fi！</h5>
		</DIV>
		<DIV class="content_words">
			<label>如果微信客户端没有自动弹出，请尝试刷新当前页面，或者升级微信客户端，手动打开微信扫描门店二维码。</label>
		</DIV>
	</DIV>
</body>
</html>