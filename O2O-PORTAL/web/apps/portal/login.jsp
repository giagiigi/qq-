<!--1438065696992-->
<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>
<html ng-app="templateApp" lang="en">
<head>
<link href="theme/resources/font-awesome/css/font-awesome.min.css"
	type="text/css" rel="stylesheet"></link>
<link
	href="theme/resources/jqueryui-bootstrap/jquery-ui-1.9.2.custom.css"
	type="text/css" rel="stylesheet"></link>
<link href="theme/resources/jqueryui-bootstrap/bootstrap.min.css"
	type="text/css" rel="stylesheet"></link>
<link href="theme/resources/css/jquery.bootstrap.custom.css"
	type="text/css" rel="stylesheet"></link>
<script type="text/javascript"
	src="theme/resources/jqueryui-bootstrap/jquery-1.9.0.min.js"></script>
<script type="text/javascript"
	src="theme/resources/jqueryui-bootstrap/jquery-ui-1.9.2.custom.min.js"></script>
<title>登录</title>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />

</head>
<body
	style="padding-top: 10px; background-repeat: no-repeat; background-size: 100% auto">
	<script>
		var denant = 60;
		var InterVal;
		function hourGlass() {
			var sends = document.getElementById("add");
			sends.setAttribute("disabled", "disabled");
			var left = denant + "s后重试";
			if (--denant > -1) {
				sends.value = left;
			} else {
				window.clearInterval(InterVal);//停止计时器
				denant = 60;
				//改变button的值
				sends.value = "发送验证码"
				//设置button可以提交
				sends.removeAttribute("disabled");
			}
		}
		function isSendSUCC(sm) {
			var phones = document.getElementById("phone");
			var phoneValue = phones.value;
			var phonepattenString = /^1[3|4|5|7|8][0-9]{9}$/;
			if (phoneValue != null && "" != phoneValue) {
				if (phoneValue.length == 11) {
					//alert("kk1");
					if (phonepattenString.test(phoneValue)) {
						var sendBtn = document.getElementById("add");
						sendBtn.removeAttribute("disabled");
						sendBtn.value = "正在发送...";
						sendBtn.style.color = "white";
						sendBtn.style.fontSize = "14px";
						sendBtn.setAttribute("disabled", "disabled");
						//短信是否发送成功
						if (sm) {
							no = "";
							InterVal = window.setInterval(hourGlass, 1000);
						}
					} else {
						alert('手机号格式错误');
					}
				} else {
					alert('手机号格式错误');
				}
			} else {
				alert('请输入手机号');
			}
		}
		function phoneDtp(val) {
			var phoneDiv = document.getElementById("phoneDiv");
			var gudingDiv = document.getElementById("gudingDiv");
			if (val == 1) {
				phoneDiv.style.display = "none";
				gudingDiv.style.display = "";
			} else {
				phoneDiv.style.display = "";
				gudingDiv.style.display = "none";
			}
		}
		function checkLogin() {
			var phones = document.getElementById("phone");
			var code = document.getElementById("password2").value;
			var phoneValue = phones.value;
			var phonepattenString = /^1[3|4|5|7|8][0-9]{9}$/;
			if(null == phoneValue||phoneValue==""){
				alert("请输入手机号");
			}else if(!phonepattenString.test(phoneValue)){
				alert("请输入正确的手机号");
			}else if(code != null && code.length == 4){
				window.location = "loginsuccess.jsp";
			}else{
				alert("请输入正确的验证码");
			}
		}
		function login(){
			var username = document.getElementById("username1").value;
			var password = document.getElementById("password1").value;
			alert("用户名或密码错误！");
		}
	</script>
	<div
		style="background-color: #87CECB; width: 100%; height: 50px; margin-top: 3%;z-index: 2;position: absolute;">
	</div>
	<div style="margin-left: 5%;position: absolute;z-index: 3"><img style="height: 130px; width: 150px" alt="绿洲" src="img/head.bmp"></div>
	<div style="float: left; margin-left: 12%; margin-top: 13%">
		<img style="height: 400px; width: 500px" alt="绿洲" src="img/leftlogo.bmp">
	</div>
	<div class="container"
		style="padding-top: 10px; position: absolute; top: 200px; right: 200px; width: 350px; height: 500px;">
		<div id="phoneTgudingForm"
			class="canvas-edited-text canvas-event-selectable canvas-event-draggable canvas-event-click canvas-event-dbclick ui-draggable ui-selectee ui-selected canvas-selected-border"
			data-canvasrole="login-role-panel" data-role="page"
			style="position: relative;">
			<div style="color: #87CECB;">
				<font size="6px">登录绿洲</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a
					href="forgetpsw.jsp">忘记密码?</a>&nbsp;|&nbsp;<a href="regist.jsp">用户注册</a>
			</div>
			<div style="height: 10px"></div>
			<div id="phoneDiv">
				<form
					class="form-horizontal templatemo-container templatemo-login-form-1"
					role="form" action="#" method="post">
					<div class="form-group">
						<div class="col-xs-12">
							<div class="control-wrapper">
								<label for="username" class="control-label fa-label"><i
									class="fa fa-user"></i></label> <input class="form-control"
									id="username1" maxlength="32" placeholder="用户名" type="text">
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="col-xs-12">
							<div class="control-wrapper">
								<label for="password" class="control-label fa-label"><i
									class="fa fa-lock"></i></label> <input class="form-control"
									id="password1" maxlength="32" placeholder="密码" type="password">
							</div>
						</div>
					</div>
					<div class="form-group">
						<a href="javascript:void(0)" onclick="login();"> <input id="loginBtn1" value="登录"
							class="canvas-edited-text btn btn-info"
							style="width: 100%; background-color: #87CECB" type="button">
						</a>
					</div>
				</form>
			</div>
			<div id="gudingDiv" style="display: none;">
				<form
					class="form-horizontal templatemo-container templatemo-login-form-1"
					role="form" action="#" method="post">
					<div class="form-group">
						<div class="col-xs-12">
							<div class="control-wrapper">
								<label for="username" class="control-label fa-label"><i
									class="fa fa-user"></i></label> <input class="form-control" id="phone"
									placeholder="手机号" type="text">
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="col-xs-8">
							<div class="control-wrapper">
								<label for="password" class="control-label fa-label"><i
									class="fa fa-lock"></i></label> <input class="form-control"
									id="password2" placeholder="验证码" type="text">
							</div>
						</div>
						<label style="font-size: 16px; color: red; margin-top: 2%"
							id="phoneTip1"></label>
						<div class="col-xs-4" style="padding-left: 0px;">
							<input id="add" value="发送验证码"
								style="width: 100%; background-color: #87CECB"
								class="canvas-edited-text btn btn-info"
								onclick="isSendSUCC(true)" type="button">
						</div>
					</div>
					<div class="form-group">
						<div class="col-xs-12">
							<input id="loginBtn2" value="登录" onclick="checkLogin();"
								class="canvas-edited-text btn btn-info"
								style="width: 100%; background-color: #87CECB" type="button">
						</div>
					</div>
					<div class="form-group">
						<div class="col-xs-12">
							<a id="phoneDtp" onclick="phoneDtp(2);" href="#">用户名登录 </a>
						</div>
					</div>
				</form>
			</div>
		</div>
		<div id="gudingForm" style="display: none;"
			class="canvas-edited-text canvas-event-selectable canvas-event-draggable canvas-event-click canvas-event-dbclick ui-draggable ui-selectee"
			data-canvasrole="login-role-panel" data-role="page">
			<form
				class="form-horizontal templatemo-container templatemo-login-form-1"
				role="form" action="#" method="post">
				<div class="form-group">
					<div class="col-xs-12">
						<div class="control-wrapper">
							<label for="username" class="control-label fa-label"><i
								class="fa fa-user"></i></label> <input class="form-control"
								id="username3" placeholder="用户名" type="text">
						</div>
					</div>
				</div>
				<div class="form-group">
					<div class="col-xs-12">
						<div class="control-wrapper">
							<label for="password" class="control-label fa-label"><i
								class="fa fa-lock"></i></label> <input class="form-control"
								id="password3" placeholder="密码" type="password">
						</div>
					</div>
				</div>
				<div>
					<div>
						<input id="loginBtn3" value="登录"
							style="width: 100%; background-color: #87CECB;" type="button">
					</div>
				</div>
			</form>
		</div>
		<div id="phoneForm" style="display: none;"
			class="canvas-edited-text canvas-event-selectable canvas-event-draggable canvas-event-click canvas-event-dbclick ui-draggable ui-selectee"
			data-canvasrole="login-role-panel" data-role="page">
			<form
				class="form-horizontal templatemo-container templatemo-login-form-1"
				role="form" action="#" method="post">
				<div class="form-group">
					<div class="col-xs-12">
						<div class="control-wrapper">
							<label for="username" class="control-label fa-label"><i
								class="fa fa-user"></i></label> <input class="form-control"
								id="username4" placeholder="用户名" type="text">
						</div>
					</div>
				</div>
				<div class="form-group">
					<div class="col-xs-8">
						<div class="control-wrapper">
							<label for="password" class="control-label fa-label"><i
								class="fa fa-lock"></i></label> <input class="form-control"
								id="password4" placeholder="验证码" type="text">
						</div>
					</div>
					<div class="col-xs-4" style="padding-left: 0px;">
						<input id="sendLoginMessageBtn4" value="Get Code"
							style="width: 100%;" class="canvas-edited-text btn btn-info"
							type="button">
					</div>
				</div>
				<div class="form-group">
					<div class="col-xs-12">
						<input id="loginBtn4" value="Login"
							class="canvas-edited-text btn btn-info" style="width: 100%;"
							type="button">
					</div>
				</div>
			</form>
		</div>
		<div>
			<div>
				<div>
					<div>
						<font size="2px" color="#A9A9A9">合作账户登录：</font> <a id="qq"
							href="/portal/qqLogin"> <img src="img/qq1.png"
							style="margin-top: 3px; cursor: pointer">
						</a>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>