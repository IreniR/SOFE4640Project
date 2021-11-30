package com.example.sofe4640ucourseproject;

public class Message {
    String message,senderId;
    int timestamp;
    public Message(){}

    public Message(String message, String sender){
        this.message = message;
        this.senderId = sender;
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

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }
}
