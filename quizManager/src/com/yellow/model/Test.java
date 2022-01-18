package com.yellow.model;

import java.util.ArrayList;
import java.util.List;


public class Test {
	public String questionId;
	public String questionDetail;
	public int questionType;
	public int questionStatus;
	public int questionDeleteStatus;
	public String categoryId;
	public List<Answer> listAnwser = new ArrayList<Answer>();
	public String getQuestionId() {
		return questionId;
	}
	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}
	public String getQuestionDetail() {
		return questionDetail;
	}
	public void setQuestionDetail(String questionDetail) {
		this.questionDetail = questionDetail;
	}
	public int getQuestionType() {
		return questionType;
	}
	public void setQuestionType(int questionType) {
		this.questionType = questionType;
	}
	public int getQuestionStatus() {
		return questionStatus;
	}
	public void setQuestionStatus(int questionStatus) {
		this.questionStatus = questionStatus;
	}
	public int getQuestionDeleteStatus() {
		return questionDeleteStatus;
	}
	public void setQuestionDeleteStatus(int questionDeleteStatus) {
		this.questionDeleteStatus = questionDeleteStatus;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public List<Answer> getListAnwser() {
		return listAnwser;
	}
	public void setListAnwser(List<Answer> listAnwser) {
		this.listAnwser = listAnwser;
	}
	
}
