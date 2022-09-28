package com.innovationcamp.finalprojectforb.dto;

import com.innovationcamp.finalprojectforb.model.StackType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestResultResponseDto {

    private Long id;
    private String stackType;
    private String description;

}
