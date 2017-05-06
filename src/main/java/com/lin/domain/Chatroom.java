package com.lin.domain;

import java.util.Date;

public class Chatroom {
    private Integer id;

    private String anchorname;

    private String message;

    private String sender;

    private Date sendtime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAnchorname() {
        return anchorname;
    }

    public void setAnchorname(String anchorname) {
        this.anchorname = anchorname == null ? null : anchorname.trim();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message == null ? null : message.trim();
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender == null ? null : sender.trim();

    }

    public Date getSendtime() {
        return sendtime;
    }

    public void setSendtime(Date sendtime) {
        this.sendtime = sendtime;
    }
}