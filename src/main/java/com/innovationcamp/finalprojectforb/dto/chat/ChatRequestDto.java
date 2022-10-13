package com.innovationcamp.finalprojectforb.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 처음으로 입력받는 메세지
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ChatRequestDto {
    //    private MessageType type; // 메시지 타입
    private Long roomId;
    private String message;
}
