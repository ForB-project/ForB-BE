package com.innovationcamp.finalprojectforb.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.innovationcamp.finalprojectforb.config.GoogleConfigUtils;
import com.innovationcamp.finalprojectforb.dto.GoogleLoginDto;
import com.innovationcamp.finalprojectforb.service.GoogleUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;

@RequiredArgsConstructor
@RestController
public class GoogleController {
    private final GoogleUserService googleUserService;

    private final GoogleConfigUtils googleConfigUtils;


    // 버튼 누르면 구글 로그인 폼 나옴
    @GetMapping(value = "/api/user/login/google")
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
    public ResponseEntity<?> redirectGoogleLogin(@RequestParam(value = "code") String authCode, HttpServletResponse response) throws JsonProcessingException {
        return googleUserService.googleLogin(authCode, response);
    }
}
