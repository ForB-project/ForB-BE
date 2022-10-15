package com.innovationcamp.finalprojectforb.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final FilterChannelInterceptor filterChannelInterceptor;

    //스프링 컨테이너에 등록한 FilterChannelInterceptor를 사용하기
    @Autowired
    public WebSocketConfig (FilterChannelInterceptor filterChannelInterceptor){
        this.filterChannelInterceptor = filterChannelInterceptor;
    }

    @Override// subscribe와 publish할 때 destination prefix
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/sub"); // 구독 주소 : /sub/chat
        registry.setApplicationDestinationPrefixes("/pub"); // 발행 주소  : /pub/chat
    }

    @Override
    //처음 webSocket에 접속할 때 HandShake와 통신을 담당할 엔드포인트를 .addEndpoint("/stomp") 지정
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/stomp")
                .setAllowedOriginPatterns("http://localhost:3000","*"); //"*://*"
       // .withSockJS(); WebSocket을 지원하지 않는 브라우저의 경우 SockJS를 통해 다른 방식으로 채팅이 이뤄질 수 있게 구현
    }
    /*
    configureClientInboundChannel : stomp 연결 시도 시 호출되는 메소드
    StompHandler가 Websocket 앞단에서 token 을 체크할 수 있도록 인터셉터로 설정
    */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(filterChannelInterceptor);
    }
}

