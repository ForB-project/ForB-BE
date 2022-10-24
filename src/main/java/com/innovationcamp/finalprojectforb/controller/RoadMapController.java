package com.innovationcamp.finalprojectforb.controller;

import com.innovationcamp.finalprojectforb.dto.ResponseDto;
import com.innovationcamp.finalprojectforb.service.RoadMapService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@Slf4j
@RequiredArgsConstructor
@RestController
public class RoadMapController {
    private final RoadMapService roadMapServercie;

    //타이틀 나타내기
    @GetMapping("/api/roadmap/title")
    public ResponseDto<?> showTitle(){
        return roadMapServercie.showTitle();
    }

    //타이틀별 카테고리 나열하기
    @GetMapping("/api/roadmap/category/{titleId}")
    public ResponseDto<?> showCategoryHtml(@PathVariable Long titleId){
        return roadMapServercie.showCategory(titleId);
    }

    //타이틀명으로 검색하기
    @GetMapping("/api/roadmap/search")
    public ResponseDto<?> searchContent(@RequestParam(value = "keyword") String keyword,
                                        HttpServletRequest request){
        return roadMapServercie.searchContents(keyword, request);
    }
}
