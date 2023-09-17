package com.example.chatroom.controller;
import com.example.chatroom.dto.ResponseDTO;
import com.example.chatroom.dto.chatroom.ChatroomDTO;
import com.example.chatroom.dto.chatroom.ChatroomResp;
import com.example.chatroom.dto.user.*;
import com.example.chatroom.service.ChatroomService;
import com.example.chatroom.service.UserService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    ChatroomService chatroomService;

    @GetMapping("")
    public ResponseEntity<ResponseDTO<String>> getUser() {
        ResponseDTO<String> dto = new ResponseDTO<>();
        dto.setStatus(400);
        dto.setMessage("Bad Request");
        return ResponseEntity.badRequest().body(dto);
    }

    @PostMapping("")
    public ResponseDTO<UserAddResp> addUser(@RequestBody UserReq userReq) {
        ResponseDTO<UserAddResp> dto = new ResponseDTO<>();
        UserDTO userDTO = userService.addUser(userReq.getName());
        UserAddResp userAddResp = new UserAddResp();
        if (userDTO != null) {
            userAddResp.setUserId(userDTO.getId());
        }
        dto.setData(userAddResp);
        dto.setStatus(200);
        dto.setMessage("OK");
        return dto;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ResponseDTO<UserInfoResp>> getUser(@PathVariable String userId) {
        ResponseDTO<UserInfoResp> dto = new ResponseDTO<>();
        UserDTO userDTO = userService.getUser(userId);
        if (userDTO != null) {
            UserInfoResp userInfoResp = new UserInfoResp();
            userInfoResp.setName(userDTO.getName());
            dto.setData(userInfoResp);
            dto.setStatus(200);
            dto.setMessage("OK");
            return ResponseEntity.ok(dto);
        }
        dto.setStatus(404);
        dto.setMessage("Fang Bin Xing");

        return new ResponseEntity<>(dto, HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ResponseDTO<UserInfoResp>> setUserName(@PathVariable String userId,
                                                                 @RequestBody UserReq req) {
        ResponseDTO<UserInfoResp> dto = new ResponseDTO<>();
        Boolean success = userService.setUserName(userId, req.getName());
        if (success) {
            UserInfoResp userInfoResp = new UserInfoResp();
            userInfoResp.setName(req.getName());
            dto.setData(userInfoResp);
            dto.setStatus(200);
            dto.setMessage("OK");
            return ResponseEntity.ok(dto);
        }
        dto.setStatus(404);
        dto.setMessage("Not Found");
        return new ResponseEntity<>(dto, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{userId}/chatroom")
    public ResponseEntity<ResponseDTO<UserChatroomResp>> getChatroom(@PathVariable String userId) {
        ResponseDTO<UserChatroomResp> responseDTO = new ResponseDTO<>();
        UserDTO userDTO = userService.getUser(userId);
        if (userDTO == null) {
            responseDTO.setStatus(404);
            responseDTO.setMessage("Not Found");
            return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
        }
        ArrayList<ChatroomResp> chatroomResps = new ArrayList<>();
        for (String chatroomId: userDTO.getChatroom()
             ) {
            ChatroomDTO chatroomDTO = chatroomService.getChatroom(chatroomId);
            if (chatroomDTO != null) {
                ChatroomResp chatroomResp = new ChatroomResp();
                chatroomResp.setId(chatroomId);
                chatroomResp.setLastMessageId(chatroomService.getLastMessageId(chatroomId));
                chatroomResp.setJoined(true);
                chatroomResp.setName(chatroomDTO.getName());
                chatroomResp.setMemberCount(chatroomDTO.getNumUsers());
                chatroomResps.add(chatroomResp);
            }
        }
        UserChatroomResp userChatroomResp = new UserChatroomResp();
        userChatroomResp.setChatroom(chatroomResps);
        responseDTO.setData(userChatroomResp);
        responseDTO.setStatus(200);
        responseDTO.setMessage("OK");
        return ResponseEntity.ok(responseDTO);
    }
}
