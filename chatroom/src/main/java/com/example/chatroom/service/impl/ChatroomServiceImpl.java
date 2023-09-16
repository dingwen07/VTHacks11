package com.example.chatroom.service.impl;

import com.example.chatroom.dto.chatroom.ChatroomDTO;
import com.example.chatroom.dto.message.MessageDTO;
import com.example.chatroom.service.ChatroomService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RList;
import org.redisson.api.RKeys;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class ChatroomServiceImpl implements ChatroomService {
    @Autowired
    RedissonClient redissonClient;

    public ChatroomDTO getChatroom(String id) {
        RBucket<ChatroomDTO> bucket = redissonClient.getBucket("chatroom:" + id);
        return bucket.get();
    }

    public List<MessageDTO> getMessageById(String chatroomId, int messageId, int offset) {
        RList<MessageDTO> list = redissonClient.getList("chatroom:" + chatroomId + ":message");
        if (list.isEmpty()) {
            return null;
        }
        if (offset == 0) return new ArrayList<>();

        int fromIndex, toIndex;
        if (offset >= 0) {
            fromIndex = messageId;
            toIndex = messageId + offset;
        } else {
            fromIndex = messageId + offset;
            toIndex = messageId;
        }
        if (fromIndex < 0) fromIndex = 0;
        if (toIndex > list.size()) toIndex = list.size();
        
        return list.subList(fromIndex, toIndex);
    }

    public int getLastMessageId(String id) {
        return redissonClient.getList("chatroom:" + id + ":message").size() - 1;
    }

    public int addMessage(String id, MessageDTO messageDTO) {
        RList<MessageDTO> list = redissonClient.getList("chatroom:" + id + ":message");
        int messageId = list.size();
        messageDTO.setId(messageId);
        list.add(messageDTO);
        return messageId;
    }

    public boolean addChatroom(String id) {
        RBucket<ChatroomDTO> bucket = redissonClient.getBucket("chatroom:" + id);
        if (bucket.get() != null) {
            return false;
        }
        ChatroomDTO dto = new ChatroomDTO();
        dto.setId(id);
        dto.setName("");
        bucket.set(dto);
        return true;
    }

    public boolean setChatroomName(String id, String newName) {
        RBucket<ChatroomDTO> bucket = redissonClient.getBucket("chatroom:" + id);
        ChatroomDTO chatroomDTO = bucket.get();
        if (chatroomDTO == null) {
            return false;
        }
        chatroomDTO.setName(newName);
        bucket.set(chatroomDTO);
        return true;
    }

    public boolean deleteChatroom(String id) {
        RKeys keys = redissonClient.getKeys();
        if (keys.countExists("chatroom:" + id) > 0) {
            keys.delete("chatroom:" + id);
            keys.delete("chatroom:" + id + ":message");
            return true;
        }
        return false;
    }


}
