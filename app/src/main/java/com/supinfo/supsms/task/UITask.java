package com.supinfo.supsms.task;

import android.app.Activity;

public abstract class UITask<Result> extends Task<Result> {

	protected Activity context;

	public UITask(Activity context) {
		super(context);
		this.context = context;
		this.setProgressVisible(false);
	}

	@Override
	protected Activity getContext() {
		return this.context;
	}

	@Override
	public Result execute() throws Exception {
		return super.execute();
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected void onPostExecute(Result result) {
		super.onPostExecute(result);
	}
	
}
