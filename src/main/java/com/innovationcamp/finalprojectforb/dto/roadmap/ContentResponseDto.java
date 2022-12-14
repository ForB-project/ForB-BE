package com.innovationcamp.finalprojectforb.dto.roadmap;

import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class ContentResponseDto {
    private Long id;
    private String title;
    private String link;
    private String thumbnail;
    private String desc;
    private Long heartCnt;
    private boolean heartCheck;
}
