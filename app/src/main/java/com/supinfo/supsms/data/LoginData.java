package com.supinfo.supsms.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.supinfo.supsms.application.SupApplication;

public class LoginData {

	private SharedPreferences prefs;
	private static LoginData _instance;
	
	private static final String KEY_USERNAME = "keyLoggedUsername";
	private static final String KEY_PASSWORD = "keyLoggedPassword";
	
    public static LoginData getInstance() {
    	if(_instance == null) _instance = new LoginData();
    	return _instance;
    }
	
    //Constructor visibility
	protected LoginData() {
		prefs = SupApplication.getInstance().getApplicationContext().getSharedPreferences("com.supinfo.supsms", Context.MODE_PRIVATE);
	}
	
	public void login(String username, String password){
		SharedPreferences.Editor prefsEdit = prefs.edit();
		
		prefsEdit.putString(KEY_USERNAME, username);
		prefsEdit.putString(KEY_PASSWORD, password);
		
		prefsEdit.commit();
	}
	
	public void logout(){
		SharedPreferences.Editor prefsEdit = prefs.edit();
		
		prefsEdit.remove(KEY_USERNAME);
		prefsEdit.remove(KEY_PASSWORD);
		
		prefsEdit.commit();
	}
	
	public boolean isLogged(){
		return prefs.contains(KEY_USERNAME) && prefs.contains(KEY_PASSWORD);
	}
	
	public String getUsername(){
		return prefs.getString(KEY_USERNAME, "");
	}

	public String getPassword(){
		return prefs.getString(KEY_PASSWORD, "");
	}
	
}
