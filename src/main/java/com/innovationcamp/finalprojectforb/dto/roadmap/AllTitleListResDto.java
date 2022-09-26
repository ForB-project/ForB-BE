package com.innovationcamp.finalprojectforb.dto.roadmap;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
@Builder
public class AllTitleListResDto {
    List<TitleListResDto> frontList;
    List<TitleListResDto> backList;
}
