package com.innovationcamp.finalprojectforb.service;

import com.innovationcamp.finalprojectforb.dto.CommentResponseDto;
import com.innovationcamp.finalprojectforb.dto.PostRequestDto;
import com.innovationcamp.finalprojectforb.dto.PostResponseDto;
import com.innovationcamp.finalprojectforb.dto.ResponseDto;
import com.innovationcamp.finalprojectforb.enums.ErrorCode;
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
        List<Post> postList = postPage.getContent(); // slice 인터페이스의 메소드, 조회된 데이터 가져오기
        List<PostResponseDto> postResponseDtoList = postResponseDtoList(postList);
        Long postCount = postPage.getTotalElements(); // page 인터페이스의 메소드, 전체 데이터 수 가져오기
        HashMap<Object,Object> response = new HashMap<>();
        response.put("postList", postResponseDtoList);
        response.put("postCount", postCount);
        return ResponseDto.success(response);
    }

    public ResponseDto<PostResponseDto> getPost(Long postId, HttpServletRequest request) {
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
        Post post = isPresentPost(postId);
        if (post == null) {
            return new ResponseDto<>(null, ErrorCode.ENTITY_NOT_FOUND);
        }

        for (Comment comment : post.getComments()) {
            CommentResponseDto commentResponseDto = CommentResponseDto.builder()
                    .postId(postId)
                    .id(comment.getId())
                    .memberId(comment.getMember().getId())
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

    public ResponseDto<?> searchPost(String keyword, Pageable pageable) {
        Page<Post> postPage = postRepository.findByTitleContainingOrderByCreatedAtDesc(keyword, pageable);
        List<Post> postList = postPage.getContent();
        List<PostResponseDto> postResponseDtoList = postResponseDtoList(postList);
        Long postCount = postPage.getTotalElements();
        HashMap<Object,Object> response = new HashMap<>();
        response.put("postSearchList", postResponseDtoList);
        response.put("postSearchCount", postCount);
        return ResponseDto.success(response);
    }

    @Transactional
    public ResponseDto<PostResponseDto> createPost(PostRequestDto requestDto, Member member, MultipartFile image) {
        //변수 초기화
        String postImage = null;

        if (image != null && !image.isEmpty()) {
            try {
                postImage = s3Upload.uploadFiles(image, "images");
            } catch (IOException e) {
                log.error(e.getMessage());
            }
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
    public ResponseDto<PostResponseDto> updatePost(Long postId, PostRequestDto requestDto, Member member, MultipartFile image) {
        Post post = isPresentPost(postId);
        if (post == null) {
            return new ResponseDto<>(null, ErrorCode.ENTITY_NOT_FOUND);
        }

        if (!Objects.equals(post.getMember().getId(), member.getId())) {
            return new ResponseDto<>(null, ErrorCode.MEMBER_NOT_FOUND);
        }

        String postImage = post.getPostImage();

        if (postImage != null) { // 원래 사진이 있을 경우
            if (image == null || image.isEmpty()) { // 입력 받은 사진이 없으면
                postImage = post.getPostImage(); // 원래 사진 불러오기
            } else if (!image.isEmpty()) { // 입력 받은 사진이 있으면
                try {
                    s3Upload.fileDelete(postImage); // 기존 파일 삭제 하고
                    postImage = s3Upload.uploadFiles(image, "images"); // 입력 받은 사진 저장
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
        } else { // 원래 사진이 없을 경우
            if (image == null || image.isEmpty()) { // 입력 받은 사진이 없으면
                postImage = null;
            } else if (!image.isEmpty()) { // 입력 받은 사진이 있으면
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

    public ResponseDto<String> deletePost(Long postId, Member member) {
        Post post = isPresentPost(postId);
        if (post == null) {
            return new ResponseDto<>(null, ErrorCode.ENTITY_NOT_FOUND);
        }

        if (!Objects.equals(post.getMember().getId(), member.getId())) {
            return new ResponseDto<>(null, ErrorCode.MEMBER_NOT_FOUND);
        }

        String postImage = post.getPostImage();

        if (postImage != null) {
            s3Upload.fileDelete(postImage);
        }
        postRepository.delete(post);
        return new ResponseDto<>("delete success");
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
                .memberId(post.getMember().getId())
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
                    .memberId(post.getMember().getId())
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
