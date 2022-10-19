package com.innovationcamp.finalprojectforb.service;

import com.innovationcamp.finalprojectforb.dto.ResponseDto;
import com.innovationcamp.finalprojectforb.dto.roadmap.ContentReqDto;
import com.innovationcamp.finalprojectforb.enums.ErrorCode;
import com.innovationcamp.finalprojectforb.jwt.TokenProvider;
import com.innovationcamp.finalprojectforb.model.Member;
import com.innovationcamp.finalprojectforb.model.roadmap.*;
import com.innovationcamp.finalprojectforb.repository.roadmap.ContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RequiredArgsConstructor
@Service
public class ContentService {
    private final S3Upload s3Upload;
    private final ContentRepository contentRepository;
    private final TokenProvider tokenProvider;
    //html upload
    public ResponseDto<?> createHtmlContent(Long htmlId,
                                            ContentReqDto contentReqDto,
                                            MultipartFile file,
                                            HttpServletRequest request) throws IOException {
        Member member = validateMember(request);
        if (member == null) {
            return new ResponseDto<>(null, ErrorCode.EXPIRED_TOKEN);
        }

        Html htmlIdSet = new Html();
        htmlIdSet.getId(htmlId);

        Member memberIdSet = new Member();
        memberIdSet.getId(member.getId());

        String image = null;

        if (file != null && !file.isEmpty()) {
            image = s3Upload.uploadFiles(file, "thumbnail"); // dir name: images에 multifile 업로드
        } else if (file == null) {
            image = null;
        }


        Content content = Content.builder()
                .html(htmlIdSet)
                .id(contentReqDto.getId())
                .thumbnail(image)
                .title(contentReqDto.getTitle())
                .contentLink(contentReqDto.getLink())
                .description(contentReqDto.getDesc())
                .heartCnt(0L)
                .member(memberIdSet)
                .build();
        contentRepository.save(content);

        return ResponseDto.success("업로드가 완료되었습니다");
    }

    //css upload
    public ResponseDto<?> createCssContent(Long cssId,
                                           ContentReqDto contentReqDto,
                                           MultipartFile file, HttpServletRequest request) throws IOException {
        Member member = validateMember(request);
        if (member == null) {
            return new ResponseDto<>(null, ErrorCode.EXPIRED_TOKEN);
        }

        Css cssIdSet = new Css();
        cssIdSet.getId(cssId);

        Member memberIdSet = new Member();
        memberIdSet.getId(member.getId());

        String image = null;

        if (file != null && !file.isEmpty()) {
            image = s3Upload.uploadFiles(file, "thumbnail"); // dir name: images에 multifile 업로드
        } else if (file == null) {
            image = null;
        }


        Content content = Content.builder()
                .css(cssIdSet)
                .id(contentReqDto.getId())
                .thumbnail(image)
                .title(contentReqDto.getTitle())
                .contentLink(contentReqDto.getLink())
                .description(contentReqDto.getDesc())
                .heartCnt(0L)
                .member(memberIdSet)
                .build();
        contentRepository.save(content);

        return ResponseDto.success("업로드가 완료되었습니다");
    }

    //js upload
    public ResponseDto<?> createJsContent(Long jsId,
                                          ContentReqDto contentReqDto,
                                          MultipartFile file, HttpServletRequest request) throws IOException {
        Member member = validateMember(request);
        if (member == null) {
            return new ResponseDto<>(null, ErrorCode.EXPIRED_TOKEN);
        }

        Js jsIdSet = new Js();
        jsIdSet.getId(jsId);

        Member memberIdSet = new Member();
        memberIdSet.getId(member.getId());

        String image = null;

        if (file != null && !file.isEmpty()) {
            image = s3Upload.uploadFiles(file, "thumbnail"); // dir name: images에 multifile 업로드
        } else if (file == null) {
            image = null;
        }

        Content content = Content.builder()
                .js(jsIdSet)
                .id(contentReqDto.getId())
                .thumbnail(image)
                .title(contentReqDto.getTitle())
                .contentLink(contentReqDto.getLink())
                .description(contentReqDto.getDesc())
                .heartCnt(0L)
                .member(memberIdSet)
                .build();
        contentRepository.save(content);

        return ResponseDto.success("업로드가 완료되었습니다");

    }

    //react upload
    public ResponseDto<?> createReactContent(Long reactId,
                                             ContentReqDto contentReqDto,
                                             MultipartFile file, HttpServletRequest request) throws IOException {
        Member member = validateMember(request);
        if (member == null) {
            return new ResponseDto<>(null, ErrorCode.EXPIRED_TOKEN);
        }

        React reactIdSet = new React();
        reactIdSet.getId(reactId);

        Member memberIdSet = new Member();
        memberIdSet.getId(member.getId());

        String image = null;

        if (file != null && !file.isEmpty()) {
            image = s3Upload.uploadFiles(file, "thumbnail"); // dir name: images에 multifile 업로드
        } else if (file == null) {
            image = null;
        }

        Content content = Content.builder()
                .react(reactIdSet)
                .id(contentReqDto.getId())
                .thumbnail(image)
                .title(contentReqDto.getTitle())
                .contentLink(contentReqDto.getLink())
                .description(contentReqDto.getDesc())
                .heartCnt(0L)
                .member(memberIdSet)
                .build();
        contentRepository.save(content);

        return ResponseDto.success("업로드가 완료되었습니다");
    }

    //java upload
    public ResponseDto<?> createJavaContent(Long javaId,
                                            ContentReqDto contentReqDto,
                                            MultipartFile file, HttpServletRequest request) throws IOException {
        Member member = validateMember(request);
        if (member == null) {
            return new ResponseDto<>(null, ErrorCode.EXPIRED_TOKEN);
        }

        Java javaIdSet = new Java();
        javaIdSet.getId(javaId);

        Member memberIdSet = new Member();
        memberIdSet.getId(member.getId());

        String image = null;

        if (file != null && !file.isEmpty()) {
            image = s3Upload.uploadFiles(file, "thumbnail"); // dir name: images에 multifile 업로드
        } else if (file == null) {
            image = null;
        }

        Content content = Content.builder()
                .java(javaIdSet)
                .id(contentReqDto.getId())
                .thumbnail(image)
                .title(contentReqDto.getTitle())
                .contentLink(contentReqDto.getLink())
                .description(contentReqDto.getDesc())
                .heartCnt(0L)
                .member(memberIdSet)
                .build();
        contentRepository.save(content);

        return ResponseDto.success("업로드가 완료되었습니다");

    }

    //spring upload
    public ResponseDto<?> createSpringContent(Long springId,
                                              ContentReqDto contentReqDto,
                                              MultipartFile file, HttpServletRequest request) throws IOException {
        Member member = validateMember(request);
        if (member == null) {
            return new ResponseDto<>(null, ErrorCode.EXPIRED_TOKEN);
        }

        Spring springIdSet = new Spring();
        springIdSet.getId(springId);

        Member memberIdSet = new Member();
        memberIdSet.getId(member.getId());

        String image = null;

        if (file != null && !file.isEmpty()) {
            image = s3Upload.uploadFiles(file, "thumbnail"); // dir name: images에 multifile 업로드
        } else if (file == null) {
            image = null;
        }

        Content content = Content.builder()
                .spring(springIdSet)
                .id(contentReqDto.getId())
                .thumbnail(image)
                .title(contentReqDto.getTitle())
                .contentLink(contentReqDto.getLink())
                .description(contentReqDto.getDesc())
                .heartCnt(0L)
                .member(memberIdSet)
                .build();
        contentRepository.save(content);

        return ResponseDto.success("업로드가 완료되었습니다");

    }

    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }
}
