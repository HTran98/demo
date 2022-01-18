<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<title>Question</title>
<%-- 
<link rel="stylesheet"
	href="<c:url value="/resources/styles/styles.css" />"> --%>
</head>
<body>
	<%@ include file="layout/header.jsp"%>
	<div class="container">
		<div class="form-row info">
			<div class="form-group col-md-3">
				<a href="#">Question management</a> <font style="color: white">&nbsp;>&nbsp;<a /></font><a
					href="#">List</a>
			</div>
		</div>
		<div class="row" id="rowC">
			<div class="col-md-3">
				<button onclick="addNew()">
					Add New
				</button>
			</div>
			<div class="col-md-9">
				<div class="search">
					<form class="formSearch" action="question.htm" method="post">
						<input type="text" name="qKeyWords" placeholder="Typing to search">

						<button type="submit"> <span><img class="icon-search"
							src="<c:url value="/resources/img/search_blue.png" />"></span></button>
					</form>

				</div>
			</div>
		</div>
		<div class="tableEX scroll" id="jar">
			<table class="table table-borderless" id="tbQuestion">
				<thead>
					<tr>
						<th scope="col">No</th>
						<th scope="col">Quest ID</th>
						<th scope="col">Text</th>
						<th scope="col">Category Name</th>
						<th scope="col">Type</th>
						<th colspan="2">Management</th>

					</tr>
				</thead>
				<tbody id="myTable">
					<c:forEach var="lists" items="${listQuestion}" varStatus="loop">
						<c:choose>
							<c:when test="${loop.index %2 ==0}">
								<tr  style="background-color: #ddebf7" class="content" >
									<td>${loop.index+1}</td>
									<td><a
										href="update-question.htm?questionId=${lists.questionId}">${lists.questionId}</a></td>
									<td>${lists.questionDetail}</td>
									<c:choose>
										<c:when test="${lists.categoryId =='C0000001'}">
											<td>Java</td>
										</c:when>
										<c:when test="${lists.categoryId =='C0000002'}">
											<td>C#</td>
										</c:when>
										<c:when test="${lists.categoryId =='C0000003'}">
											<td>PYTHON</td>
										</c:when>
										<c:when test="${lists.categoryId =='C0000004'}">
											<td>C</td>
										</c:when>
										<c:when test="${lists.categoryId =='C0000005'}">
											<td>SQL</td>
										</c:when>
										<c:otherwise>
											<td>Không tồn tại</td>
										</c:otherwise>
									</c:choose>
									<c:choose>
										<c:when test="${lists.questionType ==0}">
											<td>Single</td>
										</c:when>
										<c:when test="${lists.questionType !=0}">
											<td>Multi</td>

										</c:when>
										<c:otherwise>
											<td>Không tồn tại</td>
										</c:otherwise>
									</c:choose>
									<td><span> <a
											href="update-question.htm?questionId=${lists.questionId}"><img
												src="<c:url value="/resources/img/edit_blue.png" />"></a></span></td>
									<td><span><a href="#"
											onclick="show('${lists.questionId}')"><img
												src="<c:url value="/resources/img/delete_blue.png" />"></a></span></td>
								</tr>
							</c:when>
							<c:when test="${loop.index %2 !=0}">
								<tr  style="background-color: white" class="content" >
									<td>${loop.index+1}</td>
									<td><a
										href="update-question.htm?questionId=${lists.questionId}">${lists.questionId}</a></td>
									<td>${lists.questionDetail}</td>
									<c:choose>
										<c:when test="${lists.categoryId =='C0000001'}">
											<td>Java</td>
										</c:when>
										<c:when test="${lists.categoryId =='C0000002'}">
											<td>C#</td>
										</c:when>
										<c:when test="${lists.categoryId =='C0000003'}">
											<td>PYTHON</td>
										</c:when>
										<c:when test="${lists.categoryId =='C0000004'}">
											<td>C</td>
										</c:when>
										<c:when test="${lists.categoryId =='C0000005'}">
											<td>SQL</td>
										</c:when>
										<c:otherwise>
											<td>Không tồn tại</td>
										</c:otherwise>
									</c:choose>
									<c:choose>
										<c:when test="${lists.questionType ==0}">
											<td>Single</td>
										</c:when>
										<c:when test="${lists.questionType ==1}">
											<td>Multi</td>

										</c:when>
										<c:otherwise>
											<td>Không tồn tại</td>
										</c:otherwise>
									</c:choose>
									<td><span> <a
											href="update-question.htm?questionId=${lists.questionId}"><img
												src="<c:url value="/resources/img/edit_blue.png" />"></a></span></td>
									<td><span><a 
											onclick="show('${lists.questionId}')"><img
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
									<span id="idQ" style="font-weight: bold;"></span> <input
										type="hidden" id="inputQ">
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
	</div>
	<%@ include file="layout/footer.jsp"%>
</body>
<script type="text/javascript">
function addNew() {
	
	window.location.href = "add-question.htm";
			
}
</script>
<script type="text/javascript">
	function show(id) {
		//show id01
		document.getElementById('id01').style.display = "block";
		//xet gia tri cho the span khi click vao show(id)
		document.getElementById("idQ").innerHTML = id ;
		//xet gia tri cho input = hide bang id
		document.getElementById("inputQ").value = id;
	}
	function del() {
		var id = document.getElementById("inputQ").value
		window.location.href = "delete-question.htm?questionId=${lists.questionId}" + id;
	}
</script>
<script
	src='https://cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js'></script>
<script
	src='https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.0.0-beta/js/bootstrap.min.js'></script>
<script src="<c:url value="/resources/js/script.js" />"></script>
</html>