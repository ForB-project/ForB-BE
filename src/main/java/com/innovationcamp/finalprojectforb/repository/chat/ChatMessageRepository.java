package com.innovationcamp.finalprojectforb.repository.chat;

import com.innovationcamp.finalprojectforb.model.chat.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findAllByChatRoomIdOrderBySendTimeAsc(Long chatRoomId);
}