package com.innovationcamp.finalprojectforb.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.innovationcamp.finalprojectforb.config.GoogleConfigUtils;
import com.innovationcamp.finalprojectforb.dto.google.GoogleLoginDto;
import com.innovationcamp.finalprojectforb.dto.MemberResponseDto;
import com.innovationcamp.finalprojectforb.dto.ResponseDto;
import com.innovationcamp.finalprojectforb.enums.Authority;
import com.innovationcamp.finalprojectforb.dto.TokenDto;
import com.innovationcamp.finalprojectforb.jwt.TokenProvider;
import com.innovationcamp.finalprojectforb.model.UserDetailsImpl;
import com.innovationcamp.finalprojectforb.model.Member;
import com.innovationcamp.finalprojectforb.repository.MemberRepository;
import com.innovationcamp.finalprojectforb.dto.google.GoogleLoginRequestDto;
import com.innovationcamp.finalprojectforb.dto.google.GoogleLoginResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GoogleMemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final GoogleConfigUtils googleConfigUtils;
    private final TokenProvider tokenProvider;

    public ResponseDto<?> googleLogin(String authCode, HttpServletResponse response) throws JsonProcessingException {
        GoogleLoginDto userInfo = getGoogleUserInfo(authCode);

        Member googleMember = signupGoogleUserIfNeeded(userInfo);

        forceLogin(googleMember);

        TokenDto tokenDto = tokenProvider.generateTokenDto(googleMember);
        response.addHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
        response.addHeader("refresh-token",tokenDto.getRefreshToken());

        MemberResponseDto responseDto = MemberResponseDto.builder()
                .id(googleMember.getId())
                .nickname(googleMember.getNickname())
                .authority(googleMember.getAuthority()).build();

        return new ResponseDto<>(responseDto);
    }


    private GoogleLoginDto getGoogleUserInfo(String authCode) throws JsonProcessingException {
        // HTTP 통신을 위해 RestTemplate 활용
        RestTemplate restTemplate = new RestTemplate();
        GoogleLoginRequestDto requestParams = GoogleLoginRequestDto.builder()
                .clientId(googleConfigUtils.getGoogleClientId())
                .clientSecret(googleConfigUtils.getGoogleSecret())
                .code(authCode)
                .redirectUri(googleConfigUtils.getGoogleRedirectUri())
                .grantType("authorization_code")
                .build();

        // Http Header 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<GoogleLoginRequestDto> httpRequestEntity = new HttpEntity<>(requestParams, headers);
        ResponseEntity<String> apiResponseJson = restTemplate.postForEntity(googleConfigUtils.getGoogleAuthUrl() + "/token", httpRequestEntity, String.class);

        // ObjectMapper를 통해 String to Object로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL); // NULL이 아닌 값만 응답받기(NULL인 경우는 생략)
        GoogleLoginResponseDto googleLoginResponse = objectMapper.readValue(apiResponseJson.getBody(), new TypeReference<GoogleLoginResponseDto>() {});

        // 사용자의 정보는 JWT Token으로 저장되어 있고, Id_Token에 값을 저장한다.
        String jwtToken = googleLoginResponse.getIdToken();

        // JWT Token을 전달해 JWT 저장된 사용자 정보 확인
        String requestUrl = UriComponentsBuilder.fromHttpUrl(googleConfigUtils.getGoogleAuthUrl() + "/tokeninfo").queryParam("id_token", jwtToken).toUriString();

        String resultJson = restTemplate.getForObject(requestUrl, String.class);
        GoogleLoginDto userInfoDto = objectMapper.readValue(resultJson, new TypeReference<GoogleLoginDto>() {});

        return userInfoDto;
        }

    private Member signupGoogleUserIfNeeded(GoogleLoginDto userInfo) {
        String email = userInfo.getEmail();
        Member googleMember = memberRepository.findByEmail(email).orElse(null);

        if (googleMember == null) { // 회원가입
            String nickname = userInfo.getName();

            String password = UUID.randomUUID().toString();
            String encodedPassword = passwordEncoder.encode(password);

            Authority authority = Authority.valueOf("ROLE_USER");
            String provider = "google";


            googleMember = new Member(email, encodedPassword, nickname, authority, provider);
            memberRepository.save(googleMember);
        }
        return googleMember;
    }

    private void forceLogin(Member googleMember) {
        UserDetails userDetails = new UserDetailsImpl(googleMember);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
