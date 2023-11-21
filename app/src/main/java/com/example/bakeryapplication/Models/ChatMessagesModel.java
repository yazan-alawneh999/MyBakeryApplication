package com.example.bakeryapplication.Models;

import java.sql.Timestamp;

public class ChatMessagesModel {
    private String senderId, message;
    private com.google.firebase.Timestamp sendTime;

    public ChatMessagesModel(String senderId, String message, com.google.firebase.Timestamp sendTime) {
        this.senderId = senderId;
        this.message = message;
        this.sendTime = sendTime;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public com.google.firebase.Timestamp getSendTime() {
        return sendTime;
    }

    public void setSendTime(com.google.firebase.Timestamp sendTime) {
        this.sendTime = sendTime;
    }

    public ChatMessagesModel() {
    }
}
