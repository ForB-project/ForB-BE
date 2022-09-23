package com.innovationcamp.finalprojectforb.service;

import com.innovationcamp.finalprojectforb.dto.ResponseDto;
import com.innovationcamp.finalprojectforb.jwt.TokenProvider;
import com.innovationcamp.finalprojectforb.model.Member;
import com.innovationcamp.finalprojectforb.model.roadmap.Content;
import com.innovationcamp.finalprojectforb.model.roadmap.Css;
import com.innovationcamp.finalprojectforb.model.roadmap.Html;
import com.innovationcamp.finalprojectforb.model.roadmap.dto.CategoryResponseDto;
import com.innovationcamp.finalprojectforb.model.roadmap.dto.ContentResponseDto;
import com.innovationcamp.finalprojectforb.model.roadmap.dto.CssResponseDto;
import com.innovationcamp.finalprojectforb.model.roadmap.dto.HtmlResponseDto;
import com.innovationcamp.finalprojectforb.repository.ContentRepository;
import com.innovationcamp.finalprojectforb.repository.CssRepository;
import com.innovationcamp.finalprojectforb.repository.HtmlRepository;
import com.sun.xml.bind.v2.runtime.reflect.Accessor;
import jdk.jfr.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RoadMapServercie {
    private final TokenProvider tokenProvider;
    private final HtmlRepository htmlRepository;

    private final ContentRepository contentRepository;
    private final CssRepository cssRepository;

    //html detail
    public ResponseDto<?> showRoadmapHtml(Long htmlId) {
        Html html = isPresentHtml(htmlId);
//        Member member = validateMember(request);
//
//        if (member == null) {
//            throw new NullPointerException("Token이 유효하지 않습니다.");
//        }
            List<HtmlResponseDto> htmlDtoList = new ArrayList<>();

            //카테고리가 html아이디별로 들어가있기때음 => 아이디값을 매개변수로 넣어서 확인하기때문에
            //카테고리 리스트를 보여주기엔 어려움
//            List<Html> categoryList = htmlRepository.findAllBydCategory();
//            List<CategoryResponseDto> categoryResponseDtos = new ArrayList<>();
//            for (Html category : categoryList) {
//                categoryResponseDtos.add(
//                        CategoryResponseDto.builder()
//                                .category(category.getCategory())
//                                .build()
//                );
//            }

            List<Content> contentList = contentRepository.findByHtmlId(htmlId);
            List<ContentResponseDto> contentResponseDtoList = new ArrayList<>();

            for (Content content : contentList) {
                contentResponseDtoList.add(
                ContentResponseDto.builder()
                        .id(content.getId())
                        .title(content.getTitle())
                        .link(content.getContentLink())
                        .thumbnail(content.getThumbnail())
                        .build());
            }

            htmlDtoList.add(
                    HtmlResponseDto.builder()
                            .id(html.getId())
                            .title("HTML") // 추후 삭제여부 협의
                            .category(html.getCategory())
                            .contentList(contentResponseDtoList)
                            .build()
            );
        return ResponseDto.success(htmlDtoList);
    }

    //css detail
    public ResponseDto<?> showRoadmapCss(Long cssId) {
        Css css = isPresentCss(cssId);
//        Member member = validateMember(request);
//
//        if (member == null) {
//            throw new NullPointerException("Token이 유효하지 않습니다.");
//        }
        List<CssResponseDto> cssResponseDtoList = new ArrayList<>();


        List<Content> contentList = contentRepository.findByCssId(cssId);
        List<ContentResponseDto> contentResponseDtoList = new ArrayList<>();

        for (Content content : contentList) {
            contentResponseDtoList.add(
                    ContentResponseDto.builder()
                            .id(content.getId())
                            .title(content.getTitle())
                            .link(content.getContentLink())
                            .thumbnail(content.getThumbnail())
                            .build());
        }

        cssResponseDtoList.add(
                CssResponseDto.builder()
                        .id(css.getId())
                        .title("CSS") // 추후 삭제여부 협의
                        .category(css.getCategory())
                        .contentList(contentResponseDtoList)
                        .build()
        );
        return ResponseDto.success(cssResponseDtoList);
    }

    private Css isPresentCss(Long cssId) {
        Optional<Css> optionalCss = cssRepository.findById(cssId);
        return optionalCss.orElse(null);
    }

    private Html isPresentHtml(Long htmlId) {
        Optional<Html> optionalHtml = htmlRepository.findById(htmlId);
        return optionalHtml.orElse(null);
    }

    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }
}
