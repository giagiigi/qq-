<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>登录成功</title>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<link href="css/default.css" rel="stylesheet" type="text/css" media="screen" />
</head>
<body>
<body>
	<!-- start header -->
<div id="header">
	<div id="logo">
		<img id="img" src="css/images/1.png">
	</div>
<!-- 	<div id="search">
		<form method="get" action="">
			<fieldset>
			<input id="s" type="text" name="s" value="" />
			<input id="x" type="submit" value="Search" />
			</fieldset>
		</form>
	</div> -->
</div>
<!-- end header -->
<!-- start menu -->
<div id="menu">
<!-- 	<ul>
		<li class="current_page_item"><a href="#">首页</a></li>
		<li><a href="#">博客</a></li>
		<li><a href="#">照片</a></li>
		<li><a href="#">要闻</a></li>
		<li><a href="#">关于我们</a></li>
	</ul> -->
</div>
<!-- end menu -->
<!-- start page -->
<div id="page">
	<!-- start content -->
	<div id="content">
		<h1 class="pagetitle">H3C绿洲平台</h1>
		<a href="#" id="rss-posts">H3C</a>
		<div class="post">
			<h2 class="title">欢迎来到  H3C绿洲平台</h2>
		</div>
	</div>
	<!-- end content -->
	<!-- start sidebar -->
	<div id="sidebar">
		<ul>
			<li>
				<h2>用户信息</h2>
				<ul style="width: auto;height: auto;">
					<div id="usertop">
					     <div>
					       <!--  <div id="left">
					            <img src="img/qq.png" id="qq"/>
					        </div> -->
					        <div id="right">
					            <ui>
					               <img src="img/qq.png" id="qq"/>
					              <li> 欢迎您,<a href="#">${requestScope.userinfobean.nickname}</a></li>
					              <li> 等级：<a href="#">${requestScope.userinfobean.level}</a></li>
					              <li> 性别：<a href="#">${requestScope.userinfobean.gender}</a></li>
					              <li> ret：<a href="#">${requestScope.userinfobean.ret}</a></li>
					              <li> message：<a href="#">${requestScope.userinfobean.msg}</a></li>
					              <li> vip：<a href="#">${requestScope.userinfobean.vip}</a></li>
					              <li> yellowYearVip：<a href="#">${requestScope.userinfobean.yellowYearVip}</a></li>
					              <li><h4>AvatarURL</h4></li>
								  <li> avatarURL30：<a href="#">${requestScope.userinfobean.avatar.avatarURL30}</a></li>
								  <li> avatarURL50：<a href="#">${requestScope.userinfobean.avatar.avatarURL50}</a></li>
								  <li> avatarURL100：<a href="#">${requestScope.userinfobean.avatar.avatarURL100s}</a></li>
					            </ui>
					        </div>
					     </div>
					</div>
				</ul>
			</li>
			<!-- <li>
				<h2>最新消息</h2>
				<ul>
					<li style="margin-top: 5px;"><a href="#">我们将要开启o2o项目</a> (13)</li>
					<li style="margin-top: 5px;"><a href="#">我们将要开启o2o项目</a> (31)</li>
					<li style="margin-top: 5px;"><a href="#">我们将要开启o2o项目</a> (31)</li>
					<li style="margin-top: 5px;"><a href="#">我们将要开启o2o项目</a> (30)</li>
					<li style="margin-top: 5px;"><a href="#">我们将要开启o2o项目</a> (31)</li>
					<li style="margin-top: 5px;"><a href="#">我们将要开启o2o项目</a> (30)</li>
					<li style="margin-top: 5px;"><a href="#">我们将要开启o2o项目</a> (31)</li>
					<li style="margin-top: 5px;"><a href="#">我们将要开启o2o项目</a> (28)</li>
					<li style="margin-top: 5px;"><a href="#">我们将要开启o2o项目</a> (31)</li>
				</ul>
			</li> -->
		</ul>
	</div>
	<!-- end sidebar -->
	<div style="clear: both;">&nbsp;</div>
</div>
<!-- end page -->
<div id="footer">
	<p>&copy;2015 All Rights Reserved. &nbsp;&bull;&nbsp; Design by <a href="http://www.freecsstemplates.org/">H3C</a> &nbsp;&bull;&nbsp; Icons by <a href="http://www.famfamfam.com/">H3C</a>.</p>
</div>
</body>
</html>
