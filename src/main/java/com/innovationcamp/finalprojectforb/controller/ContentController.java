package com.innovationcamp.finalprojectforb.controller;

import com.innovationcamp.finalprojectforb.dto.ResponseDto;
import com.innovationcamp.finalprojectforb.dto.roadmap.ContentReqDto;
import com.innovationcamp.finalprojectforb.service.ContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class ContentController {

    private final ContentService contentService;

    //html upload
    @PutMapping("/api/roadmap/html/{htmlId}")
    public ResponseDto<?> createHtmlContent(@PathVariable Long htmlId,
                                            @RequestPart(value = "contentReqDto") ContentReqDto contentReqDto,
                                            @RequestPart(value = "file") MultipartFile file,
                                            HttpServletRequest request) throws IOException {
        return contentService.createHtmlContent(htmlId, contentReqDto, file, request);
    }

    //css upload
    @PutMapping("/api/roadmap/css/{cssId}")
    public ResponseDto<?> createCssContent(@PathVariable Long cssId,
                                           @RequestPart(value = "contentReqDto") ContentReqDto contentReqDto,
                                           @RequestPart(value = "file") MultipartFile file,
                                           HttpServletRequest request) throws IOException {
        return contentService.createCssContent(cssId, contentReqDto, file, request);
    }

    //js upload
    @PutMapping("/api/roadmap/js/{jsId}")
    public ResponseDto<?> createJsContent(@PathVariable Long jsId,
                                          @RequestPart(value = "contentReqDto") ContentReqDto contentReqDto,
                                          @RequestPart(value = "file") MultipartFile file,
                                          HttpServletRequest request) throws IOException {
        return contentService.createJsContent(jsId, contentReqDto, file, request);
    }

    //react upload
    @PutMapping("/api/roadmap/react/{reactId}")
    public ResponseDto<?> createReactContent(@PathVariable Long reactId,
                                             @RequestPart(value = "contentReqDto") ContentReqDto contentReqDto,
                                             @RequestPart(value = "file") MultipartFile file,
                                             HttpServletRequest request) throws IOException {
        return contentService.createReactContent(reactId, contentReqDto, file, request);
    }

    //java upload
    @PutMapping("/api/roadmap/java/{javaId}")
    public ResponseDto<?> createJavaContent(@PathVariable Long javaId,
                                            @RequestPart(value = "contentReqDto") ContentReqDto contentReqDto,
                                            @RequestPart(value = "file") MultipartFile file,
                                            HttpServletRequest request) throws IOException {
        return contentService.createJavaContent(javaId, contentReqDto, file, request);
    }

    //spring upload
    @PutMapping("/api/roadmap/spring/{springId}")
    public ResponseDto<?> createSpringContent(@PathVariable Long springId,
                                              @RequestPart(value = "contentReqDto") ContentReqDto contentReqDto,
                                              @RequestPart(value = "file") MultipartFile file,
                                              HttpServletRequest request) throws IOException {
        return contentService.createSpringContent(springId, contentReqDto, file, request);
    }
}
