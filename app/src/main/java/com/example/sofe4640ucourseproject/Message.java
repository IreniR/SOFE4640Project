package com.example.sofe4640ucourseproject;

import java.util.Date;

public class Message {
    String message,senderId;
    Date timestamp;

    public Message(){}

    public Message(String message, String sender, Date timestamp){
        this.message = message;
        this.senderId = sender;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
