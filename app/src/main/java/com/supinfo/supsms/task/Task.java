package com.supinfo.supsms.task;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import com.supinfo.supsms.R;
import com.supinfo.supsms.application.SupApplication;

public abstract class Task<Result> {

    public int getProgressPercent() {
        return progressPercent;
    }

    public void setProgressPercent(int progressPercent) {
        this.progressPercent = progressPercent;
    }

    // Interface
		public interface ITaskListener<Result> {
			void onStart();

			void onCancel();

			void onSuccess(Result result);

			void onError(Exception e);

            void onProgress(int percent, String message);
		}

		// Task
		private AsyncTask<Void, String, Result> task;
		private ITaskListener<Result> listener;
		private Result result;
		protected Exception error;

		// Progress dialog
		private boolean errorVisible;
		private boolean progressVisible;
		private boolean progressCancelable;
		private String progressMessage;
        private int progressPercent;
		private ProgressDialog progressDialog;
		private Handler handler;
		private final Context context;

		protected Context getContext() {
			return this.context;
		}

		public void setErrorVisible(boolean errorVisible) {
			this.errorVisible = errorVisible;
		}

		public void setProgressCancelable(boolean progressCancelable) {
			this.progressCancelable = progressCancelable;
		}

		public void setProgressVisible(boolean progressVisible) {
			this.progressVisible = progressVisible;
		}

		public void setProgressMessage(String progressMessage) {
			this.progressMessage = progressMessage;
		}

		// Constructor
		public Task() {
			this(null);
		}

		public Task(Context context) {
			this.context = context;
			this.errorVisible = true;
			this.progressCancelable = true;
			this.progressVisible = true;
			this.progressMessage = SupApplication.getInstance().getApplicationContext().getString(R.string.loading_progress);
		}

		public abstract Result executeTask() throws Exception;

		public void cancel() {
			if (this.task != null) {
				this.task.cancel(true);
			}
			if (this.listener != null) {
				this.listener.onCancel();
			}
		}

		public Result execute() throws Exception {
			return executeTask();
		}

		@SuppressLint("NewApi")
		public void executeAsync(ITaskListener<Result> listener) {
			this.listener = listener;
			this.handler = new Handler(Looper.getMainLooper());

			this.handler.post(new Runnable() {
				@Override
				public void run() {
					Task.this.task = new AsyncTask<Void, String, Result>() {
						@Override
						protected Result doInBackground(Void... params) {
							try {
								Task.this.result = Task.this.execute();
								return Task.this.result;
							} catch (final Exception e) {
								e.printStackTrace();
								Task.this.error = e;
								return null;
							}
						}

						@Override
						protected void onPreExecute() {
							try {
								Task.this.fireStart();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

						@Override
						protected void onProgressUpdate(String... values) {
							try {
								if ((values != null) && (values.length > 0)) {
									Task.this.publishProgress(values[0]);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						};

						@Override
						protected void onPostExecute(Result result) {
							try {
								Task.this.fireResult();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					};

					task.execute();
				}
			});
		}

		private void fireStart() {
			this.onPreExecute();
			if (this.listener != null) {
				this.listener.onStart();
			}
		}

        private void firePercent() {
            if(this.listener != null){
                if(getContext() instanceof Activity){
                    ((Activity) getContext()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listener.onProgress(progressPercent, progressMessage);
                        }
                    });
                } else {
                    this.listener.onProgress(progressPercent, progressMessage);
                }
            }
        }

        protected void setProgress(int percent, String progressMessage){
            this.progressMessage = progressMessage;
            this.progressPercent = percent;
            publishProgress(progressMessage);
            this.firePercent();
        }

		protected void publishProgress(final String message) {
			if (!this.progressVisible || this.handler == null) {
				return;
			}

			this.handler.post(new Runnable() {
				@Override
				public void run() {
					if (Task.this.progressDialog != null) {
						Task.this.progressDialog.setMessage(message);
					}
				}
			});
		}

		private void fireError() {
			if (this.listener != null) {
				this.listener.onError(this.error);
			}
		}

		private void fireResult() {
			this.onPostExecute(this.result);
			if (this.error != null) {
				// error
				this.onError(this.error);
			} else {
				// success
				if (this.listener != null) {
					this.listener.onSuccess(this.result);
				}
			}
		}

		protected void onPreExecute() {
			this.handler.post(new Runnable() {
				@Override
				public void run() {
					if (Task.this.context != null && Task.this.progressVisible) {
						Task.this.progressDialog = new ProgressDialog(Task.this.context);
						Task.this.progressDialog.setCancelable(Task.this.progressCancelable);
						if (Task.this.progressCancelable) {
							Task.this.progressDialog.setCanceledOnTouchOutside(false);
							Task.this.progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, SupApplication.getInstance().getApplicationContext().getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									Task.this.cancel();
								}
							});
						}
						Task.this.progressDialog.setMessage(Task.this.progressMessage);
						if (!((Activity) context).isFinishing()) {
							try {
								Task.this.progressDialog.show();
							} catch (Exception exc) {
								exc.printStackTrace();
							}
						}
					}
				}
			});
		}

		protected void onPostExecute(Result result) {
			this.handler.post(new Runnable() {
				@Override
				public void run() {
					if (Task.this.progressDialog != null) {
						try {
							Task.this.progressDialog.dismiss();
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}
			});
		}

		protected void onError(Exception e) {
			if (this.errorVisible && this.context != null && !((Activity) context).isFinishing()) {
				final AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setMessage(e.getMessage());
				builder.setPositiveButton(SupApplication.getInstance().getApplicationContext().getString(android.R.string.ok), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Task.this.fireError();
					}
				});
				builder.create().show();
			} else {
				try {
					this.fireError();
				} catch (Exception exc) {
					exc.printStackTrace();
				}
			}
		}

		public boolean isCancelled() {
			return (this.task != null && this.task.isCancelled());
		}
	
}
