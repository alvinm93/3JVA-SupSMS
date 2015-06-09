package com.supinfo.supsms.client.dto;

public class LoginResponse {
	
	private Boolean success;
	private User user;
	
	public Boolean getSuccess() {
		return success;
	}
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
}
