package com.innovationcamp.finalprojectforb.controller;

import com.innovationcamp.finalprojectforb.dto.ResponseDto;
import com.innovationcamp.finalprojectforb.dto.chat.ChatRequestDto;
import com.innovationcamp.finalprojectforb.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.handler.annotation.Header;

import javax.servlet.http.HttpServletRequest;


@RequiredArgsConstructor
@Controller
public class ChatController {

    private final ChatService chatService;

    // 채팅메세지 보내기
    @MessageMapping("/chat/message")
    public ResponseDto<?> message(ChatRequestDto message,
                                  HttpServletRequest request) {
        return chatService.sendMessage(message, request);
    }

    // 채팅방 입장
    @MessageMapping("/chat/enter")
    public ResponseDto<?> enterChatRoom(ChatRequestDto message,
                                        HttpServletRequest request) {
        return chatService.enterChatRoom(message, request);
    }

    /*
    MessageMapping어노테이션은 기존의 requestMapping과 비슷한 역할
    http 요청이 들어왔을때, 그 요청의 경로에 맞는 핸들러에게 처리를 위임하듯이
    stomp웹소캣 통신을 통해 메시지가 들어왔을때에도 메시지의 목적지 헤더와
    MessageMapping에 설정된 경로가 일치하는 핸들러를 찾고, 그 핸들러가 처리를 하게 된다
    config에서 설정을 해둔 pub이라는 prefix과 합쳐져서
    pub/chat/enter이라는 목적지 헤더를 가진 메시지들이 이 핸들러를 거치게 된다
     */
}