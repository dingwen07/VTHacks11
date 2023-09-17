package com.example.chatroom.controller;

import com.example.chatroom.dto.ResponseDTO;
import com.example.chatroom.dto.chatroom.ChatroomDTO;
import com.example.chatroom.dto.chatroom.ChatroomResp;
import com.example.chatroom.dto.message.MessageDTO;
import com.example.chatroom.dto.message.MessageResp;
import com.example.chatroom.dto.message.MessageSendResp;
import com.example.chatroom.dto.message.MessagesResp;
import com.example.chatroom.dto.user.UserDTO;
import com.example.chatroom.enums.MessageTypeEnum;
import com.example.chatroom.service.ChatroomService;
import com.example.chatroom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/chatroom")
public class ChatroomController {
    @Autowired
    UserService userService;
    @Autowired
    ChatroomService chatroomService;

    @GetMapping("/{chatroomId}")
    public ResponseEntity<ResponseDTO<ChatroomResp>> getChatroom(@PathVariable String chatroomId,
                                                                 @RequestParam(value = "user_id") String userId) {
        ResponseDTO<ChatroomResp> responseDTO = new ResponseDTO();
        UserDTO userDTO = userService.getUser(userId);
        if (userDTO == null) {
            responseDTO.setStatus(400);
            responseDTO.setMessage("User Does Not Exist");
            return ResponseEntity.badRequest().body(responseDTO);
        }
        ChatroomDTO chatroomDTO = chatroomService.getChatroom(chatroomId);
        if (chatroomDTO == null) {
            responseDTO.setStatus(404);
            responseDTO.setMessage("Chatroom Not Found");
            return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
        }
        ChatroomResp chatroomResp = new ChatroomResp();
        chatroomResp.setId(chatroomId);
        chatroomResp.setJoined(userDTO.getChatroom().contains(chatroomId));
        chatroomResp.setMemberCount(0);
        chatroomResp.setLastMessageId(chatroomService.getLastMessageId(chatroomId));
        chatroomResp.setName(chatroomDTO.getName());

        responseDTO.setStatus(200);
        responseDTO.setMessage("OK");
        responseDTO.setData(chatroomResp);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/{chatroomId}")
    public ResponseEntity<ResponseDTO<String>> postChatroom(@PathVariable String chatroomId,
                                                            @RequestParam(value = "user_id") String userId,
                                                            @RequestParam(value = "room_name", required = false)
                                                                String roomName) {
        ResponseDTO<String> responseDTO = new ResponseDTO();
        UserDTO userDTO = userService.getUser(userId);
        if (userDTO == null) {
            responseDTO.setStatus(400);
            responseDTO.setMessage("User Does Not Exist");
            return ResponseEntity.badRequest().body(responseDTO);
        }
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setChatroom(chatroomId);
        messageDTO.setMessageType(MessageTypeEnum.SYSTEM);
        String userIdMd5 = DigestUtils.md5DigestAsHex(userDTO.getId().getBytes(StandardCharsets.UTF_8));
        messageDTO.setSenderId("");
        if (chatroomService.addChatroom(chatroomId)) {
            userService.joinChatroom(userId, chatroomId);
            if (roomName != null) {
                chatroomService.setChatroomName(chatroomId, roomName);
            }
            messageDTO.setPayload(String.format("User %s (%s) Created the Chatroom", userDTO.getName(), userIdMd5));
        } else {
            if (!userService.joinChatroom(userId, chatroomId)) {
                responseDTO.setStatus(400);
                responseDTO.setMessage("User Already Joined Chatroom");
                return ResponseEntity.badRequest().body(responseDTO);
            }
            messageDTO.setPayload(String.format("User %s (%s) Joined the Chatroom", userDTO.getName(), userIdMd5));
        }
        chatroomService.addMessage(chatroomId, messageDTO);
        responseDTO.setStatus(200);
        responseDTO.setMessage("OK");
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/{chatroomId}")
    public ResponseEntity<ResponseDTO<String>> setChatroom(@PathVariable String chatroomId,
                                                           @RequestParam(value = "user_id") String userId,
                                                           @RequestParam(value = "room_name") String roomName) {
        ResponseDTO<String> responseDTO = new ResponseDTO();
        UserDTO userDTO = userService.getUser(userId);
        if (userDTO == null || !userDTO.getChatroom().contains(chatroomId) ||
                !chatroomService.setChatroomName(chatroomId, roomName)) {
            responseDTO.setStatus(400);
            responseDTO.setMessage("Bad Request");
            return ResponseEntity.badRequest().body(responseDTO);
        }
        responseDTO.setStatus(200);
        responseDTO.setMessage("OK");
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/{chatroomId}/chat")
    public ResponseEntity<ResponseDTO<MessagesResp>> getMessages(@PathVariable String chatroomId,
                                                                 @RequestParam(value = "offset") int offset,
                                                                 @RequestParam(value = "message_id") int messageId) {
        ResponseDTO<MessagesResp> responseDTO = new ResponseDTO();
        List<MessageDTO> messages = chatroomService.getMessageById(chatroomId, messageId, offset);
        if (messages ==  null) {
            responseDTO.setStatus(404);
            responseDTO.setMessage("Not Found");
            return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
        }
        ArrayList<MessageResp> messageRespList = new ArrayList<>();
        for (MessageDTO messageDTO: messages
             ) {
            MessageResp messageResp = new MessageResp();
            messageResp.setMessageType(messageDTO.getMessageType());
            messageResp.setChatroom(messageDTO.getChatroom());
            messageResp.setId(messageDTO.getId());
            messageResp.setPayload(messageDTO.getPayload());
            messageResp.setSenderName(messageDTO.getSenderName());
            messageResp.setTimestamp(messageDTO.getTimestamp());
            String userIdMd5 = DigestUtils.md5DigestAsHex(messageDTO.getSenderId().getBytes(StandardCharsets.UTF_8));
            messageResp.setSenderIdHash(userIdMd5);
            messageRespList.add(messageResp);
        }
        MessagesResp messagesResp = new MessagesResp();
        messagesResp.setMessage(messageRespList);
        responseDTO.setData(messagesResp);
        responseDTO.setStatus(200);
        responseDTO.setMessage("OK");
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/{chatroomId}/chat")
    public ResponseEntity<ResponseDTO<MessageSendResp>> sendMessage(@PathVariable String chatroomId,
                                                                    @RequestParam(value = "user_id") String userId,
                                                                    @RequestParam(value = "message") String payload) {
        ResponseDTO<MessageSendResp> responseDTO = new ResponseDTO();
        UserDTO userDTO = userService.getUser(userId);
        if (userDTO == null || !userDTO.getChatroom().contains(chatroomId)) {
            responseDTO.setStatus(400);
            responseDTO.setMessage("Bad Request");
            return ResponseEntity.badRequest().body(responseDTO);
        }
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setSenderName(userDTO.getName());
        messageDTO.setChatroom(chatroomId);
        messageDTO.setMessageType(MessageTypeEnum.PLAINTEXT);
        messageDTO.setPayload(payload);
        messageDTO.setSenderId(userId);
        messageDTO.setTimestamp(System.currentTimeMillis());
        int messageId = chatroomService.addMessage(chatroomId, messageDTO);
        MessageSendResp messageSendResp = new MessageSendResp();
        messageSendResp.setMessageId(messageId);
        responseDTO.setData(messageSendResp);
        responseDTO.setStatus(200);
        responseDTO.setMessage("OK");
        return ResponseEntity.ok(responseDTO);
    }

}
