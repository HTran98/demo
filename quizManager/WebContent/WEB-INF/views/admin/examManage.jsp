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
<body>

	<%@ include file="layout/header.jsp"%>
	<div class="container">

		<div class="form-row info">
			<div class="form-group col-md-3">
				<a href="#">Exam management</a> <font style="color: white">&nbsp;>&nbsp;<a /></font><a
					href="#">List</a>
			</div>
		</div>
		<div class="row" id="rowEx">
		<div class="row" id="rowC">
			<div class="col-md-3">
				
			</div>
			<div class="col-md-9">
				<div class="search">
					<form class="formSearch" action="question.htm" method="post">
						<input type="text" name="keyWords" placeholder="Typing to search" style="margin-left: 50px">

						<button type="submit"> <span><img class="icon-search"
							src="<c:url value="/resources/img/search_blue.png" />"></span></button>
					</form>

				</div>
			</div>
		</div>

			



		</div>
		<div class="tableEX scroll" id="jar">
			<table class="table table-borderless" id="result">
				<thead>
					<tr>
						<th scope="col">No</th>
						<th scope="col">Exam ID</th>
						<th scope="col">Full name</th>
						<th scope="col">Email</th>
						<th scope="col">Phone</th>
						<th scope="col">Exam Date</th>
						<th scope="col">Result</th>
						<th colspan="2">Management</th>
					</tr>
				</thead>
				<tbody id="myTable">
					<c:forEach var="list" items="${listExams}" varStatus="loop">
						<c:choose>
							<c:when test="${loop.index %2 ==0}">
								<tr class="content" style="background-color: #ddebf7">
									<td>${loop.index+1}</td>
									<td><a href="exam/Detail.htm?examId=${list.examId}">${list.examId}</a></td>
									<td>${list.fullName}</td>
									<td><a href="exam/Detail.htm?examId=${list.examId}">${list.email}</a></td>
									<td>${list.phoneNumber}</td>
									<td>${list.examDate}</td>
									<td>${list.examResult}</td>

									<td><span> <a
											href="exam/Detail.htm?examId=${list.examId}"><img
												src="<c:url value="/resources/img/edit_blue.png" />"></a></span></td>
									<td><span><a href="#"
											onclick="show('${list.examId}','${list.fullName}')"><img
												src="<c:url value="/resources/img/delete_blue.png" />"></a></span></td>
								</tr>
							</c:when>
							<c:when test="${loop.index %2 !=0}">
								<tr class="content" style="background-color: white">
									<td>${loop.index+1}</td>
									<td><a href="exam/Detail.htm?examId=${list.examId}">${list.examId}</a></td>
									<td>${list.fullName}</td>
									<td><a href="exam/Detail.htm?examId=${list.examId}">${list.email}</a></td>
									<td>${list.phoneNumber}</td>
									<td>${list.examDate}</td>
									<td>${list.examResult}</td>

									<td><span> <a
											href="exam/Detail.htm?examId=${list.examId}"><img
												src="<c:url value="/resources/img/edit_blue.png" />"></a></span></td>
									<td><span><a href="#"
											onclick="show('${list.examId}','${list.fullName}')"><img
												src="<c:url value="/resources/img/delete_blue.png" />"></a></span></td>
								</tr>
							</c:when>
						</c:choose>

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
								<div
									style="text-align: center; width: 100%; background-color: white">
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
					</c:forEach>
				</tbody>
			</table>

		</div>
		<nav>
			<ul class="pagination justify-content-center pagination-sm">
			</ul>
		</nav>

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
		if (id[it].innerHTML == "Passed") {
			id[it].style.color = "#91dd9e";
		}
		if (id[it].innerHTML == "Failed") {
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
		window.location.href = "exam/delete.htm?examId=${list.examId}" + id;
	}
</script>
<script
	src='https://cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js'></script>
<script
	src='https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.0.0-beta/js/bootstrap.min.js'></script>
<script src="<c:url value="/resources/js/script.js" />"></script>

</html>