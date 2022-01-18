<%@page import="java.io.PrintWriter"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
	integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T"
	crossorigin="anonymous">
<link rel="stylesheet"
	href="<c:url value="/resources/styles/custom-styles.css" />">
<script
	src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"
	integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM"
	crossorigin="anonymous"></script>
<script type="text/javascript"
	src="<c:url value="/resources/styles/script.js"/>"></script>
<style>
.log {
	position: absolute;
	right: 40px;
	margin-top: 15px;
}
.log a{
	color: white;
	font-weight: bold;
}
</style>
</head>
<%
	String servletPath = request.getServletPath();
	String active = "";
	if (servletPath.indexOf("question") > 0)
		active = "1";
	if (servletPath.indexOf("category") > 0)
		active = "2";
	if (servletPath.indexOf("user") > 0)
		active = "3";
	if (servletPath.indexOf("exam") > 0)
		active = "4";
%>
<body>
	<div class="row">
		<div class="col-sm-3 logo">
			<img alt="Logo" src="<c:url value="/resources/img/logo.png"/>"
				class="logoImg">
			<div class="logoTxt">CHALLENGE FOR CHANGE</div>
		</div>
	</div>

	<c:if test="${user!= null}">
		<div class="hello">Hi: ${user.fullName}</div>
		<div class="log">
			<a href="logout.htm">Logout</a>
		</div>
		<div class="menu">
			<div class="container">
				<div class="form-row title col-md-6">
					<div class="col hover ${menu1 }">
						<a href="<c:url value="/admin/question.htm" />">Question</a>
					</div>
					<div class="col hover ${menu2 }">
						<a href="<c:url value="/admin/category.htm" />">Catogories</a>
					</div>
					<div class="col hover ${menu3 }">
						<a href="<c:url value="/admin/user.htm" />">Users</a>
					</div>
					<div class="col hover ${menu4 }">
						<a href="<c:url value="/admin/exam.htm" />">Exams</a>
					</div>
				</div>
			</div>
		</div>
	</c:if>
</body>
<script>
	var active = document.getElementsByClassName("hover");
	if (
<%=active%>
	=== 1)
		active[0].style.background = "#ff9933";
	if (
<%=active%>
	=== 2)
		active[1].style.background = "#ff9933";
	if (
<%=active%>
	=== 3)
		active[2].style.background = "#ff9933";
	if (
<%=active%>
	=== 4)
		active[3].style.background = "#ff9933";
</script>
</html>

