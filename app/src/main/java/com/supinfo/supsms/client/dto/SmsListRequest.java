package com.supinfo.supsms.client.dto;

import java.util.List;

public class SmsListRequest {
	private List<SMS> SMS;
    private SMSBox box;

	public List<SMS> getSMS() {
		return SMS;
	}

	public void setSMS(List<SMS> sms) {
		SMS = sms;
	}

    public SMSBox getBox() {
        return box;
    }

    public void setBox(SMSBox box) {
        this.box = box;
    }
}
