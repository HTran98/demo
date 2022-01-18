<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <title>Exams</title>
    <link rel="stylesheet" type="text/css"
    href="<c:url value="resources/css/styles.css"/>"/>
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

    <div>
        <div class="image">
            <img src="images/Usol.jpg" />
            <p>Challenge for change</p>
        </div>
        <div class="menu">
            <ul class="nav">
                <li class="nav-item"><a class="nav-link" href="questionManage.htm">Questions</a>
                </li>
                <li class="nav-item"><a class="nav-link" href="categoryManage.htm">Categories</a>
                </li>
                <li class="nav-item"><a class="nav-link" href="userManage.htm">Users</a></li>
                <li class="nav-item"><a class="nav-link" href="#">Exams</a></li>
            </ul>
        </div>
        <div class="text">
            <p>Exams mangement > List</p>
        </div>
        <div class="search">
            <form action="exam.htm" method="post">
                <input type="text" name="keyWords" placeholder="Typing to search">

                <span><img src="images/search_blue.png"></span>
            </form>

        </div>
        <div class="tableEX">

            <table class="table table-borderless" id="result">
                <thead>
                    <tr>
                        <tr id="null">
                            <th>No</th>
                            <th>Category ID</th>
                            <th>Category Name</th>
                            <th colspan="2">Management</th>
                        </tr>
                        
                    </tr>
                </thead>
                <tbody>
                 <c:forEach var="list" items="${listCategoy}" varStatus="loop"> 
                 <tr>
                     <td>${loop.index+1}</td>
                    <td>${lists.categoryId}</td>
                    <td>${lists.categoryName}</td>

                    <td><span> <a
                        href="#"><img
                        src="images/edit_blue.png"></a></span></td>
                        <td><span><a href="#"
                            onclick="show('${lists.categoryId}','${lists.categoryName}')"><img
                            src="images/delete_blue.png"></a></span></td>
                        </tr>



                        <div id="id01" class="modal">
                            <div class="model-bottom">
                                <div class="headerModel">
                                    <p class="headerModelP">Confirm delete</p>
                                </div>
                                <div style="text-align: center; width: 100%; height: 100px; background-color: white">
                                    <p>Do you want to delete this exam?</p>
                                    <span id="idExam" style="font-weight: bold;"></span> <input type="hidden" id="inputE">
                                </div>
                                <div style="text-align: center; width: 100%; background-color: white">
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
        <div class="page">
            <nav>
                <ul class="pagination">
                    <li class="page-item"><a class="page-link" href="#">Previous</a></li>
                    <li class="page-item"><a class="page-link" href="#">1</a></li>
                    <li class="page-item"><a class="page-link" href="#">2</a></li>
                    <li class="page-item"><a class="page-link" href="#">3</a></li>
                    <li class="page-item"><a class="page-link" href="#">Next</a></li>
                </ul>
            </nav>
        </div>
        <div class="footer">
            <p>© USOL Vietnam Co.,Ltd. All Rights Reserved.</p>

        </div>
    </div>
</body>
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
    function show(id,name) {
                //show id01
                document.getElementById('id01').style.display = "block";
                //xet gia tri cho the span khi click vao show(id)
                document.getElementById("idExam").innerHTML = id +'_'+name ;
                //xet gia tri cho input = hide bang id
                document.getElementById("inputE").value = id;
            }
            function del() {
                var id = document.getElementById("inputE").value
                window.location.href = "exam/delete.htm?examId=${list.examId}" + id;
            }
        </script>
        </html>