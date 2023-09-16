package com.example.chatroom.dto;
import lombok.Data;

import java.io.Serializable;

@Data
public class ResponseDTO<T> implements Serializable {
    private int status;
    private T data;
    private String message;
}
