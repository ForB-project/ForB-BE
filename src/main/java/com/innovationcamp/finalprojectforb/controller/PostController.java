package com.innovationcamp.finalprojectforb.controller;

import com.innovationcamp.finalprojectforb.dto.PostRequestDto;
import com.innovationcamp.finalprojectforb.dto.PostResponseDto;
import com.innovationcamp.finalprojectforb.dto.ResponseDto;
import com.innovationcamp.finalprojectforb.enums.ErrorCode;
import com.innovationcamp.finalprojectforb.model.Member;
import com.innovationcamp.finalprojectforb.model.UserDetailsImpl;
import com.innovationcamp.finalprojectforb.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;


@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/api/post")
    public ResponseDto<?> getAllPost(@RequestParam("page") int page, @RequestParam("size") int size) {
        page = page -1;
        Pageable pageable = PageRequest.of(page, size);
        try {
            return postService.getAllPost(pageable);
        }catch (Exception e){
            log.error(e.getMessage());
            return new ResponseDto<>(null,ErrorCode.INVALID_ERROR);
        }
    }

    @GetMapping("/api/post/{postId}")
    public ResponseDto<PostResponseDto> getPost(@PathVariable Long postId, HttpServletRequest request) {
        try {
            return postService.getPost(postId,request);
        }catch (Exception e){
            log.error(e.getMessage());
            return new ResponseDto<>(null,ErrorCode.INVALID_ERROR);
        }
    }

    @GetMapping("/api/post/search")
    public ResponseDto<?> searchPost(@RequestParam(value = "keyword") String keyword,
                                     @RequestParam("page") int page,
                                     @RequestParam("size") int size) {
        page = page -1;
        Pageable pageable = PageRequest.of(page, size);
        try {
            return postService.searchPost(keyword, pageable);
        }catch (Exception e){
            log.error(e.getMessage());
            return new ResponseDto<>(null,ErrorCode.INVALID_ERROR);
        }
    }

    @PostMapping("/api/auth/post")
    public ResponseDto<PostResponseDto> createPost(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                   @RequestPart("data") PostRequestDto requestDto,
                                                   @RequestPart(required = false) MultipartFile image) {
        try {
            Member member = userDetails.getMember();
            return postService.createPost(requestDto, member, image);
        }catch (Exception e){
            log.error(e.getMessage());
            return new ResponseDto<>(null,ErrorCode.INVALID_ERROR);
        }
    }

    @PutMapping("/api/auth/post/{postId}")
    public ResponseDto<PostResponseDto> updatePost(@PathVariable Long postId,
                                                   @AuthenticationPrincipal UserDetailsImpl userDetails,
                                                   @RequestPart("data") PostRequestDto requestDto,
                                                   @RequestPart(required = false) MultipartFile image) {
        try {
            Member member = userDetails.getMember();
            return postService.updatePost(postId, requestDto, member, image);
        }catch (Exception e){
            log.error(e.getMessage());
            return new ResponseDto<>(null,ErrorCode.INVALID_ERROR);
        }
    }

    @DeleteMapping("/api/auth/post/{postId}")
    public ResponseDto<String> deletePost(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            Member member = userDetails.getMember();
            return postService.deletePost(postId, member);
        }catch (Exception e){
            log.error(e.getMessage());
            return new ResponseDto<>(null,ErrorCode.INVALID_ERROR);
        }
    }

}
