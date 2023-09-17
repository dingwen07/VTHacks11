package com.example.chatroom.dto.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class MessageSendReq implements Serializable {
    @JsonProperty("user_id")
    private String userId;
    private String message;
}
