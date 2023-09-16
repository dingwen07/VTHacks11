package com.example.chatroom.dto.message;

import lombok.Data;

import java.io.Serializable;

@Data
public class MessageSendResp implements Serializable {
    private Integer messageId;
}
