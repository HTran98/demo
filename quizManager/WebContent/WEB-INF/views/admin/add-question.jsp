<%@page import="java.util.ArrayList"%>
<%@page import="com.yellow.model.Categories"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Add new question</title>
</head>
<body>
	<%@ include file="layout/header.jsp"%>
	<div class="container">
		<div class="form-row info">
			<div class="form-group col-md-3">
				<a href="#">Question management</a> <font style="color: white">&nbsp;>&nbsp;<a /></font><a
					href="#">Add new</a>
			</div>
		</div>
		<form action="save-question.htm" method="post">
			<div class="form-row">
				<div class="form-group col-md-2"></div>
				<div class="form-group col-md-5 info"><b>Question Information</b></div>
				<div class="form-group col-md-2"></div>
				<div class="form-group col-md-3 info"><b>Answer</b></div>
			</div>
			<div class="form-row">
				<div class="form-group col-md-8">
					<div class="form-row">
						<div class="form-group col-md-3"></div>
						<div class="form-group col-md-2"><font style="color: #5b9bd5"><b>Quest ID</b></font></div>
						<div class="form-group col-md-6">
							<input class="questionId" name="questionId" class="questionId"
								readonly="readonly" value="${questionId }" type="text">
							<input name="questionDeleteStatus" value="0" type="hidden">
						</div>

					</div>
					<div class="form-row">
						<div class="form-group col-md-3"></div>
						<div class="form-group col-md-2
						"><font style="color: #5b9bd5"><b>Text</b></font></div>
						<div class="form-group col-md-6">
							<textarea class="questionDetail" name="questionDetail" rows="3"
								cols="20"></textarea>
						</div>
						<div class="form-group col-md-1 fill">(*)</div>
					</div>
					<div class="form-row">
						<div class="form-group col-md-3"></div>
						<div class="form-group col-md-2"><font style="color: #5b9bd5"><b>Category</b></font></div>
						<div class="form-group col-md-6">
							<select class="selectCatogory" name="categoryId">
								<%
									List<Categories> list = (ArrayList<Categories>) request.getAttribute("listCategory");
									for (Categories cate : list) {
								%>
								<option value="<%=cate.getCategoryId()%>"><%=cate.getCategoryName()%></option>
								<%
									}
								%>
							</select>
						</div>
						<div class="form-group col-md-1 fill">(*)</div>
					</div>
					<div class="form-row">
						<div class="form-group col-md-3"></div>
						<div class="form-group col-md-2"><font style="color: #5b9bd5"><b>Type</b></font></div>
						<div class="form-group col-md-6">
							<select class="selectType" name="questionType">
								<option value="0">Single</option>
								<option value="1">Multi</option>
							</select>
						</div>
						<div class="form-group col-md-1 fill">(*)</div>
					</div>
					<div class="form-row">
						<div class="form-group col-md-3"></div>
						<div class="form-group col-md-2"><font style="color: #5b9bd5"><b>Status</b></font></div>
						<div class="form-group col-md-6">
							<select class="selectStatus" name="questionStatus">
								<option value="1">Display</option>
								<option value="0">Hidden</option>
							</select>
						</div>
						<div class="form-group col-md-1 fill">(*)</div>
					</div>
				</div>
				<div class="form-group col-md-4">
					<div class="form-row">
						<div class="form-group col-md-9"></div>
						<div class="form-group col-md-2">
							<button type="button" class="add" onclick="addAnswer()">Add</button>
						</div>
					</div>
					<div class="form-row an">
						<div class="form-group col-md-3"><font style="color: #5b9bd5"><b>Answer 1:</b></font></div>
						<div class="form-group col-md-5">
							<textarea class="answer" name="answer1" rows="2" cols="10"></textarea>
						</div>
						<div class="form-group col-md-1 fill">(*)</div>
						<div class="form-group col-md-2">
							<div class="form-row radio">
								<div class="form-group col-md-6">
									<input type="radio" id="true" value="1" name="statusAnswer1">
								</div>
								<div class="form-group col-md-6">True</div>

							</div>
							<div class="form-row radio">
								<div class="form-group col-md-6">
									<input type="radio" id="false" checked="checked" value="0"
										name="statusAnswer1">
								</div>
								<div class="form-group col-md-6">False</div>

							</div>
						</div>
						<div class="form-group col-md-1">
							<img onclick="removeAnswer()" class="delete_blue"
								src="<c:url value="/resources/img/delete_answer.png"/>">
						</div>
					</div>
					<div class="form-row an">
						<div class="form-group col-md-3"><font style="color: #5b9bd5"><b>Answer 2:</b></font></div>
						<div class="form-group col-md-5">
							<textarea class="answer" name="answer2" rows="2" cols="10"></textarea>
						</div>
						<div class="form-group col-md-1 fill">(*)</div>
						<div class="form-group col-md-2">
							<div class="form-row radio">
								<div class="form-group col-md-6">
									<input type="radio" id="true" value="1" name="statusAnswer2">
								</div>
								<div class="form-group col-md-6">True</div>
							</div>
							<div class="form-row radio">
								<div class="form-group col-md-6">
									<input type="radio" id="false" checked="checked" value="0"
										name="statusAnswer2">
								</div>
								<div class="form-group col-md-6">False</div>

							</div>
						</div>
						<div class="form-group col-md-1">
							<img onclick="removeAnswer()" class="delete_blue"
								src="<c:url value="/resources/img/delete_answer.png"/>">
						</div>
					</div>
					<div class="form-row an">
						<div class="form-group col-md-3"><font style="color: #5b9bd5"><b>Answer 3:</b></font></div>
						<div class="form-group col-md-5">
							<textarea class="answer" name="answer3" rows="2" cols="10"></textarea>
						</div>
						<div class="form-group col-md-1 fill">(*)</div>
						<div class="form-group col-md-2">
							<div class="form-row radio">
								<div class="form-group col-md-6">
									<input type="radio" id="true" value="1" name="statusAnswer3">
								</div>
								<div class="form-group col-md-6">True</div>

							</div>
							<div class="form-row radio">
								<div class="form-group col-md-6">
									<input type="radio" id="false" checked="checked" value="0"
										name="statusAnswer3">
								</div>
								<div class="form-group col-md-6">False</div>

							</div>
						</div>
						<div class="form-group col-md-1">
							<img onclick="removeAnswer()" class="delete_blue"
								src="<c:url value="/resources/img/delete_answer.png"/>">
						</div>
					</div>
					<div class="form-row an">
						<div class="form-group col-md-3"><font style="color: #5b9bd5"><b>Answer 4:</b></font></div>
						<div class="form-group col-md-5">
							<textarea class="answer" name="answer4" rows="2" cols="10"></textarea>
						</div>
						<div class="form-group col-md-1 fill">(*)</div>
						<div class="form-group col-md-2">
							<div class="form-row radio">
								<div class="form-group col-md-6">
									<input type="radio" id="true" value="1" name="statusAnswer4">
								</div>
								<div class="form-group col-md-6">True</div>

							</div>
							<div class="form-row radio">
								<div class="form-group col-md-6">
									<input type="radio" id="false" checked="checked" value="0"
										name="statusAnswer4">
								</div>
								<div class="form-group col-md-6">False</div>

							</div>
						</div>
						<div class="form-group col-md-1">
							<img onclick="removeAnswer()" class="delete_blue"
								src="<c:url value="/resources/img/delete_answer.png"/>">
						</div>
					</div>
				</div>
			</div>

			<div class="form-row">
				<div class="form-group col-md-8">
					<div class="form-row">
						<div class="form-group col-md-3"></div>
						<div class="form-group col-md-2"></div>
						<div class="form-group col-md-3">
							<button type="submit" name="add" onclick="return validate()"
								class="btn-blue">Add</button>
						</div>
						<div class="form-group col-md-3">
							<button type="submit" name="cancel" class="btn-blue">Cancel</button>
						</div>

					</div>
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

.btnDialog {
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
		function an() {
			errorDialog[0].style.display = "none";
		}
		function validate() {
			var text = document.getElementsByClassName("questionDetail");
			if (text[0].value === "") {
				errorContent[0].innerHTML = "Please input Question!";
				errorDialog[0].style.display = "block";
				return false;
			}
			var answer = document.getElementsByClassName("answer");
			var hien = document.getElementsByClassName("hien");
			for (var i = 0; i < hien.length; i++) {
				if (answer[i].value === "") {
					errorContent[0].innerHTML = "Please input Answer!";
					errorDialog[0].style.display = "block";
					return false;
				}
			}
			var kt = false;
			for (var i = 1; i <= 4; i++) {
				const selector = `input[name=statusAnswer` + i + `]:checked`;
				console.log(selector);
				var checkedValue = document.querySelector(selector).value;
				if (checkedValue === "1") {
					kt = "true";
					break;
				}
			}
			if (!kt) {
				errorContent[0].innerHTML = "Please check Answer";
				errorDialog[0].style.display = "block";
				return false;
			} else
				return true;
		}
	</script>
</body>
</html>