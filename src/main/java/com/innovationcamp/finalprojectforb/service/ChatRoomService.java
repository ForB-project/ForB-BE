package com.innovationcamp.finalprojectforb.service;

import com.innovationcamp.finalprojectforb.dto.ResponseDto;
import com.innovationcamp.finalprojectforb.dto.chat.ChatRoomResDto;
import com.innovationcamp.finalprojectforb.enums.ErrorCode;
import com.innovationcamp.finalprojectforb.jwt.TokenProvider;
import com.innovationcamp.finalprojectforb.model.Member;
import com.innovationcamp.finalprojectforb.model.chat.ChatMember;
import com.innovationcamp.finalprojectforb.model.chat.ChatRoom;
import com.innovationcamp.finalprojectforb.repository.MemberRepository;
import com.innovationcamp.finalprojectforb.repository.chat.ChatMemberRepository;
import com.innovationcamp.finalprojectforb.repository.chat.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Slf4j
@RequiredArgsConstructor
public class ChatRoomService {
    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMemberRepository chatMemberRepository;

    @Transactional
    public ResponseDto<?> createChatRoom(Long targetMemberId, HttpServletRequest request) {
        //멤버아이디는 해당 방 유저
        Member targetMember = isPresentTargetMember(targetMemberId);
        // 토큰으로 '나' 유저찾기
        Member member = validateMember(request);
        if (member == null) {
            return new ResponseDto<>(null, ErrorCode.BAD_TOKEN_REQUEST);
        }

        Member memberIdSet = new Member();
        memberIdSet.getId(targetMember.getId());

        //방만들기 ( roomId + 수신자Id )
        ChatRoom chatRoom = ChatRoom.builder()
                .member(memberIdSet)
                .build();
        chatRoomRepository.save(chatRoom);

        ChatRoom chatRommIdSet = new ChatRoom();
        chatRommIdSet.getId(chatRoom.getId());

        Optional<ChatRoom> findChatMember = chatRoomRepository.findByIdAndMemberId(chatRoom.getId(), targetMemberId);

        // 이미 동일 타겟 멤버와 있는 채팅방이라면 막아야함.
        if (findChatMember == null) {
            return new ResponseDto<>(null, ErrorCode.DUPLICATE_ROOM);
        }
        //챗멤버 추가하기 - 없다면 채팅방 멤버목록에 넣기
        ChatMember chatMember = ChatMember.builder()
                .member(targetMember)
                .chatRoom(chatRommIdSet)
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
