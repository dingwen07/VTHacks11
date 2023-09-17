package com.example.chatroom.controller;


import com.example.chatroom.dto.ResponseDTO;
import com.example.chatroom.dto.file.FileUploadResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.chatroom.exception.StorageFileNotFoundException;
import com.example.chatroom.service.StorageService;

@Controller
public class FileUploadController {

    private final StorageService storageService;

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/files")
    public ResponseEntity<ResponseDTO<FileUploadResp>> handleFileUpload(@RequestParam("file") MultipartFile file) {

        String filename = storageService.store(file);

        ResponseDTO<FileUploadResp> responseDTO = new ResponseDTO<>();
        FileUploadResp fileUploadResp = new FileUploadResp();
        fileUploadResp.setFilename(filename);
        responseDTO.setData(fileUploadResp);
        responseDTO.setStatus(200);
        responseDTO.setMessage("OK");

        return ResponseEntity.ok(responseDTO);
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}