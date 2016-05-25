<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<fmt:setBundle basename="com.h3c.o2o.portal.LocalStrings_zh_CN" var="commonBundle"/>
<title>Error</title>
<script src="./theme/resources/jquery-2.1.1.min.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	var params = getAllUriParamsJson();
	var error = params.errorCode;
	var content = $("#" + error + "").val();
	$("#content").html(content);
});

/**
 * 获取URI中的参数
 * @returns 返回JSON格式的参数集合
 */
function getAllUriParamsJson() {
	var uri = window.document.location.href.toString();
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
</script>
</head>
<body>
<span id="content"></span>
<input id="60001" type="hidden" value=<fmt:message key="errorCode.60001" bundle="${commonBundle}" />></input>
<input id="60002" type="hidden" value=<fmt:message key="errorCode.60002" bundle="${commonBundle}" />></input>
<input id="60012" type="hidden" value=<fmt:message key="errorCode.60012" bundle="${commonBundle}" />></input>
<input id="60013" type="hidden" value=<fmt:message key="errorCode.60013" bundle="${commonBundle}" />></input>
<input id="60014" type="hidden" value=<fmt:message key="errorCode.60014" bundle="${commonBundle}" />></input>
<input id="60015" type="hidden" value=<fmt:message key="errorCode.60015" bundle="${commonBundle}" />></input>
<input id="60016" type="hidden" value=<fmt:message key="errorCode.60016" bundle="${commonBundle}" />></input>
<input id="60017" type="hidden" value=<fmt:message key="errorCode.60017" bundle="${commonBundle}" />></input>
<input id="60018" type="hidden" value=<fmt:message key="errorCode.60018" bundle="${commonBundle}" />></input>
<input id="60019" type="hidden" value=<fmt:message key="errorCode.60019" bundle="${commonBundle}" />></input>
</body>
</html>