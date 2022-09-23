package com.innovationcamp.finalprojectforb.controller;

import com.innovationcamp.finalprojectforb.dto.CommentRequestDto;
import com.innovationcamp.finalprojectforb.dto.CommentResponseDto;
import com.innovationcamp.finalprojectforb.dto.ResponseDto;
import com.innovationcamp.finalprojectforb.enums.ErrorCode;
import com.innovationcamp.finalprojectforb.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @PostMapping("/api/auth/comment/{roadmapId}")
    public ResponseDto<CommentResponseDto> createComment(@PathVariable Long roadmapId, @RequestBody CommentRequestDto requestDto) {
        CommentResponseDto commentResponseDto;
        try {
            commentResponseDto = commentService.createComment(roadmapId,requestDto);
        }catch (EntityNotFoundException e){
            log.error(e.getMessage());
            return new ResponseDto<>(null, ErrorCode.ENTITY_NOT_FOUND);
        }catch (Exception e){
            log.error(e.getMessage());
            return new ResponseDto<>(null,ErrorCode.INVALID_ERROR);
        }

        return new ResponseDto<>(commentResponseDto);
    }

    @PutMapping("/api/auth/comment/{commentId}")
    public ResponseDto<CommentResponseDto> updateComment(@PathVariable Long commentId, @RequestBody CommentRequestDto requestDto) {
        CommentResponseDto commentResponseDto;
        try {
            commentResponseDto = commentService.updateComment(commentId, requestDto);
        }catch (EntityNotFoundException e){
            log.error(e.getMessage());
            return new ResponseDto<>(null, ErrorCode.ENTITY_NOT_FOUND);
        }catch (Exception e){
            log.error(e.getMessage());
            return new ResponseDto<>(null,ErrorCode.INVALID_ERROR);
        }
        return new ResponseDto<>(commentResponseDto);
    }

    @DeleteMapping("/api/auth/comment/{commentId}")
    public ResponseDto<String> deleteComment(@PathVariable Long commentId) {
        try {
            commentService.deleteComment(commentId);
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
