package com.innovationcamp.finalprojectforb.controller;

import com.innovationcamp.finalprojectforb.dto.ResponseDto;
import com.innovationcamp.finalprojectforb.service.RoadMapServercie;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class RoadMapController {

    private final RoadMapServercie roadMapServercie;


    //html detail
    @GetMapping("/api/roadmap/html/{htmlId}")
    public ResponseDto<?> showRoadmapHtml(@PathVariable Long htmlId){
        return roadMapServercie.showRoadmapHtml(htmlId);
    }

    //css detail
    @GetMapping("/api/roadmap/css/{cssId}")
    public ResponseDto<?> showRoadmapCss(@PathVariable Long cssId){
        return roadMapServercie.showRoadmapCss(cssId);
    }

}
