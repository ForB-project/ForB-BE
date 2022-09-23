package com.innovationcamp.finalprojectforb.model.roadmap.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ContentResponseDto {
    private Long id;
    private String title;
    private String link;
    private String thumbnail;
}
