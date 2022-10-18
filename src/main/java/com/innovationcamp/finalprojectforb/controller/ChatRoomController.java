package com.innovationcamp.finalprojectforb.controller;

import com.innovationcamp.finalprojectforb.dto.ResponseDto;
import com.innovationcamp.finalprojectforb.service.ChatRoomService;
import com.innovationcamp.finalprojectforb.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PageableDefault;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class ChatRoomController {
    private final ChatService chatService;

    private final ChatRoomService chatRoomService;

//    // 채팅방에 있는지 확인
//    @GetMapping("/api/chat/member/{eventId}")
//    public ResponseDto<?> getChatMember (@PathVariable Long eventId, HttpServletRequest request) {
//        return chatService.getChatMember(eventId, request);
//    }


    // 채팅메세지 불러오기
    @GetMapping("/api/chat/message/{roomId}")
    public ResponseDto<?> getMessageLog(@PathVariable Long roomId,@Header("Authorization") String token) {
        return chatService.getMessage(roomId, token);
    }

    // 채팅방 나가기
    @DeleteMapping("/api/chat/member/{roomId}")
    public ResponseDto<?> exitChatRoom(@PathVariable Long roomId, @Header("Authorization") String token) {
        return chatService.exitChatRoom(roomId, token);
    }

    //채팅방 생성
    @GetMapping("/api/roadmap/{targetMemberId}/chat")
    public ResponseDto<?> createChatRoom(@PathVariable Long targetMemberId, HttpServletRequest request){
        return chatRoomService.createChatRoom(targetMemberId, request);
    }
}