package com.supinfo.supsms.application;

import android.app.Application;

public class SupApplication extends Application {

	private static SupApplication _instance;
	
	public static SupApplication getInstance(){
		return _instance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		
		SupApplication._instance = this;
	}
	
	
}
