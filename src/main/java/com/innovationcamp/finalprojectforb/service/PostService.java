package com.innovationcamp.finalprojectforb.service;

import com.innovationcamp.finalprojectforb.dto.CommentResponseDto;
import com.innovationcamp.finalprojectforb.dto.PostRequestDto;
import com.innovationcamp.finalprojectforb.dto.PostResponseDto;
import com.innovationcamp.finalprojectforb.dto.ResponseDto;
import com.innovationcamp.finalprojectforb.enums.ErrorCode;
import com.innovationcamp.finalprojectforb.exception.CustomException;
import com.innovationcamp.finalprojectforb.jwt.TokenProvider;
import com.innovationcamp.finalprojectforb.model.Comment;
import com.innovationcamp.finalprojectforb.model.Member;
import com.innovationcamp.finalprojectforb.model.Post;
import com.innovationcamp.finalprojectforb.repository.LikePostRepository;
import com.innovationcamp.finalprojectforb.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PostService {
    private final PostRepository postRepository;
    private final LikePostRepository likePostRepository;
    private final S3Upload s3Upload;
    private final TokenProvider tokenProvider;

    public ResponseDto<?> getAllPost(Pageable pageable) {
        Page<Post> postPage = postRepository.findAllByOrderByCreatedAtDesc(pageable);
        List<Post> postList = postPage.getContent();
        List<PostResponseDto> postResponseDtoList = postResponseDtoList(postList);
        Long postCount = postPage.getTotalElements();
        HashMap<Object,Object> response = new HashMap<>();
        response.put("postList",postResponseDtoList);
        response.put("postCount",postCount);
        return ResponseDto.success(response);
    }

    public ResponseDto<PostResponseDto> getPost(Long postId, HttpServletRequest request) {
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

        //로그인 안 한 멤버일 때
        String accessToken = request.getHeader("Authorization");
        if(accessToken==null) {
            return ResponseDto.success(postResponseDto(post, commentResponseDtoList, false));
        }

        try {
            // 로그인 한 멤버일 때
            Member member = validateMember(request);
            // 멤버가 좋아요를 눌렀는 지 여부를 확인
            boolean checkLike = likePostRepository.existsByMemberAndPost(member, post);
            PostResponseDto postResponseDto = postResponseDto(post, commentResponseDtoList, checkLike);
            return ResponseDto.success(postResponseDto);
        } catch(Exception e){
            return ResponseDto.success(postResponseDto(post, commentResponseDtoList, false));
        }


    }

    @Transactional
    public ResponseDto<PostResponseDto> createPost(PostRequestDto requestDto, Member member, MultipartFile image) throws IOException {
        //변수 초기화
        String postImage = null;

        if (image != null && !image.isEmpty()) {
                postImage = s3Upload.uploadFiles(image, "images"); // dir name: images에 multifile 업로드
        } else if (image == null) {
            postImage = null;
        }

        Post post = new Post(requestDto, member,postImage);
        postRepository.save(post);

        return ResponseDto.success(
                PostResponseDto.builder()
                        .id(post.getId())
                        .nickname(post.getMember().getNickname())
                        .title(post.getTitle())
                        .content(post.getContent())
                        .postImage(postImage)
                        .createdAt(post.getCreatedAt())
                        .likes(post.getLikes())
                        .build());
    }

    @Transactional
    public ResponseDto<PostResponseDto> updatePost(Long postId, PostRequestDto requestDto, Member member, MultipartFile image) throws IOException {
        Post post = isPresentPost(postId);
        Post postFindById = postRepository.findPostById(postId);

        if (!Objects.equals(post.getMember().getId(), member.getId())) {
            throw new CustomException(ErrorCode.NOT_SAME_MEMBER);
        }

        String postImage = postFindById.getPostImage();

        if (postImage != null) {
            if (image == null || image.isEmpty()) {
                postImage = post.getPostImage();
            } else if (!image.isEmpty()) {
                try {
                    s3Upload.fileDelete(postImage);
                    postImage = s3Upload.uploadFiles(image, "images");
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
        } else {
            if (image == null || image.isEmpty()) {
                postImage = null;
            } else if (!image.isEmpty()) {
                try {
                    postImage = s3Upload.uploadFiles(image, "images");
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
        }

        post.update(requestDto, postImage);
        post = postRepository.save(post);
        return ResponseDto.success(
                PostResponseDto.builder()
                        .id(post.getId())
                        .nickname(post.getMember().getNickname())
                        .title(post.getTitle())
                        .content(post.getContent())
                        .postImage(postImage)
                        .createdAt(post.getCreatedAt())
                        .likes(post.getLikes())
                        .build());
    }

    public List<PostResponseDto> searchPost(String keyword) {
        List<Post> postList = postRepository.findByTitleContainingOrderByCreatedAtDesc(keyword);
        List<PostResponseDto> postResponseDtoList = postResponseDtoList(postList);
        return postResponseDtoList;
    }

    public void deletePost(Long postId, Member member) {
        Post post = isPresentPost(postId);
        if (!Objects.equals(post.getMember().getId(), member.getId())) {
            throw new CustomException(ErrorCode.NOT_SAME_MEMBER);
        }

        String postImage = post.getPostImage();

        if (postImage != null) {
            s3Upload.fileDelete(postImage);
        }
        postRepository.delete(post);
    }

    public Post isPresentPost(Long id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        return optionalPost.orElse(null);
    }

    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }

    private static PostResponseDto postResponseDto(Post post,List<CommentResponseDto> commentResponseDtoList, boolean checkLike){
        return PostResponseDto.builder()
                .id(post.getId())
                .nickname(post.getMember().getNickname())
                .title(post.getTitle())
                .content(post.getContent())
                .postImage(post.getPostImage())
                .createdAt(post.getCreatedAt())
                .commentCount(post.getCommentCount())
                .commentList(commentResponseDtoList)
                .likes(post.getLikes())
                .checkLike(checkLike)
                .build();
    }

    private static List<PostResponseDto> postResponseDtoList(List<Post> postList) {
        List<PostResponseDto> responseDtoList = new ArrayList<>();
        for (Post post : postList) {
            PostResponseDto responseDto = PostResponseDto.builder()
                    .id(post.getId())
                    .nickname(post.getMember().getNickname())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .postImage(post.getPostImage())
                    .createdAt(post.getCreatedAt())
                    .commentCount(post.getCommentCount())
                    .likes(post.getLikes())
                    .build();
            responseDtoList.add(responseDto);
        }
        return responseDtoList;
    }
}
