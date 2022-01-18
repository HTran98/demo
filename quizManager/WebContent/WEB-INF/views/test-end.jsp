<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>Screen test phần thi tự chọn (JAVA, C#, C, PYTHON…)</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="<c:url value="/resources/styles/style3.css" />">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"
	integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm"
	crossorigin="anonymous">
</head>
<body>
	<!-- 	<div class="container"> -->
	<div class="header">
		<div class="row">
			<div class="col-md-12">
				<div class="row">
					<div class="col-md-6">
						<img alt="Logo" src="<c:url value="/resources/img/logo.png"/>"
							class="logoImg">
					</div>
					<div class="col-md-6">
						<div class="row">
							<div class="col-md-8"></div>
							<div class="col-md-4">
								<div class="row2" id="date">${date }</div>
								<div class="row2" id="time">${time } </div>
							</div>
						</div>
					</div>
					<div class="row">
						<p>CHALLENGE FOR CHANGE</p>
					</div>
				</div>
			</div>
		</div>
		<div class="main">
			<div class="row">
				<div class="col-md-9"></div>
				<div class="col-md-2">Hi : ${fullName }</div>
				<div class="row col-md-10">
					<div class="title">Theory test : Finished</div>
					<div class="col-md-10">
						<div class="done">
							Congratulations, you've just completed the theory test.</br> Thank you
							for choosing USOL-V as the next company.</br> Hope to be working with
							you soon.</br>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="footer">© USOL Vietnam Co.,Ltd. All Rights Reserved.
		</div>
		<!-- </div> -->
</body>
</html>