package com.innovationcamp.finalprojectforb.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.innovationcamp.finalprojectforb.config.GoogleConfigUtils;
import com.innovationcamp.finalprojectforb.dto.MemberRequestDto;
import com.innovationcamp.finalprojectforb.dto.NicknameResDto;
import com.innovationcamp.finalprojectforb.dto.ResponseDto;
import com.innovationcamp.finalprojectforb.enums.ErrorCode;
import com.innovationcamp.finalprojectforb.model.Member;
import com.innovationcamp.finalprojectforb.model.UserDetailsImpl;
import com.innovationcamp.finalprojectforb.service.GoogleMemberService;
import com.innovationcamp.finalprojectforb.service.KakaoMemberService;
import com.innovationcamp.finalprojectforb.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final KakaoMemberService kakaoMemberService;
    private final GoogleMemberService googleMemberService;
    private final MemberService memberService;
    private final GoogleConfigUtils googleConfigUtils;


    @GetMapping("/login/oauth2/code/google")
    public ResponseDto<?> GoogleLogin(@RequestParam(value = "code") String authCode, HttpServletResponse response) throws JsonProcessingException {
        return googleMemberService.googleLogin(authCode, response);
    }

    @GetMapping("/api/member/login/kakao")
    public ResponseDto<?> KakaoLogin(@RequestParam("code") String code, HttpServletResponse response) throws JsonProcessingException {
        return kakaoMemberService.kakaoLogin(code, response);
    }

    @PostMapping("/api/auth/member/logout")
    public ResponseDto<?> logout(HttpServletRequest request) {
        return memberService.logoutMember(request);
    }

    @GetMapping("api/member/stackType")
    public ResponseDto<?> getStackType(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            Member member = userDetails.getMember();
            return memberService.getStackType(member);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseDto<>(null, ErrorCode.INVALID_ERROR);
        }
    }

    @PutMapping("/api/member/nickname")
    public ResponseDto<?> updateNickname(@RequestBody NicknameResDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            Member member = userDetails.getMember();
            return memberService.updateNickname(requestDto, member);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseDto<>(null, ErrorCode.INVALID_ERROR);
        }
    }

    @PostMapping("/api/member/signup")
    public ResponseDto<?> signup(@RequestBody MemberRequestDto requestDto) {
        return memberService.createMember(requestDto);
    }

    @PostMapping("/api/member/login")
    public ResponseDto<?> login(@RequestBody MemberRequestDto requestDto, HttpServletResponse response) {
        return memberService.loginMember(requestDto, response);
    }

}