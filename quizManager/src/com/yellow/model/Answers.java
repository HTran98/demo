package com.yellow.model;

public class Answers {
private String examDetailId;
private String examId;
private String questionId;
private String answerSelected;
private String answerDetail;
private String answerResult;

public Answers() {
	super();
}

public Answers(String examDetailId, String examId, String questionId, String answerSelected, String answerDetail,
		String answerResult) {
	super();
	this.examDetailId = examDetailId;
	this.examId = examId;
	this.questionId = questionId;
	this.answerSelected = answerSelected;
	this.answerDetail = answerDetail;
	this.answerResult = answerResult;
}

public String getExamDetailId() {
	return examDetailId;
}

public void setExamDetailId(String examDetailId) {
	this.examDetailId = examDetailId;
}

public String getExamId() {
	return examId;
}

public void setExamId(String examId) {
	this.examId = examId;
}

public String getQuestionId() {
	return questionId;
}

public void setQuestionId(String questionId) {
	this.questionId = questionId;
}

public String getAnswerSelected() {
	return answerSelected;
}

public void setAnswerSelected(String answerSelected) {
	this.answerSelected = answerSelected;
}

public String getAnswerDetail() {
	return answerDetail;
}

public void setAnswerDetail(String answerDetail) {
	this.answerDetail = answerDetail;
}

public String getAnswerResult() {
	return answerResult;
}

public void setAnswerResult(String answerResult) {
	this.answerResult = answerResult;
}



}
