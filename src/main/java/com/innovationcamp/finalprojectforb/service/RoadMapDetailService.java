package com.innovationcamp.finalprojectforb.service;

import com.innovationcamp.finalprojectforb.dto.ResponseDto;
import com.innovationcamp.finalprojectforb.dto.roadmap.*;
import com.innovationcamp.finalprojectforb.jwt.TokenProvider;
import com.innovationcamp.finalprojectforb.model.Member;
import com.innovationcamp.finalprojectforb.model.roadmap.*;
import com.innovationcamp.finalprojectforb.repository.roadmap.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    private final JavaRepository javaRepository;
    private final SpringRepository springRepository;
    private final ContentRepository contentRepository;


    private final TitleRepository titleRepository;

    //html detail
    public ResponseDto<?> showRoadmapHtml(Long htmlId, int page, int size) {
        Html html = isPresentHtml(htmlId);
//        Member member = validateMember(request);
//
//        if (member == null) {
//            throw new NullPointerException("Token이 유효하지 않습니다.");
//        }
        List<HtmlResponseDto> htmlDtoList = new ArrayList<>();
        Pageable pageable = PageRequest.of(page,size);
            Page<Content> contentList = contentRepository.findByHtmlId(htmlId,pageable);
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
                            .title(html.getTitle().getTitle())
                            .category(html.getCategory())
                            .contentList(contentResponseDtoList)
                            .build()
            );
        return ResponseDto.success(htmlDtoList);
    }

    //css detail
    public ResponseDto<?> showRoadmapCss(Long cssId, int page, int size) {
        Css css = isPresentCss(cssId);
//        Member member = validateMember(request);
//
//        if (member == null) {
//            throw new NullPointerException("Token이 유효하지 않습니다.");
//        }
        List<CssResponseDto> cssResponseDtoList = new ArrayList<>();
        Pageable pageable = PageRequest.of(page,size);
        Page<Content> contentList = contentRepository.findByCssId(cssId,pageable);
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

        cssResponseDtoList.add(
                CssResponseDto.builder()
                        .id(css.getId())
                        .title(css.getTitle().getTitle())
                        .category(css.getCategory())
                        .contentList(contentResponseDtoList)
                        .build()
        );
        return ResponseDto.success(cssResponseDtoList);
    }

    //js detail
    public ResponseDto<?> showRoadmapJs(Long jsId, int page, int size) {
        Js js = isPresentJs(jsId);
//        Member member = validateMember(request);
//
//        if (member == null) {
//            throw new NullPointerException("Token이 유효하지 않습니다.");
//        }
        List<JsResponseDto> jsResponseDtoList = new ArrayList<>();
        Pageable pageable = PageRequest.of(page,size);
        Page<Content> contentList = contentRepository.findByJsId(jsId,pageable);
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

        jsResponseDtoList.add(
                JsResponseDto.builder()
                        .id(js.getId())
                        .title(js.getTitle().getTitle())
                        .category(js.getCategory())
                        .contentList(contentResponseDtoList)
                        .build()
        );
        return ResponseDto.success(jsResponseDtoList);
    }

    //react detail
    public ResponseDto<?> showRoadmapReact(Long reactId, int page, int size) {
        React react = isPresentReact(reactId);
//        Member member = validateMember(request);
//
//        if (member == null) {
//            throw new NullPointerException("Token이 유효하지 않습니다.");
//        }
        List<ReactResponseDto> reactResponseDtoList = new ArrayList<>();
        Pageable pageable = PageRequest.of(page,size);
        Page<Content> contentList = contentRepository.findByReactId(reactId,pageable);
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

        reactResponseDtoList.add(
                ReactResponseDto.builder()
                        .id(react.getId())
                        .title(react.getTitle().getTitle())
                        .category(react.getCategory())
                        .contentList(contentResponseDtoList)
                        .build()
        );
        return ResponseDto.success(reactResponseDtoList);
    }

    //java detail
    public ResponseDto<?> showRoadmapJava(Long javaId, int page, int size) {
        Java java = isPresentJava(javaId);
//        Member member = validateMember(request);
//
//        if (member == null) {
//            throw new NullPointerException("Token이 유효하지 않습니다.");
//        }
        List<JavaResponseDto> javaResponseDtoList = new ArrayList<>();
        Pageable pageable = PageRequest.of(page,size);
        Page<Content> contentList = contentRepository.findByJavaId(javaId,pageable);
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

        javaResponseDtoList.add(
                JavaResponseDto.builder()
                        .id(java.getId())
                        .title(java.getTitle().getTitle())
                        .category(java.getCategory())
                        .contentList(contentResponseDtoList)
                        .build()
        );
        return ResponseDto.success(javaResponseDtoList);
    }



    //spring detail
    public ResponseDto<?> showRoadmapSpring(Long springId, int page, int size) {
        Spring spring = isPresentSpring(springId);
//        Member member = validateMember(request);
//
//        if (member == null) {
//            throw new NullPointerException("Token이 유효하지 않습니다.");
//        }
        List<SpringResponseDto> springResponseDtoList = new ArrayList<>();
        Pageable pageable = PageRequest.of(page,size);
        Page<Content> contentList = contentRepository.findBySpringId(springId,pageable);
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

        springResponseDtoList.add(
                SpringResponseDto.builder()
                        .id(spring.getId())
                        .title(spring.getTitle().getTitle())
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

    private Java isPresentJava(Long javaId) {
        Optional<Java> optionalJava = javaRepository.findById(javaId);
        return optionalJava.orElse(null);
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
