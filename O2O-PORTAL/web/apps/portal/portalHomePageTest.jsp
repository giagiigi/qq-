<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>商家主页测试页面</title>
</head>
<body>
	<h2>
	这里是商家[nas_id=
	<% out.print(request.getParameter("nas_id")); %>
	]的主页。
	</h2>
</body>
</html>