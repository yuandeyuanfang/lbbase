<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.lb.base.entity.Knowledge"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>知识点查询</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script>
		function goPage(pageNum){
			var actionUrl = document.getElementById("actionUrl").value;
			if(actionUrl.indexOf("?")>=0){
				actionUrl = actionUrl+"&"
			}else{
				actionUrl = actionUrl+"?"
			}
			window.location.href=actionUrl+"curPage="+pageNum;
		}
		
		function add(){
			window.location.href="<%=basePath%>knowledge/add.action";
		}
		
		function update(id){
			window.location.href="<%=basePath%>knowledge/update.action?id="+id;
		}
		
		function removeK(id){
			window.location.href="<%=basePath%>knowledge/remove.action?id="+id;
		}
		
	</script>
  </head>
  
  <body>
  	<table border="1">
  			<tr><td>名称</td><td>介绍</td><td>页面链接</td><td>文件路径</td><td>知识点类型</td><td>创建时间</td><td>图片（blob）</td><td>文章（clob）</td><td>编辑</td><td>删除</td></tr>
  		<c:forEach var="knowledge" items="${knowledgeList}">
  			<tr>
  				<td>${knowledge.name}</td>
  				<td>${knowledge.menu}</td>
  				<td>${knowledge.pageLink}</td>
  				<td>${knowledge.javaPath}</td>
  				<td>${knowledge.pointTypeStr}</td>
  				<td>${knowledge.createTime}</td>
  				<td>${knowledge.picture}</td>
  				<td>${knowledge.article}</td>
  				<td><input type="button" value="编辑" onclick="javascript:update(${knowledge.id});"></td>
  				<td><input type="button" value="删除" onclick="javascript:removeK(${knowledge.id});"></td>
  			</tr>
    	</c:forEach>
    		<tr>
    			<td><input type="button" value="新增" onclick="javascript:add();"></td>
    			<td colspan="9" id="pageHelper">${pageHtml}</td>
    		</tr>
  	</table>
  </body>
</html>
