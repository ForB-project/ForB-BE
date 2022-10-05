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
import org.springframework.data.domain.Sort;
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

    public ResponseDto<List<MyRoadMapResDto>> showMyRoadMap(HttpServletRequest request, int page, int size, String sortBy) {

        Member member = validateMember(request);
        if (member == null) {
            return new ResponseDto<>(null, ErrorCode.BAD_TOKEN_REQUEST);
        }

        Sort sort = Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Content> contentList = contentRepository.findByMemberId(member.getId(), pageable);

        List<MyRoadMapResDto> myRoadMapResDtoList = new ArrayList<>();
        makeContentList(member, contentList, myRoadMapResDtoList);
        return ResponseDto.success(myRoadMapResDtoList);
    }


    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }

    private boolean isHeartCheck(Member member) {
        List<Heart> heartList = heartRepository.findByMemberId(member.getId());
        boolean heartCheck = false;
        if (!heartList.isEmpty()) {
            heartCheck = true;
        }
        return heartCheck;
    }

    private void makeContentList(Member member, Page<Content> contentList, List<MyRoadMapResDto> myRoadMapResDtoList) {
        for (Content content : contentList) {
            boolean heartCheck = isHeartCheck(member);
            myRoadMapResDtoList.add(
                    MyRoadMapResDto.builder()
                            .id(content.getId())
                            .title(content.getTitle())
                            .link(content.getContentLink())
                            .thumbnail(content.getThumbnail())
                            .desc(content.getDescription())
                            .heartCnt(content.getHeartCnt())
                            .heartCheck(heartCheck)
                            .build());
        }
    }
}
