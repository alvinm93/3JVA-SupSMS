package com.supinfo.supsms.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;
import com.supinfo.supsms.R;
import com.supinfo.supsms.client.dto.Contact;
import com.supinfo.supsms.data.vo.BackupSMSResult;
import com.supinfo.supsms.task.BackupContactsTask;
import com.supinfo.supsms.task.BackupSMSTask;
import com.supinfo.supsms.task.Task;

import java.util.List;

public class OperationActivity extends ActionBarActivity {

    public static final String KEY_OPERATION = "operation";
    public static final String OPERATION_BACKUP_SMS = "BackupSMS";
    public static final String OPERATION_BACKUP_CONTACTS = "BackupContacts";

    private String selectedOperation;

    private ProgressBar pgProgress;
    private TextView tvProgress;
    private TextView tvPercent;
    private TextView tvStatusFinal;
    private FloatingActionButton fab;

    private Task<?> operationTask;
    private boolean operationSuccess = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operation);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pgProgress = (ProgressBar) findViewById(R.id.pgProgress);
        tvProgress = (TextView) findViewById(R.id.tvProgress);
        tvPercent = (TextView) findViewById(R.id.tvPercent);
        tvStatusFinal = (TextView) findViewById(R.id.tvStatusFinal);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        selectedOperation = getIntent().getStringExtra(KEY_OPERATION);

        if (selectedOperation.equals(OPERATION_BACKUP_SMS)) {
            setTitle(R.string.activity_title_operation_backup_sms);
        } else if (selectedOperation.equals(OPERATION_BACKUP_CONTACTS)) {
            setTitle(R.string.activity_title_operation_backup_contacts);
        }

        fab.setVisibility(View.GONE);
        fab.hide();
        //fab.setShadow(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(operationSuccess){
                    finish();
                } else {
                    fab.hide();
                    launchOperation();
                }
            }
        });

        pgProgress.setProgress(0);
        tvStatusFinal.setText("");
        tvPercent.setText(String.format(getResources().getString(R.string.operation_ui_percent), 0));

        launchOperation();
    }



    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case android.R.id.home:
                if(operationTask != null) operationTask.cancel();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("unchecked")
    private void launchOperation() {
        switch(selectedOperation){
            case OPERATION_BACKUP_SMS:
                operationTask = new BackupSMSTask(OperationActivity.this);
                ((BackupSMSTask) operationTask).executeAsync((Task.ITaskListener<BackupSMSResult>) operationListener);
                break;
            case OPERATION_BACKUP_CONTACTS:
                operationTask = new BackupContactsTask(OperationActivity.this);
                ((BackupContactsTask) operationTask).executeAsync((Task.ITaskListener<List<Contact>>) operationListener);
                break;
        }
    }

    private Task.ITaskListener<?> operationListener = new Task.ITaskListener<Object>() {
        @Override
        public void onStart() {
            pgProgress.setProgress(0);
            tvStatusFinal.setVisibility(View.GONE);
            //fab.hide();
        }

        @Override
        public void onCancel() {

        }

        @Override
        @SuppressWarnings("unchecked")
        public void onSuccess(Object o) {
            switch(selectedOperation){
                case OPERATION_BACKUP_SMS:
                    BackupSMSResult resultSMS = (BackupSMSResult) o;

                    showResultUiElements(resultSMS.getSuccess());

                    if(resultSMS.getSuccess()) {
                        Toast.makeText(OperationActivity.this,
                                String.format(getResources().getString(R.string.operation_result_message_backup_sms),
                                        resultSMS.getInboxSMS().size() + resultSMS.getOutboxSMS().size()), Toast.LENGTH_LONG)
                                .show();
                    }
                    break;

                case OPERATION_BACKUP_CONTACTS:
                    if(o == null){
                        showResultUiElements(false);
                        break;
                    }
                    showResultUiElements(true);

                    List<Contact> resultContacts = (List<Contact>) o;
                    Toast.makeText(OperationActivity.this,
                            String.format(getResources().getString(R.string.operation_result_message_backup_contacts), resultContacts.size()),
                            Toast.LENGTH_LONG).show();

                    break;
            }
        }

        @Override
        public void onError(Exception e) {
            e.printStackTrace();
            showResultUiElements(false);
        }

        @Override
        public void onProgress(int percent, String message) {
            tvProgress.setText(message);
            pgProgress.setProgress(percent);
            tvPercent.setText(String.format(getResources().getString(R.string.operation_ui_percent), percent));
        }
    };

    private void showResultUiElements(boolean success){
        tvStatusFinal.setVisibility(View.VISIBLE);
        fab.setVisibility(View.VISIBLE);
        operationSuccess = success;
        if(success){
            tvStatusFinal.setText(R.string.operation_ui_success);
            tvStatusFinal.setTextColor(getResources().getColor(R.color.operation_ui_success));
            fab.setImageResource(R.drawable.ic_done_white_36dp);
        } else {
            tvStatusFinal.setText(R.string.operation_ui_error);
            tvStatusFinal.setTextColor(getResources().getColor(R.color.operation_ui_error));
            fab.setImageResource(R.drawable.ic_autorenew_white_36dp);
        }
        fab.show();
    }
}
