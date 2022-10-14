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

    @Autowired
    public WebSocketConfig (FilterChannelInterceptor filterChannelInterceptor){
        this.filterChannelInterceptor = filterChannelInterceptor;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/sub"); // 구독 주소 : /sub/chat
        registry.setApplicationDestinationPrefixes("/pub"); // 발행 주소  : /pub/chat
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/stomp")
                .setAllowedOriginPatterns("http://localhost:3000","*");  //"*://*"
        //.withSockJS();
    }
    // 새로 추가
    /*configureClientInboundChannel : stomp 연결 시도 시 호출되는 메소드
    *인터셉터를 등록해서 연결을 시도하면 FilterChannelInterceptor가 실행됨
    */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(filterChannelInterceptor);
    }

}

