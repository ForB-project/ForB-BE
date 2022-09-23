package com.innovationcamp.finalprojectforb.model.roadmap.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class HtmlResponseDto {

    private Long id;

    private String title;

    private String category;

    private List<ContentResponseDto> contentList;
}
