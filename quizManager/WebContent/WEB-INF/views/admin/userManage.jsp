<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<title>User</title>

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
				<a href="#">User management</a> <font style="color: white">&nbsp;>&nbsp;<a /></font><a
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
					<form class="formSearch" action="user.htm" method="post">
						<input type="text" name="uKeyWords" placeholder="Typing to search">

						<button type="submit"> <span><img class="icon-search"
							src="<c:url value="/resources/img/search_blue.png" />"></span></button>
					</form>

				</div>
			</div>


		</div>


		<div class="tableEX scroll" id="jar">

			<table class="table table-borderless" id="tbUser">
				<thead>
					<tr>
					<tr id="null">
						<th>No</th>
						<th>User Name</th>
						<th>Full Name</th>
						<th colspan="2">Management</th>
					</tr>
				</thead>
				<tbody id="myTable">
					<c:forEach var="lists" items="${listUser}" varStatus="loop">
						<c:choose>
							<c:when test="${loop.index %2 ==0}">
								<tr class="content" style="background-color: #ddebf7">
									<td>${loop.index+1}</td>
									<td><a href="update-user.htm?userId=${lists.userId}">${lists.userName}</a></td>
									<td>${lists.fullName }</td>
									<td><span> <a
											href="update-user.htm?userId=${lists.userId}"><img
												src="<c:url value="/resources/img/edit_blue.png" />"></a></span></td>
									<td><span><a href="#"
											onclick="show('${lists.userId}','${lists.userName}')"><img
												src="<c:url value="/resources/img/delete_blue.png" />"></a></span></td>
								</tr>


							</c:when>
							<c:when test="${loop.index %2 !=0}">
								<tr class="content" style="background-color: white">
									<td>${loop.index+1}</td>
									<td><a href="update-user.htm?userId=${lists.userId}">${lists.userName}</a></td>
									<td>${lists.fullName }</td>
									<td><span> <a
											href="update-user.htm?userId=${lists.userId}"><img
												src="<c:url value="/resources/img/edit_blue.png" />"></a></span></td>
									<td><span><a href="#"
											onclick="show('${lists.userId}','${lists.userName}')"><img
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
function addNew() {
	
	window.location.href = "add-user.htm";
}
</script>
<script type="text/javascript">
	var result = document.getElementById('resultExam').innerHTML;
	if (result == "Passed") {
		$('#resultExam').css("color", "#91dd9e");
		console.log(result);
	} else {
		$("#resultExam").css({
			'color' : '#ff7e8'
		});
		console.log(result);

	}
</script>
<script type="text/javascript">
	function show(id, name) {
		//show id01
		document.getElementById('id01').style.display = "block";
		//xet gia tri cho the span khi click vao show(id)
		document.getElementById("idExam").innerHTML = id + '_' + name;
		//xet gia tri cho input = hide bang id
		document.getElementById("inputE").value = id;
	}
	function del() {
		var id = document.getElementById("inputE").value;
		window.location.href = "delete-user.htm?userId=${lists.userId}" + id;
}
</script>
<script
	src='https://cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js'></script>
<script
	src='https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.0.0-beta/js/bootstrap.min.js'></script>
<script src="<c:url value="/resources/js/script.js" />"></script>
</html>