package com.yellow.model;


public class Answer {
	public String answerId;
	public String answerDetail;
	public int answerStatus;
	public int answerDeleteStatus;
	public String questionId;
	public String getAnswerId() {
		return answerId;
	}
	public void setAnswerId(String answerId) {
		this.answerId = answerId;
	}
	public String getAnswerDetail() {
		return answerDetail;
	}
	public void setAnswerDetail(String answerDetail) {
		this.answerDetail = answerDetail;
	}
	public int getAnswerStatus() {
		return answerStatus;
	}
	public void setAnswerStatus(int answerStatus) {
		this.answerStatus = answerStatus;
	}
	public int getAnswerDeleteStatus() {
		return answerDeleteStatus;
	}
	public void setAnswerDeleteStatus(int answerDeleteStatus) {
		this.answerDeleteStatus = answerDeleteStatus;
	}
	public String getQuestionId() {
		return questionId;
	}
	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}
	
}
