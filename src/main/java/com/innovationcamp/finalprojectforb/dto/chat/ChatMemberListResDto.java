package com.innovationcamp.finalprojectforb.dto.chat;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ChatMemberListResDto {
    private Long roomId;
    private String subMember;
    private String pubMember;
}
