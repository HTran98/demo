package com.yellow.model;

public class Users {
	public String userId;
	public String userName;
	public String passWord;
	public String fullName;
	public int userDeleteStatus;
	public int role;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public int getUserDeleteStatus() {
		return userDeleteStatus;
	}
	public void setUserDeleteStatus(int userDeleteStatus) {
		this.userDeleteStatus = userDeleteStatus;
	}
	public int getRole() {
		return role;
	}
	public void setRole(int role) {
		this.role = role;
	}
	
}
