package com.innovationcamp.finalprojectforb.service;

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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class HeartService {

    private final ContentRepository contentRepository;
    private final HeartRepository heartRepository;
    private final TokenProvider tokenProvider;
    private final PostRepository postRepository;
    private final LikePostRepository likePostRepository;

    // 좋아요 + 좋아요 해제 둘다 가능
    @Transactional
    public ResponseDto<?> heartContent(Long contentId, HttpServletRequest request) throws IOException {


        if (null == request.getHeader("Authorization")) {
            return new ResponseDto<>(null,ErrorCode.NEED_LOGIN);
        }

        Member member = validateMember(request);
        if (null == member) {
            return new ResponseDto<>(null,ErrorCode.BAD_TOKEN_REQUEST);
        }

        Content content = isPresentContent(contentId);

//        if (null == content) {
//            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 콘텐츠 id 입니다.");
//        }

        List<Heart> heartList = heartRepository.findByMemberIdAndContentId(member.getId(), contentId);


        boolean check = false;//안좋아요
        for (Heart heart : heartList) {
            //이미 해당 유저가 좋아요 했을 경우
            if (Objects.equals(heart.getMember().getId(), member.getId())) {
                check = true; //좋아요
                log.info("이미 좋아요 한 게시물 입니다.");
                heartRepository.delete(heart);//좋아요 해제
                content.setHeartCnt(content.getHeartCnt() - 1L);
                break;
            }
        }
        // 안좋아요에서 POST할 경우 좋아요로 변경
        if (check != true) {
            log.info("좋아요");
            Heart heart = Heart.builder()
                    .member(member)
                    .content(content)
                    .build();
            heartRepository.save(heart);// 좋아요 저장
            content.setHeartCnt(content.getHeartCnt() + 1L);
        }
        return ResponseDto.success("좋아요 버튼이 작동됐습니다");
    }

    @Transactional
    public ResponseDto<?> LikePost(Long postId, HttpServletRequest request) {

        Member member = validateMember(request);
        if (null == member) {
            return new ResponseDto<>(null,ErrorCode.MEMBER_NOT_FOUND);
        }

        Post post = isPresentPost(postId);

        LikePost checkLike = likePostRepository.findByPostIdAndMemberId(post.getId(), member.getId());
        if (checkLike == null) {
            // like 등록
            LikePost likePost = LikePost.builder()
                    .member(member)
                    .post(post)
                    .build();
            likePostRepository.save(likePost);
        } else {
            //like 취소
            likePostRepository.deleteById(checkLike.getId());
        }

        // 해당 게시물 likes 업데이트
        Long likes = likePostRepository.countAllByPostId(post.getId());
        post.updateLikes(likes);

        if (checkLike == null) {
            return ResponseDto.success("like post success");
        } else {
            return ResponseDto.success("like post cancel");
        }
    }

    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }

    public Content isPresentContent(Long contentId) {
        Optional<Content> optionalContent = contentRepository.findById(contentId);
        return optionalContent.orElse(null);
    }

    public Post isPresentPost(Long postId) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        return optionalPost.orElse(null);
    }
}