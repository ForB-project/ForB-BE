package com.innovationcamp.finalprojectforb.service;

import com.innovationcamp.finalprojectforb.dto.ResponseDto;
import com.innovationcamp.finalprojectforb.dto.roadmap.*;
import com.innovationcamp.finalprojectforb.enums.ErrorCode;
import com.innovationcamp.finalprojectforb.jwt.TokenProvider;
import com.innovationcamp.finalprojectforb.model.Heart;
import com.innovationcamp.finalprojectforb.model.Member;
import com.innovationcamp.finalprojectforb.model.roadmap.*;
import com.innovationcamp.finalprojectforb.repository.HeartRepository;
import com.innovationcamp.finalprojectforb.repository.roadmap.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RoadMapService {
    private final TokenProvider tokenProvider;
    private final HtmlRepository htmlRepository;
    private final CssRepository cssRepository;

    private final JsRepository jsRepository;

    private final ReactRepository reactRepository;
    private final JavaRepository javaRepository;

    private final SpringRepository springRepository;

    private final TitleRepository titleRepository;

    private final ContentRepository contentRepository;
    private final HeartRepository heartRepository;

    //타이틀 나타내기
    public ResponseDto<?> showTitle() {
        List<Title> frontList = titleRepository.findTop4ByOrderById();
        List<Title> backList = titleRepository.findAllByIdNot(3L);
        //프론트엔드 백엔드 리스트 각각 반환
        List<TitleListResDto> frontListResDtos = new ArrayList<>();
        List<TitleListResDto> backListResDtos = new ArrayList<>();
        //둘다
        List<AllTitleListResDto> allTitleListResDtoList = new ArrayList<>();
        for (Title title : frontList) {
            frontListResDtos.add(
                    TitleListResDto.builder()
                            .id(title.getId())
                            .title(title.getTitle())
                            .build());
        }
        for (Title title : backList) {
            backListResDtos.add(
                    TitleListResDto.builder()
                            .id(title.getId())
                            .title(title.getTitle())
                            .build());
            if (title.getId() == 4L) {
                backListResDtos.remove(2);
            }
        }
        allTitleListResDtoList.add(
                AllTitleListResDto.builder()
                        .frontList(frontListResDtos)
                        .backList(backListResDtos)
                        .build());
        return ResponseDto.success(allTitleListResDtoList);
    }

    //타이틀별 카테고리 나열하기
    public ResponseDto<?> showCategory(Long titleId) {
        Title title = isCategory(titleId);
        List<TitleResponseDto> titleResponseDtoList = new ArrayList<>();

        List<Html> htmlList = htmlRepository.findAll();
        List<Css> cssList = cssRepository.findAll();
        List<Js> jsList = jsRepository.findAll();
        List<React> reactList = reactRepository.findAll();
        List<Java> javaList = javaRepository.findAll();
        List<Spring> springList = springRepository.findAll();

        if (titleId == 1) {
            for (Html html : htmlList) {
                titleResponseDtoList.add(
                        TitleResponseDto.builder()
                                .id(html.getId())
                                .title(title.getTitle())
                                .category(html.getCategory())
                                .build());
            }
        } else if (titleId == 2) {
            for (Css css : cssList) {
                titleResponseDtoList.add(
                        TitleResponseDto.builder()
                                .id(css.getId())
                                .title(title.getTitle())
                                .category(css.getCategory())
                                .build());

            }
        } else if (titleId == 3) {
            for (Js js : jsList) {
                titleResponseDtoList.add(
                        TitleResponseDto.builder()
                                .id(js.getId())
                                .title(title.getTitle())
                                .category(js.getCategory())
                                .build());

            }
        } else if (titleId == 4) {
            for (React react : reactList) {
                titleResponseDtoList.add(
                        TitleResponseDto.builder()
                                .id(react.getId())
                                .title(title.getTitle())
                                .category(react.getCategory())
                                .build());

            }
        } else if (titleId == 5) {
            for (Java java : javaList) {
                titleResponseDtoList.add(
                        TitleResponseDto.builder()
                                .id(java.getId())
                                .title(title.getTitle())
                                .category(java.getCategory())
                                .build());

            }
        } else if (titleId == 6) {
            for (Spring spring : springList) {
                titleResponseDtoList.add(
                        TitleResponseDto.builder()
                                .id(spring.getId())
                                .title(title.getTitle())
                                .category(spring.getCategory())
                                .build());

            }
        }
        return ResponseDto.success(titleResponseDtoList);
    }


    //타이틀명으로 검색하기
    public ResponseDto<?> searchContents(String keyword, HttpServletRequest request) {
        Member member = validateMember(request);
        if (member == null) {
            return new ResponseDto<>(null, ErrorCode.BAD_TOKEN_REQUEST);
        }

        List<Content> contentList = contentRepository.findByTitleContaining(keyword);
        List<ContentResponseDto> contentResponseDtos = new ArrayList<>();


        for (Content content : contentList) {
            List<Heart> heartList = heartRepository.findByMemberIdAndContentId(member.getId(),content.getId());
            boolean heartCheck = false;
            if (!heartList.isEmpty()) {
                heartCheck = true;
            }
            contentResponseDtos.add(
                    ContentResponseDto.builder()
                            .id(content.getId())
                            .title(content.getTitle())
                            .link(content.getContentLink())
                            .thumbnail(content.getThumbnail())
                            .desc(content.getDescription())
                            .heartCheck(heartCheck)
                            .heartCnt(content.getHeartCnt())
                            .build());
        }
        return ResponseDto.success(contentResponseDtos);
    }


    private Title isCategory(Long titleId) {
        Optional<Title> optionalTitle = titleRepository.findById(titleId);
        return optionalTitle.orElse(null);
    }

    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }
}
