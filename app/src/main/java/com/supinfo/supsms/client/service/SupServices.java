package com.supinfo.supsms.client.service;

import java.util.List;

import com.google.gson.Gson;
import com.supinfo.supsms.client.dto.Contact;
import com.supinfo.supsms.client.dto.ContactListRequest;
import com.supinfo.supsms.client.dto.LoginResponse;
import com.supinfo.supsms.client.dto.SMSBox;
import com.supinfo.supsms.client.dto.SimpleResponse;
import com.supinfo.supsms.client.dto.SmsListRequest;
import com.supinfo.supsms.client.http.SupPostFormHttpClient;
import com.supinfo.supsms.data.LoginData;

public class SupServices {

	public static LoginResponse login(String username, String password) throws Exception{
		SupPostFormHttpClient<LoginResponse> client = new SupPostFormHttpClient<>(LoginResponse.class);
		client.setUrl("/API/");
		client.addFormParameter("action", "login");
		client.addFormParameter("login", username);
		client.addFormParameter("password", password);
		
		return client.execute();
	}
	
	public static SimpleResponse smsBackup(SmsListRequest sms) throws Exception{
		SupPostFormHttpClient<SimpleResponse> client = new SupPostFormHttpClient<>(SimpleResponse.class);
		Gson gson = new Gson();
		
		client.setUrl("/API/");
		client.addFormParameter("action", "backupsms");
		client.addFormParameter("box", sms.getBox().name());
        String smsJson = gson.toJson(sms);
		client.addFormParameter("sms", smsJson);
        client.addFormParameter("login", LoginData.getInstance().getUsername());
		client.addFormParameter("password", LoginData.getInstance().getPassword());
		
		return client.execute();
	}
	
	public static SimpleResponse contactsBackup(List<Contact> contacts) throws Exception{
		SupPostFormHttpClient<SimpleResponse> client = new SupPostFormHttpClient<>(SimpleResponse.class);
		Gson gson = new Gson();

        ContactListRequest contactsRequest = new ContactListRequest();
        contactsRequest.setContacts(contacts);
		
		client.setUrl("/API/");
		client.addFormParameter("action", "backupcontacts");
        String contactsJson = gson.toJson(contactsRequest);
        client.addFormParameter("contacts", contactsJson);
		client.addFormParameter("login", LoginData.getInstance().getUsername());
		client.addFormParameter("password", LoginData.getInstance().getPassword());
		
		return client.execute();
	}
}
