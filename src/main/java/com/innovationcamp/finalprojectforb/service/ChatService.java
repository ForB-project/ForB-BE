package com.innovationcamp.finalprojectforb.service;


import com.innovationcamp.finalprojectforb.dto.ResponseDto;
import com.innovationcamp.finalprojectforb.dto.chat.ChatMessageDto;
import com.innovationcamp.finalprojectforb.dto.chat.ChatRequestDto;
import com.innovationcamp.finalprojectforb.enums.ErrorCode;
import com.innovationcamp.finalprojectforb.jwt.JwtFilter;
import com.innovationcamp.finalprojectforb.jwt.TokenProvider;
import com.innovationcamp.finalprojectforb.model.Member;
import com.innovationcamp.finalprojectforb.model.chat.ChatMember;
import com.innovationcamp.finalprojectforb.model.chat.ChatMessage;
import com.innovationcamp.finalprojectforb.model.chat.ChatRoom;
import com.innovationcamp.finalprojectforb.repository.chat.ChatMemberRepository;
import com.innovationcamp.finalprojectforb.repository.chat.ChatMessageRepository;
import com.innovationcamp.finalprojectforb.repository.chat.ChatRoomRepository;
import com.innovationcamp.finalprojectforb.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {

    private final SimpMessageSendingOperations messageTemplate;
    private final TokenProvider tokenProvider;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatMemberRepository chatMemberRepository;

    private final MemberRepository memberRepository;

    // destination 정보에서 roomId 추출
    public String getRoomId(String destination) {
        int lastIndex = destination.lastIndexOf('/');
        if (lastIndex != -1)
            return destination.substring(lastIndex + 1);
        else
            return "";
    }

    // 이미 채팅방에 있는 멤버인지 확인
//    public ResponseDto<?> getChatMember(Long eventId, HttpServletRequest request) {
//        ResponseDto<?> chkResponse = validateCheck(request);
//        if (!chkResponse.isSuccess())
//            return chkResponse;
//        Member member = memberRepository.findById(((Member) chkResponse.getData()).getId()).orElse(null);
//        assert member != null;  // 동작할일은 없는 코드
//
//        ChatRoom chatRoom = chatRoomRepository.findById(eventId).orElse(null);
//
//        Optional<ChatMember> chatMember = chatMemberRepository.findByMemberAndChatRoom(member, chatRoom);
//        if (chatMember.isPresent())
//            return ResponseDto.fail("이미 존재하는 회원입니다.");
//        return ResponseDto.success("채팅방에 없는 회원입니다.");
//    }

    // 채팅방 입장
    @Transactional
    public ResponseDto<?> enterChatRoom(ChatRequestDto message, String accessor) {
        // 토큰으로 유저찾기
        Member member = validateMember(accessor);

        if (member == null) {
            return new ResponseDto<>(null, ErrorCode.BAD_TOKEN_REQUEST);
        }

        ChatRoom chatRoom = chatRoomRepository.findChatRoomById(message.getRoomId());

        if (chatRoom == null){
            return new ResponseDto<>(null, ErrorCode.NOTFOUND_ROOM);
        }

        // 이미 채팅방에 있는 멤버면 막아야함.
        ChatMember findChatMember = chatMemberRepository.findChatMemberByChatRoomAndMember(chatRoom, member);
        if (findChatMember != null){
            return new ResponseDto<>(null, ErrorCode.DUPLICATE_ROOM);
        }

        // 없다면 채팅방 멤버목록에 넣기
        ChatMember chatMember = new ChatMember(member, chatRoom);
        chatMemberRepository.save(chatMember);

        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 E요일 - a hh:mm "));
        ChatMessageDto chatMessageDto = new ChatMessageDto(member, time);

        // 메세지 보내기
        messageTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), chatMessageDto);

        return ResponseDto.success(member.getNickname()+" 입장 성공");
    }

    // 채팅방 나가기
    @Transactional
    public ResponseDto<?> exitChatRoom(Long roomId, String request) {

        //Member member = memberService.getMember(request);
        Member member = validateMember(request);

        if (member == null) {
            return new ResponseDto<>(null, ErrorCode.BAD_TOKEN_REQUEST);
        }

        ChatRoom chatRoom = chatRoomRepository.findChatRoomById(roomId);

        if (chatRoom == null){
            return new ResponseDto<>(null, ErrorCode.NOTFOUND_ROOM);
        }

        ChatMember chatMember = chatMemberRepository.findChatMemberByChatRoomAndMember(chatRoom, member);
        if (chatMember == null){
            return new ResponseDto<>(null, ErrorCode.INVALID_MEMBER);
        }

        chatMemberRepository.delete(chatMember);

        return ResponseDto.success("나가기 완료");
    }

    // 메세지 보내기
    @Transactional
    public ResponseDto<?> sendMessage(ChatRequestDto message, String token) {
        log.info("서비스단 : 토큰 잘 들어오는지 : ", token);
        // 토큰으로 유저찾기
        Member member = validateMember(token);

        if (member == null) {
            return new ResponseDto<>(null, ErrorCode.BAD_TOKEN_REQUEST);
        }
        log.info("서비스단 : 토큰 검증이 잘 되었는지 : ", member.getId());
        ChatRoom chatRoom = chatRoomRepository.findChatRoomById(message.getRoomId());

        if (chatRoom == null){
            return new ResponseDto<>(null, ErrorCode.NOTFOUND_ROOM);
        }
        log.info("RoomId 가져오는지 : ", chatRoom);
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 E요일 - a hh:mm "));
        ChatMessageDto chatMessageDto = new ChatMessageDto(member, time);
        log.info("time이랑 member 가져오는지 : ", time + member);
        // 메세지 보내기
        messageTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), chatMessageDto);
        log.info("메시지 : " + message.getMessage());
        log.info("룸넘버 : " + message.getRoomId());
        // 보낸 메세지 저장 (db바뀔때 timestamp 없애고 위의 값을 저장하는것으로 바꾸기)
        ChatMessage chatMessage = new ChatMessage(member, chatRoom, message, time);
        log.info("chatMessage 잘 만들어지는지 : " + member, chatRoom, message, time);
        chatMessageRepository.save(chatMessage);

        return ResponseDto.success("success send message!");
    }


    // 기존 채팅방 메세지들 불러오기
    public ResponseDto<?> getMessage(Long roomId, String request) {

        //Member member = memberService.getMember(request);
        Member member = validateMember(request);

        if (member == null) {
            return new ResponseDto<>(null, ErrorCode.BAD_TOKEN_REQUEST);
        }

        ChatRoom chatRoom = chatRoomRepository.findChatRoomById(roomId);

        if (chatRoom == null){
            return new ResponseDto<>(null, ErrorCode.NOTFOUND_ROOM);
        }

        ChatMember chatMember = chatMemberRepository.findChatMemberByChatRoomAndMember(chatRoom, member);
        if (chatMember == null){
            return new ResponseDto<>(null, ErrorCode.INVALID_MEMBER);
        }

        List<ChatMessage> chatMessageList = chatMessageRepository.findAllByChatRoom(chatRoom);
        List<ChatMessageDto> chatMessageDtos = new ArrayList<>();

        for (ChatMessage chatMessage : chatMessageList) {
            Member getMember = chatMessage.getMember();

            chatMessageDtos.add(new ChatMessageDto(getMember, chatMessage));
        }
        return ResponseDto.success(chatMessageDtos);
    }

    public Member validateMember(String jwtToken) {
        if (!tokenProvider.validateToken(jwtToken)) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }

    public void setUserEnterInfo(String sessionId, String roomId) {
    }
//
//    public ResponseDto<?> enterChatRoom(Long user_id, HttpServletRequest request) {
//        ResponseDto<?> result = memberService.checkMember(request);
//        Member member = null;
//        if (result.isSuccess()) {
//            member = (Member) result.getData();
//        }
//
//        Member chatToMember = memberRepository.findMemberById(user_id);
//
//        List<ChatMember> chatMembers = chatMemberRepository.findAllByMember(member);
//
//        for (ChatMember chatMember : chatMembers) {
//            ChatRoom chatRoom = chatMember.getChatRoom();
//            ChatMember isChatRoom = chatMemberRepository.findChatMemberByChatRoomAndMember(chatRoom, chatToMember);
//            if (isChatRoom != null) {
//                ChatRoomResponseDto chatRoomResponseDto = new ChatRoomResponseDto(isChatRoom.getChatRoom().getId());
//                return ResponseDto.success(chatRoomResponseDto);
//            }
//        }
//
//        ChatRoom chatRoom = new ChatRoom();
//        chatRoomRepository.save(chatRoom);
//
//        ChatMember chatMember = new ChatMember(member, chatRoom);
//        ChatMember chatMember1 = new ChatMember(chatToMember, chatRoom);
//        chatMemberRepository.save(chatMember1);
//        chatMemberRepository.save(chatMember);
//
//        ChatRoomResponseDto chatRoomResponseDto = new ChatRoomResponseDto(chatRoom.getId());
//
//        return ResponseDto.success(chatRoomResponseDto);
//    }
//
//}
}