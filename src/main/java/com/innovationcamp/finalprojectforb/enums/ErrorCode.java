package com.innovationcamp.finalprojectforb.enums;

import lombok.Getter;

@Getter
public enum ErrorCode {

    ENTITY_NOT_FOUND("NOT_FOUND", "데이터가 존재하지 않습니다."),
    NEED_LOGIN("NEED_LOGIN", "로그인이 필요합니다."),
    BAD_TOKEN_REQUEST("BAD_TOKEN_REQUEST", "토큰이 유효하지 않습니다."),
    BAD_TOKEN("BAD_TOKEN", "잘못된 토큰 정보입니다."),
    EXPIRED_TOKEN("EXPIRED_TOKEN", "만료된 JWT 토큰입니다."),
    WRONG_TOKEN("WRONG_TOKEN", "지원되지 않는 JWT 토큰입니다."),
    TOKEN_NOT_FOUND("TOKEN_NOT_FOUND", "존재하지 않는 Token 입니다."),
    DUPLICATED_EMAIL("DUPLICATED_EMAIL", "이미 가입한 이메일 입니다."),
    EMAIL_NOT_FOUND("EMAIL_NOT_FOUND", "이메일 또는 비밀번호를 확인해주세요."),
    MEMBER_NOT_FOUND("MEMBER_NOT_FOUND", "사용자를 찾을 수 없습니다."),
    PASSWORDS_NOT_MATCHED("PASSWORDS_NOT_MATCHED", "이메일 또는 비밀번호를 확인해주세요."),
    NOT_SAME_MEMBER("NOT_SAME_MEMBER", "해당 작성자만 수정이 가능합니다."),
    NOT_SAME_AUTHORITY("NOT_SAME_AUTHORITY", "권한이 일치하지 않습니다"),
    REQUIRE_AUTHORITY("REQUIRE_AUTHORITY", "필요한 권한이 없습니다."),
    INVALID_ERROR("INVALID_ERROR", "에러 발생"),
    //채팅 관련 오류
    NOTFOUND_ROOM("NOTFOUND_ROOM", "Room id가 잘못되었습니다."),
    INVALID_MEMBER("INVALID_MEMBER", "해당 유저는 권한이 없습니다."),
    DUPLICATE_ROOM("DUPLICATE_ROOM", "이미 참여한 채팅방입니다.");

    private final String code;
    private final String message;

    ErrorCode(String code, String message){
        this.code = code;
        this.message = message;
    }

}

