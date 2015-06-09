package com.supinfo.supsms.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.transition.Explode;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.supinfo.supsms.R;
import com.supinfo.supsms.client.dto.LoginResponse;
import com.supinfo.supsms.task.LoginTask;
import com.supinfo.supsms.task.Task.ITaskListener;

public class LoginActivity extends ActionBarActivity {

	private ProgressBar pgLoad;
	private Button btLogin;
	private EditText etUsername;
	private EditText etPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setEnterTransition(new Explode());
        }

		setContentView(R.layout.activity_login);

		pgLoad = (ProgressBar) findViewById(R.id.pgLoad);
		btLogin = (Button) findViewById(R.id.btLogin);
		etUsername = (EditText) findViewById(R.id.etUsername);
		etPassword = (EditText) findViewById(R.id.etPassword);

		displayLoadingView(false);

		btLogin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				doLogin();
			}
		});
		
		etUsername.setOnEditorActionListener(imeActionListner);
		etPassword.setOnEditorActionListener(imeActionListner);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	private void displayLoadingView(boolean loading) {
		if (loading) {
			pgLoad.setVisibility(View.VISIBLE);
			btLogin.setVisibility(View.GONE);
		} else {
			pgLoad.setVisibility(View.GONE);
			btLogin.setVisibility(View.VISIBLE);
		}

		etUsername.setEnabled(!loading);
		etPassword.setEnabled(!loading);
	}

	private void doLogin() {
		// form validation
		String username = etUsername.getText().toString();
		String password = etPassword.getText().toString();
		if (username.length() == 0 || password.length() == 0) {
			Toast.makeText(this, R.string.error_fill_all_field,
					Toast.LENGTH_SHORT).show();
			return;
		}

		// Login request
		LoginTask loginTask = new LoginTask(this, username, password);
		loginTask.executeAsync(new ITaskListener<LoginResponse>() {

			@Override
			public void onStart() {
				displayLoadingView(true);
			}

			@Override
			public void onCancel() {
				displayLoadingView(false);
			}

			@Override
			public void onSuccess(LoginResponse result) {
				displayLoadingView(false);
				
				if(!result.getSuccess()){
					Toast.makeText(LoginActivity.this, R.string.error_login_incorrect, Toast.LENGTH_SHORT).show();
					return;
				}
				
				LoginActivity.this.startActivity(new Intent(LoginActivity.this,
						MainActivity.class));
                finish();
			}

			@Override
			public void onError(Exception e) {
				displayLoadingView(false);
				e.printStackTrace();
				Toast.makeText(LoginActivity.this, R.string.error_occured,
						Toast.LENGTH_SHORT).show();
			}

            @Override
            public void onProgress(int percent, String message) {

            }

        });
	}

	private OnEditorActionListener imeActionListner = new OnEditorActionListener() {

		@Override
		public boolean onEditorAction(TextView view, int actionId,
				KeyEvent event) {
			if (actionId == EditorInfo.IME_ACTION_NEXT) {
				etPassword.requestFocus();
				return true;
			} else if (actionId == EditorInfo.IME_ACTION_DONE) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(etPassword.getWindowToken(), 0);
				doLogin();
				return true;
			}

			return false;
		}

	};
}
