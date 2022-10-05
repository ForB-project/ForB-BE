package com.innovationcamp.finalprojectforb.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.innovationcamp.finalprojectforb.dto.ResponseDto;
import com.innovationcamp.finalprojectforb.dto.roadmap.ContentReqDto;
import com.innovationcamp.finalprojectforb.dto.roadmap.ContentResponseDto;
import com.innovationcamp.finalprojectforb.enums.ErrorCode;
import com.innovationcamp.finalprojectforb.jwt.TokenProvider;
import com.innovationcamp.finalprojectforb.model.Member;
import com.innovationcamp.finalprojectforb.model.roadmap.*;
import com.innovationcamp.finalprojectforb.repository.roadmap.ContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UploadService {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    @Value("${cloud.aws.region.static}")
    private String region;
    @Value("${cloud.aws.s3.dir}")
    private String dir;
    private final AmazonS3Client s3Client;

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

        String image = getImage(file);
        Html htmlIdSet = new Html();
        htmlIdSet.getId(htmlId);
        Member memberIdSet = new Member();
        memberIdSet.getId(member.getId());
        //프론트에서 null값 파일 줄 때 => 파일을 받긴 하되 blob으로 끝나는게 null값
        if (image.endsWith("blob")) {
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

        return ResponseDto.success(
                ContentResponseDto.builder()
                        .id(content.getId())
                        .thumbnail(content.getThumbnail())
                        .title(content.getTitle())
                        .link(content.getContentLink())
                        .desc(content.getDescription())
                        .build());
    }

    //css upload
    public ResponseDto<?> createCssContent(Long cssId,
                                           ContentReqDto contentReqDto,
                                           MultipartFile file, HttpServletRequest request) throws IOException {
        Member member = validateMember(request);
        if (member == null) {
            return new ResponseDto<>(null, ErrorCode.EXPIRED_TOKEN);
        }

        String image = getImage(file);
        Css cssIdSet = new Css();
        cssIdSet.getId(cssId);
        if (image.endsWith("blob")) {
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

        String image = getImage(file);
        Js jsIdSet = new Js();
        jsIdSet.getId(jsId);
        if (image.endsWith("blob")) {
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

        String image = getImage(file);
        React reactIdSet = new React();
        reactIdSet.getId(reactId);
        if (image.endsWith("blob")) {
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

        String image = getImage(file);
        Java javaIdSet = new Java();
        javaIdSet.getId(javaId);
        if (image.endsWith("blob")) {
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

        String image = getImage(file);
        Spring springIdSet = new Spring();
        springIdSet.getId(springId);
        if (image.endsWith("blob")) {
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
                .build();
        contentRepository.save(content);

        return ResponseDto.success("업로드가 완료되었습니다");

    }


    //이미지 업로드 함수
    private String getImage(MultipartFile file) throws IOException {
        String s3FileName = UUID.randomUUID() + "-" + file.getOriginalFilename();

        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentLength(file.getSize());

        s3Client.putObject(bucket, s3FileName, file.getInputStream(), objMeta);

        String httpUrl = s3Client.getUrl(bucket, dir + s3FileName).toString();
        //https://myspartabucket12.s3.ap-northeast-2.amazonaws.com/52af4048-2405-4531-b13d-c6a515896759-beach.jpg",
        String temp = httpUrl.substring(httpUrl.lastIndexOf("/") + 1);
        String answer = "https://s3." + region + ".amazonaws.com/" + bucket + "/" + temp;
        return answer;
    }

    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }
}
