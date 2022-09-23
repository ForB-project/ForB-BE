package com.innovationcamp.finalprojectforb.service;

import com.innovationcamp.finalprojectforb.dto.ResponseDto;
import com.innovationcamp.finalprojectforb.dto.roadmap.*;
import com.innovationcamp.finalprojectforb.model.roadmap.*;
import com.innovationcamp.finalprojectforb.repository.roadmap.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RoadMapService {
    private final HtmlRepository htmlRepository;
    private final CssRepository cssRepository;

    private final JsRepository jsRepository;

    private final ReactRepository reactRepository;

    private final SpringRepository springRepository;

    private final TitleRepository titleRepository;

    //타이틀별 카테고리 나열하기
    public ResponseDto<?> showCategory(Long titleId) {
        Title title = isCategory(titleId);
        List<TitleResponseDto> titleResponseDtoList = new ArrayList<>();

        List<Html> htmlList = htmlRepository.findAll();
        List<HtmlResponseDto> htmlResponseDtos = new ArrayList<>();
        List<Css> cssList = cssRepository.findAll();
        List<CssResponseDto> cssResponseDtos = new ArrayList<>();
        List<Js> jsList = jsRepository.findAll();
        List<JsResponseDto> jsResponseDtos = new ArrayList<>();
        List<React> reactList = reactRepository.findAll();
        List<ReactResponseDto> reactResponseDtos = new ArrayList<>();
        List<Spring> springList = springRepository.findAll();
        List<SpringResponseDto> springResponseDtos = new ArrayList<>();

        if (titleId == 1) {
            for (Html html : htmlList) {
                htmlResponseDtos.add(
                        HtmlResponseDto.builder()
                                .id(title.getId())
                                .category(html.getCategory())
                                .build());
                titleResponseDtoList.add(
                        TitleResponseDto.builder()
                                .id(titleId)
                                .title(title.getTitle())
                                .category(html.getCategory())
                                .build());
            }
        } else if (titleId == 2) {
            for (Css css : cssList) {
                cssResponseDtos.add(
                        CssResponseDto.builder()
                                .id(title.getId())
                                .category(css.getCategory())
                                .build());
                titleResponseDtoList.add(
                        TitleResponseDto.builder()
                                .id(titleId)
                                .title(title.getTitle())
                                .category(css.getCategory())
                                .build());

            }
        }else if (titleId == 3) {
            for (Js js : jsList) {
                jsResponseDtos.add(
                        JsResponseDto.builder()
                                .id(title.getId())
                                .category(js.getCategory())
                                .build());
                titleResponseDtoList.add(
                        TitleResponseDto.builder()
                                .id(titleId)
                                .title(title.getTitle())
                                .category(js.getCategory())
                                .build());

            }
        } else if (titleId == 4) {
            for (React react : reactList) {
                reactResponseDtos.add(
                        ReactResponseDto.builder()
                                .id(title.getId())
                                .category(react.getCategory())
                                .build());
                titleResponseDtoList.add(
                        TitleResponseDto.builder()
                                .id(titleId)
                                .title(title.getTitle())
                                .category(react.getCategory())
                                .build());

            }
        }else if (titleId == 5) {
            for (Spring spring : springList) {
                springResponseDtos.add(
                        SpringResponseDto.builder()
                                .id(title.getId())
                                .category(spring.getCategory())
                                .build());
                titleResponseDtoList.add(
                        TitleResponseDto.builder()
                                .id(titleId)
                                .title(title.getTitle())
                                .category(spring.getCategory())
                                .build());

            }
        }
        return ResponseDto.success(titleResponseDtoList);
    }

    private Title isCategory(Long titleId) {
        Optional<Title> optionalTitle = titleRepository.findById(titleId);
        return optionalTitle.orElse(null);
    }
}
