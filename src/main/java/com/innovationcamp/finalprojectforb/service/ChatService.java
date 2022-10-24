package com.innovationcamp.finalprojectforb.service;


import com.innovationcamp.finalprojectforb.dto.ResponseDto;
import com.innovationcamp.finalprojectforb.dto.chat.ChatMessageDto;
import com.innovationcamp.finalprojectforb.dto.chat.ChatRequestDto;
import com.innovationcamp.finalprojectforb.enums.ErrorCode;
import com.innovationcamp.finalprojectforb.jwt.TokenProvider;
import com.innovationcamp.finalprojectforb.model.Member;
import com.innovationcamp.finalprojectforb.model.chat.ChatMessage;
import com.innovationcamp.finalprojectforb.model.chat.ChatRoom;
import com.innovationcamp.finalprojectforb.repository.chat.ChatMessageRepository;
import com.innovationcamp.finalprojectforb.repository.chat.ChatRoomRepository;
import com.innovationcamp.finalprojectforb.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {

    private final SimpMessageSendingOperations messageTemplate;
    private final TokenProvider tokenProvider;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final MemberRepository memberRepository;

    // 메세지 보내기
    @Transactional
    public ResponseDto<?> sendMessage(ChatRequestDto message, String token) {
        log.info("서비스단 : 토큰 잘 넘어오는지 : " + token);
        // 토큰으로 유저찾기
        String memberEmail = tokenProvider.getAuthenticationUsername(token);

        if (memberEmail == null) {
            return new ResponseDto<>(null, ErrorCode.BAD_TOKEN_REQUEST);
        }
        log.info("서비스단 : 토큰 검증 후 이름 : " + memberEmail);

        Optional<Member> findMember = memberRepository.findByEmail(memberEmail);
        log.info("서비스단 : findMember  : " + findMember);

        ChatRoom chatRoom = chatRoomRepository.findChatRoomById(message.getRoomId());

        if (chatRoom == null) {
            return new ResponseDto<>(null, ErrorCode.NOTFOUND_ROOM);
        }
        log.info("RoomId 가져오는지 : " + chatRoom.getId());

        String time = LocalDateTime.now().plusHours(9).format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 E요일 - a hh:mm "));
        ChatMessageDto chatMessageDto = new ChatMessageDto(findMember.get().getNickname(), time);
        log.info("time이랑 member 가져오는지 : " + "시간 : " + time + "멤버닉네임 : " + findMember.get().getNickname());
        log.info("chatMessageDto 가져오는지 : " + chatMessageDto);


        messageTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
        log.info("메시지 : " + message.getMessage());
        log.info("룸넘버 : " + message.getRoomId());

        //보낸 메세지 저장 (db바뀔때 timestamp 없애고 위의 값을 저장하는것으로 바꾸기)
        ChatRoom chatRoomIdSet = new ChatRoom();
        chatRoomIdSet.getId(chatRoom.getId());

        Member memberIdSet = new Member();
        memberIdSet.getId(findMember.get().getId());

        ChatMessage chatMessage = ChatMessage.builder()
                .message(message.getMessage())
                .chatRoom(chatRoomIdSet)
                .memberName(findMember.get().getNickname())
                .member(memberIdSet)
                .sendTime(chatMessageDto.getSendTime())
                .build();
        chatMessageRepository.save(chatMessage);

        return ResponseDto.success("메세지가 보내졌습니다.");
    }
}