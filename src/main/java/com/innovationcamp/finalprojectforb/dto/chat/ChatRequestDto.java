package com.innovationcamp.finalprojectforb.dto.chat;

import lombok.*;

// 처음으로 입력받는 메세지
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString //toString()메서드를 재정의(Override)해줌
public class ChatRequestDto {
    //    private MessageType type; // 메시지 타입
    private Long roomId;
    private String message;
}
