package com.supinfo.supsms.client.http;

import java.lang.reflect.Type;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerPNames;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.supinfo.supsms.R;
import com.supinfo.supsms.application.SupApplication;
import com.supinfo.supsms.util.Util;

public abstract class SupHttpClient<T> {
	
	protected static final String BASE_URL = "http://91.121.105.200/";

	private static final int TIME_OUT1 = 10000;
	private static final int TIME_OUT2 = 30000;
	private final DefaultHttpClient client;
	protected Gson gson;
	private final Type type;

	// Constructor
	public SupHttpClient(Type type) {
		this.type = type;

		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));

		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, "utf-8");
		params.setParameter(ConnManagerPNames.MAX_TOTAL_CONNECTIONS, 30);
		params.setParameter(ConnManagerPNames.MAX_CONNECTIONS_PER_ROUTE,
				new ConnPerRouteBean(30));
		params.setParameter(HttpProtocolParams.USE_EXPECT_CONTINUE, false);
		HttpConnectionParams.setConnectionTimeout(params, TIME_OUT1);
		HttpConnectionParams.setSoTimeout(params, TIME_OUT2);

		ClientConnectionManager cm = new SingleClientConnManager(params,
				schemeRegistry);
		this.client = new DefaultHttpClient(cm, params);

		this.gson = new GsonBuilder().create();
	}

	public abstract void setUrl(String url);

	protected abstract HttpUriRequest getRequest();

	@SuppressWarnings("unchecked")
	public T execute() throws Exception {
		if (!Util.isOnline()) {
			throw new Exception(SupApplication.getInstance().getApplicationContext().getString(R.string.error_offline));
		}

		HttpResponse response;

		response = this.client.execute(this.getRequest());

		String responseString = null;

		if (response.getEntity() != null) {
			responseString = EntityUtils.toString(response.getEntity());
		}

		if (responseString == null) {
			return null;
		} else {

			if (this.type.equals(String.class)) {
				return (T) responseString;
			} else {
				return this.gson.fromJson(responseString, this.type);
			}

		}

	}

}
