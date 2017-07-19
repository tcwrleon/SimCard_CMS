package com.sunyard.entity.business;

/**
 * Created by LC on 2017-01-12.
 */
public class SmsRecEntity {

    private String id;
    private String simnum;
    private String content;
    private String number;
    private String time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSimnum() {
        return simnum;
    }

    public void setSimnum(String simnum) {
        this.simnum = simnum;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
