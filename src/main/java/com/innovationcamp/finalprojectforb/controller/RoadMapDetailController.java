package com.innovationcamp.finalprojectforb.controller;

import com.innovationcamp.finalprojectforb.dto.ResponseDto;
import com.innovationcamp.finalprojectforb.service.RoadMapDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class RoadMapDetailController {

    private final RoadMapDetailService roadMapDetailServercie;


    //html detail
    @GetMapping("/api/roadmap/html/{htmlId}")
    public ResponseDto<?> showRoadmapHtml(@PathVariable Long htmlId){
        return roadMapDetailServercie.showRoadmapHtml(htmlId);
    }

    //css detail
    @GetMapping("/api/roadmap/css/{cssId}")
    public ResponseDto<?> showRoadmapCss(@PathVariable Long cssId){
        return roadMapDetailServercie.showRoadmapCss(cssId);
    }

    //js detail
    @GetMapping("/api/roadmap/js/{jsId}")
    public ResponseDto<?> showRoadmapJs(@PathVariable Long jsId){
        return roadMapDetailServercie.showRoadmapJs(jsId);
    }

    //react detail
    @GetMapping("/api/roadmap/react/{reactId}")
    public ResponseDto<?> showRoadmapReact(@PathVariable Long reactId){
        return roadMapDetailServercie.showRoadmapReact(reactId);
    }

    //java detail
    @GetMapping("/api/roadmap/java/{javaId}")
    public ResponseDto<?> showRoadmapJava(@PathVariable Long javaId){
        return roadMapDetailServercie.showRoadmapJava(javaId);
    }

    //spring detail
    @GetMapping("/api/roadmap/spring/{springId}")
    public ResponseDto<?> showRoadmapSpring(@PathVariable Long springId){
        return roadMapDetailServercie.showRoadmapSpring(springId);
    }
}
