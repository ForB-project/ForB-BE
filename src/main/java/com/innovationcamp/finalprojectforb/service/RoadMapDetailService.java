package com.innovationcamp.finalprojectforb.service;

import com.innovationcamp.finalprojectforb.dto.ResponseDto;
import com.innovationcamp.finalprojectforb.dto.roadmap.*;
import com.innovationcamp.finalprojectforb.jwt.TokenProvider;
import com.innovationcamp.finalprojectforb.model.Member;
import com.innovationcamp.finalprojectforb.model.roadmap.*;
import com.innovationcamp.finalprojectforb.repository.roadmap.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RoadMapDetailService {
    private final TokenProvider tokenProvider;
    private final HtmlRepository htmlRepository;
    private final CssRepository cssRepository;

    private final JsRepository jsRepository;

    private final ReactRepository reactRepository;

    private final SpringRepository springRepository;
    private final ContentRepository contentRepository;


    private final TitleRepository titleRepository;

    //html detail
    public ResponseDto<?> showRoadmapHtml(Long htmlId) {
        Html html = isPresentHtml(htmlId);
//        Member member = validateMember(request);
//
//        if (member == null) {
//            throw new NullPointerException("Token이 유효하지 않습니다.");
//        }
            List<HtmlResponseDto> htmlDtoList = new ArrayList<>();
            List<Content> contentList = contentRepository.findByHtmlId(htmlId);
            List<ContentResponseDto> contentResponseDtoList = new ArrayList<>();

            for (Content content : contentList) {
                contentResponseDtoList.add(
                ContentResponseDto.builder()
                        .id(content.getId())
                        .title(content.getTitle())
                        .link(content.getContentLink())
                        .thumbnail(content.getThumbnail())
                        .desc(content.getDescription())
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

    //js detail
    public ResponseDto<?> showRoadmapJs(Long jsId) {
        Js js = isPresentJs(jsId);
//        Member member = validateMember(request);
//
//        if (member == null) {
//            throw new NullPointerException("Token이 유효하지 않습니다.");
//        }
        List<JsResponseDto> jsResponseDtoList = new ArrayList<>();


        List<Content> contentList = contentRepository.findByJsId(jsId);
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

        jsResponseDtoList.add(
                JsResponseDto.builder()
                        .id(js.getId())
                        .title("JS") // 추후 삭제여부 협의
                        .category(js.getCategory())
                        .contentList(contentResponseDtoList)
                        .build()
        );
        return ResponseDto.success(jsResponseDtoList);
    }

    //react detail
    public ResponseDto<?> showRoadmapReact(Long reactId) {
        React react = isPresentReact(reactId);
//        Member member = validateMember(request);
//
//        if (member == null) {
//            throw new NullPointerException("Token이 유효하지 않습니다.");
//        }
        List<ReactResponseDto> reactResponseDtoList = new ArrayList<>();


        List<Content> contentList = contentRepository.findByReactId(reactId);
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

        reactResponseDtoList.add(
                ReactResponseDto.builder()
                        .id(react.getId())
                        .title("REACT") // 추후 삭제여부 협의
                        .category(react.getCategory())
                        .contentList(contentResponseDtoList)
                        .build()
        );
        return ResponseDto.success(reactResponseDtoList);
    }


    //spring detail
    public ResponseDto<?> showRoadmapSpring(Long springId) {
        Spring spring = isPresentSpring(springId);
//        Member member = validateMember(request);
//
//        if (member == null) {
//            throw new NullPointerException("Token이 유효하지 않습니다.");
//        }
        List<SpringResponseDto> springResponseDtoList = new ArrayList<>();


        List<Content> contentList = contentRepository.findBySpringId(springId);
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

        springResponseDtoList.add(
                SpringResponseDto.builder()
                        .id(spring.getId())
                        .title("SPRING") // 추후 삭제여부 협의
                        .category(spring.getCategory())
                        .contentList(contentResponseDtoList)
                        .build()
        );
        return ResponseDto.success(springResponseDtoList);
    }


    private Html isPresentHtml(Long htmlId) {
        Optional<Html> optionalHtml = htmlRepository.findById(htmlId);
        return optionalHtml.orElse(null);
    }

    private Css isPresentCss(Long cssId) {
        Optional<Css> optionalCss = cssRepository.findById(cssId);
        return optionalCss.orElse(null);
    }

    private Js isPresentJs(Long jsId) {
        Optional<Js> optionalJs = jsRepository.findById(jsId);
        return optionalJs.orElse(null);
    }


    private React isPresentReact(Long reactId) {
        Optional<React> optionalReact = reactRepository.findById(reactId);
        return optionalReact.orElse(null);
    }

    private Spring isPresentSpring(Long springId) {
        Optional<Spring> optionalSpring = springRepository.findById(springId);
        return optionalSpring.orElse(null);
    }

    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }

}
