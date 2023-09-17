package com.example.chatroom.dto.message;

import com.example.chatroom.enums.MessageTypeEnum;
import lombok.Data;

import java.io.Serializable;

@Data
public class MessageDTO implements Serializable {
    private String chatroom;
    private Integer id;
    private String payload;
    private MessageTypeEnum messageType;
    private String senderId;
    private String senderName;
    private Long timestamp;
}
