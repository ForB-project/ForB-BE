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
        List<Css> cssList = cssRepository.findAll();
        List<Js> jsList = jsRepository.findAll();
        List<React> reactList = reactRepository.findAll();
        List<Spring> springList = springRepository.findAll();

        if (titleId == 1) {
            for (Html html : htmlList) {
                titleResponseDtoList.add(
                        TitleResponseDto.builder()
                                .id(titleId)
                                .title(title.getTitle())
                                .category(html.getCategory())
                                .build());
            }
        } else if (titleId == 2) {
            for (Css css : cssList) {
                titleResponseDtoList.add(
                        TitleResponseDto.builder()
                                .id(titleId)
                                .title(title.getTitle())
                                .category(css.getCategory())
                                .build());

            }
        }else if (titleId == 3) {
            for (Js js : jsList) {
                titleResponseDtoList.add(
                        TitleResponseDto.builder()
                                .id(titleId)
                                .title(title.getTitle())
                                .category(js.getCategory())
                                .build());

            }
        } else if (titleId == 4) {
            for (React react : reactList) {
                titleResponseDtoList.add(
                        TitleResponseDto.builder()
                                .id(titleId)
                                .title(title.getTitle())
                                .category(react.getCategory())
                                .build());

            }
        }else if (titleId == 5) {
            for (Spring spring : springList) {
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
