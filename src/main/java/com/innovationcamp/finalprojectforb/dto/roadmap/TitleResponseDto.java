package com.innovationcamp.finalprojectforb.dto.roadmap;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class TitleResponseDto {
    private Long id;
    private String title;
    private String category;
}
