package com.innovationcamp.finalprojectforb.service;

import com.innovationcamp.finalprojectforb.dto.CommentRequestDto;
import com.innovationcamp.finalprojectforb.dto.CommentResponseDto;
import com.innovationcamp.finalprojectforb.dto.ResponseDto;
import com.innovationcamp.finalprojectforb.enums.ErrorCode;
import com.innovationcamp.finalprojectforb.exception.CustomException;
import com.innovationcamp.finalprojectforb.model.Comment;
import com.innovationcamp.finalprojectforb.model.Member;
import com.innovationcamp.finalprojectforb.model.Post;
import com.innovationcamp.finalprojectforb.repository.CommentRepository;
import com.innovationcamp.finalprojectforb.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public List<CommentResponseDto> getAllComment() {
        List<Comment> commentList = commentRepository.findAllByOrderByCreatedAtDesc();
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

        for (Comment comment : commentList) {
            commentResponseDtoList.add(new CommentResponseDto(comment));
        }
        return commentResponseDtoList;
    }

    public List<CommentResponseDto> getComment(Long postId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Comment> commentList = commentRepository.findAllByPostIdOrderByCreatedAtDesc(postId, pageable);
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

        for (Comment comment : commentList) {
            commentResponseDtoList.add(new CommentResponseDto(comment));
        }
        return commentResponseDtoList;
    }

    @Transactional
    public ResponseDto<CommentResponseDto> createComment(Long postId, CommentRequestDto requestDto, Member member) {
        Post post = isPresentPost(postId);
        Comment comment = new Comment(requestDto, member, post);
        commentRepository.save(comment);
        post.updateCommentCount(true);
        return ResponseDto.success(
                CommentResponseDto.builder()
                        .postId(postId)
                        .id(comment.getId())
                        .memberId(member.getId())
                        .nickname(member.getNickname())
                        .content(comment.getContent())
                        .createdAt(comment.getCreatedAt())
                        .build());
    }

    @Transactional
    public ResponseDto<CommentResponseDto>updateComment(Long commentId, CommentRequestDto requestDto, Member member) {
        Comment comment = isPresentComment(commentId);
        if (!Objects.equals(comment.getMember().getId(), member.getId())) {
            throw new CustomException(ErrorCode.NOT_SAME_MEMBER);
        }
        comment.update(requestDto);
        comment = commentRepository.save(comment);
        return ResponseDto.success(
                CommentResponseDto.builder()
                        .id(comment.getId())
                        .memberId(member.getId())
                        .nickname(member.getNickname())
                        .content(comment.getContent())
                        .createdAt(comment.getCreatedAt())
                        .build());
    }

    @Transactional
    public void deleteComment(Long commentId, Member member) {
        Comment comment = isPresentComment(commentId);
        if (!Objects.equals(comment.getMember().getId(), member.getId())) {
            throw new CustomException(ErrorCode.NOT_SAME_MEMBER);
        }
        commentRepository.delete(comment);
        Post post = comment.getPost();
        post.updateCommentCount(false);
    }

    public Post isPresentPost(Long id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        return optionalPost.orElse(null);
    }

    public Comment isPresentComment(Long id) {
        Optional<Comment> optionalComment = commentRepository.findById(id);
        return optionalComment.orElse(null);
    }

}
