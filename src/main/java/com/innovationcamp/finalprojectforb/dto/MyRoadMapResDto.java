package com.innovationcamp.finalprojectforb.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class MyRoadMapResDto {
    private Long id;
    private String title;
    private String link;
    private String thumbnail;
    private String desc;
    private Long heartCnt;
    private boolean heartCheck;
}
