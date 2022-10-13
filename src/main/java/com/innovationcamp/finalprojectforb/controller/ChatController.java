package com.innovationcamp.finalprojectforb.controller;

import com.innovationcamp.finalprojectforb.dto.ResponseDto;
import com.innovationcamp.finalprojectforb.dto.chat.ChatRequestDto;
import com.innovationcamp.finalprojectforb.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.aspectj.bridge.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.handler.annotation.Header;

import javax.servlet.http.HttpServletRequest;

@Log4j2
@RequiredArgsConstructor
@Controller
public class ChatController {

    private final SimpMessageSendingOperations simpMessageSendingOperations;

    /*
    채팅을 수신(sub)하고, 송신(pub)하기 위한 Controller
    @MessageMapping: 이 어노테이션은 Stomp에서 들어오는 message를 서버에서 발송(pub)한 메세지가 도착하는 엔드포인트입니다

     */

    private final ChatService chatService;

    // 채팅메세지 보내기
    @MessageMapping("/chat/message")
    public ResponseDto<?> message(ChatRequestDto message,
                                  HttpServletRequest request) {
        return chatService.sendMessage(message, request);
    }
     ////테스트
    @MessageMapping("/chat/test")
    public void messageTest(ChatRequestDto message) {
        //convertAndSend
        //이 메서드는 매개변수로 각각 메시지의 도착지점과 객체를 넣어줍니다.
        //이를 통해서 도착지점 즉 sub 되는 지점으로 인자로 들어온 객체를 Message 객체로 변환하여
        //해당 도착지점을 sub 하고 있는 모든 사용자에게 메시지를 보내줍니다.

        simpMessageSendingOperations.convertAndSend("/sub/chat/"+message.getRoomId(),message.getMessage() + "서버전송가능");
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