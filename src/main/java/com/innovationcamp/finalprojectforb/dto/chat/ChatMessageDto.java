package com.innovationcamp.finalprojectforb.dto.chat;

import com.innovationcamp.finalprojectforb.model.Member;
import com.innovationcamp.finalprojectforb.model.chat.ChatMessage;
import lombok.*;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter
@Setter
public class ChatMessageDto {
    private String sender;
    private String message;
    private String sendTime;


    public ChatMessageDto(String memberName, String time) {
        this.sender = memberName;
        this.sendTime = time;
    }
}