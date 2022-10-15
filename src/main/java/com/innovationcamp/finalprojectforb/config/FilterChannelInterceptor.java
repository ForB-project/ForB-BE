package com.innovationcamp.finalprojectforb.config;

import com.innovationcamp.finalprojectforb.jwt.TokenProvider;
import com.innovationcamp.finalprojectforb.model.Member;
import com.innovationcamp.finalprojectforb.repository.MemberRepository;
import io.jsonwebtoken.io.Decoders;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.util.Optional;
import java.util.logging.Logger;

@Log4j2
@RequiredArgsConstructor
@Component
//Spring Security보다 Interceoptor의 우선순위를 올리기 위해 해당 어노테이션 사용
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class FilterChannelInterceptor implements ChannelInterceptor {
    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;


    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        //stomp헤더에서 토큰 가져오기 : 클라이언트단에서 웹 소켓 요청시 헤더값에 토큰검증값을 얻을수 있다.
        //StompHeaderAccessor 를 통해서 세션의 데이터를 얻어올 수 있다(각종 웹소켓 관련 정보)
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        //StompCommand.CONNECT : command로 connect를 시도할 때 로그인 여부 확인
        if (StompCommand.CONNECT == accessor.getCommand()) { // websocket 연결요청
            String jwtToken = Optional.ofNullable(accessor.getFirstNativeHeader("Authorization")).orElse("UnknownUser");
            log.info("CONNECT {}", jwtToken);
            // Header의 jwt token 검증
            tokenProvider.validateToken(jwtToken);
        }
        return message;
        //StompCommand.SUBSCRIBE : 구독 요청이 들어왔을 때 user가 일치하는지 확인하고 일치한다면 채팅방 목록에 추가함.
//        } else if (StompCommand.SUBSCRIBE == accessor.getCommand()) { // 채팅룸 구독요청
//            // header정보에서 구독 destination정보를 얻고, roomId를 추출한다.
//            // roomId를 URL로 전송해주고 있어 추출 필요
//            String roomId = chatService.getRoomId(Optional.ofNullable((String) message.getHeaders().get("simpDestination")).orElse("InvalidRoomId"));
//
//            // 채팅방에 들어온 클라이언트 sessionId를 roomId와 맵핑해 놓는다.(나중에 특정 세션이 어떤 채팅방에 들어가 있는지 알기 위함)
//            // sessionId는 정상적으로 들어가고있음
        //setSessionRoomId() : Redis를 이용하여 user와 채팅방을 매핑시킴
//            String sessionId = (String) message.getHeaders().get("simpSessionId");
//            chatRoomService.setUserEnterInfo(sessionId, roomId);
//
//            // 클라이언트 입장 메시지를 채팅방에 발송한다.(redis publish)
//            String token = Optional.ofNullable(accessor.getFirstNativeHeader("token")).orElse("UnknownUser");
//            String name = jwtTokenProvider.getAuthenticationUsername(token);
//            chatService.sendChatMessage(ChatMessage.builder().type(ChatMessage.MessageType.ENTER).roomId(roomId).sender(name).build());
//
//            log.info("SUBSCRIBED {}, {}", name, roomId);
////        }
//
//        return message;

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