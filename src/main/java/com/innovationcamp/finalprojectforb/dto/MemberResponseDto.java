package com.innovationcamp.finalprojectforb.dto;

import com.innovationcamp.finalprojectforb.enums.Authority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MemberResponseDto {
    private final Long id;
    private final String nickname;
    private final String stackType;
    private final Authority authority;
}

