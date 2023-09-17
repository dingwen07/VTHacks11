package com.example.chatroom.dto.chatroom;

import lombok.Data;

import java.io.Serializable;

@Data
public class ChatroomDTO implements Serializable {
    private String id;
    private String name;
    private Integer numUsers;
}
