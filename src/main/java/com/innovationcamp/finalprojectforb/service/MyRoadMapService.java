package com.innovationcamp.finalprojectforb.service;

import com.innovationcamp.finalprojectforb.dto.MyRoadMapResDto;
import com.innovationcamp.finalprojectforb.dto.ResponseDto;
import com.innovationcamp.finalprojectforb.enums.ErrorCode;
import com.innovationcamp.finalprojectforb.jwt.TokenProvider;
import com.innovationcamp.finalprojectforb.model.Heart;
import com.innovationcamp.finalprojectforb.model.Member;
import com.innovationcamp.finalprojectforb.model.roadmap.Content;
import com.innovationcamp.finalprojectforb.repository.HeartRepository;
import com.innovationcamp.finalprojectforb.repository.roadmap.ContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MyRoadMapService {
    private final TokenProvider tokenProvider;
    private final ContentRepository contentRepository;

    private final HeartRepository heartRepository;

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
}
