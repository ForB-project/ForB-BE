package com.innovationcamp.finalprojectforb.controller;

import com.innovationcamp.finalprojectforb.dto.ResponseDto;
import com.innovationcamp.finalprojectforb.jwt.OauthTokenDto;
import com.innovationcamp.finalprojectforb.service.KakaoMemberService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@AllArgsConstructor
@RequestMapping("/api/oauth2")
public class KakaoController {

    private final KakaoMemberService kakaoMemberService;

    @GetMapping("/kakao")
    public ResponseDto<?> getLogin(@RequestParam("code") String code, HttpServletResponse response) {
        OauthTokenDto oauthTokenDto = kakaoMemberService.getAccessToken(code);
        return kakaoMemberService.saveMember(oauthTokenDto.getAccess_token(), response);
    }
}
