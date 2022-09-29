package com.innovationcamp.finalprojectforb.service;

import com.innovationcamp.finalprojectforb.dto.ResponseDto;
import com.innovationcamp.finalprojectforb.enums.ErrorCode;
import com.innovationcamp.finalprojectforb.jwt.TokenProvider;
import com.innovationcamp.finalprojectforb.model.Heart;
import com.innovationcamp.finalprojectforb.model.Member;
import com.innovationcamp.finalprojectforb.model.roadmap.Content;
import com.innovationcamp.finalprojectforb.repository.HeartRepository;
import com.innovationcamp.finalprojectforb.repository.roadmap.ContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class HeartService {

    private final ContentRepository contentRepository;
    private final HeartRepository heartRepository;
    private final TokenProvider tokenProvider;

    // 좋아요 + 좋아요 해제 둘다 가능
    @Transactional
    public ResponseDto<?> heart(Long contentId, HttpServletRequest request) throws IOException {


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

        List<Heart> reviewHeart = heartRepository.findByMemberIdAndContentId(member.getId(), contentId);


        boolean check = false;//안좋아요
        for (Heart heart : reviewHeart) {
            if (heart.getMember().equals(member)) {//이미 해당 유저가 좋아요 했을 경우
                check = true; //좋아요
                System.out.println("이미 좋아요 한 게시물 입니다.");
                content.setHeartCnt(content.getHeartCnt() - 1);
                heartRepository.delete(heart);//좋아요 해제
                break;
            }
        }
        if (check != true) { // 안좋아요에서 POST할 경우 좋아요로 변경
            System.out.println("좋아요");
            Heart heart = Heart.builder()
                    .member(member)
                    .content(content)
                    .build();
            heartRepository.save(heart);// 좋아요 저장
            content.setHeartCnt(content.getHeartCnt() + 1);
        }
        return ResponseDto.success("좋아요 버튼이 작동됐습니다");
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
}