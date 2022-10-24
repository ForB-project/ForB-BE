package com.innovationcamp.finalprojectforb.service;

import com.innovationcamp.finalprojectforb.dto.CommentRequestDto;
import com.innovationcamp.finalprojectforb.dto.CommentResponseDto;
import com.innovationcamp.finalprojectforb.dto.ResponseDto;
import com.innovationcamp.finalprojectforb.enums.ErrorCode;
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

    public ResponseDto<?> getComment(Long postId, int page, int size) {
        Post post = isPresentPost(postId);
        if (post == null) {
            return new ResponseDto<>(null, ErrorCode.ENTITY_NOT_FOUND);
        }
        Pageable pageable = PageRequest.of(page, size);
        List<Comment> commentList = commentRepository.findAllByPostIdOrderByCreatedAtDesc(postId, pageable);
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

        for (Comment comment : commentList) {
            commentResponseDtoList.add(new CommentResponseDto(comment));
        }
        return ResponseDto.success(commentResponseDtoList);
    }

    @Transactional
    public ResponseDto<CommentResponseDto> createComment(Long postId, CommentRequestDto requestDto, Member member) {
        Post post = isPresentPost(postId);
        if (post == null) {
            return new ResponseDto<>(null, ErrorCode.ENTITY_NOT_FOUND);
        }
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
        if (comment == null) {
            return new ResponseDto<>(null, ErrorCode.ENTITY_NOT_FOUND);
        }
        if (!Objects.equals(comment.getMember().getId(), member.getId())) {
            return new ResponseDto<>(null, ErrorCode.MEMBER_NOT_FOUND);
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
    public ResponseDto<String> deleteComment(Long commentId, Member member) {
        Comment comment = isPresentComment(commentId);
        if (comment == null) {
            return new ResponseDto<>(null, ErrorCode.ENTITY_NOT_FOUND);
        }
        if (!Objects.equals(comment.getMember().getId(), member.getId())) {
            return new ResponseDto<>(null, ErrorCode.MEMBER_NOT_FOUND);
        }
        commentRepository.delete(comment);
        Post post = comment.getPost();
        post.updateCommentCount(false);
        return new ResponseDto<>("delete success");
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
