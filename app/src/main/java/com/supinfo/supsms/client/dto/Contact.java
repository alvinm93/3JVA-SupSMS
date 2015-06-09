package com.supinfo.supsms.client.dto;

public class Contact {

    private Long _ID;
    private String DNAME;
    private String EMAIL;
    private String PNUM;

    public Long get_ID() {
        return _ID;
    }

    public void set_ID(Long _ID) {
        this._ID = _ID;
    }

    public String getDNAME() {
        return DNAME;
    }

    public void setDNAME(String DNAME) {
        this.DNAME = DNAME;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }

    public String getPNUM() {
        return PNUM;
    }

    public void setPNUM(String PNUM) {
        this.PNUM = PNUM;
    }
}
