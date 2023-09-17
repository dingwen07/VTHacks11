package com.example.chatroom.dto.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserReq implements Serializable {
    private String name;
}
