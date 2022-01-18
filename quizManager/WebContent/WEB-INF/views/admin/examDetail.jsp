<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<title>Exams</title>
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
	integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh"
	crossorigin="anonymous">

<link
	href="https://maxcdn.bootstrapcdn.com/font-awesome/4.6.3/css/font-awesome.min.css"
	rel="stylesheet">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>


</head>
</head>
<body>

	<%@ include file="layout/header.jsp"%>
	<div class="container">

		<div class="form-row info">
			<div class="form-group col-md-3">
				<a href="#">Exams mangement </a> <font style="color: white">&nbsp;>&nbsp;<a /></font><a
					href="#">${examDetail.examId}</a>
			</div>
		</div>


		<div class="row" id="examDetail">
			<div class="col-md-3" id="info">
				<form>
					<table class="table table-borderless" id="ifoDetail">
						<thead>
							<tr>
								<th colspan="2" style="text-align: center">Candidate Information</th>
							</tr>
							<tr>

								<th>Exam ID</th>
								<td scope="col">${examDetail.examId}</td>
							</tr>
							<tr>
								<th scope="col">Full name</th>
								<td>${examDetail.fullName}</td>
							</tr>
							<tr>
								<th scope="col">Email</th>
								<td>${examDetail.email}</td>
							</tr>
							<tr>
								<th scope="col">Phone</th>
								<td>${examDetail.phoneNumber}</td>
							</tr>
							<tr>
								<th scope="col">Exam Date</th>
								<td>${examDetail.examDate}</td>

							</tr>
							<tr>
								<th scope="col">Result</th>
								<td>(${resultAll}/30)</td>
							</tr>
							<tr>
								<th scope="col">Status</th>
								<td><c:if test="${resultAll >= 15}"><font style="color: green;">Passed</font></c:if><c:if test="${resultAll < 15}"><font style="color: red;">Failed</font></c:if></td>
							</tr>
						</thead>
					</table>
					<dev class="btDelete">
					<button type="button"
						onclick="show('${examDetail.examId}','${examDetail.fullName}')">Delete</button>
					</dev>
					<div id="id01" class="modal">
						<div class="model-bottom">
							<div class="headerModel">
								<p class="headerModelP">Confirm delete</p>
							</div>
							<div
								style="text-align: center; width: 100%; height: 100px; background-color: white">
								<p>Do you want to delete this exam?</p>
								<span id="idExam" style="font-weight: bold;"></span> <input
									type="hidden" id="inputE">
							</div>
							<div style="text-align: center; width: 100%">
								<div class="" style="float: left; width: 40%;">
									<div class="okBtn">
										<a onclick="del()" href="#" style="color: white">OK</a>
									</div>
								</div>
								<div class="" style="float: left; width: 40%">
									<div class="okBtn">
										<a style="color: white"
											onclick="document.getElementById('id01').style.display='none'">Cancel</a>
									</div>
								</div>
							</div>
						</div>
					</div>
				</form>
			</div>
			<div class="col-md-7" id="r01">

				<div id="resultE">
					<form>
						<p>
							Result Detail
							<h7>(${resultAll}/30)</h7>
						</p>

					</form>
				</div>
				<div class="elective">
					<form>
						<p>
							Elective section :
							<c:if test="${examDetail.categoryName !='SQL'}">
								<h7>${examDetail.categoryName}(${resultAll-resultSQL}/20)</h7>
							</c:if>
						</p>
					</form>

				</div>
				<form class="ex3">
					<table class="table table-borderless" id="tableElective">
						<thead>
							<tr>
								<th scope="col">No</th>
								<th scope="col">Correct</th>
								<th scope="col">Answer</th>
								<th scope="col">Result</th>
							</tr>
						</thead>
						<tbody>
						<%int dem=0; %>
							<c:forEach items="${listAnswers}" var="lists" varStatus="loop">
								<tr>
								<%dem++; %>
									<td>${loop.index+1}</td>
									<td>${lists.answer}</td>
									<td>${lists.correct}</td>
									
									<c:if test="${lists.answer == lists.correct}"><td id="result1Exam">Correct</td></c:if>
									<c:if test="${lists.answer != lists.correct}"><td id="result1Exam">Incorrect</td></c:if>
									
								</tr>
								<%if(dem==20) break; %>
							</c:forEach>


						</tbody>
					</table>
				</form>
				<div class="elective">
					<form>
						<p>Required section : SQL (${resultSQL}/10) </p>
					</form>

				</div>
				<form class="ex3">
					<table class="table table-borderless" id="tableElective">
						<thead>
							<tr>
								<th scope="col">No</th>
								<th scope="col">Correct</th>
								<th scope="col">Answer</th>
								<th scope="col">Result</th>
								
							</tr>
						</thead>
						<tbody>
						<%int dem2 = 0; %>
							<c:forEach items="${listAnswers}" var="lists" varStatus="loop">
							<%dem2++; if(dem2>20) { %>
								<tr>
									<td>${loop.index-19}</td>
									<td>${lists.answer}</td>
									<td>${lists.correct}</td>
									
									<c:if test="${lists.answer == lists.correct}"><td id="result1Exam">Correct</td></c:if>
									<c:if test="${lists.answer != lists.correct}"><td id="result1Exam">Incorrect</td></c:if>
								</tr>
							<% } %>
							</c:forEach>

						</tbody>
					</table>
				</form>
			</div>
		</div>
		<div class="footer">
			<p>© USOL Vietnam Co.,Ltd. All Rights Reserved.</p>

		</div>

	</div>

	<%@ include file="layout/footer.jsp"%>
</body>
<script type="text/javascript">
	var id = document.getElementsByTagName("TD");

	var it;
	for (it = 0; it < id.length; it++) {
		if (id[it].innerHTML == "Correct") {
			id[it].style.color = "#91dd9e";
		}
		if (id[it].innerHTML == "Incorrect") {
			id[it].style.color = "#FE2E64";
		}
	}
</script>


<script type="text/javascript">
	function show(id, name) {
		//show id01
		document.getElementById('id01').style.display = "block";
		//xet gia tri cho the span khi click vao show(id)
		document.getElementById("idExam").innerHTML = id + ' - ' + name;
		//xet gia tri cho input = hide bang id
		document.getElementById("inputE").value = id;
	}
	function del() {
		var id = document.getElementById("inputE").value
		window.location.href = "delete.htm?examId=${list.examId}" + id;
	}
</script>
</html>