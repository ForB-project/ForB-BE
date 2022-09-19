package com.innovationcamp.finalprojectforb.enums;

import lombok.Getter;

@Getter
public enum ErrorCode {


    ENTITY_NOT_FOUND("NOT_FOUND", "데이터가 존재하지 않습니다."),

    NEED_LOGIN("NEED_LOGIN", "로그인이 필요합니다."),

    BAD_TOKEN_REQUEST("BAD_TOKEN_REQUEST", "토큰이 유효하지 않습니다."),

    TOKEN_NOT_FOUND("TOKEN_NOT_FOUND", "존재하지 않는 Token 입니다."),

    DUPLICATED_EMAIL("DUPLICATED_EMAIL", "이미 가입한 이메일 입니다."),

    DUPLICATED_NICKNAME("DUPLICATED_NICKNAME","중복된 닉네임 입니다."),

    EMAIL_NOT_FOUND("EMAIL_NOT_FOUND", "이메일 또는 비밀번호를 확인해주세요."),

    MEMBER_NOT_FOUND("MEMBER_NOT_FOUND", "사용자를 찾을 수 없습니다."),

    PASSWORDS_NOT_MATCHED("PASSWORDS_NOT_MATCHED", "이메일 또는 비밀번호를 확인해주세요."),


    NOT_SAME_MEMBER("NOT_SAME_MEMBER", "해당 작성자만 수정이 가능합니다."),

    INVALID_ERROR("INVALID_ERROR", "에러 발생"),

    USED_EMAIL("USED_EMAIL", "이미 동일한 이메일 주소로 가입된 정보가 있습니다.");

//  NICKNAME_NOT_EXIST("NICKNAME_NOT_EXIST", "사용자를 찾을 수 없습니다.");



    private final String code;

    private final String message;

    ErrorCode(String code, String message){
        this.code = code;
        this.message = message;
    }
}

