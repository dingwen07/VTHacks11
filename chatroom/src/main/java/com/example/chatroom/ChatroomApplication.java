package com.example.chatroom;

import com.example.chatroom.config.StorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class ChatroomApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatroomApplication.class, args);
	}



}
