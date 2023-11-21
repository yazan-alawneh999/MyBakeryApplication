package com.example.bakeryapplication.Models;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.util.List;

public class ChatRoomModel  {


    private String chatRoomId ;
    private List<String> userIds ;
    private String lastMessageSenderId;
    private String lastMessage ;

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    private com.google.firebase.Timestamp lastTimeStamp ;

    public String getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(String chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    public String getLastMessageSenderId() {
        return lastMessageSenderId;
    }

    public void setLastMessageSenderId(String lastMessageSenderId) {
        this.lastMessageSenderId = lastMessageSenderId;
    }

    public Timestamp getLastTimeStamp() {
        return lastTimeStamp;
    }

    public void setLastTimeStamp(Timestamp lastTimeStamp) {
        this.lastTimeStamp = lastTimeStamp;
    }

    public ChatRoomModel(String chatRoomId, List<String> userIds, String lastMessageSenderId, Timestamp lastTimeStamp) {
        this.chatRoomId = chatRoomId;
        this.userIds = userIds;
        this.lastMessageSenderId = lastMessageSenderId;
        this.lastTimeStamp = lastTimeStamp;
    }

    public ChatRoomModel() {
    }

}
