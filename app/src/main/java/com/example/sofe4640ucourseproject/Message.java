package com.example.sofe4640ucourseproject;

import java.util.Date;

public class Message {
    String message,senderId, urlImage;
    Date timestamp;
    boolean isLocation;
    String videoPath;

    public Message(){}

    public Message(String message, String sender, Date timestamp, String location, String videoPath){
        this.message = message;
        this.senderId = sender;
        this.timestamp = timestamp;
//        this.urlImage = urlImage;
        if (location.equals("true")) {
            this.isLocation = true;
        } else {
            this.isLocation = false;
        }
        this.videoPath = videoPath;
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

    public boolean getLocationBoolaen() {
        return this.isLocation;
    }

    public String getVideoPath() {
        return this.videoPath;
    }
}
