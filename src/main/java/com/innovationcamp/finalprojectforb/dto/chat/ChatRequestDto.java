package com.innovationcamp.finalprojectforb.dto.chat;

import lombok.*;

// 처음으로 입력받는 메세지
@Getter
@Setter
@Data
public class ChatRequestDto {
    private Long roomId;
    private String message;
}
