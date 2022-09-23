package com.innovationcamp.finalprojectforb.service;

import com.innovationcamp.finalprojectforb.dto.CommentRequestDto;
import com.innovationcamp.finalprojectforb.dto.CommentResponseDto;
import com.innovationcamp.finalprojectforb.dto.ResponseDto;
import com.innovationcamp.finalprojectforb.enums.ErrorCode;
import com.innovationcamp.finalprojectforb.exception.CustomException;
import com.innovationcamp.finalprojectforb.jwt.UserDetailsImpl;
import com.innovationcamp.finalprojectforb.model.Comment;
import com.innovationcamp.finalprojectforb.model.Member;
import com.innovationcamp.finalprojectforb.model.MyRoadmap;
import com.innovationcamp.finalprojectforb.repository.CommentRepository;
import com.innovationcamp.finalprojectforb.repository.MyRoadmapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MyRoadmapRepository myRoadmapRepository;

    public List<CommentResponseDto> getAllComment() {
        List<Comment> commentList = commentRepository.findAllByOrderByCreatedAtDesc();

        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
        for (Comment comment : commentList) {
            commentResponseDtoList.add(new CommentResponseDto(comment));
        }
        return commentResponseDtoList;
    }

    public CommentResponseDto createComment(Long roadmapId, CommentRequestDto requestDto) {
        Member member = getMember();
        MyRoadmap myRoadmap = isPresentRoadmap(roadmapId);
        Comment comment = Comment.builder()
                .myRoadmap(myRoadmap)
                .nickname(getMember().getNickname())
                .content(requestDto.getContent())
                .member(member)
                .build();
        commentRepository.save(comment);
        return new CommentResponseDto(comment);
    }

    public CommentResponseDto updateComment(Long commentId, CommentRequestDto requestDto ) {
        Member member = getMember();
        Comment comment = isPresentComment(commentId);
        if (!Objects.equals(comment.getMember().getId(), member.getId())) {
            throw new CustomException(ErrorCode.NOT_SAME_MEMBER);
        }
        comment.update(requestDto);
        comment = commentRepository.save(comment);
        return new CommentResponseDto(comment);
    }

    public void deleteComment(Long commentId) {
        Member member = getMember();
        Comment comment = isPresentComment(commentId);
        if (!Objects.equals(comment.getMember().getId(), member.getId())) {
            throw new CustomException(ErrorCode.NOT_SAME_MEMBER);
        }
        commentRepository.delete(comment);
    }

    public Member getMember(){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getMember();
    }
    public MyRoadmap isPresentRoadmap(Long id) {
        Optional<MyRoadmap> optionalMyRoadmap = myRoadmapRepository.findById(id);
        return optionalMyRoadmap.orElse(null);
    }
    public Comment isPresentComment(Long id) {
        Optional<Comment> optionalComment = commentRepository.findById(id);
        return optionalComment.orElse(null);
    }

}
