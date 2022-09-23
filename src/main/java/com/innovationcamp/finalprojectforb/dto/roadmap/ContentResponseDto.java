package com.innovationcamp.finalprojectforb.dto.roadmap;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ContentResponseDto {
    private Long id;
    private String title;
    private String link;
    private String thumbnail;
    private String desc;
}
