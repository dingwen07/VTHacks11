package com.example.chatroom.dto.message;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;

@Data
public class MessagesResp implements Serializable {
    ArrayList<MessageResp> message;
}
