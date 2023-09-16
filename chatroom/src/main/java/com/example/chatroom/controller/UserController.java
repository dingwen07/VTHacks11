package com.example.chatroom.controller;
import com.example.chatroom.dto.ResponseDTO;
import com.example.chatroom.dto.user.UserAddResp;
import com.example.chatroom.dto.user.UserChatroomResp;
import com.example.chatroom.dto.user.UserDTO;
import com.example.chatroom.dto.user.UserInfoResp;
import com.example.chatroom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("")
    public ResponseEntity<ResponseDTO<String>> getUser() {
        ResponseDTO<String> dto = new ResponseDTO<>();
        dto.setStatus(400);
        dto.setMessage("Bad Request");
        return ResponseEntity.badRequest().body(dto);
    }

    @PostMapping("")
    public ResponseDTO<UserAddResp> addUser(@RequestParam(value = "name") String name) {
        ResponseDTO<UserAddResp> dto = new ResponseDTO<>();
        UserDTO userDTO = userService.addUser(name);
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
                                                                 @RequestParam(value = "name") String newUserName) {
        ResponseDTO<UserInfoResp> dto = new ResponseDTO<>();
        Boolean success = userService.setUserName(userId, newUserName);
        if (success) {
            UserInfoResp userInfoResp = new UserInfoResp();
            userInfoResp.setName(newUserName);
            dto.setData(userInfoResp);
            dto.setStatus(200);
            dto.setMessage("OK");
        }
        dto.setStatus(404);
        dto.setMessage("Not Found");
        return new ResponseEntity<>(dto, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{userId}/chatroom")
    public ResponseEntity<ResponseDTO<UserChatroomResp>> getChatroom(@PathVariable String userId) {
        return null;
    }
}
