package com.example.sofe4640ucourseproject;

import java.io.Serializable;

public class Message implements Serializable {
    String message,senderId, type, timestamp;

    public Message(){}

    public Message( String message, String sender, String timestamp, String type){
        this.message = message;
        this.senderId = sender;
        this.timestamp = timestamp;
        this.type = type;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
