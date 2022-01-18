<%@page import="java.util.ArrayList"%>
<%@page import="com.yellow.model.Test"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	String list = request.getAttribute("strListQuestion").toString();
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>Screen test phần thi tự chọn (JAVA, C#, C, PYTHON…)</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="<c:url value="/resources/styles/style2.css" />">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"
	integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm"
	crossorigin="anonymous">
</head>
<body>
	<script>
		window.onload = setInterval(clock, 0);
		function clock() {
			var d = new Date();
			var date = d.getDate();
			var year = d.getFullYear();
			var month = d.getMonth();
			var monthArr = [ "01", "02", "03", "04", "05", "06", "07", "08",
					"09", "10", "11", "12" ];
			month = monthArr[month];
			document.getElementById("date").innerHTML = "<b>Date</b>: " + year + "/"
					+ month + "/" + date;
			document.getElementById("dateForm").value = "<b>Date</b>: " + year + "/"
			+ month + "/" + date;
		}
		var distance = 1800000;
		// Update the count down every 1 second
		var x = setInterval(function() {
			// Time calculations for days, hours, minutes and seconds
			var minutes = Math.floor((distance % (1000 * 60 * 60))
					/ (1000 * 60));
			var seconds = Math.floor((distance % (1000 * 60)) / 1000);

			// Output the result in an element with id="demo"
			document.getElementById("time").innerHTML = "<b>Time</b>: " + minutes
					+ ": " + seconds;
			document.getElementById("timeForm").value = "<b>Time</b>: " + minutes
			+ ": " + seconds;

			distance = distance - 1000;
			if (distance == 0) {
				document.getElementById("time").innerHTML = "<b>Time</b>: " + 0 + ": "
						+ 0;
				document.forms["test"].submit();
				clearInterval(x);
			}

		}, 1000);
	</script>
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
								<div class="row2" id="date"></div>
								<div class="row2" id="time"></div>
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
				<form id="test" name="test" action="save-exam-detail.htm"
					method="post">
					<input name="examId" value="${examId }" type="hidden" />
					<input id="timeForm" name="time" value="" type="hidden" />
					<input id="dateForm" name="date" value="" type="hidden" />
					<div class="quiz-container">
						<div id="quiz"></div>
					</div>
					<div class="form-row col-md-12">
						<div class="form-group col-md-2"></div>
						<div class="form-group col-md-2"></div>
						<div class="form-group col-md-2">
							<button id="previous" class="btnSub" onclick="return false">Prev</button>
						</div>
						<div class="form-group col-md-2">
							<button id="next" class="btnSub" onclick="return false">Next</button>
							<button id="submit2" class="btnSub" onclick="return formSubmit()"
								style="background: rgb(146, 208, 80);">Submit</button>
						</div>
					</div>
					<div class="errorDialog">
						<div class="titleDialog">Confirm</div>
						<div class="detailDialog">
							<font class="errorContent">Will not be able to change the
								result after submitting.<br>Do you want to continue?
							</font> <br>
						</div>
						<div class="btn-group">
							<button type="submit" class="btnDialog" name="btnDialog">OK</button>
							<button type="button" onclick="return an()" class="btnDialog"
								name="btnDialog">Cancel</button>
						</div>
					</div>
				</form>

				<script>
					function formSubmit() {
						document.getElementsByClassName("errorDialog")[0].style.display = "block";
						return false;
					}
					function an() {
						document.getElementsByClassName("errorDialog")[0].style.display = "none";
						return false;
					}
				</script>
				<style>
.errorDialog {
	z-index: 3;
	top: 0;
	bottom: 0;
	left: 0;
	right: 0;
	display: none;
	margin: auto;
	position: absolute;
	width: 350px;
	height: 170px;
	border: 1px solid black;
	text-align: center;
	background: white;
}

.titleDialog {
	width: 100%;
	height: 30px;
	background: #FE9A2E;
	border-bottom: 1px solid red;
}

.detailDialog {
	width: 100%;
	height: 70px;
	padding-top: 10px;
}

.btnDialog {
	height: 35px;
	width: 75px;
	margin: 20px;
	line-height: 20px;
	background: #FE9A2E;
	border: 1px solid black;
}

button {
	color: white;
	background: #5b9bd5;
}

.fill {
	color: red;
}
</style>
				<script type="text/javascript">
		(function() {
  const myQuestions = <%=list%>;

  function buildQuiz() {
    // we'll need a place to store the HTML output
    const output = [];
	
	 var dem = 0;
    // for each question...
    myQuestions.forEach((currentQuestion, questionNumber) => {
	dem++;
      // we'll want to store the list of answer choices
      const answers = [];

      // and for each available answer...
      for (letter in currentQuestion.answers) {
        // ...add an HTML radio button
        if(currentQuestion.questionType=="0"){
        	answers.push(
        	          `<div class="row col-md-12">
        					<div class="c-select col-md-2"></div>
        					<div class="result col-md-10">
        						<label>
        	            			<input type="radio" name="answer`+questionNumber+`" value="`+letter+`">
        	              			`+letter+` :  `+currentQuestion.answers[letter]+`
        	           			</label>
        	            	</div>
        	            </div>`
        	        );
        } else {
        	answers.push(
        	          `<div class="row col-md-12">
        					<div class="c-select col-md-2"></div>
        					<div class="result col-md-10">
        						<label>
        	            			<input type="checkbox" name="answer`+questionNumber+`" value="`+letter+`">
        	              			`+letter+` :  `+currentQuestion.answers[letter]+`
        	           			</label>
        	            	</div>
        	            </div>`
        	        );
        }
      }

      // add this question and its answers to the output
      output.push(
        `<div class="slide">
			<div class="name-question">Câu `+dem+`: `+currentQuestion.questionDetail+`</div>
				<div class="question-abcd">
	           		<input type="hidden" name="questionId`+questionNumber+`" value="`+currentQuestion.questionId+`">
	           		<input type="hidden" name="questionType`+questionNumber+`" value="`+currentQuestion.questionType+`">
	           	<div class="answers"> `+answers.join("")+` </div>
           	</div>
         </div>`
      );
    });

    // finally combine our output list into one string of HTML and put it on the page
    quizContainer.innerHTML = `<div class="row col-md-10">
		<div class="title" id="title"></div>`+output.join("");+`</div>`
  }
  

  function showSlide(n) {
    slides[currentSlide].classList.remove("active-slide");
    slides[n].classList.add("active-slide");
    currentSlide = n;
    
    /* if (currentSlide === 0) {
      previousButton.style.display = "none";
    } else {
      previousButton.style.display = "inline-block";
    } */
    if(currentSlide<20) {
    	document.getElementById("title").innerHTML = `Theory test : `+`<%=request.getAttribute("categoryName")%>`+` (20 questions)`;
    } else document.getElementById("title").innerHTML = `Theory test : SQL (10 questions)`;
    
    if (currentSlide === slides.length - 1) {
      nextButton.style.display = "none";
      submitButton.style.display = "inline-block";
    } else {
      nextButton.style.display = "inline-block";
      submitButton.style.display = "none";
    }
  }

  function showNextSlide() {
    showSlide(currentSlide + 1);
  }

  function showPreviousSlide() {
	  if(currentSlide>0) showSlide(currentSlide - 1);
  }

  const quizContainer = document.getElementById("quiz");
  const resultsContainer = document.getElementById("results");
  const submitButton = document.getElementById("submit2");

  buildQuiz();

  const previousButton = document.getElementById("previous");
  const nextButton = document.getElementById("next");
  const slides = document.querySelectorAll(".slide");
  let currentSlide = 0;

  showSlide(0);

  previousButton.addEventListener("click", showPreviousSlide);
  nextButton.addEventListener("click", showNextSlide);
})();
	</script>
			</div>
		</div>
	</div>
	<div class="footer">© USOL Vietnam Co.,Ltd. All Rights Reserved.
	</div>
</body>
</html>