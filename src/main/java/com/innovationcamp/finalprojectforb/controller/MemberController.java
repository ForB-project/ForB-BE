package com.innovationcamp.finalprojectforb.controller;

import com.innovationcamp.finalprojectforb.dto.MemberRequestDto;
import com.innovationcamp.finalprojectforb.dto.ResponseDto;
import com.innovationcamp.finalprojectforb.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/api/member/signup")
    public ResponseDto<?> signup(@RequestBody MemberRequestDto requestDto) {
        return memberService.createMember(requestDto);
    }

    @PostMapping("/api/member/login")
    public ResponseDto<?> login(@RequestBody MemberRequestDto requestDto, HttpServletResponse response) {
        return memberService.loginMember(requestDto, response);
    }

    @PostMapping("/api/auth/member/logout")
    public ResponseDto<?> logout(HttpServletRequest request) {
        return  memberService.logoutMember(request);
    }
}
