package com.innovationcamp.finalprojectforb.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.innovationcamp.finalprojectforb.config.GoogleConfigUtils;
import com.innovationcamp.finalprojectforb.dto.MemberRequestDto;
import com.innovationcamp.finalprojectforb.dto.ResponseDto;
import com.innovationcamp.finalprojectforb.service.GoogleMemberService;
import com.innovationcamp.finalprojectforb.service.KakaoMemberService;
import com.innovationcamp.finalprojectforb.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final KakaoMemberService kakaoMemberService;
    private final GoogleMemberService googleMemberService;
    private final MemberService memberService;

    private final GoogleConfigUtils googleConfigUtils;

    // 로그인 폼으로 이동 ==> 백에서 인가코드 받기
    @GetMapping(value = "/api/member/login/google")
    public ResponseEntity<Object> moveGoogleInitUrl() {
        String authUrl = googleConfigUtils.googleInitUrl();
        URI redirectUri;
        try {
            redirectUri = new URI(authUrl);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setLocation(redirectUri);
            return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return ResponseEntity.badRequest().build();
    }

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
        return  memberService.logoutMember(request);
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
