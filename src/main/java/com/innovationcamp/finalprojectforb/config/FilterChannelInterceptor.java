package com.innovationcamp.finalprojectforb.config;

import com.innovationcamp.finalprojectforb.dto.ResponseDto;
import com.innovationcamp.finalprojectforb.dto.chat.ChatRequestDto;
import com.innovationcamp.finalprojectforb.enums.ErrorCode;
import com.innovationcamp.finalprojectforb.exception.CustomException;
import com.innovationcamp.finalprojectforb.jwt.TokenProvider;
import com.innovationcamp.finalprojectforb.model.Member;
import com.innovationcamp.finalprojectforb.model.chat.ChatMember;
import com.innovationcamp.finalprojectforb.model.chat.ChatRoom;
import com.innovationcamp.finalprojectforb.model.roadmap.Html;
import com.innovationcamp.finalprojectforb.repository.MemberRepository;
import com.innovationcamp.finalprojectforb.repository.chat.ChatMemberRepository;
import com.innovationcamp.finalprojectforb.repository.chat.ChatRoomRepository;
import com.innovationcamp.finalprojectforb.service.ChatService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.io.Decoders;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.logging.Logger;

@Log4j2
@RequiredArgsConstructor
@Component
//Spring Security보다 Interceoptor의 우선순위를 올리기 위해 해당 어노테이션 사용
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
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
//            if(isOkToken == true) {
//                Member member = tokenProvider.getMemberFromAuthentication();
//                log.info("CONNECT 시 memberId가 들어오는지 : " + member.getId());
//                log.info("CONNECT 시 memberEmail이 들어오는지 : " + member.getEmail());
//                log.info("CONNECT 시 memberEmail이 들어오는지 : " + member.getChatMessages());
//            }

        //StompCommand.SUBSCRIBE : 구독 요청이 들어왔을 때 user가 일치하는지 확인하고 일치한다면 채팅방 목록에 추가함.
        } else if (StompCommand.SUBSCRIBE == accessor.getCommand()) { // 채팅룸 구독요청

            String simpSessionId = Optional.ofNullable((String) message.getHeaders().get("simpSessionId")).orElse("no session");
            log.info("SUBSCRIBE 시 simpSessionId 들어오는지 : " + simpSessionId);
            // header정보에서 구독 destination정보를 얻고, roomId를 추출한다.
            // roomId를 URL로 전송해주고 있어 추출 필요
            String roomId = Optional.ofNullable((String) message.getHeaders().get("simpDestination")).orElse("InvalidRoomId");
            log.info("SUBSCRIBE 시 roomId 들어오는지 : " + roomId);
            // 채팅방에 들어온 클라이언트 sessionId를 roomId와 맵핑해 놓는다.(나중에 특정 세션이 어떤 채팅방에 들어가 있는지 알기 위함)
            // sessionId는 정상적으로 들어가고있음
        //    String sessionId = (String) message.getHeaders().get("simpSessionId");
      //      Long userId = (Long) message.getHeaders().get("User");
      //      log.info("SUBSCRIBE 시 추가된 userId 들어오는지 : " + userId);
//            Member memberIdSet = new Member();
//            memberIdSet.getId(userId);
//            ChatMember chatMember = new ChatMember(memberIdSet,sessionId);
//            chatMemberRepository.save(chatMember);

           // chatRoom.setChatMember(tokenProvider.getMemberFromAuthentication(jwtToken));

////            // 채팅방에 들어온 클라이언트 sessionId를 roomId와 맵핑해 놓는다.(나중에 특정 세션이 어떤 채팅방에 들어가 있는지 알기 위함)
////            // sessionId는 정상적으로 들어가고있음
//            //setSessionRoomId() : Redis를 이용하여 user와 채팅방을 매핑시킴
////            String sessionId = (String) message.getHeaders().get("simpSessionId");
////            chatRoomService.setUserEnterInfo(sessionId, roomId);
////
////            // 클라이언트 입장 메시지를 채팅방에 발송한다.(redis publish)
////            String token = Optional.ofNullable(accessor.getFirstNativeHeader("token")).orElse("UnknownUser");
////            String name = jwtTokenProvider.getAuthenticationUsername(token);
////            chatService.sendChatMessage(ChatMessage.builder().type(ChatMessage.MessageType.ENTER).roomId(roomId).sender(name).build());
////
////            log.info("SUBSCRIBED {}, {}", name, roomId);
//////        }

        }
        return message;
    }

    //    @EventListener(SessionConnectEvent.class)
//    public void onConnect(SessionConnectEvent event){
//        String sessionId = event.getMessage().getHeaders().get("simpSessionId").toString();
//        String userId = event.getMessage().getHeaders().get("nativeHeaders").toString().split("User=\\[")[1].split("]")[0];
//
//        sessions.put(sessionId, Integer.valueOf(userId));
//    }
    /*
    spring boot로 websocket을 개발하다보면 Session의 Connect / Disconnect 되는 시점을 알고 싶을 때가 있는데
    (사용자 동시 접속 리스트 관리 차원)
    */
    public Member validateMember(String jwtToken) {
        if (!tokenProvider.validateToken(jwtToken)) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }

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