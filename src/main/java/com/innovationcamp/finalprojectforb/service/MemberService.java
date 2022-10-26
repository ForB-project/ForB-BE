package com.innovationcamp.finalprojectforb.service;

import com.innovationcamp.finalprojectforb.dto.*;
import com.innovationcamp.finalprojectforb.enums.Authority;
import com.innovationcamp.finalprojectforb.enums.ErrorCode;
import com.innovationcamp.finalprojectforb.jwt.TokenProvider;
import com.innovationcamp.finalprojectforb.model.Member;
import com.innovationcamp.finalprojectforb.model.TestResult;
import com.innovationcamp.finalprojectforb.repository.MemberRepository;
import com.innovationcamp.finalprojectforb.repository.TestResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final TestResultRepository testResultRepository;

    public ResponseDto<?> createMember(MemberRequestDto requestDto) {

        if(!memberRepository.findByEmail(requestDto.getEmail()).isEmpty()) {
            return new ResponseDto<>(null, ErrorCode.DUPLICATED_EMAIL);
        }
        String[] nickname = requestDto.getEmail().split("@");

        Member member = Member.builder()        //provider <- null
                .email(requestDto.getEmail())
                .nickname(nickname[0])
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .authority(Authority.ROLE_USER)
                .build();

        memberRepository.save(member);

        return new ResponseDto<>("회원가입 완료");
    }

    public ResponseDto<?> loginMember(MemberRequestDto requestDto, HttpServletResponse response) {
        Member member = isPresentMember(requestDto.getEmail());

        if(null == member) {
            return new ResponseDto<>(null,ErrorCode.EMAIL_NOT_FOUND);
        }

        if(!member.validatePassword(passwordEncoder, requestDto.getPassword())) {
            return new ResponseDto<>(null,ErrorCode.PASSWORDS_NOT_MATCHED);
        }

        putToken(member, response);

        MemberResponseDto responseDto = MemberResponseDto.builder()
                .id(member.getId())
                .nickname(member.getNickname())
                .authority(member.getAuthority()).build();

        return new ResponseDto<>(responseDto);
    }

    public ResponseDto<?> logoutMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return new ResponseDto<>(null, ErrorCode.BAD_TOKEN_REQUEST);
        }
        Member member = tokenProvider.getMemberFromAuthentication();
        if (null == member) {
            return new ResponseDto<>(null, ErrorCode.MEMBER_NOT_FOUND);
        }

        return tokenProvider.deleteRefreshToken(member);
    }


    public ResponseDto<?> getStackType(Member member) {
        Member existMember = memberRepository.findMemberById(member.getId());
        List<TestResult> testResultList = testResultRepository.findByStackType(existMember.getStackType());
        List<TestResultResponseDto> testResultResponseDtoList = new ArrayList<>();

        for (TestResult testResult : testResultList) {
            testResultResponseDtoList.add(
                    TestResultResponseDto.builder()
                            .id(testResult.getId())
                            .stackType(testResult.getStackType())
                            .title1(testResult.getTitle1())
                            .title2(testResult.getTitle2())
                            .description1(testResult.getDescription1())
                            .description2(testResult.getDescription2())
                            .build());
        }

        return ResponseDto.success(testResultResponseDtoList);

    }

    public ResponseDto<?> updateNickname(NicknameResDto requestDto, HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return new ResponseDto<>(null, ErrorCode.BAD_TOKEN_REQUEST);
        }
        Member existMember = tokenProvider.getMemberFromAuthentication();
        if (null == existMember) {
            return new ResponseDto<>(null, ErrorCode.MEMBER_NOT_FOUND);
        }
        Member member = memberRepository.findMemberById(existMember.getId());
        member.updateNickname(requestDto);
        member = memberRepository.save(member);

        MemberResponseDto responseDto = MemberResponseDto.builder()
                .id(member.getId())
                .nickname(member.getNickname())
                .stackType(member.getStackType())
                .authority(member.getAuthority()).build();

        return new ResponseDto<>(responseDto);
    }


    @Transactional(readOnly = true)
    public Member isPresentMember(String email) {
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        return optionalMember.orElse(null);
    }

    @Transactional
    public void putToken(Member member, HttpServletResponse response) {
        TokenDto tokenDto = tokenProvider.generateTokenDto(member);
        response.addHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
        response.addHeader("Refresh-Token", tokenDto.getRefreshToken());
        response.addHeader("Access-Token-Expire-Time", tokenDto.getAccessTokenExpiresIn().toString());
    }

}
