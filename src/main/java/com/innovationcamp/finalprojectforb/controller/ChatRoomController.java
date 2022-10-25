package com.innovationcamp.finalprojectforb.controller;

import com.innovationcamp.finalprojectforb.dto.ResponseDto;
import com.innovationcamp.finalprojectforb.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    // 채팅방 생성
    @GetMapping("/api/roadmap/{targetMemberId}/chat")
    public ResponseDto<?> createChatRoom(@PathVariable Long targetMemberId, HttpServletRequest request){
        return chatRoomService.createChatRoom(targetMemberId, request);
    }

    // 채팅유저 목록 불러오기
    @GetMapping("/api/chat/list")
    public ResponseDto<?> getChatMembers(HttpServletRequest request){
        return chatRoomService.getChatMembers(request);
    }

    // 채팅메세지 불러오기
    @GetMapping("/api/chat/{roomId}")
    public ResponseDto<?> getMessageLog(@PathVariable Long roomId,HttpServletRequest request) {
        return chatRoomService.getMessage(roomId, request);
    }

    // 채팅방 나가기
    @DeleteMapping("/api/chat/{roomId}")
    public ResponseDto<?> exitChatRoom(@PathVariable Long roomId, HttpServletRequest request) {
        return chatRoomService.exitChatRoom(roomId, request);
    }
}