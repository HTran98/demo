<%@page import="com.yellow.model.Answer"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.yellow.model.Categories"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Update question</title>
</head>
<body>
	<%@ include file="layout/header.jsp"%>
	<div class="container">
		<div class="form-row info">
			<div class="form-group col-md-3">
				<a href="#">Question management</a> <font style="color: white">&nbsp;>&nbsp;<a /></font><a
					href="#">${question.questionId }</a>
			</div>
		</div>
		<form action="save-question.htm" method="post">
			<div class="form-row">
				<div class="form-group col-md-2"></div>
				<div class="form-group col-md-5 info">Question Information</div>
				<div class="form-group col-md-2"></div>
				<div class="form-group col-md-3 info">Answer</div>
			</div>
			<div class="form-row">
				<div class="form-group col-md-8">
					<div class="form-row">
						<div class="form-group col-md-3"></div>
						<div class="form-group col-md-2">
							<font style="color: #5b9bd5"><b>Quest ID</b></font>
						</div>
						<div class="form-group col-md-6">
							<input class="questionId" name="questionId" class="questionId"
								readonly="readonly" value="${question.questionId }" type="text">
						</div>

					</div>
					<div class="form-row">
						<div class="form-group col-md-3"></div>
						<div class="form-group col-md-2
						">
							<font style="color: #5b9bd5"><b>Text</b></font>
						</div>
						<div class="form-group col-md-6">
							<textarea class="questionDetail" name="questionDetail" rows="3"
								cols="20">${question.questionDetail }</textarea>
						</div>
						<div class="form-group col-md-1 fill">(*)</div>
					</div>
					<div class="form-row">
						<div class="form-group col-md-3"></div>
						<div class="form-group col-md-2">
							<font style="color: #5b9bd5"><b>Category</b></font>
						</div>
						<div class="form-group col-md-6">
							<select class="selectCatogory" name="categoryId">
								<%
									List<Answer> listAnswer = (ArrayList<Answer>) request.getAttribute("listAnswer");
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
						<div class="form-group col-md-2">
							<font style="color: #5b9bd5"><b>Type</b></font>
						</div>
						<div class="form-group col-md-6">
							<select class="selectType" name="type">
								<option value="0">Single</option>
								<option value="1">Multi</option>
							</select>
						</div>
						<div class="form-group col-md-1 fill">(*)</div>
					</div>
					<div class="form-row">
						<div class="form-group col-md-3"></div>
						<div class="form-group col-md-2">
							<font style="color: #5b9bd5"><b>Status</b></font>
						</div>
						<div class="form-group col-md-6">
							<select class="selectStatus" name="status">
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

					<%
						int dem = 0;
						for (Answer an : listAnswer) {

							if (an.getAnswerDeleteStatus() == 0) {
								dem++;
					%>
					<div class="form-row hien">
						<div class="form-group col-md-3">
							<font style="color: #5b9bd5"><b>Answer <%=dem%>:
							</b></font>
						</div>
						<div class="form-group col-md-5">
							<input type="hidden" value="<%=an.getAnswerId()%>"
								name="answerId<%=dem%>">
							<textarea class="answer" name="answer<%=dem%>" rows="2" cols="10"><%=an.getAnswerDetail()%></textarea>
						</div>
						<div class="form-group col-md-1 fill">(*)</div>
						<div class="form-group col-md-2">
							<div class="form-row radio">
								<div class="form-group col-md-6">
									<input type="radio" id="true" value="1"
										name="statusAnswer<%=dem%>">
								</div>
								<div class="form-group col-md-6">True</div>

							</div>
							<div class="form-row radio">
								<div class="form-group col-md-6">
									<input type="radio" id="false" checked="checked" value="0"
										name="statusAnswer<%=dem%>">
								</div>
								<div class="form-group col-md-6">False</div>

							</div>
						</div>
						<div class="form-group col-md-1">
							<img onclick="removeAnswer()" class="delete_blue"
								src="<c:url value="/resources/img/delete_answer.png"/>">
						</div>
					</div>
					<%
						}
						}
						for (int i = dem + 1; i <= 4; i++) {
					%>
					<div class="form-row an">
						<div class="form-group col-md-3">
							<font style="color: #5b9bd5"><b>Answer <%=i%>:
							</b></font>
						</div>
						<div class="form-group col-md-5">
							<input type="hidden" value="" name="answerId<%=i%>">
							<textarea class="answer" name="answer<%=i%>" rows="2" cols="10"></textarea>
						</div>
						<div class="form-group col-md-1 fill">(*)</div>
						<div class="form-group col-md-2">
							<div class="form-row radio">
								<div class="form-group col-md-6">
									<input type="radio" id="true" value="1"
										name="statusAnswer<%=i%>">
								</div>
								<div class="form-group col-md-6">True</div>

							</div>
							<div class="form-row radio">
								<div class="form-group col-md-6">
									<input type="radio" id="false" checked="checked" value="0"
										name="statusAnswer<%=i%>">
								</div>
								<div class="form-group col-md-6">False</div>

							</div>
						</div>
						<div class="form-group col-md-1">
							<img onclick="removeAnswer()" class="delete_blue"
								src="<c:url value="/resources/img/delete_answer.png"/>">
						</div>
					</div>
					<%
						}
					%>
				</div>
			</div>

			<div class="form-row">
				<div class="form-group col-md-8">
					<div class="form-row">
						<div class="form-group col-md-3"></div>
						<div class="form-group col-md-2"></div>
						<div class="form-group col-md-2">
							<button type="submit" onclick="return validate()" name="update"
								class="btn-blue">Update</button>
						</div>
						<div class="form-group col-md-2">
							<button type="submit" name="delete" class="btn-blue">Delete</button>
						</div>
						<div class="form-group col-md-2">
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

button {
	color: white;
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
				errorContent[0].innerHTML = "Please input again Question!";
				errorDialog[0].style.display = "block";
				return false;
			}
			var answer = document.getElementsByClassName("answer");
			var hien = document.getElementsByClassName("hien");
			for (var i = 0; i < hien.length; i++) {
				if (answer[i].value === "") {
					errorContent[0].innerHTML = "Please input again Answer!";
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
				errorContent[0].innerHTML = "Please check again Answer";
				errorDialog[0].style.display = "block";
				return false;
			} else
				return true;
		}
	</script>
</body>
</html>