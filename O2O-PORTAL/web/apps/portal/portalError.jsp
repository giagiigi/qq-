<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport"
	content="width=device-width,height=device-height,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
<fmt:setBundle basename="com.h3c.o2o.portal.LocalStrings_zh_CN"
	var="commonBundle" />
<title>抱歉，出错了</title>

<script src="./theme/resources/jquery-2.1.1.min.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		var params = getAllUriParamsJson();
		var error = params.errorCode;
		var content = $("#" + error + "").val();
		$("#errorcode").html(error);
		$("#content").html(content);
	});

	/**
	 *
	 */
	function getAllUriParamsJson() {
		var uri = window.document.location.href.toString();
		var u = uri.split("?");
		if (typeof (u[1]) == "string") {
			u = u[1].split("&");
			var get = {};
			for ( var i in u) {
				var j = u[i].split("=");
				get[j[0]] = j[1];
			}
			return get;
		} else {
			return {};
		}
	}
</script>
<STYLE>
BODY {
	BACKGROUND: #dad9d7;
	FONT-FAMILY: "微软雅黑"
}

.header_img {
	MARGIN-TOP: 0px
}

.bg {
	BACKGROUND: url(img/errorBackground.jpg) #dad9d7 no-repeat center top;
	LEFT: 0px;
	OVERFLOW: hidden;
	WIDTH: 100%;
	POSITION: absolute;
	TOP: 10%;
	height: 100%
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

.content_words {
	FONT-SIZE: 14px;
	COLOR: #999;
	TEXT-ALIGN: left;
	word-wrap: break-word
}
</STYLE>
</head>
<body>
	<DIV class="bg">
		<DIV class="header">
			<IMG class=header_img src="img/errorOccurer.png">
			<h4>
				错误码：<label id="errorcode"></label>
			</h4>

			<DIV class="content_words">
				<label id="content"></label>
			</DIV>
		</DIV>
	</DIV>

	<input id="60000" type="hidden"
		value=<fmt:message key="errorCode.60000" bundle="${commonBundle}" />></input>
	<input id="60001" type="hidden"
		value=<fmt:message key="errorCode.60001" bundle="${commonBundle}" />></input>
	<input id="60002" type="hidden"
		value=<fmt:message key="errorCode.60002" bundle="${commonBundle}" />></input>
	<input id="60012" type="hidden"
		value=<fmt:message key="errorCode.60012" bundle="${commonBundle}" />></input>
	<input id="60013" type="hidden"
		value=<fmt:message key="errorCode.60013" bundle="${commonBundle}" />></input>
	<input id="60014" type="hidden"
		value=<fmt:message key="errorCode.60014" bundle="${commonBundle}" />></input>
	<input id="60015" type="hidden"
		value=<fmt:message key="errorCode.60015" bundle="${commonBundle}" />></input>
	<input id="60016" type="hidden"
		value=<fmt:message key="errorCode.60016" bundle="${commonBundle}" />></input>
	<input id="60017" type="hidden"
		value=<fmt:message key="errorCode.60017" bundle="${commonBundle}" />></input>
	<input id="60018" type="hidden"
		value=<fmt:message key="errorCode.60018" bundle="${commonBundle}" />></input>
	<input id="60019" type="hidden"
		value=<fmt:message key="errorCode.60019" bundle="${commonBundle}" />></input>
	<input id="60020" type="hidden"
		value=<fmt:message key="errorCode.60020" bundle="${commonBundle}" />></input>
	<input id="60021" type="hidden"
		value=<fmt:message key="errorCode.60021" bundle="${commonBundle}" />></input>
	<input id="60024" type="hidden"
		value=<fmt:message key="errorCode.60024" bundle="${commonBundle}" />></input>
	<input id="71006" type="hidden"
		value=<fmt:message key="errorCode.71006" bundle="${commonBundle}" />></input>
</body>
</html>