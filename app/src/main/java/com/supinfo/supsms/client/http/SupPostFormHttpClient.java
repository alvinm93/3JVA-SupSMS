package com.supinfo.supsms.client.http;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicNameValuePair;

public class SupPostFormHttpClient<T> extends SupHttpClient<T> {

	private List<NameValuePair> nameValuePairs;
	
	public SupPostFormHttpClient(Type type) {
		super(type);
		nameValuePairs = new ArrayList<>();
	}

	private HttpPost request;

	@Override
	public void setUrl(String url) {
		this.request = new HttpPost(BASE_URL + url);
		this.request.setHeader("Content-Type", "application/x-www-form-urlencoded");
	}

	@Override
	protected HttpUriRequest getRequest() {
		return this.request;
	}

	@Override
	public T execute() throws Exception {
		this.request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		return super.execute();
	}
	
	public void addFormParameter(String key, String value){
		nameValuePairs.add(new BasicNameValuePair(key, value));
	}
	
}
