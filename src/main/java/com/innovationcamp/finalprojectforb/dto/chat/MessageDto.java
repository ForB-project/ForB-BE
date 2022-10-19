package com.innovationcamp.finalprojectforb.dto.chat;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class MessageDto {
    private String me;
    private String sender;
    private String message;
    private String sendTime;
}
