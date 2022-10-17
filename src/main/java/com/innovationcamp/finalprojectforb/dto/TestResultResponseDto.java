package com.innovationcamp.finalprojectforb.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestResultResponseDto {
    private Long id;
    private String stackType;
    private String title;
    private String description1;
    private String description2;

}
