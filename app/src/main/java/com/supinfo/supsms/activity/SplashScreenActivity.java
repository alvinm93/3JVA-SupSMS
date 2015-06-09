package com.supinfo.supsms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.supinfo.supsms.R;
import com.supinfo.supsms.data.LoginData;

public class SplashScreenActivity extends ActionBarActivity {

	private static final long SPLASH_DISPLAY_TIME = 1000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splashscreen);

        getSupportActionBar().hide();
	}

	@Override
	protected void onStart() {
		super.onStart();
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(SPLASH_DISPLAY_TIME);
				} catch (InterruptedException e) {
					e.printStackTrace();
					finish();
				}
				
				if(LoginData.getInstance().isLogged()){
					SplashScreenActivity.this.startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
				} else {
					SplashScreenActivity.this.startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
				}

                SplashScreenActivity.this.finish();
			}
		}).start();

	}

	
}
