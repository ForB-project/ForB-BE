package com.innovationcamp.finalprojectforb.dto.chat;

import com.innovationcamp.finalprojectforb.model.Member;
import com.innovationcamp.finalprojectforb.model.chat.ChatMessage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ChatMessageDto {
    private String sender;
    private String message;
    private String sendTime;

    public ChatMessageDto(Member getMember, ChatMessage chatMessage) {
        this.sender = getMember.getNickname();
        this.message = chatMessage.getMessage();
        this.sendTime = chatMessage.getSendTime();
    }

    public ChatMessageDto(Member member, String time) {
        this.sender = member.getNickname();
        this.message = member.getNickname() + "님이 입장하셨습니다.";
        this.sendTime = time;
    }
}