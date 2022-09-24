package com.innovationcamp.finalprojectforb.dto.roadmap;

import com.innovationcamp.finalprojectforb.model.roadmap.Title;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class TitleListResDto {
    private Long id;
    private String title;
}
