$(document).ready(function() {
	var params = getAllUriParamsJson();
	// 初始化图片
	// 模板已定制，这里不再初始化图片
	initImage(params['templateId'], params['nas_id'], params['ssid']);
	// 初始化登录面板
	initLoginForm();
	
	// 保存广告统计数据pv,uv 
	saveAdcountData(params, 2);
	// 统计广告点击数据
	adClickCount(params, 2);

	// 是否需要禁用功能
	$.get("/portal/func/control?storeId=" + params['nas_id'])
	.done(function(data) {
		if (typeof(data) != 'undefined') {
			// 如果打开QQ认证功能
			if (data.isEnableQQ) {
				$("#qqDiv").css("display", "inline-block");
			}
			// 如果是小小贝设备
			if (data.isXXBDev) {
				$("#weixinDiv").css("display", "none");
			}
			if (data.error) {
				alert(data.error);
			}
		} else {
			$("#qqDiv").css("display", "none");
			$("#wxwifiDiv").css("display", "none");
		}
	})
	.fail(function(data) {
		alert($("#60000").val() + ':' + data);
	});
});

/**
 * 获取url中的参数。
 *
 * @param paramName 参数名
 * @returns 参数值
 */
function getUrlParamValue(paramName) {
    var re = new RegExp("(^|\\?|&)"+ paramName + "=([^&]*)(&|$)",'g');
    re.exec(window.location.href);
    return RegExp.$2;
}

/**
 * qq登录链接处理
 */
function qqLogin(){
	$("#qq").attr({"disabled":"disabled"});
	var nas_id = getUrlParamValue("nas_id");
	// 跳转到腾讯服务器
	$.post("/portal/qqLogin", {nas_id:nas_id})
	.done(function(data) {
		if (typeof(data.error) != 'undefined') {
			alert($("#60004").val() + data.error);
			$("#qq").removeAttr("disabled");
		} else {
			location.href = data;
		}
	}).fail(function(data) {
		alert($("#60004").val());
		$("#qq").removeAttr("disabled");
	});
}

/**
浏览器版本信息
* @type {Object}
* @return {Boolean}  返回布尔值
*/
function browser() {
    var u = navigator.userAgent.toLowerCase();
    var app = navigator.appVersion.toLowerCase();
    return {
        txt: u, // 浏览器版本信息
        version: (u.match(/.+(?:rv|it|ra|ie)[\/: ]([\d.]+)/) || [])[1], // 版本号
        msie: /msie/.test(u) && !/opera/.test(u), // IE内核
        mozilla: /mozilla/.test(u) && !/(compatible|webkit)/.test(u), // 火狐浏览器
        safari: /safari/.test(u) && !/chrome/.test(u), //是否为safair
        chrome: /chrome/.test(u), //是否为chrome
        opera: /opera/.test(u), //是否为oprea
        presto: u.indexOf('presto/') > -1, //opera内核
        webKit: u.indexOf('applewebkit/') > -1, //苹果、谷歌内核
        gecko: u.indexOf('gecko/') > -1 && u.indexOf('khtml') == -1, //火狐内核
        mobile: !!u.match(/applewebkit.*mobile.*/), //是否为移动终端
        ios: !!u.match(/\(i[^;]+;( u;)? cpu.+mac os x/), //ios终端
        android: u.indexOf('android') > -1, //android终端
        iPhone: u.indexOf('iphone') > -1, //是否为iPhone
        iPad: u.indexOf('ipad') > -1, //是否iPad
        webApp: !!u.match(/applewebkit.*mobile.*/) && u.indexOf('safari/') == -1 //是否web应该程序，没有头部与底部
    };
}

/**
 * 微信登录链接处理
 */
function weixinLogin() {
	var b = browser();
	if(b.ios||b.iPhone||b.iPad){
		// iOS终端，可以主动打开app store
		window.location.href = "weixin://";
	}else if(b.android){
		// android提示安装微信
		window.location.href = "weixin://";
	} else {
		// 其他终端提示不支持
		alert($("#60006").val());
	}
}

/**
 * 获取URI中的参数
 * @returns 返回URI中‘？’以后的字符串
 */
function getAllUriParams() {
	var uri = window.document.location.href.toString();
	var u = uri.split("?");
	if(typeof(u[1]) == "string"){
		return u[1];
	} else {
		return "";
	}
}

/**
 * 获取URI中的参数
 * @returns 返回JSON格式的参数集合
 */
function getAllUriParamsJson() {
	var uri = window.document.location.href.toString();
	if (uri.substr(uri.length - 1, uri.length) == '#') {
		uri = uri.substr(0, uri.length - 1);
	}
	var u = uri.split("?");
	if(typeof(u[1]) == "string"){
		u = u[1].split("&");
		var get = {};
		for(var i in u){
			var j = u[i].split("=");
			get[j[0]] = j[1];
		}
		return get;
	} else {
		return {};
	}
}

String.prototype.format=function()
{
  if(arguments.length==0) return this;
  for(var s=this, i=0; i<arguments.length; i++)
    s=s.replace(new RegExp("\\{"+i+"\\}","g"), arguments[i]);
  return s;
};

var counter = 60;
var myTimer;

function startMessageTimer(formId, btnVal) {
	myTimer = setInterval(function() {
		counter--;
		$("#sendLoginMessageBtn" + formId).val(btnVal.format(counter));
		if (counter <= 0) {
			$("#sendLoginMessageBtn" + formId).removeAttr("disabled");
			$("#sendLoginMessageBtn" + formId).val($("#getcode").val());
			stopTimer();
		}
	}, 1000);
}
function stopTimer() {
	clearInterval(myTimer);
}
/**
 * 发送短信获取登录校验码
 */
function sendLoginMessage(formId) {
	$("#sendLoginMessageBtn" + formId).attr({"disabled":"disabled"});
	var userName = $("#username" + formId).val();
	var params = getAllUriParamsJson();
	if (userName == undefined || userName == "") {
		$("#sendLoginMessageBtn" + formId).removeAttr("disabled");
		alert($("#60007").val());
	} else {
		$.get('/portal/login?phoneNO=' + userName + "&operateType=2" + "&storeId=" + params.nas_id + "&ssid=" + params.ssid)
		.done(function(data) {
			if (typeof(data.error) != 'undefined') {
				$("#sendLoginMessageBtn" + formId).removeAttr("disabled");
				alert($("#60009").val() + data.error);
			} else {
				counter = data.resendTime;
				startMessageTimer(formId, data.btnVal);
			}
		})
		.fail(function() {
			$("#sendLoginMessageBtn" + formId).removeAttr("disabled");
			alert($("#60009").val());
		});
	}
}

/**
 * 登录
 */
function login(formId) {
	var userName = $("#username" + formId).val();
	var password = $("#password" + formId).val();
	if (userName == undefined || userName == "") {
		if (formId == 1 || formId == 3) {
			alert($("#60010").val());
		} else if (formId == 2 || formId == 4) {
			alert($("#60007").val());
		}

		return;
	}
	if (password == undefined || password == "") {
		if (formId == 1 || formId == 3) {
			alert($("#60011").val());
		} else if (formId == 2 || formId == 4) {
			alert($("#60008").val());
		}

		return;
	}

	var params = getAllUriParamsJson();
	params.userName = userName;
	params.signature = password;
	if (formId == 1 || formId == 3) {
		params.operateType = 7;
	} else if (formId == 2 || formId == 4) {
		params.operateType = 3;
	}

	$("#loginBtn" + formId).attr({"disabled":"disabled"});
	$.post("/portal/login", params)
	.done(function(data) {
		if (typeof(data.error) != 'undefined') {
			alert($("#60004").val() + data.error);
			$("#loginBtn" + formId).removeAttr("disabled");
		} else {
			location.href = data.redirect_uri;
		}
	})
	.fail(function(data) {
		alert($("#60004").val());
		$("#loginBtn" + formId).removeAttr("disabled");
	});
}

/**
 * 初始化页面中的图片
 * @param templateId 模板ID
 * @param nasId 店铺ID
 * @param ssid
 */
function initImage(templateId, nasId, ssid) {
	var uri = "/portal/login?pageType=2&templateId=" + templateId + "&operateType=5&nas_id=" + nasId + "&ssid=" + ssid;
	$.get(uri)
	.done(function(data) {
		if(data.error){
			alert(data.error);
		} else if (typeof(data.error) != 'undefined') {
			alert($("#60000").val());
		} else {
			var imageInfo = data.image;
			for (var key in imageInfo)
		    {
				var starIndex = imageInfo[key][0].indexOf("/");
				var endIndex = imageInfo[key][0].indexOf(".");
				// 截取图片名称用于唯一标识
				var imageName = imageInfo[key][0].substring(starIndex + 1, endIndex);
				imageId = "image" + imageName;
			
				$('#'+key+ ' .canvas_img_replace:first-child').attr("src", imageInfo[key][0]);
				if (imageInfo[key].length == 4 && imageInfo[key][3] != -1) {
					$('#'+key+ ' .canvas_img_replace:first-child').wrap("<a href='" + imageInfo[key][3] + "' />");
					// 修改父id,为广告统计
					$('#'+key+ ' .canvas_img_replace:first-child').parent().parent().attr("id", imageId);
				} else {
					// 修改父id
					$('#'+key+ ' .canvas_img_replace:first-child').parent().attr("id", imageId);
				}
		    }
		}
	})
	.fail(function(data) {
		alert($("#60000").val() + ':' + data);
	});

}

/**
 * 初始化登录面板
 */
function initLoginForm() {
	var params = getAllUriParamsJson();
	var uri = "/portal/login?operateType=6&nas_id=" + params['nas_id'] 
		+ "&ssid=" + params['ssid']
		+ "&usermac=" + params['usermac']
		+ "&userip=" + params['userip'];

	var phoneTgudingForm = $("#phoneTgudingForm");
	var gudingForm = $("#gudingForm");
	var phoneForm = $("#phoneForm");
	var qq = $("#qq");
	var weixin = $("#weixin");
	var wxwifi = $("#wxwifi");
	
	$.get(uri)
	.done(function(data) {
		if (data.phone2guding == 0) {
			phoneTgudingForm.hide();
			gudingForm.hide();
			phoneForm.hide();
		} else if (data.phone2guding == 1) {
			phoneTgudingForm.hide();
			gudingForm.hide();
			phoneForm.show();
			// 按钮事件
			$("#loginBtn4").click(function(){
				login(4);
			});
			$("#sendLoginMessageBtn4").click(function(){
				sendLoginMessage(4);
			});
		} else if (data.phone2guding == 2) {
			phoneTgudingForm.hide();
			gudingForm.show();
			phoneForm.hide();
			// 按钮事件
			$("#loginBtn3").click(function(){
				login(3);
			});
		} else if (data.phone2guding == 3) {
			phoneTgudingForm.show();
			gudingForm.hide();
			phoneForm.hide();
			// 按钮事件
			$("#loginBtn1").click(function(){
				login(1);
			});
			$("#loginBtn2").click(function(){
				login(2);
			});
			$("#sendLoginMessageBtn2").click(function(){
				sendLoginMessage(2);
			});
		}
		// 设置微信公众号事件及样式
		if (data.weixin == 1) {
			$("#weixinDiv").css("display","inline-block");
			weixin.click(function(){
				weixinLogin();
			});
		} else {
			$("#weixinDiv").css("display","none");
		}
		// 设置微信连wifi事件及样式
		if (data.weixinConnectWifi.isWeixinConnectWifi == 1) {
			$("#wxwifiDiv").css("display","inline-block");
			wxwifi.click(function(){
				callWechatBrowser(params);
			});
		} else {
			$("#wxwifiDiv").css("display","none");
		}
		//	qq
		if (data.qq == 1) {
			$("#qqDiv").css("display","inline-block");
			// 点击qq登录逻辑处理
			qq.click(function(){
				qqLogin();
			});
		} else {
			$("#qqDiv").css("display","none");
		}
		if(data.error){
			alert(data.error);
		}
	})
	.fail(function() {
		alert($("#60000").val());
	});
}

function callWechatBrowser(params) {
	var params = getAllUriParamsJson(); 
	loginUrl = "/portal/wifilogin.jsp?" 
			+ "nas_id=" + params['nas_id'] 
			+ "&redirect_uri=" + params['redirect_uri']
			+ "&ssid=" + params['ssid']
			+ "&templateId=" + params['templateId']
			+ "&userip=" + params['userip']
			+ "&usermac=" + params['usermac']
			+ "&userurl=" + params['userurl'];
	// window.location.replace(loginUrl);
	// 换一种方式，能够通过返回，返回到原来页面
	window.location.href = loginUrl;
}
