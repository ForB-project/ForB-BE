package com.innovationcamp.finalprojectforb.service;

import com.innovationcamp.finalprojectforb.dto.MyRoadMapResDto;
import com.innovationcamp.finalprojectforb.dto.PostMyRoadmapResponseDto;
import com.innovationcamp.finalprojectforb.dto.ResponseDto;
import com.innovationcamp.finalprojectforb.enums.ErrorCode;
import com.innovationcamp.finalprojectforb.jwt.TokenProvider;
import com.innovationcamp.finalprojectforb.model.Heart;
import com.innovationcamp.finalprojectforb.model.LikePost;
import com.innovationcamp.finalprojectforb.model.Member;
import com.innovationcamp.finalprojectforb.model.Post;
import com.innovationcamp.finalprojectforb.model.roadmap.Content;
import com.innovationcamp.finalprojectforb.repository.HeartRepository;
import com.innovationcamp.finalprojectforb.repository.LikePostRepository;
import com.innovationcamp.finalprojectforb.repository.PostRepository;
import com.innovationcamp.finalprojectforb.repository.roadmap.ContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MyRoadMapService {
    private final TokenProvider tokenProvider;
    private final ContentRepository contentRepository;
    private final PostRepository postRepository;
    private final HeartRepository heartRepository;
    private final LikePostRepository likePostRepository;

    public ResponseDto<List<MyRoadMapResDto>> showMyRoadMap(HttpServletRequest request, Long pathId, int page, int size) {

        Member member = validateMember(request);
        if (member == null) {
            return new ResponseDto<>(null, ErrorCode.EXPIRED_TOKEN);
        }

        List<MyRoadMapResDto> myRoadMapResDtoList = new ArrayList<>();

        if (pathId == 1) { //본인이 업로드한 것
            Pageable pageable = PageRequest.of(page, size);
            Page<Content> contentList = contentRepository.findByMemberIdOrderByIdDesc(member.getId(), pageable);

            makeContentList(member, myRoadMapResDtoList, contentList);

        } else if (pathId == 2) { //본인이 좋아요한 것
            List<Heart> heartList = heartRepository.findByMemberId(member.getId());
            Pageable pageable = PageRequest.of(page, size);
            for (Heart heart : heartList) {
                Page<Content> contentList = contentRepository.findByIdOrderByHeartCntDesc(heart.getContent().getId(), pageable);
                makeContentList(member, myRoadMapResDtoList, contentList);
            }
        }

        return ResponseDto.success(myRoadMapResDtoList);
    }

    public ResponseDto<?> deleteMyRoadMap(HttpServletRequest request,
                                          Long contentId) {
        Member member = validateMember(request);
        if (member == null) {
            return new ResponseDto<>(null, ErrorCode.EXPIRED_TOKEN);
        }
        Content content = isPresentContent(contentId);
        if (!Objects.equals(content.getMember().getId(), member.getId())) {
            return new ResponseDto<>(null, ErrorCode.NOT_SAME_MEMBER);
        }
        contentRepository.delete(content);
        return ResponseDto.success("삭제되었습니다.");
    }

    public ResponseDto<?> showMyPost(HttpServletRequest request, int page, int size) {
        Member member = validateMember(request);
        if (member == null) {
            return new ResponseDto<>(null, ErrorCode.EXPIRED_TOKEN);
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> postPage = postRepository.findByMemberId(member.getId(), pageable);
        List<Post> postList = postPage.getContent();
        List<PostMyRoadmapResponseDto> postResponseDtoList = postMyRoadmapResponseDtoList(postList);
        return ResponseDto.success(postResponseDtoList);
    }

    public ResponseDto<?> showMyLikePost(HttpServletRequest request, int page, int size) {
        Member member = validateMember(request);
        if (member == null) {
            return new ResponseDto<>(null, ErrorCode.EXPIRED_TOKEN);
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<LikePost> likePostPage = likePostRepository.findByMember(member, pageable);
        List<LikePost> likePostList = likePostPage.getContent();
        List<PostMyRoadmapResponseDto> postResponseDtoList = likePostMyRoadmapResponseDtoList(likePostList);
        return ResponseDto.success(postResponseDtoList);
    }

    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }

    private boolean isHeartCheck(Member member, Content content) {
        List<Heart> heartList = heartRepository.findByMemberIdAndContentId(member.getId(), content.getId());
        boolean heartCheck = false;
        if (!heartList.isEmpty()) {
            heartCheck = true;
        }
        return heartCheck;
    }

    private void makeContentList(Member member, List<MyRoadMapResDto> myRoadMapResDtoList, Page<Content> contentList) {
        for (Content content : contentList) {
            boolean heartCheck = isHeartCheck(member, content);
            myRoadMapResDtoList.add(
                    MyRoadMapResDto.builder()
                            .id(content.getId())
                            .title(content.getTitle())
                            .thumbnail(content.getThumbnail())
                            .link(content.getContentLink())
                            .desc(content.getDescription())
                            .heartCnt(content.getHeartCnt())
                            .heartCheck(heartCheck)
                            .build());
        }
    }

    private static List<PostMyRoadmapResponseDto> postMyRoadmapResponseDtoList(List<Post> postList) {
        List<PostMyRoadmapResponseDto> postMyRoadmapResponseDtoList = new ArrayList<>();
        for (Post post : postList) {
            PostMyRoadmapResponseDto postResponseDto = PostMyRoadmapResponseDto.builder()
                    .id(post.getId())
                    .nickname(post.getMember().getNickname())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .postImage(post.getPostImage())
                    .createdAt(post.getCreatedAt())
                    .build();
            postMyRoadmapResponseDtoList.add(postResponseDto);
        }
        return postMyRoadmapResponseDtoList;
    }

    private static List<PostMyRoadmapResponseDto> likePostMyRoadmapResponseDtoList(List<LikePost> likePostList) {
        List<PostMyRoadmapResponseDto> postMyRoadmapResponseDtoList = new ArrayList<>();
        for (LikePost likePost : likePostList) {
            PostMyRoadmapResponseDto postResponseDto = PostMyRoadmapResponseDto.builder()
                    .id(likePost.getPost().getId())
                    .nickname(likePost.getPost().getMember().getNickname())
                    .title(likePost.getPost().getTitle())
                    .content(likePost.getPost().getContent())
                    .postImage(likePost.getPost().getPostImage())
                    .createdAt(likePost.getPost().getCreatedAt())
                    .build();
            postMyRoadmapResponseDtoList.add(postResponseDto);
        }
        return postMyRoadmapResponseDtoList;
    }

    public Content isPresentContent(Long id) {
        Optional<Content> optionalContent = contentRepository.findById(id);
        return optionalContent.orElse(null);
    }

    public Post isPresentPost(Long id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        return optionalPost.orElse(null);
    }
}
