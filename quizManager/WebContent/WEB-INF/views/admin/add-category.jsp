<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Add new category</title>
</head>
<body>
	<%@ include file="layout/header.jsp"%>
	<div class="container">
		<div class="form-row info">
			<div class="form-group col-md-3">
				<a href="#"><b>Categories management</b></a> <font style="color: white">&nbsp; <b> > </b> &nbsp;<a /></font><a
					href="#"><b>Add new</b></a>
			</div>
		</div>
		<form action="save-category.htm" method="post">
			<div class="form-row">
				<div class="form-group col-md-3"></div>
				<div class="form-group col-md-6 info"><b>Category Information</b></div>
			</div>
			<div class="form-row">
				<div class="form-group col-md-3"></div>
				<div class="form-group col-md-2" style="color: #5b9bd5"><b>Category ID</b></div>
				<div class="form-group col-md-4">
					<input name="categoryId" class="categoryId" readonly="readonly"
						value="${categoryId }" type="text">
				</div>

			</div>
			<div class="form-row">
				<div class="form-group col-md-3"></div>
				<div class="form-group col-md-2"><font style="color: #5b9bd5"><b>Category Name</b></font></div>
				<div class="form-group col-md-4">
					<input name="categoryName" class="categoryName" type="text">
				<span class="fill">(*)</span>
				</div>
			</div>
			<div class="form-row">
				<div class="form-group col-md-5"></div>
				<div class="form-group col-md-1">
					<button type="submit" name="add" onclick="return validate()"
						class="btn-blue"><b>Add</b></button>
				</div>
				<div class="form-group col-md-1"></div>
				<div class="form-group col-md-1">
					<button type="submit" name="cancel" class="btn-blue" onclick="" ><b>Cancel</b></button>
				</div>
			</div>
			<div class="errorDialog">
				<div class="titleDialog">Error</div>
				<div class="detailDialog">
					<font class="errorContent">Error Massage</font>
					<button type="button" onclick="cclick()" class="btnDialog"
						name="btnDialog">OK</button>
				</div>
			</div>
			<div class="errorDialog">
				<div class="titleDialog">Error</div>
				<div class="detailDialog">
					<font class="errorContent">Category name existing!</font>
					<br>
					<button type="button" onclick="cclick2()" class="btnDialog"
						name="btnDialog">OK</button>
				</div>
			</div>
			${error }
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

.btnDialog {
    margin-top:15px;
	height: 25px;
	width: 40px;
	line-height: 20px;
}

button {
	color: white;
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
		var errorDialog = document.getElementsByClassName("errorDialog");
		var errorContent = document.getElementsByClassName("errorContent");
		function cclick() {
			errorDialog[0].style.display = "none";
		}
		function cclick2() {
			errorDialog[1].style.display = "none";
		}
		function validate() {
			var Category_Name = document.getElementsByClassName("categoryName");
			if (Category_Name[0].value === "") {
				errorContent[0].innerHTML = "Please input Category Name!";
				errorDialog[0].style.display = "block";
				return false;
			} else
				return true;
		}
		
	</script>
</body>
</html>