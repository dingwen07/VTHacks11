package com.example.chatroom.service;

import com.example.chatroom.dto.user.UserDTO;

public interface UserService {

    UserDTO addUser(String name);

    UserDTO getUser(String userId);

    boolean setUserName(String userId, String newUserName);

    boolean joinChatroom(String userId, String chatroomId);

    boolean leaveChatroom(String userId, String chatroomId);
}
