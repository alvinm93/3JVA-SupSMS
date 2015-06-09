package com.supinfo.supsms.task;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Base64;

import com.supinfo.supsms.R;
import com.supinfo.supsms.client.dto.Contact;
import com.supinfo.supsms.client.dto.SimpleResponse;
import com.supinfo.supsms.client.service.SupServices;

import java.util.ArrayList;
import java.util.List;

public class BackupContactsTask extends UITask<List<Contact>> {

    public BackupContactsTask(Activity context) {
        super(context);
    }

    @Override
    public List<Contact> executeTask() throws Exception {
        setProgress(0, getContext().getResources().getString(R.string.operation_preparing));
        List<Contact> contactList = new ArrayList<>();

        //Récupération de ls liste de contacts
        setProgress(10, getContext().getResources().getString(R.string.operation_backup_contacts_progress_retrieve));

        ContentResolver cr = getContext().getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                //Contact recuperation request
                Contact newContactObject = new Contact();

                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                newContactObject.set_ID(Long.valueOf(id));

                //on encode en base64 car certains caractères ne passent pas (serveur retourne false)
                newContactObject.setDNAME(Base64.encodeToString(name.getBytes("UTF-8"), Base64.DEFAULT));
                //newContactObject.setDNAME(name);

                //Phone number recuperation request
                if (Integer.parseInt(cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor curPhone = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (curPhone.moveToNext()) {
                        String phoneNo = curPhone.getString(curPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        newContactObject.setPNUM(phoneNo);
                    }
                    curPhone.close();
                }

                //Email recuperation request
                Cursor curEmail = cr.query(
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                        new String[]{id}, null);
                while (curEmail.moveToNext()) {
                    //to get the contact names
                    String email = curEmail.getString(curEmail.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    if (email != null && email.length() > 0) {
                        newContactObject.setEMAIL(email);
                        break;
                    }
                }
                curEmail.close();

                //On ajoute si il y a bien un numéro de téléphone ou un email
                if((newContactObject.getEMAIL() != null && newContactObject.getEMAIL().length() > 0) ||
                        (newContactObject.getPNUM() != null && newContactObject.getPNUM().length() > 0)) {
                    contactList.add(newContactObject);
                }
            }
        }

        //Envoi au serveur
        setProgress(60, getContext().getResources().getString(R.string.operation_backup_contacts_progress_sending));
        SimpleResponse serverResponse = SupServices.contactsBackup(contactList);

        setProgress(100, getContext().getResources().getString(R.string.operation_finished));
        if (serverResponse == null || !serverResponse.getSuccess()) return null;
        else return contactList;
    }
}
