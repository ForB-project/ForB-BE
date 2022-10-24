package com.innovationcamp.finalprojectforb.repository.chat;

import com.innovationcamp.finalprojectforb.model.chat.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    ChatRoom findChatRoomById(Long id);
    ChatRoom findByIdAndMemberId(Long id, Long membrId);
    List<ChatRoom> findByMemberId(Long memberId);
    boolean existsByMemberId(Long memberId);
    List<ChatRoom> findAllByMemberId(Long memberId);

}