package com.supinfo.supsms.task;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.util.Base64;

import com.supinfo.supsms.R;
import com.supinfo.supsms.client.dto.SMS;
import com.supinfo.supsms.client.dto.SMSBox;
import com.supinfo.supsms.client.dto.SimpleResponse;
import com.supinfo.supsms.client.dto.SmsListRequest;
import com.supinfo.supsms.client.service.SupServices;
import com.supinfo.supsms.data.vo.BackupSMSResult;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BackupSMSTask extends UITask<BackupSMSResult> {

    private static final String COLUMN_SMS_ID = "_id";
    private static final String COLUMN_SMS_THREAD_ID = "thread_id";
    private static final String COLUMN_SMS_ADDRESS = "address";
    private static final String COLUMN_SMS_DATE = "date";
    private static final String COLUMN_SMS_BODY = "body";

	public BackupSMSTask(Activity context) {
		super(context);
	}

	@Override
	public BackupSMSResult executeTask() throws Exception {
        setProgress(0, getContext().getResources().getString(R.string.operation_preparing));

        BackupSMSResult resultObj = new BackupSMSResult();
        List<Long> inboxSmsIDs = new ArrayList<>();

        //INBOX
        setProgress(10, getContext().getResources().getString(R.string.operation_backup_sms_progress_retrieve_inbox));

        SmsListRequest inboxRequest = new SmsListRequest();
        inboxRequest.setBox(SMSBox.inbox);
        inboxRequest.setSMS(new ArrayList<SMS>());

        //Récupération depuis l'appareil
        Cursor cursorInbox = getContext().getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);
        if(cursorInbox.getCount() > 0){
            cursorInbox.moveToFirst();
            do{
                SMS inboxSMS = new SMS();
                Long smsId = cursorInbox.getLong(cursorInbox.getColumnIndex(COLUMN_SMS_ID));
                inboxSmsIDs.add(smsId);

                inboxSMS.set_id(smsId);
                inboxSMS.setAddress(cursorInbox.getString(cursorInbox.getColumnIndex(COLUMN_SMS_ADDRESS)));
                inboxSMS.setBody(cursorInbox.getString(cursorInbox.getColumnIndex(COLUMN_SMS_BODY)));
                inboxSMS.setBox(SMSBox.inbox.name());
                inboxSMS.setDate(new Date(Long.parseLong(cursorInbox.getString(cursorInbox.getColumnIndex(COLUMN_SMS_DATE)))));
                inboxSMS.setThread_id(Integer.getInteger(cursorInbox.getString(cursorInbox.getColumnIndex(COLUMN_SMS_THREAD_ID))));

                inboxRequest.getSMS().add(inboxSMS);
            }while(cursorInbox.moveToNext());
        }

        resultObj.setInboxSMS(inboxRequest.getSMS());

        //OUTBOX
        setProgress(40, getContext().getResources().getString(R.string.operation_backup_sms_progress_retrieve_outbox));

        SmsListRequest outboxRequest = new SmsListRequest();
        outboxRequest.setBox(SMSBox.sent);
        outboxRequest.setSMS(new ArrayList<SMS>());

        //Récupération depuis l'appareil
        Cursor cursorAll = getContext().getContentResolver().query(Uri.parse("content://sms/"), new String[] { COLUMN_SMS_ID, COLUMN_SMS_ADDRESS,
                COLUMN_SMS_BODY, COLUMN_SMS_DATE, COLUMN_SMS_THREAD_ID }, null, null, null);
        if(cursorAll.getCount() > 0){
            cursorAll.moveToFirst();
            do{
                SMS outboxSMS = new SMS();
                Long smsId = cursorAll.getLong(cursorInbox.getColumnIndex(COLUMN_SMS_ID));

                if(inboxSmsIDs.contains(smsId)) break;

                outboxSMS.set_id(smsId);
                outboxSMS.setAddress(cursorAll.getString(cursorAll.getColumnIndex(COLUMN_SMS_ADDRESS)));

                String body = cursorAll.getString(cursorAll.getColumnIndex(COLUMN_SMS_BODY));
                byte[] bodyData = body.getBytes("UTF-8");
                String bodyBase64 = Base64.encodeToString(bodyData, Base64.DEFAULT);
                outboxSMS.setBody(bodyBase64);

                outboxSMS.setBox(SMSBox.inbox.name());
                outboxSMS.setDate(new Date(Long.parseLong(cursorAll.getString(cursorAll.getColumnIndex(COLUMN_SMS_DATE)))));
                outboxSMS.setThread_id(Integer.getInteger(cursorAll.getString(cursorAll.getColumnIndex(COLUMN_SMS_THREAD_ID))));

                outboxRequest.getSMS().add(outboxSMS);
            }while(cursorAll.moveToNext());

            cursorInbox.close();
            cursorAll.close();
        }

        resultObj.setOutboxSMS(outboxRequest.getSMS());

        //Envoi des données
        setProgress(65, getContext().getResources().getString(R.string.operation_backup_sms_progress_sending_inbox));
        SimpleResponse inboxResponse = SupServices.smsBackup(inboxRequest);

        setProgress(80, getContext().getResources().getString(R.string.operation_backup_sms_progress_sending_outbox));
        SimpleResponse outboxResponse = SupServices.smsBackup(outboxRequest);

        setProgress(99, getContext().getResources().getString(R.string.operation_finalizing));

        resultObj.setSuccess(outboxResponse != null && outboxResponse.getSuccess());

        setProgress(100, getContext().getResources().getString(R.string.operation_finished));
		return resultObj;
	}

}
