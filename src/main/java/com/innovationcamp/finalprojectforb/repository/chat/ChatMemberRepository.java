package com.innovationcamp.finalprojectforb.repository.chat;

import com.innovationcamp.finalprojectforb.model.chat.ChatMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMemberRepository extends JpaRepository<ChatMember, Long> {
    List<ChatMember> findAllByChatRoomId(Long chatRoomId);
    List<ChatMember> findAllByMemberId(Long chatRoomId);
    ChatMember findByChatRoomIdAndMemberId(Long chatRoomId, Long memberId);
    boolean existsByMemberId(Long memberId);
    List<ChatMember> findByMemberId(Long targetMemberId);

}
