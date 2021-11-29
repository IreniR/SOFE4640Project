package com.example.sofe4640ucourseproject;

public class Message {
    String message,senderId, timestamp;

    public Message(){}

    public Message(String message, String sender, String timestamp){
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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
