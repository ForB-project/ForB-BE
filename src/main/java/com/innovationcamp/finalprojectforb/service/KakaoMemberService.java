package com.innovationcamp.finalprojectforb.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.innovationcamp.finalprojectforb.dto.KakaoMemberInfoDto;
import com.innovationcamp.finalprojectforb.dto.MemberResponseDto;
import com.innovationcamp.finalprojectforb.dto.ResponseDto;
import com.innovationcamp.finalprojectforb.enums.Authority;
import com.innovationcamp.finalprojectforb.dto.TokenDto;
import com.innovationcamp.finalprojectforb.jwt.TokenProvider;
import com.innovationcamp.finalprojectforb.model.UserDetailsImpl;
import com.innovationcamp.finalprojectforb.model.Member;
import com.innovationcamp.finalprojectforb.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class KakaoMemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    @Value("${spring.security.oauth2.client.registration.kakao.authorization-grant-type}") String grant_type;
    @Value("${spring.security.oauth2.client.registration.kakao.client-id}") String client_id;
    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}") String client_secret;
    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}") String redirect_url;
    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}") String token_url;
    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}") String user_info_url;

    // 토큰 발급 요청(POST)
    public ResponseDto<?> kakaoLogin(String code, HttpServletResponse response) throws JsonProcessingException {
        String accessToken = getAccessToken(code);

        KakaoMemberInfoDto kakaoMemberInfo = getKakaoMemberInfo(accessToken);
        Member kakaoMember = registerKakaoUserIfNeed(kakaoMemberInfo);
        forceLogin(kakaoMember);
        kakaoMembersAuthorizationInput(kakaoMember, response);

        MemberResponseDto responseDto = MemberResponseDto.builder()
                .id(kakaoMember.getId())
                .nickname(kakaoMember.getNickname())
                .authority(kakaoMember.getAuthority()).build();

        return new ResponseDto<>(responseDto);

    }

    private String getAccessToken(String code) throws JsonProcessingException{
        // "인가 코드"로 "액세스 토큰" 요청
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", grant_type);
        body.add("client_id", client_id);
        body.add("client_secret", client_secret);
        body.add("redirect_uri", redirect_url);
        body.add("code", code);

        // Http Header 와 Http Body를 하나의 오브젝트에 담기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(body, headers);

        // HTTP 요청 보내기 그리고 response의 응답 받기
        // RestTemplate : 간편하게 rest API 호출할 수 있는 스프링 내장 클래스
        RestTemplate rt = new RestTemplate();

        ResponseEntity<String> response = rt.exchange(
                token_url,
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        // ObjectMapper : json을 자바 객체로.
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        String accessToken = jsonNode.get("access_token").asText();

        return accessToken;
    }
    private KakaoMemberInfoDto getKakaoMemberInfo(String accessToken) throws JsonProcessingException{
        HttpHeaders headers = new HttpHeaders();
        // 토큰으로 카카오 API 호출
        // HTTP Header 생성
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoMemberInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                user_info_url,
                HttpMethod.POST,
                kakaoMemberInfoRequest,
                String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        String nickname = jsonNode.get("properties").get("nickname").asText();
        String email = jsonNode.get("kakao_account").get("email").asText();

        return new KakaoMemberInfoDto(nickname, email);
    }
    private Member registerKakaoUserIfNeed(KakaoMemberInfoDto kakaoMemberInfo){
        String kakaoEmail = kakaoMemberInfo.getEmail();
        Member kakaoMember = memberRepository.findByEmail(kakaoEmail).orElse(null);

        if(kakaoMember == null){
            String nickname = kakaoMemberInfo.getNickname();

            String password = UUID.randomUUID().toString();
            String encodedPassword = passwordEncoder.encode(password);

            Authority authority = Authority.valueOf("ROLE_USER");
            String provider = "kakao";


            kakaoMember = new Member(kakaoEmail, encodedPassword, nickname, authority, provider);
            memberRepository.save(kakaoMember);
        }

        return kakaoMember;
    }
    private void forceLogin(Member kakaoMember) {
        // 강제 로그인 처리
        UserDetails userDetails = new UserDetailsImpl(kakaoMember);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void kakaoMembersAuthorizationInput(Member kakaoMember, HttpServletResponse response) {
        // response header에 token 추가
        TokenDto token = tokenProvider.generateTokenDto(kakaoMember);
        response.addHeader("Authorization", "BEARER " + token.getAccessToken());
        response.addHeader("refresh-token",token.getRefreshToken());
    }

}

