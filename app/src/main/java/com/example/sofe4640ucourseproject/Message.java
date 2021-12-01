package com.example.sofe4640ucourseproject;

import java.util.Date;

public class Message {
    String message,senderId, urlImage;
    Date timestamp;
    Boolean location;

    public Message(){}

    public Message(String message, String sender, Date timestamp, Boolean location){//{,String urlImage){
        this.message = message;
        this.senderId = sender;
        this.timestamp = timestamp;
        this.urlImage = urlImage;
        this.location = location;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
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
