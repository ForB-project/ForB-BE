package com.innovationcamp.finalprojectforb.controller;

import com.innovationcamp.finalprojectforb.dto.ResponseDto;
import com.innovationcamp.finalprojectforb.dto.chat.ChatRequestDto;
import com.innovationcamp.finalprojectforb.repository.chat.ChatMessageRepository;
import com.innovationcamp.finalprojectforb.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@RestController
public class ChatController {
    private final ChatService chatService;


    // 채팅메세지 보내기
    @MessageMapping("/chat/message")
    public ResponseDto<?> message(ChatRequestDto message,
                                  @Header("Authorization") String token) {
        //인자에 SimpMessageHeaderAccessor를 추가하면 해당 메시지의 헤더에 접근할 수 있다
        log.info(Optional.ofNullable("test 컨트롤러 : token 값 : " + token).orElse("UnknownUser"));
        return chatService.sendMessage(message, token);
    }

    /*
    또 Spring에서 제공해주는 SimpMessagingTemplate.class를 사용한다.
    SimpMessagingTemplate을 기존에 사용하던 @SendTo 어노테이션을 대체해주는데 여러가지 메서드를 지원해준다
    위 예제에서 사용한 convertAndSend()같은 경우는 파라미터를 두개받는데 첫번째가 destination 두번째는 payload를 뜻한다.
     */

    /*
    MessageMapping어노테이션은 기존의 requestMapping과 비슷한 역할
    http 요청이 들어왔을때, 그 요청의 경로에 맞는 핸들러에게 처리를 위임하듯이
    stomp웹소캣 통신을 통해 메시지가 들어왔을때에도 메시지의 목적지 헤더와
    MessageMapping에 설정된 경로가 일치하는 핸들러를 찾고, 그 핸들러가 처리를 하게 된다
    config에서 설정을 해둔 pub이라는 prefix과 합쳐져서
    pub/chat/enter이라는 목적지 헤더를 가진 메시지들이 이 핸들러를 거치게 된다
     */
}