package com.innovationcamp.finalprojectforb.controller;

import com.innovationcamp.finalprojectforb.dto.ResponseDto;
import com.innovationcamp.finalprojectforb.service.RoadMapService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class RoadMapController {
    private final RoadMapService roadMapServercie;

    //타이틀별 카테고리 나열하기
    @GetMapping("/api/roadmap/category/{titleId}")
    public ResponseDto<?> showCategoryHtml(@PathVariable Long titleId){
        return roadMapServercie.showCategory(titleId);
    }
}
