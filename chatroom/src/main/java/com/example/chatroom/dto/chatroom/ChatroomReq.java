package com.example.chatroom.dto.chatroom;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ChatroomReq {
    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("room_name")
    private String roomName;
}
