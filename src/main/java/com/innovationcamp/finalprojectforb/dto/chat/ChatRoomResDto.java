package com.innovationcamp.finalprojectforb.dto.chat;

import com.innovationcamp.finalprojectforb.model.Member;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ChatRoomResDto {
    private Long roomId;
    private String member;
    private String targetMember;

    public ChatRoomResDto(Long roomId, String member, String targetMember) {
        this.roomId = roomId;
        this.member = member + " 님이 입장하셨습니다.";
        this.targetMember = targetMember;

    }
}

