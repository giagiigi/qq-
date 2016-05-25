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
		function regist(){
			var username = document.getElementById("username1").value;
			if("" == username){
				alert("请输入您的姓名");
			}else{
				myform.submit();
			}
		}
		function bind(){
			var username = document.getElementById("username").value;
			var password = document.getElementById("password").value;
			if(username!=null&&password!=null&&username.length>=6&&username.length<=32&&password.length>=6&&password.length<=32){
				window.location="bindsuccess.jsp";
			}else if(username == ""){
				alert("请输入用户名");
			}else if(password == ""){
				alert("请输入密码");
			}else{
				alert("用户名或密码错误！");
			}
		}
	</script>
	<div
		style="background-color: #87CECB; width: 100%; height: 50px; margin-top: 4%; position: absolute; z-index: 2">
	</div>
	<div style="margin-left: 5%; position: absolute; z-index: 3">
		<img style="height: 130px; width: 150px" alt="绿洲" src="img/head.bmp">
	</div>
	<div style="margin-left: 9%; margin-top: 15%;">
		<font size="5px" color="#87CECB">恭喜您,[${name}]！差一步您就能成功登录绿洲！！</font>
	</div>
	<div align="center" style="float: right; margin-right: 5%;">
		<img src="img/xiaobei.bmp">
	</div>
	<div class="container"
		style="margin-top: 3.5%; margin-left: 10%; width: 350px; height: 400px; border: 1px solid #A9A9A9;">
		<div id="phoneTgudingForm"
			class="canvas-edited-text canvas-event-selectable canvas-event-draggable canvas-event-click canvas-event-dbclick ui-draggable ui-selectee ui-selected canvas-selected-border"
			data-canvasrole="login-role-panel" data-role="page"
			style="position: relative;">
			<div style="color: #87CECB;">
				<font size="6px">完善信息</font>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<input id="loginBtn2" value="跳过本步   >>"
					onclick="window.location='loginsuccess.jsp'"
					class="canvas-edited-text btn btn-info"
					style="width: 40%; background-color: #87CECB" type="button">
			</div>
			<div style="height: 10px"></div>
			<div id="phoneDiv">
				<form name="myform"
					class="form-horizontal templatemo-container templatemo-login-form-1"
					role="form" action="updateUsername" method="post">
					<br />
					<div class="col-xs-12">
						<div class="control-wrapper">
							<label for="username" class="control-label fa-label"><i
								class="fa fa-user"></i></label> <input class="form-control"
								id="username1" name="username" maxlength="32" placeholder="您的姓名" type="text">
						</div>
					</div>
					<br /> <br /> <br />
					<div class="col-xs-12">
						<div class="control-wrapper">
							<label for="password" class="control-label fa-label"><i
								class="fa fa-user"></i></label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<input checked="checked" name="sex" type="radio">&nbsp;<font
								color="#87CECB">男</font>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<input name="sex" type="radio">&nbsp;<font
								color="#87CECB">女</font>
						</div>
					</div>
					<br /> <br /> <br />
					<div class="form-group">
						<div class="col-xs-12">
							&nbsp;&nbsp;&nbsp; <input id="loginBtn2" value="确认"
								onclick="regist();"
								class="canvas-edited-text btn btn-info"
								style="width: 100%; background-color: #87CECB" type="button">
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</body>
</html>
