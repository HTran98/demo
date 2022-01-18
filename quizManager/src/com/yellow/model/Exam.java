package com.yellow.model;

public class Exam {
	private String examId;
	private String fullName;
	private String email;
	private String phoneNumber;
	private String examDate;
	private String examDeleteStatus;
	private String questionId;
	private String categoryId;
	private String categoryName;
	private String examResult;
	
	public Exam() {
		super();
	}

	public Exam(String examId, String fullName, String email, String phoneNumber, String examDate,
			String examDeleteStatus, String questionId, String categoryId, String categoryName, String examResult) {
		super();
		this.examId = examId;
		this.fullName = fullName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.examDate = examDate;
		this.examDeleteStatus = examDeleteStatus;
		this.questionId = questionId;
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		this.examResult = examResult;
	}

	public String getExamId() {
		return examId;
	}

	public void setExamId(String examId) {
		this.examId = examId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getExamDate() {
		return examDate;
	}

	public void setExamDate(String examDate) {
		this.examDate = examDate;
	}

	public String getExamDeleteStatus() {
		return examDeleteStatus;
	}

	public void setExamDeleteStatus(String examDeleteStatus) {
		this.examDeleteStatus = examDeleteStatus;
	}

	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getExamResult() {
		return examResult;
	}

	public void setExamResult(String examResult) {
		this.examResult = examResult;
	}
    
	
	
}
