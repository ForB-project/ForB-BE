package com.innovationcamp.finalprojectforb.model.chat;
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
    private Long id;

    // 챗 멤버 객체
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMember> chatMember;
}