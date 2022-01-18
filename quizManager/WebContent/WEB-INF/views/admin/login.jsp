<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet"
	href="<c:url value="/resources/styles/admin.css" />">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Login</title>
</head>
<body>
	<%@ include file="layout/header.jsp"%>
	<div class="content">
		<div class="thong-tin">
			<p class="inf">Login</p>
		</div>
		<form action="logincheck.htm" method="post">
			<div class="form-group row">
				<label for="Username" class="col-md-3 col-form-label text-md-right">User name</label>
				<div class="col-md-6">
					<input type="text" class="Username" id="Username" name="userName">
				</div>
				<div class="col-md-1 username">
					<span> (*)</span>
				</div>
			</div>

			<div class="form-group row">
				<label for="password" class="col-md-3 col-form-label text-md-right">Password</label>
				<div class="col-md-6">
					<input type="password" id="password" class="password" name="passWord">
				</div>
				<div class="col-md-1 password">
					<span> (*)</span>
				</div>
				<div class="col-md-4 offset-md-1">
					<input type="submit" onclick="return validate()" class="btn-blue" value="Login">
				</div>
			</div>
			<div class="errorShow">
				<div class="titleError">Error</div>
				<div class="detailError">
					<font class="errorContent">Error Massage</font><br>
					<button type="button" onclick="cclick()" class="btnDialog"
						name="btnDialog">OK</button>
				</div>
				
			</div>
			<div class="errorShow">
				<div class="titleError">Error</div>
				<div class="detailError">
					<font class="errorContent">Username or password incorrect<br> Please try again!!</font>
				</div>
				<button type="button" onclick="cclick2()" class="btnDialog"
						name="btnDialog">OK</button>
			</div>
			
${error }
			<style>
.errorShow {
	top: 0px;
	bottom: 0px;
	left: 0px;
	right: 0px;
	display: none;
	margin: auto;
	position: absolute;
	width: 220px;
	height: 145px;
	border: 1px solid black;
	text-align: center;
	background: white;
	width: 320px;
}

.titleError {
	color:white ;
	text-align:left;
	width: 100%;
	height: 30px;
	background: #5b9bd5;
}

.detailError {
	width: 100%;
	height: 70px;
	padding-top: 10px;
}

.btnDialog {
	margin-top:15px;
	height: 40px;
	width: 75px;
	text-align: center;
	line-height: 20px;
}
button{
color:white ;
background: #5b9bd5;
}
.fill {
	color: red;
}
</style>
		</form>
	</div>
	<%@ include file="layout/footer.jsp"%>
	<script type="text/javascript">
		var errorShow = document.getElementsByClassName("errorShow");
		var errorContent = document.getElementsByClassName("errorContent");
		function cclick() {
			errorShow[0].style.display = "none";
		}
		function cclick2() {
			errorShow[1].style.display = "none";
		}
		function validate() {
			var Username = document.getElementsByClassName("Username");
			if (Username[0].value === "") {
				errorContent[0].innerHTML = "Please input<b> User name</b>";
				errorShow[0].style.display = "block";
				return false;
			}
			var password = document.getElementsByClassName("password");
			if (password[0].value === "") {
				errorContent[0].innerHTML = "Please input <b>password</b>";
				errorShow[0].style.display = "block";
				return false;
			} else
				return true;
		}
	</script>
</body>
</html>