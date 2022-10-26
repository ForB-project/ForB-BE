package com.innovationcamp.finalprojectforb.model.chat;


import com.innovationcamp.finalprojectforb.model.Member;
import com.innovationcamp.finalprojectforb.model.Timestamped;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ChatRoom extends Timestamped {
    // 채팅방 번호
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 수신자
    @OneToMany(mappedBy = "chatRoom", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMember> chatMember;

    // 채팅메시지
    @OneToMany(mappedBy = "chatRoom", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMessage> chatMessage;

    //발신자
    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private Member member;

    public Long getId(Long chatRoomId) {
        this.id = chatRoomId;
        return chatRoomId;
    }
}