package com.innovationcamp.finalprojectforb.controller;

import com.innovationcamp.finalprojectforb.dto.ResponseDto;
import com.innovationcamp.finalprojectforb.dto.roadmap.ContentReqDto;
import com.innovationcamp.finalprojectforb.dto.roadmap.ContentResponseDto;
import com.innovationcamp.finalprojectforb.service.UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class UploadController {

    private final UploadService uploadService;

    //html upload
    @PutMapping("/api/roadmap/html/{htmlId}")
    public ResponseDto<?> createHtmlContent(@PathVariable Long htmlId,
                                            @RequestPart(value = "contentReqDto") ContentReqDto contentReqDto,
                                            @RequestPart(value = "file") MultipartFile file,
                                            HttpServletRequest request) throws IOException {
        return uploadService.createHtmlContent(htmlId, contentReqDto, file, request);
    }

    //css upload
    @PutMapping("/api/roadmap/css/{cssId}")
    public ResponseDto<?> createCssContent(@PathVariable Long cssId,
                                           @RequestPart(value = "contentReqDto") ContentReqDto contentReqDto,
                                           @RequestPart(value = "file") MultipartFile file,
                                           HttpServletRequest request) throws IOException {
        return uploadService.createCssContent(cssId, contentReqDto, file, request);
    }

    //js upload
    @PutMapping("/api/roadmap/js/{jsId}")
    public ResponseDto<?> createJsContent(@PathVariable Long jsId,
                                          @RequestPart(value = "contentReqDto") ContentReqDto contentReqDto,
                                          @RequestPart(value = "file") MultipartFile file,
                                          HttpServletRequest request) throws IOException {
        return uploadService.createJsContent(jsId, contentReqDto, file, request);
    }

    //react upload
    @PutMapping("/api/roadmap/react/{reactId}")
    public ResponseDto<?> createReactContent(@PathVariable Long reactId,
                                             @RequestPart(value = "contentReqDto") ContentReqDto contentReqDto,
                                             @RequestPart(value = "file") MultipartFile file,
                                             HttpServletRequest request) throws IOException {
        return uploadService.createReactContent(reactId, contentReqDto, file, request);
    }

    //java upload
    @PutMapping("/api/roadmap/java/{javaId}")
    public ResponseDto<?> createJavaContent(@PathVariable Long javaId,
                                            @RequestPart(value = "contentReqDto") ContentReqDto contentReqDto,
                                            @RequestPart(value = "file") MultipartFile file,
                                            HttpServletRequest request) throws IOException {
        return uploadService.createJavaContent(javaId, contentReqDto, file, request);
    }

    //spring upload
    @PutMapping("/api/roadmap/spring/{springId}")
    public ResponseDto<?> createSpringContent(@PathVariable Long springId,
                                              @RequestPart(value = "contentReqDto") ContentReqDto contentReqDto,
                                              @RequestPart(value = "file") MultipartFile file,
                                              HttpServletRequest request) throws IOException {
        return uploadService.createSpringContent(springId, contentReqDto, file, request);
    }
}
