package com.example.chatroom.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("")
    public String getTest(@RequestParam(value = "data") String data) {
        return data;
    }
}
