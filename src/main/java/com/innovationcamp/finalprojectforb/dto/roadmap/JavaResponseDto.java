package com.innovationcamp.finalprojectforb.dto.roadmap;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
@Builder
public class JavaResponseDto {
    private Long id;

    private String title;

    private String category;

    private List<ContentResponseDto> contentList;
}
