package com.example.chatroom.dto.user;

import com.example.chatroom.dto.chatroom.ChatroomResp;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;

@Data
public class UserChatroomResp implements Serializable {
    ArrayList<ChatroomResp> chatroom;
}
