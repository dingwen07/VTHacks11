package com.example.chatroom.service;

import com.example.chatroom.dto.chatroom.ChatroomDTO;
import com.example.chatroom.dto.message.MessageDTO;

import java.util.List;

public interface ChatroomService {
    ChatroomDTO getChatroom(String id);
    List<MessageDTO> getMessageById(String chatroomId, int startMessageId, int endMessageId);
    int addMessage(String id, MessageDTO messageDTO);
    int getLastMessageId(String id);
    boolean addChatroom(String id);
    boolean setChatroomName(String id, String newName);
    boolean incrChatroomNumUser(String id, int num);
    boolean refreshChatroom(String id);
    boolean deleteChatroom(String id);
}
