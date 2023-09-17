package com.example.chatroom.service.impl;

import com.example.chatroom.dto.user.UserDTO;
import com.example.chatroom.service.ChatroomService;
import com.example.chatroom.service.UserService;
import org.redisson.api.RBucket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    ChatroomService chatroomService;

    public UserDTO addUser(String name) {
        String userId = UUID.randomUUID().toString();
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userId);
        userDTO.setName(name);
        userDTO.setChatroom(new ArrayList<String>());
        RBucket<UserDTO> bucket = redissonClient.getBucket("user:" + userId);
        bucket.set(userDTO);
        return userDTO;
    }

    public UserDTO getUser(String userId) {
        RBucket<UserDTO> bucket = redissonClient.getBucket("user:" + userId);
        return bucket.get();
    }

    public boolean setUserName(String userId, String newUserName) {
        RBucket<UserDTO> bucket = redissonClient.getBucket("user:" + userId);
        UserDTO userDTO = bucket.get();
        if (userDTO != null) {
            userDTO.setName(newUserName);
            bucket.set(userDTO);
            return true;
        }
        return false;
    }

    public boolean joinChatroom(String userId, String chatroomId) {
        RBucket<UserDTO> bucket = redissonClient.getBucket("user:" + userId);
        UserDTO userDTO = bucket.get();
        if (userDTO != null) {
            if (!userDTO.getChatroom().contains(chatroomId)) {
                userDTO.getChatroom().add(chatroomId);
                chatroomService.incrChatroomNumUser(chatroomId, 1);
                bucket.set(userDTO);
                return true;
            }
        }
        return false;
    }

    public boolean leaveChatroom(String userId, String chatroomId) {
        RBucket<UserDTO> bucket = redissonClient.getBucket("user:" + userId);
        UserDTO userDTO = bucket.get();
        if (userDTO != null) {
            if (userDTO.getChatroom().contains(chatroomId)) {
                userDTO.getChatroom().remove(chatroomId);
                chatroomService.incrChatroomNumUser(chatroomId, -1);
                bucket.set(userDTO);
                return true;
            }
        }
        return false;
    }
}
