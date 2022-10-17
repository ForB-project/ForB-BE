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
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
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
    private final HeartRepository heartRepository;

    //html detail
    public ResponseDto<?> showRoadmapHtml(Long htmlId,
                                          int page,
                                          int size,
                                          String sortBy,
                                          HttpServletRequest request) {
        Html html = isPresentHtml(htmlId);
        Member member = validateMember(request);

        if (member == null) {
            return new ResponseDto<>(null, ErrorCode.BAD_TOKEN_REQUEST);
        }
        log.info(Optional.ofNullable("member 받아지는 값 : " + member).orElse("UnknownUser"));
        Sort sort = Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Content> contentList = contentRepository.findByHtmlId(htmlId, pageable);

        List<HtmlResponseDto> htmlDtoList = new ArrayList<>();
        List<ContentResponseDto> contentResponseDtoList = new ArrayList<>();

        makeContentList(member, contentList, contentResponseDtoList);

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
    public ResponseDto<?> showRoadmapCss(Long cssId,
                                         int page,
                                         int size,
                                         String sortBy,
                                         HttpServletRequest request) {
        Css css = isPresentCss(cssId);
        Member member = validateMember(request);

        if (member == null) {
            return new ResponseDto<>(null, ErrorCode.BAD_TOKEN_REQUEST);
        }

        Sort sort = Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Content> contentList = contentRepository.findByCssId(cssId, pageable);

        List<CssResponseDto> cssResponseDtoList = new ArrayList<>();
        List<ContentResponseDto> contentResponseDtoList = new ArrayList<>();

        makeContentList(member, contentList, contentResponseDtoList);

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
    public ResponseDto<?> showRoadmapJs(Long jsId,
                                        int page,
                                        int size,
                                        String sortBy,
                                        HttpServletRequest request) {
        Js js = isPresentJs(jsId);
        Member member = validateMember(request);

        if (member == null) {
            return new ResponseDto<>(null, ErrorCode.BAD_TOKEN_REQUEST);
        }

        Sort sort = Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Content> contentList = contentRepository.findByJsId(jsId, pageable);

        List<JsResponseDto> jsResponseDtoList = new ArrayList<>();
        List<ContentResponseDto> contentResponseDtoList = new ArrayList<>();

        makeContentList(member, contentList, contentResponseDtoList);

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
    public ResponseDto<?> showRoadmapReact(Long reactId,
                                           int page,
                                           int size,
                                           String sortBy,
                                           HttpServletRequest request) {
        React react = isPresentReact(reactId);
        Member member = validateMember(request);

        if (member == null) {
            return new ResponseDto<>(null, ErrorCode.BAD_TOKEN_REQUEST);
        }

        Sort sort = Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Content> contentList = contentRepository.findByReactId(reactId, pageable);

        List<ReactResponseDto> reactResponseDtoList = new ArrayList<>();
        List<ContentResponseDto> contentResponseDtoList = new ArrayList<>();

        makeContentList(member, contentList, contentResponseDtoList);

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
    public ResponseDto<?> showRoadmapJava(Long javaId,
                                          int page,
                                          int size,
                                          String sortBy,
                                          HttpServletRequest request) {
        Java java = isPresentJava(javaId);
        Member member = validateMember(request);

        if (member == null) {
            return new ResponseDto<>(null, ErrorCode.BAD_TOKEN_REQUEST);
        }

        Sort sort = Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Content> contentList = contentRepository.findByJavaId(javaId, pageable);

        List<JavaResponseDto> javaResponseDtoList = new ArrayList<>();
        List<ContentResponseDto> contentResponseDtoList = new ArrayList<>();

        makeContentList(member, contentList, contentResponseDtoList);

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
    public ResponseDto<?> showRoadmapSpring(Long springId,
                                            int page,
                                            int size,
                                            String sortBy,
                                            HttpServletRequest request) {
        Spring spring = isPresentSpring(springId);
        Member member = validateMember(request);

        if (member == null) {
            return new ResponseDto<>(null, ErrorCode.BAD_TOKEN_REQUEST);
        }

        Sort sort = Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Content> contentList = contentRepository.findBySpringId(springId, pageable);

        List<SpringResponseDto> springResponseDtoList = new ArrayList<>();
        List<ContentResponseDto> contentResponseDtoList = new ArrayList<>();

        makeContentList(member, contentList, contentResponseDtoList);

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

    private boolean isHeartCheck(Member member, Content content) {
        List<Heart> heartList = heartRepository.findByMemberIdAndContentId(member.getId(), content.getId());
        boolean heartCheck = false;
        if (!heartList.isEmpty()) {
            heartCheck = true;
        }
        return heartCheck;
    }
    private void makeContentList(Member member, Page<Content> contentList, List<ContentResponseDto> contentResponseDtoList) {
        for (Content content : contentList) {
            boolean heartCheck = isHeartCheck(member, content);
            contentResponseDtoList.add(
                    ContentResponseDto.builder()
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
