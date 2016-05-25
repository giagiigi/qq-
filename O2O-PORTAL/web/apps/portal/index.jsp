<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
<title>绿洲平台</title>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
<meta property="qc:admins" content="1540737617075603363757072414" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<style>
a{text-decoration:none}
.alsp{ font-family:"楷体";}
</style>
<script type="text/javascript">
	function updatename(){
		var name = prompt("请输入名字");
		var oldname = document.getElementById("name");
		if("" != name){
			oldname.value = name;
			alert("修改成功");
			myform.submit();
		}else{
			alert("请输入名字")
		}
	}
	function change(){
		var text = document.getElementById("text");
		text.color='red';
	}
	function change1(){
		var text = document.getElementById("text");
		text.color='white';
	}
	function loginout(){
		if(confirm('确认退出当前账号吗?')){
			window.location='loginOut';
		}
	}
</script>
</head>
<body>
<div
	style="background-color: #87CECB; width: 100%; height: 50px;margin-top: 3%;position: absolute;z-index: 2">
	<c:choose>
	<c:when test="${null == name || ''==name}">
	<div style="margin-left: 75% ; padding-top: 15px">
		<img alt="" src="img/touxiang.png">&nbsp;<a href="login.jsp"><font size="2px" color="white">登录</font></a>&nbsp;<a href="regist.jsp"><font size="2px" color="white">注册</font></a>
		&nbsp;
		<a href="/portal/qqLogin"><img alt="" src="img/qqlogin.png">
		<font size="2px" color="white">QQ登录</font></a>
	</div>
	</c:when>
	<c:otherwise>
	<div style="margin-left: 75% ; padding-top: 15px">
	<font size="2px" color="white">亲爱的qq用户,<img alt="" src="img/qqlogin.png">
	<a>
	[${name}]
	</a>
	</font>
	<span id="sp" class="sp1" onmouseout="change1();" onmouseover="change();"><a onclick="loginout();" href="javascript:void(0);"><font id="text" color="white" size="2px">退出</font></a></span>
	</div>
	</c:otherwise>
	</c:choose>
</div>
<div style="margin-left: 5%;position: absolute;z-index: 3"><img style="height: 130px; width: 150px" alt="绿洲" src="img/head.bmp"></div>
<form action="updateUsername" id="myform">
	<input type="hidden" name="username" id="name">
</form>
<br>
<br>
<br>
<div style="margin-top: 7%;width: 100%;">
	<img alt="" src="img/main.bmp">
	<div align="left" style="color: #87CECB;float: right;height: 500px;width: 550px;padding-top: 10%">
	<font size="5px">
	<c:choose>
		<c:when test="${null == name || ''==name}">
		请您先登录！
		</c:when>
		<c:otherwise>
		恭喜！您已成功登录绿洲平台
		</c:otherwise>
	</c:choose>
	</font>
	<br>
	<br>
	<br>
	<br>
	<div style="margin-right: 10%;height: 250px" class="alsp">
		<font size="5px">
		<br>
		<img src="img/point.bmp">云端大互联时代<br><br>
		<img src="img/point.bmp">多业务融合<br><br>
		<img src="img/point.bmp">让O2O变得更简单
		<br>
		</font>
	</div>
	</div>
</div>
<!-- <div style="border: 1px solid #87CECB;height: 500px;width: 50%;"></div> -->
<hr style="color: #87CECB;margin-top: 12%;">
<div align="center">
	<p><font color="#87CECB">&copy;2015 All Rights Reserved. &nbsp;&bull;&nbsp; Design by <a href="http://www.freecsstemplates.org/">H3C</a> &nbsp;&bull;&nbsp; Icons by <a href="http://www.famfamfam.com/">H3C</a>.</font></p>
</div>
</body>
</html>
