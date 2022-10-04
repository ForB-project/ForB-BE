package com.innovationcamp.finalprojectforb.controller;

import com.innovationcamp.finalprojectforb.dto.CommentRequestDto;
import com.innovationcamp.finalprojectforb.dto.CommentResponseDto;
import com.innovationcamp.finalprojectforb.dto.ResponseDto;
import com.innovationcamp.finalprojectforb.enums.ErrorCode;
import com.innovationcamp.finalprojectforb.model.UserDetailsImpl;
import com.innovationcamp.finalprojectforb.model.Member;
import com.innovationcamp.finalprojectforb.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/api/comment")
    public ResponseDto<List<CommentResponseDto>> getAllComment() {
        List<CommentResponseDto> commentResponseDtoList;
        try {
            commentResponseDtoList = commentService.getAllComment();
        }catch (EntityNotFoundException e){
            log.error(e.getMessage());
            return new ResponseDto<>(null, ErrorCode.ENTITY_NOT_FOUND);
        }catch (Exception e){
            log.error(e.getMessage());
            return new ResponseDto<>(null,ErrorCode.INVALID_ERROR);
        }
        return new ResponseDto<>(commentResponseDtoList);
    }

    @PostMapping("/api/auth/comment/{postId}")
    public ResponseDto<CommentResponseDto> createComment(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody CommentRequestDto requestDto) {
        try {
            Member member = userDetails.getMember();
            return commentService.createComment(postId, requestDto, member);
        }catch (EntityNotFoundException e){
            log.error(e.getMessage());
            return new ResponseDto<>(null, ErrorCode.ENTITY_NOT_FOUND);
        }catch (Exception e){
            log.error(e.getMessage());
            return new ResponseDto<>(null,ErrorCode.INVALID_ERROR);
        }
    }

    @PutMapping("/api/auth/comment/{commentId}")
    public ResponseDto<CommentResponseDto> updateComment(@PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody CommentRequestDto requestDto) {
        try {
            Member member = userDetails.getMember();
            return commentService.updateComment(commentId, requestDto, member);
        }catch (EntityNotFoundException e){
            log.error(e.getMessage());
            return new ResponseDto<>(null, ErrorCode.ENTITY_NOT_FOUND);
        }catch (Exception e){
            log.error(e.getMessage());
            return new ResponseDto<>(null,ErrorCode.INVALID_ERROR);
        }
    }

    @DeleteMapping("/api/auth/comment/{commentId}")
    public ResponseDto<String> deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            Member member = userDetails.getMember();
            commentService.deleteComment(commentId, member);
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
