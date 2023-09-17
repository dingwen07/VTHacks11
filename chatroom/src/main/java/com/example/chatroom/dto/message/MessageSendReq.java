package com.example.chatroom.dto.message;

import com.example.chatroom.enums.MessageTypeEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class MessageSendReq implements Serializable {
    @JsonProperty("user_id")
    private String userId;
    private String message;
    private MessageTypeEnum messageType;
}
