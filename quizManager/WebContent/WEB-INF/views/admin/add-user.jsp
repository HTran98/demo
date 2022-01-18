<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Add new user</title>
</head>
<body>
	<%@ include file="layout/header.jsp"%>
	<div class="container">
		<div class="form-row info">
			<div class="form-group col-md-3">
				<a href="#"><b>Users management</b></a> <font style="color: white">&nbsp;>&nbsp;<a /></font><a
					href="#">Add new</a>
			</div>
		</div>
		<form action="save-user.htm" method="post">
			<div class="form-row">
				<div class="form-group col-md-3"></div>
				<div class="form-group col-md-6 info">
					<b>Users Information</b>
				</div>
			</div>
			<div class="form-row">
				<div class="form-group col-md-3"></div>
				<div class="form-group col-md-2" style="color: #5b9bd5">
					<b>User ID</b>
				</div>
				<div class="form-group col-md-4">
					<input name="userId" readonly="readonly" value="${userId }"
						class="userId" type="text">
				</div>

			</div>
			<div class="form-row">
				<div class="form-group col-md-3"></div>
				<div class="form-group col-md-2" style="color: #5b9bd5">
					<b>Username</b>
				</div>
				<div class="form-group col-md-4">
					<input name="userName" class="userName" type="text"> <span
						class="fill">(*)</span>

				</div>
			</div>
			<div class="form-row">
				<div class="form-group col-md-3"></div>
				<div class="form-group col-md-2" style="color: #5b9bd5">
					<b>Password</b>
				</div>
				<div class="form-group col-md-4">
					<input name="passWord" class="passWord" type="password"> <span
						class="fill">(*)</span>

				</div>

			</div>
			<div class="form-row">
				<div class="form-group col-md-3"></div>
				<div class="form-group col-md-2" style="color: #5b9bd5">
					<b>Fullname</b>
				</div>
				<div class="form-group col-md-4">
					<input name="fullName" class="fullName" type="text"> <input
						name="userDeleteStatus" type="hidden" value="0"> <span
						class="fill">(*)</span>
				</div>


			</div>
			<div class="form-row">
				<div class="form-group col-md-3"></div>
				<div class="form-group col-md-2" style="color: #5b9bd5">
					<b>Role</b>
				</div>
				<div class="form-group col-md-4">
					<select class="selecteRole" name="role">
						<option value="1">Admin</option>
						<option value="0">Mod</option>
					</select>
				</div>
			</div>

			<div class="form-row">
				<div class="form-group col-md-5"></div>
				<div class="form-group col-md-2">
					<button type="submit" name="add" onclick="return validate()"
						class="btn-blue">
						<b>Add</b>
					</button>
				</div>
				<div class="form-group col-md-1"></div>
				<div class="form-group col-md-1">
					<button type="submit" name="cancel" class="btn-blue">
						<b>Cancel</b>
					</button>
				</div>
			</div>
			<div class="errorDialog">
				<div class="titleDialog">Error</div>
				<div class="detailDialog">
					<font class="errorContent">Lỗi rồi nhé</font>
				</div>
				<button type="button" onclick="an()" class="btnDialog"
					name="btnDialog">OK</button>
			</div>
			<style>
.errorDialog {
	top: 0;
	bottom: 0;
	left: 0;
	right: 0;
	display: none;
	margin: auto;
	position: absolute;
	width: 220px;
	height: 135px;
	border: 1px solid black;
	text-align: center;
	background: white;
}

.titleDialog {
	width: 100%;
	height: 30px;
	background: #5b9bd5;
}

.detailDialog {
	width: 100%;
	height: 70px;
	padding-top: 30px;
}

button {
	color: white;
	background: #5b9bd5;
}

.btnDialog {
	height: 25px;
	width: 40px;
	line-height: 20px;
}

.fill {
	color: red;
}
</style>
		</form>
	</div>
	<%@ include file="layout/footer.jsp"%>
	<script type="text/javascript">
		var errorDialog = document.getElementsByClassName("errorDialog");
		var errorContent = document.getElementsByClassName("errorContent");
		function an() {
			errorDialog[0].style.display = "none";
		}
		function validate() {
			var text = document.getElementsByClassName("userName");
			if (text[0].value === "") {
				errorContent[0].innerHTML = "Please input userName!";
				errorDialog[0].style.display = "block";
				return false;
			}
			var text = document.getElementsByClassName("passWord");
			if (text[0].value === "") {
				errorContent[0].innerHTML = "Please input Password!";
				errorDialog[0].style.display = "block";
				return false;
			} else
				var text = document.getElementsByClassName("fullName");
			if (text[0].value === "") {
				errorContent[0].innerHTML = "Please input fullName!";
				errorDialog[0].style.display = "block";
				return false;
			} else
				return true;
		}
	</script>
</body>
</html>