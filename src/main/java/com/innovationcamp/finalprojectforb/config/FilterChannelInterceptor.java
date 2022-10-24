package com.innovationcamp.finalprojectforb.config;

import com.innovationcamp.finalprojectforb.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@Component
public class FilterChannelInterceptor implements ChannelInterceptor {
    private final TokenProvider tokenProvider;


    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        //stomp헤더에서 토큰 가져오기 : 클라이언트단에서 웹 소켓 요청시 헤더값에 토큰검증값을 얻을수 있다.
        //StompHeaderAccessor 를 통해서 세션의 데이터를 얻어올 수 있다(각종 웹소켓 관련 정보)
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        /*
        스톰프 헤더에서 받아지는 값 예시
        StompHeaderAccessor [
        headers={simpMessageType=MESSAGE,
        stompCommand=SEND,
        nativeHeaders={destination=[/pub/chat/message],
        content-length=[28]},
        simpSessionAttributes={},
        simpHeartbeat=[J@5179bc2f, lookupDestination=/chat/message,
        simpSessionId=ce8ed936-93ea-d472-0d0e-99e8eab260e3,
        simpDestination=/pub/chat/message}]
        -----------------------------------------------
         StompHeaderAccessor [
         headers={simpMessageType=MESSAGE,
         stompCommand=SEND,
         nativeHeaders={destination=[/pub/chat/message],
         Authorization=[eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ3cGZoMTg4QGdtYWlsLmNvbSIsImF1dGgiOiJST0xFX1VTRVIiLCJleHAiOjE2NjU4Mzc0Mjl9.BZZPhByoqxPuzCQIIgiHlxZwIb1P5pv5rMrMnew7B8Q],
         content-length=[28]},
         simpSessionAttributes={},
         simpHeartbeat=[J@510a605b,
         lookupDestination=/chat/message,
         simpSessionId=0643ee26-5bfd-f06b-1d9e-997e1

         ---------------------------------------------
          StompHeaderAccessor [
          headers={simpMessageType=MESSAGE,
          stompCommand=SEND,
          nativeHeaders={destination=[/pub/chat/message],
          Authorization=[eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ3cGZoMTg4QGdtYWlsLmNvbSIsImF1dGgiOiJST0xFX1VTRVIiLCJleHAiOjE2NjU5ODgzNjR9.7dqamy0BekInyLc8a8JYFHVwA-byH1i_tVKCfrzlpho],
          content-length=[28]},
          simpSessionAttributes={},
          simpHeartbeat=[J@38564888, lookupDestination=/chat/message,
          simpSessionId=3763baa7-1910-aa9c-01a8-c334519f5110,
          simpDestination=/pub/chat/message}]
         */

        //StompCommand.CONNECT : command로 connect를 시도할 때 로그인 여부 확인
        //첫 연결 시도에만 사용자를 인증하고 이후엔 저장해둔 정보를 사용할 것이므로 getCommand()로 연결 시도인지 확인한다
        if (StompCommand.CONNECT == accessor.getCommand()) { // websocket 연결요청
            String jwtToken = Optional.ofNullable(accessor.getFirstNativeHeader("Authorization")).orElse("UnknownUser");
            log.info("CONNECT 시 토큰이 들어오는지 : " + jwtToken);

            //Header의 jwt token 검증
            boolean isOkToken = tokenProvider.validateToken(jwtToken);
            log.info("CONNECT 시 토큰검증 : " + isOkToken);

        //StompCommand.SUBSCRIBE : 구독 요청이 들어왔을 때 user가 일치하는지 확인하고 일치한다면 채팅방 목록에 추가함.
        } else if (StompCommand.SUBSCRIBE == accessor.getCommand()) { // 채팅룸 구독요청
            String roomId = Optional.ofNullable((String) message.getHeaders().get("simpDestination")).orElse("InvalidRoomId");
            log.info("SUBSCRIBE 시 roomId 들어오는지 : " + roomId);
        }
        return message;
    }

    /*
    spring boot로 websocket을 개발하다보면 Session의 Connect / Disconnect 되는 시점을 알고 싶을 때가 있는데
    (사용자 동시 접속 리스트 관리 차원)
    */

    @Override
    public void postSend(Message message, MessageChannel channel, boolean sent) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        //세션의 ID를 가져오려면
        String sessionId = accessor.getSessionId();
        switch (accessor.getCommand()) {
            case CONNECT:
                // 유저가 Websocket으로 connect()를 한 뒤 호출됨
                break;
            case DISCONNECT:
                // 유저가 Websocket으로 disconnect() 를 한 뒤 호출됨 or 세션이 끊어졌을 때 발생함(페이지 이동~ 브라우저 닫기 등)
                break;
            default:
                break;
        }
    }

//    @Override
//    public void postSend(Message message, MessageChannel channel, boolean sent) {
//        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
//        String sessionId = accessor.getSessionId();
//        switch (accessor.getCommand()) {
//            case CONNECT:
//                // 유저가 Websocket으로 connect()를 한 뒤 호출됨
//
//                break;
//            case DISCONNECT:
//                log.info("DISCONNECT");
//                log.info("sessionId: {}", sessionId);
//                log.info("channel:{}", channel);
//                // 유저가 Websocket으로 disconnect() 를 한 뒤 호출됨 or 세션이 끊어졌을 때 발생함(페이지 이동~ 브라우저 닫기 등)
//                break;
//            default:
//                break;
//        }
//    }
}