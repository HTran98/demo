<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<!DOCTYPE html>
<html>
<link rel="stylesheet" type="text/css"
	href="<c:url value="/resources/styles/input.css"/>">
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
	integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh"
	crossorigin="anonymous">
<head>
<title>website quan li trac nghiem</title>
</head>
<body>
	<div class="header">
		<div>
			<img src="<c:url value="/resources/img/logo.png"/>" class="image">
		</div>
		<div class="text">
			<p>Challenge for change</p>
		</div>
	</div>
	<div class="content">
		<div class="information">
			<p>Candidater Information</p>
		</div>
		<div class="input">
			<form action="test.htm" method="post">
				<div class="row">
					<div class="col-md-2">
						<label>Full name</label>
					</div>
					<div class="col-md-6">
						<input type="text" name="fullName">
					</div>
					<div class="col-md-1">
						<span>(*)</span>
					</div>

				</div>
				<div class="row">
					<div class="col-md-2">
						<label>Phone</label>
					</div>
					<div class="col-md-6">
						<input type="text" name="phoneNumber">
					</div>
					<div class="col-md-1">
						<span>(*)</span>
					</div>

				</div>
				<div class="row">
					<div class="col-md-2">
						<label>Email</label>
					</div>
					<div class="col-md-6">
						<input type="text" name="email">
					</div>
					<div class="col-md-1">
						<span>(*)</span>
					</div>

				</div>
				<div class="row">
					<div class="col-md-2">
						<label id="lagg">Language</label>
					</div>
					<div class="col-md-6">

						<select class="select" name="categoryId">
							<c:forEach var="lists" items="${listCategory}">
							<c:if test="${lists.categoryId != 'C0000005' }">
							  <option value="${lists.categoryId }">${lists.categoryName}</option>
							</c:if>
								
							</c:forEach>
						</select>


					</div>
					<div class="col-md-1">
						<span class="kt">(*)</span>
					</div>

				</div>
				<div class="start">
					<button onclick="return validate()">Start</button>
				</div>
			</form>

		</div>

	</div>
	<div class="errorDialog">
		<div class="titleError"><b>Error</b></div>
		<div class="contentError">Please input <b>FullName</b>, <b>Phone</b>, <b>Email</b></div>
		<button name="btnError" onclick="return an()"><b>OK</b></button>
	</div>
	<div class="footer">
	<style>
		.errorDialog{
			width: 300px;
			height: 140px;
			background: white;
			text-align: center;
			position: absolute;
			top: 0;
			bottom: 0;
			left: 0;
			right: 0;
			margin: auto;
			border: 1px solid black;
			display: none;
		}
		.titleError{
			height: 28px;
			line-height: 28px;
			text-align: left;
			padding-left: 10px;
			background: rgb(255, 153, 51);
			border-bottom: 1px solid black;
		}
		.contentError{
			margin-top: 20px;
			margin-bottom: 15px;
		}
		button {
		    border: 1px solid black;
		}
	</style>
	<script type="text/javascript">
		function an() {
			var errorDialog = document.getElementsByClassName("errorDialog");
			errorDialog[0].style.display = "none";
			return false;
		}
		function validate() {
			var input = document.getElementsByTagName("input");
			var strError = "Please input ";
			var kt = true;
			if(input[0].value==="") {
				kt = false;
				strError+= "<b>Full Name</b>";
			}
			if(input[1].value==="") {
				if(!kt) strError+= ", ";
				else kt = false;
				strError+= "<b>Phone</b>";
			}
			if(input[2].value==="") {
				if(!kt) strError+= ", ";
				else kt = false;
				strError+= "<b>Email</b>";
			}
			if(!kt){
				var contentError = document.getElementsByClassName("contentError");
				contentError[0].innerHTML = strError;
				var errorDialog = document.getElementsByClassName("errorDialog");
				errorDialog[0].style.display = "block";
				return false;
			}
		}
	</script>
		<div>
			<p>© USOL Vietnam Co.,Ltd. All Rights Reserved.</p>
		</div>
	</div>
</body>
</html>