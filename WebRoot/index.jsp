<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'index.jtpl' starting page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
  </head>
  
  <body>
  	<a href="http://127.0.0.1:8080/lb-base/knowledge/query.action">使用原始servlet处理请求 http://127.0.0.1:8080/lb-base/knowledge/query.action</a>
  	<br><br>
    <a href="http://127.0.0.1:8080/lb-base/knowledge/query.do">使用struts2处理请求,默认处理.do结尾的请求 http://127.0.0.1:8080/lb-base/knowledge/query.do</a>
  </body>
</html>
