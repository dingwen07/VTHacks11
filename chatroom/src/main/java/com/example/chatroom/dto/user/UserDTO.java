package com.example.chatroom.dto.user;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;

@Data
public class UserDTO implements Serializable {
    private String id;
    private String name;
    private ArrayList<String> chatroom;
}
