package com.innovationcamp.finalprojectforb.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostMyRoadmapResponseDto {
    private Long id;
    private String nickname;
    private String title;
    private String content;
    private String postImage;
    private LocalDateTime createdAt;
}
