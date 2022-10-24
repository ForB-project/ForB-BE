package com.innovationcamp.finalprojectforb.controller;

import com.innovationcamp.finalprojectforb.dto.ResponseDto;
import com.innovationcamp.finalprojectforb.dto.chat.ChatRequestDto;
import com.innovationcamp.finalprojectforb.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;
@Log4j2
@RequiredArgsConstructor
@RestController
public class ChatController {
    private final ChatService chatService;

    // 채팅메세지 보내기
    @MessageMapping("/chat/message")
    public ResponseDto<?> message(ChatRequestDto message,
                                  @Header("Authorization") String token) {
        return chatService.sendMessage(message, token);
    }
}