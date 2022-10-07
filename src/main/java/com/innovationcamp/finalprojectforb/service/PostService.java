package com.innovationcamp.finalprojectforb.service;

import com.innovationcamp.finalprojectforb.dto.CommentResponseDto;
import com.innovationcamp.finalprojectforb.dto.PostRequestDto;
import com.innovationcamp.finalprojectforb.dto.PostResponseDto;
import com.innovationcamp.finalprojectforb.dto.ResponseDto;
import com.innovationcamp.finalprojectforb.enums.ErrorCode;
import com.innovationcamp.finalprojectforb.exception.CustomException;
import com.innovationcamp.finalprojectforb.model.Comment;
import com.innovationcamp.finalprojectforb.model.Member;
import com.innovationcamp.finalprojectforb.model.Post;
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
@Transactional
public class PostService {
    private final PostRepository postRepository;

    public List<PostResponseDto> getAllPost(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Post> postList = postRepository.findAllByOrderByCreatedAtDesc(pageable);
        List<PostResponseDto> postResponseDtoList = new ArrayList<>();

        for (Post post : postList) {
            postResponseDtoList.add(new PostResponseDto(post));
        }
        return postResponseDtoList;
    }

    public ResponseDto<PostResponseDto> getPost(Long postId) {
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
        Post post = isPresentPost(postId);

        for (Comment comment : post.getComments()) {
            CommentResponseDto commentResponseDto = CommentResponseDto.builder()
                    .postId(postId)
                    .id(comment.getId())
                    .nickname(comment.getMember().getNickname())
                    .content(comment.getContent())
                    .createdAt(comment.getCreatedAt())
                    .build();
            commentResponseDtoList.add(commentResponseDto);
        }
        return ResponseDto.success(
                        PostResponseDto.builder()
                                .id(post.getId())
                                .nickname(post.getMember().getNickname())
                                .title(post.getTitle())
                                .content(post.getContent())
                                .createdAt(post.getCreatedAt())
                                .commentList(commentResponseDtoList)
                                .likes(post.getLikes())
                                .build());
    }

    public ResponseDto<PostResponseDto> createPost(PostRequestDto requestDto, Member member) {
        Post post = new Post(requestDto, member);
        postRepository.save(post);
        return ResponseDto.success(
                PostResponseDto.builder()
                        .id(post.getId())
                        .nickname(member.getNickname())
                        .title(post.getTitle())
                        .content(post.getContent())
                        .createdAt(post.getCreatedAt())
                        .likes(post.getLikes())
                        .build());
    }

    public ResponseDto<PostResponseDto> updatePost(Long postId, PostRequestDto requestDto, Member member) {
        Post post = isPresentPost(postId);
        if (!Objects.equals(post.getMember().getId(), member.getId())) {
            throw new CustomException(ErrorCode.NOT_SAME_MEMBER);
        }
        post.update(requestDto);
        post = postRepository.save(post);
        return ResponseDto.success(
                PostResponseDto.builder()
                        .id(post.getId())
                        .nickname(member.getNickname())
                        .title(post.getTitle())
                        .content(post.getContent())
                        .createdAt(post.getCreatedAt())
                        .likes(post.getLikes())
                        .build());
    }

    public List<PostResponseDto> searchPost(String keyword) {
        List<Post> postList = postRepository.findByTitleContainingOrderByCreatedAtDesc(keyword);
        List<PostResponseDto> postResponseDtoList = new ArrayList<>();

        for (Post post : postList) {
            postResponseDtoList.add(new PostResponseDto(post));
        }
        return postResponseDtoList;
    }

    public void deletePost(Long postId, Member member) {
        Post post = isPresentPost(postId);
        if (!Objects.equals(post.getMember().getId(), member.getId())) {
            throw new CustomException(ErrorCode.NOT_SAME_MEMBER);
        }
        postRepository.delete(post);
    }

    public Post isPresentPost(Long id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        return optionalPost.orElse(null);
    }

}
