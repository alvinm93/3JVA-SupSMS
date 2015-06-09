package com.supinfo.supsms.client.dto;

import java.util.List;

public class ContactListRequest {

    private List<Contact> contacts;

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }
}
