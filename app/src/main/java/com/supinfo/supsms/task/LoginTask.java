package com.supinfo.supsms.task;

import android.app.Activity;

import com.supinfo.supsms.client.dto.LoginResponse;
import com.supinfo.supsms.client.service.SupServices;
import com.supinfo.supsms.data.LoginData;

public class LoginTask extends UITask<LoginResponse>{

	private String username, password;
	
	public LoginTask(Activity context, String username, String password) {
		super(context);
		this.username = username;
		this.password = password;
	}

	@Override
	public LoginResponse executeTask() throws Exception {
		LoginResponse response = SupServices.login(username, password);
		
		if(response.getSuccess()){
			LoginData.getInstance().login(username, password);
		}
		
		return response;
	}

}
