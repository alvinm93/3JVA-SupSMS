package com.supinfo.supsms.data.vo;

import java.util.List;

import com.supinfo.supsms.client.dto.SMS;

public class BackupSMSResult {

	private List<SMS> inboxSMS;
    private List<SMS> outboxSMS;
	private Boolean success;

	public Boolean getSuccess() {
		return success;
	}
	public void setSuccess(Boolean success) {
		this.success = success;
	}

    public List<SMS> getInboxSMS() {
        return inboxSMS;
    }

    public void setInboxSMS(List<SMS> inboxSMS) {
        this.inboxSMS = inboxSMS;
    }

    public List<SMS> getOutboxSMS() {
        return outboxSMS;
    }

    public void setOutboxSMS(List<SMS> outboxSMS) {
        this.outboxSMS = outboxSMS;
    }
}
