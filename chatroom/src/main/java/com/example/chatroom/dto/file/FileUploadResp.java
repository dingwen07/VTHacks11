package com.example.chatroom.dto.file;

import lombok.Data;

import java.io.Serializable;

@Data
public class FileUploadResp implements Serializable {
    private String filename;
}
