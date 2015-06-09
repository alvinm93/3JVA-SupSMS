package com.supinfo.supsms.client.dto;

import java.util.Date;

public class SMS {
	private String body;
	private String box;
	private Date date;
	private Long _id;
	private String address;
	private Integer thread_id;

	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getBox() {
		return box;
	}
	public void setBox(String box) {
		this.box = box;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Long get_id() {
		return _id;
	}
	public void set_id(Long _id) {
		this._id = _id;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Integer getThread_id() {
		return thread_id;
	}
	public void setThread_id(Integer thread_id) {
		this.thread_id = thread_id;
	}
	
	
}
