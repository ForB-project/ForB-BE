package com.innovationcamp.finalprojectforb.service;

import com.innovationcamp.finalprojectforb.dto.ResponseDto;
import com.innovationcamp.finalprojectforb.dto.chat.ChatMemberListResDto;
import com.innovationcamp.finalprojectforb.dto.chat.ChatRoomResDto;
import com.innovationcamp.finalprojectforb.dto.chat.MessageDto;
import com.innovationcamp.finalprojectforb.enums.ErrorCode;
import com.innovationcamp.finalprojectforb.jwt.TokenProvider;
import com.innovationcamp.finalprojectforb.model.Member;
import com.innovationcamp.finalprojectforb.model.chat.ChatMember;
import com.innovationcamp.finalprojectforb.model.chat.ChatMessage;
import com.innovationcamp.finalprojectforb.model.chat.ChatRoom;
import com.innovationcamp.finalprojectforb.repository.MemberRepository;
import com.innovationcamp.finalprojectforb.repository.chat.ChatMemberRepository;
import com.innovationcamp.finalprojectforb.repository.chat.ChatMessageRepository;
import com.innovationcamp.finalprojectforb.repository.chat.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.*;


@Service
@Slf4j
@RequiredArgsConstructor
public class ChatRoomService {
    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMemberRepository chatMemberRepository;
    private final ChatMessageRepository chatMessageRepository;

    // 채팅방 생성
    @Transactional
    public ResponseDto<?> createChatRoom(Long targetMemberId, HttpServletRequest request) {

        // targetMemberId는 수신 유저
        Member targetMember = isPresentTargetMember(targetMemberId);

        // 토큰으로 '나' 발신 유저
        Member member = validateMember(request);
        if (member == null) {
            return new ResponseDto<>(null, ErrorCode.BAD_TOKEN_REQUEST);
        }

        boolean existPubMember = chatRoomRepository.existsByMemberId(member.getId()); //먼저 말 건 사람
        boolean existPubMember2 = chatRoomRepository.existsByMemberId(targetMemberId); //먼저 말 건 사람

        boolean existSubMember = chatMemberRepository.existsByMemberId(targetMemberId); //말걸음을 당한사람
        boolean existSubMember2 = chatMemberRepository.existsByMemberId(member.getId()); //말걸음을 당한사람
        List<ChatRoom> findPubRoom = chatRoomRepository.findByMemberId(member.getId());
        List<ChatMember> findSubRoom = chatMemberRepository.findByMemberId(targetMemberId);

        // 이미 동일한 채팅방!!에 pub/sub 유저가 있다면 방 만들기 취소 => 방만 체크
        if (existPubMember == true && existSubMember == true) {
            for (ChatRoom chatRoom : findPubRoom) {
                for (ChatMember chatMember : findSubRoom) {
                    if (chatRoom.getId() == chatMember.getChatRoom().getId()) {
                        return new ResponseDto<>(null, ErrorCode.DUPLICATE_ROOM);
                    }
                }
            }
        }

        List<ChatRoom> findPubRoom2 = chatRoomRepository.findAll();
        List<ChatMember> findSubRoom2 = chatMemberRepository.findAll();

        // 이미 대화중인 사람하고 다시 대화하지 않기
        if (existPubMember2 == true && existSubMember2 == true) {
            for (ChatRoom chatRoom : findPubRoom2) {
                for (ChatMember chatMember : findSubRoom2) {
                    if (chatRoom.getMember().getId() == targetMemberId && chatMember.getMember().getId() == member.getId()) {
                        return new ResponseDto<>(null, ErrorCode.DUPLICATE_ROOM);
                    }
                }
            }
        }

        Member memberIdSet = new Member();
        memberIdSet.getId(member.getId());

        // 방만들기 ( roomId + 발행(pub)유저 )
        ChatRoom chatRoom = ChatRoom.builder()
                .member(memberIdSet)
                .build();
        chatRoomRepository.save(chatRoom);

        ChatRoom chatRoomIdSet = new ChatRoom();
        chatRoomIdSet.getId(chatRoom.getId());

        // 챗멤버 만들기 ( roomId + 수신(sub)Id )
        ChatMember chatMember = ChatMember.builder()
                .chatRoom(chatRoomIdSet)
                .member(targetMember)
                .build();
        chatMemberRepository.save(chatMember);

        List<ChatRoomResDto> chatRoomResDtos = new ArrayList<>();
        chatRoomResDtos.add(
                ChatRoomResDto.builder()
                        .roomId(chatRoom.getId())
                        .targetMember(targetMember.getNickname())
                        .member(member.getNickname())
                        .build());
        return ResponseDto.success(chatRoomResDtos);

    }

    // 채팅유저 목록 불러오기
    public ResponseDto<?> getChatMembers(HttpServletRequest request) {
        // 토큰으로 '나' 발신 유저
        Member member = validateMember(request);
        if (member == null) {
            return new ResponseDto<>(null, ErrorCode.BAD_TOKEN_REQUEST);
        }
        List<ChatMemberListResDto> chatMemberListResDtoList = new ArrayList<>();

        // pub 유저가 만든 채팅방 넘버 => 해당 채팅방 + sub 유저 불러오기
        List<ChatRoom> findRoomId1 = chatRoomRepository.findAllByMemberId(member.getId());
        for (ChatRoom room : findRoomId1) {
            List<ChatMember> chatList = chatMemberRepository.findAllByChatRoomId(room.getId());
            for (ChatMember chatMemberList : chatList) {
                chatMemberListResDtoList.add(
                        ChatMemberListResDto.builder()
                                .roomId(chatMemberList.getChatRoom().getId())
                                .subMember(chatMemberList.getMember().getNickname())
                                .pubMember(member.getNickname())
                                .build());
            }
        }
        // sub 유저 입장 => 해당 채팅방 + pub 유저 불러오기
        List<ChatMember> findRoomId2 = chatMemberRepository.findAllByMemberId(member.getId());
        for (ChatMember room : findRoomId2) {
            List<ChatRoom> chatList = chatRoomRepository.findAllById(Collections.singleton(room.getChatRoom().getId()));
            for (ChatRoom chatRoomList : chatList) {
                chatMemberListResDtoList.add(
                            ChatMemberListResDto.builder()
                                    .roomId(chatRoomList.getId())
                                    .subMember(member.getNickname())
                                    .pubMember(chatRoomList.getMember().getNickname())
                                    .build());
                }

        }

        return ResponseDto.success(chatMemberListResDtoList);
    }

    // 채팅방 나가기
    @Transactional
    public ResponseDto<?> exitChatRoom(Long roomId, HttpServletRequest request) {
        // 토큰으로 '나' 발신 유저
        Member member = validateMember(request);
        if (member == null) {
            return new ResponseDto<>(null, ErrorCode.BAD_TOKEN_REQUEST);
        }
        // CascadeType으로 ChatRoom 삭제되면 ChatMember도 삭제됨
        ChatRoom chatRoom = chatRoomRepository.findByIdAndMemberId(roomId, member.getId());  //pub 유저 입장
        ChatMember chatMember = chatMemberRepository.findByChatRoomIdAndMemberId(roomId, member.getId()); //sub 유저 입장
        if (chatRoom == null && chatMember == null) {
            return new ResponseDto<>(null, ErrorCode.NOTFOUND_ROOM);
        } else if (chatMember != null) {
            chatRoomRepository.deleteAllById(Collections.singleton(roomId));
        } else if (chatRoom != null) {
            chatRoomRepository.delete(chatRoom);
        }
        return ResponseDto.success("채팅방 나가기 완료");
    }

    // 기존 채팅방 메세지들 불러오기
    public ResponseDto<?> getMessage(Long roomId, HttpServletRequest request) {

        Member member = validateMember(request);
        if (member == null) {
            return new ResponseDto<>(null, ErrorCode.BAD_TOKEN_REQUEST);
        }

        ChatRoom chatRoom = chatRoomRepository.findChatRoomById(roomId);
        if (chatRoom == null) {
            return new ResponseDto<>(null, ErrorCode.NOTFOUND_ROOM);
        }

        List<ChatMessage> chatMessageList = chatMessageRepository.findAllByChatRoomIdOrderBySendTimeAsc(roomId);
        if (chatMessageList == null && chatMessageList.isEmpty()) {
            return new ResponseDto<>(null, ErrorCode.INVALID_MEMBER);
        }

        List<MessageDto> messageDtos = new ArrayList<>();

        for (ChatMessage chatMessage : chatMessageList) {
            messageDtos.add(
                    MessageDto.builder()
                            .me(member.getNickname())
                            .sender(chatMessage.getMemberName())
                            .message(chatMessage.getMessage())
                            .sendTime(chatMessage.getSendTime())
                            .build());
        }
        return ResponseDto.success(messageDtos);
    }

    private Member isPresentTargetMember(Long targetMemberId) {
        Optional<Member> optionalMember = memberRepository.findById(targetMemberId);
        return optionalMember.orElse(null);
    }

    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }
}
