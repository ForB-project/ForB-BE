package com.innovationcamp.finalprojectforb.controller;

import com.innovationcamp.finalprojectforb.dto.ResponseDto;
import com.innovationcamp.finalprojectforb.dto.chat.ChatRequestDto;
import com.innovationcamp.finalprojectforb.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collections;
import java.util.List;

@Log4j2
@RequiredArgsConstructor
@Controller
public class ChatController {
   /*
    둘의 차이는 별로 다를게 없다.
    위에꺼는 interface - contract : 실제로 특정 사용자에게 메세지를 보내는 경우 많이 사용
    아래꺼는 위에꺼의 좀더 구체적인 하위 인터페이스
    */
    private final SimpMessageSendingOperations simpMessageSendingOperations;
    private final SimpMessagingTemplate simpMessagingTemplate;

    private final ChatService chatService;


    /*
    채팅을 수신(sub)하고, 송신(pub)하기 위한 Controller
    @MessageMapping: 이 어노테이션은 Stomp에서 들어오는 message를 서버에서 발송(pub)한 메세지가 도착하는 엔드포인트입니다
     */

    // 채팅메세지 보내기
    @MessageMapping("/chat/test")
    public ResponseDto<?> message(@RequestBody ChatRequestDto message,
                                  SimpMessageHeaderAccessor accessor) {
        //인자에 SimpMessageHeaderAccessor를 추가하면 해당 메시지의 헤더에 접근할 수 있다
        return chatService.sendMessage(message, accessor);
    }
    /*
    또 Spring에서 제공해주는 SimpMessagingTemplate.class를 사용한다.
    SimpMessagingTemplate을 기존에 사용하던 @SendTo 어노테이션을 대체해주는데 여러가지 메서드를 지원해준다
    위 예제에서 사용한 convertAndSend()같은 경우는 파라미터를 두개받는데 첫번째가 destination 두번째는 payload를 뜻한다.
     */

     ////테스트용////
     // 채팅 메시지를 @MessageMapping 형태로 받는다
     // 웹소켓으로 publish 된 메시지를 받는 곳이다
    @MessageMapping("/chat/message")
    public void messageTest(@RequestBody ChatRequestDto message,
                            SimpMessageHeaderAccessor accessor) {

        //@Header : 커스텀헤더에서 토큰값 가져올때 사용
        /*
        convertAndSend
        이 메서드는 매개변수로 각각 메시지의 도착지점과 객체를 넣어줍니다.
        이를 통해서 도착지점 즉 sub 되는 지점으로 인자로 들어온 객체를 Message 객체로 변환하여
        해당 도착지점을 sub 하고 있는 모든 사용자에게 메시지를 보내줍니다.
         */
        simpMessagingTemplate.convertAndSend("/sub/chat/room/"+ message.getRoomId(),message.getMessage());
    }

    // 채팅방 입장
    @MessageMapping("/chat/enter")
    public ResponseDto<?> enterChatRoom(ChatRequestDto message,
                                        @Header("Authorization") String token) {
        return chatService.enterChatRoom(message, token);
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