package com.innovationcamp.finalprojectforb.repository.chat;

import com.innovationcamp.finalprojectforb.model.Member;
import com.innovationcamp.finalprojectforb.model.chat.ChatMember;
import com.innovationcamp.finalprojectforb.model.chat.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMemberRepository extends JpaRepository<ChatMember, Long> {
    ChatMember findChatMemberByChatRoomAndMember(ChatRoom chatRoom, Member member);
}
