package com.innovationcamp.finalprojectforb.model.roadmap.dto;

import com.innovationcamp.finalprojectforb.model.roadmap.Content;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class TitleResponseDto {
    private String title;
    private String category;
    private List<Content> contentList;


}
