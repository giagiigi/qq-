/*
 * Generated by the Jasper component of Apache Tomcat
 * Version: Apache Tomcat/7.0.39
 * Generated at: 2016-03-03 08:04:38 UTC
 * Note: The last modified time of this file was set to
 *       the last modified time of the source file after
 *       generation to assist with modification tracking.
 */
package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class regist_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final javax.servlet.jsp.JspFactory _jspxFactory =
          javax.servlet.jsp.JspFactory.getDefaultFactory();

  private static java.util.Map<java.lang.String,java.lang.Long> _jspx_dependants;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.tomcat.InstanceManager _jsp_instancemanager;

  public java.util.Map<java.lang.String,java.lang.Long> getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_instancemanager = org.apache.jasper.runtime.InstanceManagerFactory.getInstanceManager(getServletConfig());
  }

  public void _jspDestroy() {
  }

  public void _jspService(final javax.servlet.http.HttpServletRequest request, final javax.servlet.http.HttpServletResponse response)
        throws java.io.IOException, javax.servlet.ServletException {

    final javax.servlet.jsp.PageContext pageContext;
    javax.servlet.http.HttpSession session = null;
    final javax.servlet.ServletContext application;
    final javax.servlet.ServletConfig config;
    javax.servlet.jsp.JspWriter out = null;
    final java.lang.Object page = this;
    javax.servlet.jsp.JspWriter _jspx_out = null;
    javax.servlet.jsp.PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html;charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("<!--1438065696992-->\r\n");
      out.write("\r\n");
      out.write("<!DOCTYPE html>\r\n");
      out.write("<html ng-app=\"templateApp\" lang=\"en\">\r\n");
      out.write("<head>\r\n");
      out.write("<link href=\"theme/resources/font-awesome/css/font-awesome.min.css\"\r\n");
      out.write("\ttype=\"text/css\" rel=\"stylesheet\"></link>\r\n");
      out.write("<link\r\n");
      out.write("\thref=\"theme/resources/jqueryui-bootstrap/jquery-ui-1.9.2.custom.css\"\r\n");
      out.write("\ttype=\"text/css\" rel=\"stylesheet\"></link>\r\n");
      out.write("<link href=\"theme/resources/jqueryui-bootstrap/bootstrap.min.css\"\r\n");
      out.write("\ttype=\"text/css\" rel=\"stylesheet\"></link>\r\n");
      out.write("<link href=\"theme/resources/css/jquery.bootstrap.custom.css\"\r\n");
      out.write("\ttype=\"text/css\" rel=\"stylesheet\"></link>\r\n");
      out.write("<script type=\"text/javascript\"\r\n");
      out.write("\tsrc=\"theme/resources/jqueryui-bootstrap/jquery-1.9.0.min.js\"></script>\r\n");
      out.write("<script type=\"text/javascript\"\r\n");
      out.write("\tsrc=\"theme/resources/jqueryui-bootstrap/jquery-ui-1.9.2.custom.min.js\"></script>\r\n");
      out.write("<title>登录</title>\r\n");
      out.write("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\">\r\n");
      out.write("<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\" />\r\n");
      out.write("</head>\r\n");
      out.write("<body\r\n");
      out.write("\tstyle=\"padding-top: 10px; background-repeat: no-repeat; background-size: 100% auto\">\r\n");
      out.write("\t<script>\r\n");
      out.write("\t\tfunction phoneDtp(val) {\r\n");
      out.write("\t\t\tvar phoneDiv = document.getElementById(\"phoneDiv\");\r\n");
      out.write("\t\t\tvar gudingDiv = document.getElementById(\"gudingDiv\");\r\n");
      out.write("\t\t\tif (val == 1) {\r\n");
      out.write("\t\t\t\tphoneDiv.style.display = \"none\";\r\n");
      out.write("\t\t\t\tgudingDiv.style.display = \"\";\r\n");
      out.write("\t\t\t} else {\r\n");
      out.write("\t\t\t\tphoneDiv.style.display = \"\";\r\n");
      out.write("\t\t\t\tgudingDiv.style.display = \"none\";\r\n");
      out.write("\t\t\t}\r\n");
      out.write("\t\t}\r\n");
      out.write("\t\tvar denant = 60;\r\n");
      out.write("\t\tvar InterVal;\r\n");
      out.write("\t\tfunction hourGlass() {\r\n");
      out.write("\t\t\tvar sends = document.getElementById(\"add\");\r\n");
      out.write("\t\t\tsends.setAttribute(\"disabled\", \"disabled\");\r\n");
      out.write("\t\t\tvar left = denant + \"s后重试\";\r\n");
      out.write("\t\t\tif (--denant > -1) {\r\n");
      out.write("\t\t\t\tsends.value = left;\r\n");
      out.write("\t\t\t} else {\r\n");
      out.write("\t\t\t\twindow.clearInterval(InterVal);//停止计时器\r\n");
      out.write("\t\t\t\tdenant = 60;\r\n");
      out.write("\t\t\t\t//改变button的值\r\n");
      out.write("\t\t\t\tsends.value = \"发送验证码\"\r\n");
      out.write("\t\t\t\t//设置button可以提交\r\n");
      out.write("\t\t\t\tsends.removeAttribute(\"disabled\");\r\n");
      out.write("\t\t\t}\r\n");
      out.write("\t\t}\r\n");
      out.write("\t\tfunction isSendSUCC(sm) {\r\n");
      out.write("\t\t\tvar phones = document.getElementById(\"phone\");\r\n");
      out.write("\t\t\tvar phoneValue = phones.value;\r\n");
      out.write("\t\t\tvar phonepattenString = /^1[3|4|5|7|8][0-9]{9}$/;\r\n");
      out.write("\t\t\tif (phoneValue != null && \"\" != phoneValue) {\r\n");
      out.write("\t\t\t\tif (phoneValue.length == 11) {\r\n");
      out.write("\t\t\t\t\t//alert(\"kk1\");\r\n");
      out.write("\t\t\t\t\tif (phonepattenString.test(phoneValue)) {\r\n");
      out.write("\t\t\t\t\t\tvar sendBtn = document.getElementById(\"add\");\r\n");
      out.write("\t\t\t\t\t\tsendBtn.removeAttribute(\"disabled\");\r\n");
      out.write("\t\t\t\t\t\tsendBtn.value = \"正在发送...\";\r\n");
      out.write("\t\t\t\t\t\tsendBtn.style.color = \"white\";\r\n");
      out.write("\t\t\t\t\t\tsendBtn.style.fontSize = \"14px\";\r\n");
      out.write("\t\t\t\t\t\tsendBtn.setAttribute(\"disabled\", \"disabled\");\r\n");
      out.write("\t\t\t\t\t\t//短信是否发送成功\r\n");
      out.write("\t\t\t\t\t\tif (sm) {\r\n");
      out.write("\t\t\t\t\t\t\tno = \"\";\r\n");
      out.write("\t\t\t\t\t\t\tInterVal = window.setInterval(hourGlass, 1000);\r\n");
      out.write("\t\t\t\t\t\t}\r\n");
      out.write("\t\t\t\t\t} else {\r\n");
      out.write("\t\t\t\t\t\talert('手机号格式错误');\r\n");
      out.write("\t\t\t\t\t}\r\n");
      out.write("\t\t\t\t} else {\r\n");
      out.write("\t\t\t\t\talert('手机号格式错误');\r\n");
      out.write("\t\t\t\t}\r\n");
      out.write("\t\t\t} else {\r\n");
      out.write("\t\t\t\talert('请输入手机号');\r\n");
      out.write("\t\t\t}\r\n");
      out.write("\t\t}\r\n");
      out.write("\t\tfunction updatePsw(){\r\n");
      out.write("\t\t\tvar username = document.getElementById(\"username1\").value;\r\n");
      out.write("\t\t\tvar password = document.getElementById(\"password1\").value;\r\n");
      out.write("\t\t\tvar password2 = document.getElementById(\"password2\").value;\r\n");
      out.write("\t\t\tvar code = document.getElementById(\"password3\").value;\r\n");
      out.write("\t\t\tif(password == password2&&code != null && code.length == 4&&username != null && username!=\"\"&&password!=null&&password!=\"\"&&username.length>=6&&password.length>=6){\r\n");
      out.write("\t\t\t\twindow.location=\"loginsuccess.jsp\";\r\n");
      out.write("\t\t\t}else if(username == \"\"){\r\n");
      out.write("\t\t\t\talert(\"请输入用户名\");\r\n");
      out.write("\t\t\t}else if(password == \"\"){\r\n");
      out.write("\t\t\t\talert(\"请输入密码\");\r\n");
      out.write("\t\t\t}else if(code == null){\r\n");
      out.write("\t\t\t\talert(\"请输入正确的验证码\");\r\n");
      out.write("\t\t\t}else if(code.length != 4){\r\n");
      out.write("\t\t\t\talert(\"请输入正确的验证码\");\r\n");
      out.write("\t\t\t}else if(password != password2){\r\n");
      out.write("\t\t\t\talert(\"两次输入的密码不一致\");\r\n");
      out.write("\t\t\t}else{\r\n");
      out.write("\t\t\t\talert(\"用户名或密码错误！\");\r\n");
      out.write("\t\t\t}\r\n");
      out.write("\t\t}\r\n");
      out.write("\t</script>\r\n");
      out.write("\t<div style=\"background-color:#87CECB;width:100%;height:50px;margin-top: 4%\">\r\n");
      out.write("\t</div>\r\n");
      out.write("\t<div style=\"float: left;margin-left: 12%;margin-top:7%\"><img style=\"height: 300px;width: 400px\" alt=\"绿洲\" src=\"img/logo.bmp\"></div>\r\n");
      out.write("\t<div class=\"container\"\r\n");
      out.write("\t\tstyle=\"padding-top: 10px; position: absolute; top: 200px; right: 200px; width: 350px; height: 500px;\">\r\n");
      out.write("\t\t<div id=\"phoneTgudingForm\"\r\n");
      out.write("\t\t\tclass=\"canvas-edited-text canvas-event-selectable canvas-event-draggable canvas-event-click canvas-event-dbclick ui-draggable ui-selectee ui-selected canvas-selected-border\"\r\n");
      out.write("\t\t\tdata-canvasrole=\"login-role-panel\" data-role=\"page\"\r\n");
      out.write("\t\t\tstyle=\"position: relative;\">\r\n");
      out.write("\t\t\t<div style=\"color:#87CECB;\"><font size=\"6px\">用户注册</font></div>\r\n");
      out.write("\t\t\t<div style=\"height:10px\"></div>\r\n");
      out.write("\t\t\t<div id=\"phoneDiv\">\r\n");
      out.write("\t\t\t\t<form class=\"form-horizontal templatemo-container templatemo-login-form-1\"\r\n");
      out.write("\t\t\t\t\trole=\"form\" action=\"#\" method=\"post\">\r\n");
      out.write("\t\t\t\t\t<div class=\"form-group\">\r\n");
      out.write("\t\t\t\t\t\t<div class=\"col-xs-12\">\r\n");
      out.write("\t\t\t\t\t\t\t<div class=\"control-wrapper\">\r\n");
      out.write("\t\t\t\t\t\t\t\t<label for=\"username\" class=\"control-label fa-label\"><i\r\n");
      out.write("\t\t\t\t\t\t\t\t\tclass=\"fa fa-user\"></i></label> <input class=\"form-control\"\r\n");
      out.write("\t\t\t\t\t\t\t\t\tid=\"username1\" placeholder=\"用户名\" type=\"text\">\r\n");
      out.write("\t\t\t\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t\t<div class=\"form-group\">\r\n");
      out.write("\t\t\t\t\t\t<div class=\"col-xs-12\">\r\n");
      out.write("\t\t\t\t\t\t\t<div class=\"control-wrapper\">\r\n");
      out.write("\t\t\t\t\t\t\t\t<label for=\"password\" class=\"control-label fa-label\"><i\r\n");
      out.write("\t\t\t\t\t\t\t\t\tclass=\"fa fa-lock\"></i></label> <input class=\"form-control\"\r\n");
      out.write("\t\t\t\t\t\t\t\t\tid=\"password1\" placeholder=\"密码\" type=\"password\">\r\n");
      out.write("\t\t\t\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t\t<div class=\"form-group\">\r\n");
      out.write("\t\t\t\t\t\t<div class=\"col-xs-12\">\r\n");
      out.write("\t\t\t\t\t\t\t<div class=\"control-wrapper\">\r\n");
      out.write("\t\t\t\t\t\t\t\t<label for=\"password\" class=\"control-label fa-label\"><i\r\n");
      out.write("\t\t\t\t\t\t\t\t\tclass=\"fa fa-lock\"></i></label> <input class=\"form-control\"\r\n");
      out.write("\t\t\t\t\t\t\t\t\tid=\"password2\" placeholder=\"确认密码\" type=\"password\">\r\n");
      out.write("\t\t\t\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t\t<div class=\"form-group\">\r\n");
      out.write("\t\t\t\t\t\t<div class=\"col-xs-12\">\r\n");
      out.write("\t\t\t\t\t\t\t<div class=\"control-wrapper\">\r\n");
      out.write("\t\t\t\t\t\t\t\t<label for=\"username\" class=\"control-label fa-label\"><i\r\n");
      out.write("\t\t\t\t\t\t\t\t\tclass=\"fa fa-user\"></i></label> <input class=\"form-control\"\r\n");
      out.write("\t\t\t\t\t\t\t\t\tid=\"phone\" placeholder=\"手机号\" type=\"text\">\r\n");
      out.write("\t\t\t\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t\t<div class=\"form-group\">\r\n");
      out.write("\t\t\t\t\t\t<div class=\"col-xs-8\">\r\n");
      out.write("\t\t\t\t\t\t\t<div class=\"control-wrapper\">\r\n");
      out.write("\t\t\t\t\t\t\t\t<label for=\"password2\" class=\"control-label fa-label\"><i\r\n");
      out.write("\t\t\t\t\t\t\t\t\tclass=\"fa fa-lock\"></i></label> <input class=\"form-control\"\r\n");
      out.write("\t\t\t\t\t\t\t\t\tid=\"password3\" placeholder=\"验证码\" type=\"text\">\r\n");
      out.write("\t\t\t\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t\t\t<div class=\"col-xs-4\" style=\"padding-left: 0px;\">\r\n");
      out.write("\t\t\t\t\t\t\t<input id=\"add\" value=\"发送验证码\"\r\n");
      out.write("\t\t\t\t\t\t\t\tonclick=\"isSendSUCC(true)\"\r\n");
      out.write("\t\t\t\t\t\t\t\tstyle=\"width: 100%; background-color:#87CECB \" class=\"canvas-edited-text btn btn-info\"\r\n");
      out.write("\t\t\t\t\t\t\t\ttype=\"button\">\r\n");
      out.write("\t\t\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t\t<div class=\"form-group\">\r\n");
      out.write("\t\t\t\t\t\t<div class=\"col-xs-12\">\r\n");
      out.write("\t\t\t\t\t\t&nbsp;&nbsp;&nbsp;&nbsp;\r\n");
      out.write("\t\t\t\t\t\t\t<input id=\"loginBtn2\" value=\"确认\"\r\n");
      out.write("\t\t\t\t\t\t\t\tonclick=\"updatePsw();\"\r\n");
      out.write("\t\t\t\t\t\t\t\tclass=\"canvas-edited-text btn btn-info\" style=\"width: 40%;background-color: #87CECB\"\r\n");
      out.write("\t\t\t\t\t\t\t\ttype=\"button\">\r\n");
      out.write("\t\t\t\t\t\t\t\t&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\r\n");
      out.write("\t\t\t\t\t\t\t<input id=\"loginBtn2\" value=\"返回\"\r\n");
      out.write("\t\t\t\t\t\t\tonclick=\"window.location='login.jsp'\"\r\n");
      out.write("\t\t\t\t\t\t\tclass=\"canvas-edited-text btn btn-info\" style=\"width: 40%;background-color: #87CECB\"\r\n");
      out.write("\t\t\t\t\t\t\ttype=\"button\">\r\n");
      out.write("\t\t\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t</form>\r\n");
      out.write("\t\t\t</div>\r\n");
      out.write("\t\t</div>\r\n");
      out.write("\t\t<div id=\"gudingForm\" style=\"display: none;\"\r\n");
      out.write("\t\t\tclass=\"canvas-edited-text canvas-event-selectable canvas-event-draggable canvas-event-click canvas-event-dbclick ui-draggable ui-selectee\"\r\n");
      out.write("\t\t\tdata-canvasrole=\"login-role-panel\" data-role=\"page\">\r\n");
      out.write("\t\t\t<form\r\n");
      out.write("\t\t\t\tclass=\"form-horizontal templatemo-container templatemo-login-form-1\"\r\n");
      out.write("\t\t\t\trole=\"form\" action=\"#\" method=\"post\">\r\n");
      out.write("\t\t\t\t<div class=\"form-group\">\r\n");
      out.write("\t\t\t\t\t<div class=\"col-xs-12\">\r\n");
      out.write("\t\t\t\t\t\t<div class=\"control-wrapper\">\r\n");
      out.write("\t\t\t\t\t\t\t<label for=\"username\" class=\"control-label fa-label\"><i\r\n");
      out.write("\t\t\t\t\t\t\t\tclass=\"fa fa-user\"></i></label> <input class=\"form-control\"\r\n");
      out.write("\t\t\t\t\t\t\t\tid=\"username3\" placeholder=\"用户名\" type=\"text\">\r\n");
      out.write("\t\t\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t<div class=\"form-group\">\r\n");
      out.write("\t\t\t\t\t<div class=\"col-xs-12\">\r\n");
      out.write("\t\t\t\t\t\t<div class=\"control-wrapper\">\r\n");
      out.write("\t\t\t\t\t\t\t<label for=\"password\" class=\"control-label fa-label\"><i\r\n");
      out.write("\t\t\t\t\t\t\t\tclass=\"fa fa-lock\"></i></label> <input class=\"form-control\"\r\n");
      out.write("\t\t\t\t\t\t\t\tid=\"password3\" placeholder=\"密码\" type=\"password\">\r\n");
      out.write("\t\t\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t<div>\r\n");
      out.write("\t\t\t\t\t<div>\r\n");
      out.write("\t\t\t\t\t\t<input id=\"loginBtn3\" value=\"注册\" \r\n");
      out.write("\t\t\t\t\t\t\tstyle=\"width: 100%;background-color:#87CECB;\" type=\"button\">\r\n");
      out.write("\t\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t</div>\r\n");
      out.write("\t\t\t</form>\r\n");
      out.write("\t\t</div>\r\n");
      out.write("\t\t<div id=\"phoneForm\" style=\"display: none;\"\r\n");
      out.write("\t\t\tclass=\"canvas-edited-text canvas-event-selectable canvas-event-draggable canvas-event-click canvas-event-dbclick ui-draggable ui-selectee\"\r\n");
      out.write("\t\t\tdata-canvasrole=\"login-role-panel\" data-role=\"page\">\r\n");
      out.write("\t\t\t<form\r\n");
      out.write("\t\t\t\tclass=\"form-horizontal templatemo-container templatemo-login-form-1\"\r\n");
      out.write("\t\t\t\trole=\"form\" action=\"#\" method=\"post\">\r\n");
      out.write("\t\t\t\t<div class=\"form-group\">\r\n");
      out.write("\t\t\t\t\t<div class=\"col-xs-12\">\r\n");
      out.write("\t\t\t\t\t\t<div class=\"control-wrapper\">\r\n");
      out.write("\t\t\t\t\t\t\t<label for=\"username\" class=\"control-label fa-label\"><i\r\n");
      out.write("\t\t\t\t\t\t\t\tclass=\"fa fa-user\"></i></label> <input class=\"form-control\"\r\n");
      out.write("\t\t\t\t\t\t\t\tid=\"username4\" placeholder=\"用户名\" type=\"text\">\r\n");
      out.write("\t\t\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t<div class=\"form-group\">\r\n");
      out.write("\t\t\t\t\t<div class=\"col-xs-8\">\r\n");
      out.write("\t\t\t\t\t\t<div class=\"control-wrapper\">\r\n");
      out.write("\t\t\t\t\t\t\t<label for=\"password\" class=\"control-label fa-label\"><i\r\n");
      out.write("\t\t\t\t\t\t\t\tclass=\"fa fa-lock\"></i></label> <input class=\"form-control\"\r\n");
      out.write("\t\t\t\t\t\t\t\tid=\"password4\" placeholder=\"验证码\" type=\"text\">\r\n");
      out.write("\t\t\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t\t<div class=\"col-xs-4\" style=\"padding-left: 0px;\">\r\n");
      out.write("\t\t\t\t\t\t<input id=\"sendLoginMessageBtn4\" value=\"Get Code\"\r\n");
      out.write("\t\t\t\t\t\t\tstyle=\"width: 100%;\" class=\"canvas-edited-text btn btn-info\"\r\n");
      out.write("\t\t\t\t\t\t\ttype=\"button\">\r\n");
      out.write("\t\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t<div class=\"form-group\">\r\n");
      out.write("\t\t\t\t\t<div class=\"col-xs-12\">\r\n");
      out.write("\t\t\t\t\t\t<input id=\"loginBtn4\" value=\"Login\"\r\n");
      out.write("\t\t\t\t\t\t\tclass=\"canvas-edited-text btn btn-info\" style=\"width: 100%;\"\r\n");
      out.write("\t\t\t\t\t\t\ttype=\"button\">\r\n");
      out.write("\t\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t</div>\r\n");
      out.write("\t\t\t</form>\r\n");
      out.write("\t\t</div>\r\n");
      out.write("\t\t<div>\r\n");
      out.write("\t\t\t<div>\r\n");
      out.write("\t\t\t\t<div>\r\n");
      out.write("\t\t\t\t\t<div>\r\n");
      out.write("\t\t\t\t\t\t<font size=\"2px\" color=\"#A9A9A9\">合作账户登录：</font> \r\n");
      out.write("\t\t\t\t\t\t<a id=\"qq\" href=\"/portal/qqLogin\">\r\n");
      out.write("\t\t\t\t\t\t\t<img src=\"img/qq1.png\" style=\"margin-top: 3px; cursor: pointer\">\r\n");
      out.write("\t\t\t\t\t\t</a>\r\n");
      out.write("\t\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t</div>\r\n");
      out.write("\t\t\t</div>\r\n");
      out.write("\t\t</div>\r\n");
      out.write("\t</div>\r\n");
      out.write("</body>\r\n");
      out.write("</html>\r\n");
    } catch (java.lang.Throwable t) {
      if (!(t instanceof javax.servlet.jsp.SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try { out.clearBuffer(); } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
