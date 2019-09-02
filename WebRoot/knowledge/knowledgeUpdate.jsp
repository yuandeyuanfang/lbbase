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
    
    <title>My JSP 'knowledgeSelect.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	
	<script type="text/javascript">
		function update(){
			if(document.getElementById("pointType")!=null && document.getElementById("pointType")!=undefined && document.getElementById("pointType").value!=''){
			}else{
				alert("请选择知识点类型");
				return false;
			}
			document.getElementById("update_form").action = "<%=basePath%>knowledge/update.action";
			document.getElementById("update_form").submit();
		}
	</script>

  </head>
  
  <body>
  	<form action="" id="update_form" method="post">
  	<input type="hidden" name="id" value="${knowledge.id}" >
  	<table border="1">
  			<tr><td>名称</td><td><input type="text" name="name" value="${knowledge.name}" ></td></tr>
  			<tr><td>介绍</td><td><input type="text" name="menu" value="${knowledge.menu}" ></td></tr>
  			<tr><td>页面链接</td><td><input type="text" name="pageLink" value="${knowledge.pageLink}" ></td></tr>
  			<tr><td>文件路径</td><td><input type="text" name="javaPath" value="${knowledge.javaPath}" ></td></tr>
  			<tr><td>知识点类型</td><td><select id="pointType" name="pointType"><option value="">请选择知识点类型</option>
						<c:forEach var="dataDictionary" items="${dataDictionaryList}">
							<option value="${dataDictionary.id}" <c:if test="${dataDictionary.id==knowledge.pointType}" var="adminchock">selected="selected"</c:if>>${dataDictionary.dataTypeName}</option>
						</c:forEach>
				</select>
				</td>
			</tr>
  			<tr><td>图片（blob）</td><td><input type="text" name="picture" value="${knowledge.picture}" ></td></tr>
  			<tr><td>文章（clob）</td><td><input type="text" name="article" value="${knowledge.article}" ></td></tr>
    		<tr><td colspan="2"><input type="button" value="提交"  onclick="javascript:update();"></td></tr>
  	</table>
  	</form>
  </body>
</html>
