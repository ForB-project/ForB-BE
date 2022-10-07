package com.innovationcamp.finalprojectforb.controller;

import com.innovationcamp.finalprojectforb.dto.*;
import com.innovationcamp.finalprojectforb.enums.ErrorCode;
import com.innovationcamp.finalprojectforb.model.UserDetailsImpl;
import com.innovationcamp.finalprojectforb.model.Member;
import com.innovationcamp.finalprojectforb.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import javax.persistence.EntityNotFoundException;
import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/api/post")
    public ResponseDto<List<PostResponseDto>> getAllPost(@RequestParam("page") int page, @RequestParam("size") int size) {
        List<PostResponseDto> postResponseDtoList;
        page = page -1;
        try {
            postResponseDtoList = postService.getAllPost(page, size);
        }catch (EntityNotFoundException e){
            log.error(e.getMessage());
            return new ResponseDto<>(null, ErrorCode.ENTITY_NOT_FOUND);
        }catch (Exception e){
            log.error(e.getMessage());
            return new ResponseDto<>(null,ErrorCode.INVALID_ERROR);
        }
        return new ResponseDto<>(postResponseDtoList);
    }

    @GetMapping("/api/post/{postId}")
    public ResponseDto<PostResponseDto> getPost(@PathVariable Long postId) {
        try {
            return postService.getPost(postId);
        }catch (EntityNotFoundException e){
            log.error(e.getMessage());
            return new ResponseDto<>(null, ErrorCode.ENTITY_NOT_FOUND);
        }catch (Exception e){
            log.error(e.getMessage());
            return new ResponseDto<>(null,ErrorCode.INVALID_ERROR);
        }
    }

    @PostMapping("/api/auth/post")
    public ResponseDto<PostResponseDto> createPost(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody PostRequestDto requestDto) {
        try {
            Member member = userDetails.getMember();
            return postService.createPost(requestDto, member);
        }catch (EntityNotFoundException e){
            log.error(e.getMessage());
            return new ResponseDto<>(null, ErrorCode.ENTITY_NOT_FOUND);
        }catch (Exception e){
            log.error(e.getMessage());
            return new ResponseDto<>(null,ErrorCode.INVALID_ERROR);
        }
    }

    @PutMapping("/api/auth/post/{postId}")
    public ResponseDto<PostResponseDto> updatePost(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody PostRequestDto requestDto) {
        try {
            Member member = userDetails.getMember();
            return postService.updatePost(postId, requestDto, member);
        }catch (EntityNotFoundException e){
            log.error(e.getMessage());
            return new ResponseDto<>(null, ErrorCode.ENTITY_NOT_FOUND);
        }catch (Exception e){
            log.error(e.getMessage());
            return new ResponseDto<>(null,ErrorCode.INVALID_ERROR);
        }
    }

    @DeleteMapping("/api/auth/post/{postId}")
    public ResponseDto<String> deletePost(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            Member member = userDetails.getMember();
            postService.deletePost(postId, member);
        }catch (EntityNotFoundException e){
            log.error(e.getMessage());
            return new ResponseDto<>(null, ErrorCode.ENTITY_NOT_FOUND);
        }catch (Exception e){
            log.error(e.getMessage());
            return new ResponseDto<>(null,ErrorCode.INVALID_ERROR);
        }
        return new ResponseDto<>("delete success");
    }
}
