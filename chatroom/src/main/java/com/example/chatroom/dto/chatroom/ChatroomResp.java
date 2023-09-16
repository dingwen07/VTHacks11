package com.example.chatroom.dto.chatroom;

import lombok.Data;

import java.io.Serializable;

@Data
public class ChatroomResp implements Serializable {
    private String id;
    private Boolean joined;
    private String name;
    private Integer memberCount;
    private Integer lastMessageId;
}
