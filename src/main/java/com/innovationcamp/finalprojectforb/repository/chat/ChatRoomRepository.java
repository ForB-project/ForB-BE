package com.innovationcamp.finalprojectforb.repository.chat;

import com.innovationcamp.finalprojectforb.model.chat.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    ChatRoom findChatRoomById(Long id);
}