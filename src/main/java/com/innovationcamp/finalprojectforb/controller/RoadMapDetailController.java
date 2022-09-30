package com.innovationcamp.finalprojectforb.controller;

import com.innovationcamp.finalprojectforb.dto.ResponseDto;
import com.innovationcamp.finalprojectforb.service.RoadMapDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
public class RoadMapDetailController {

    private final RoadMapDetailService roadMapDetailServercie;


    //html detail
    @GetMapping("/api/roadmap/html/{htmlId}")
    public ResponseDto<?> showRoadmapHtml(@PathVariable Long htmlId,
                                          @RequestParam("page") int page,
                                          @RequestParam("size") int size,
                                          HttpServletRequest request){
        page = page -1;
        return roadMapDetailServercie.showRoadmapHtml(htmlId,page,size,request);
    }

    //css detail
    @GetMapping("/api/roadmap/css/{cssId}")
    public ResponseDto<?> showRoadmapCss(@PathVariable Long cssId,
                                         @RequestParam("page") int page,
                                         @RequestParam("size") int size,
                                         HttpServletRequest request){
        page = page -1;
        return roadMapDetailServercie.showRoadmapCss(cssId,page,size,request);
    }

    //js detail
    @GetMapping("/api/roadmap/js/{jsId}")
    public ResponseDto<?> showRoadmapJs(@PathVariable Long jsId,
                                        @RequestParam("page") int page,
                                        @RequestParam("size") int size,
                                        HttpServletRequest request){
        page = page -1;
        return roadMapDetailServercie.showRoadmapJs(jsId,page,size,request);
    }

    //react detail
    @GetMapping("/api/roadmap/react/{reactId}")
    public ResponseDto<?> showRoadmapReact(@PathVariable Long reactId,
                                           @RequestParam("page") int page,
                                           @RequestParam("size") int size,
                                           HttpServletRequest request){
        page = page -1;
        return roadMapDetailServercie.showRoadmapReact(reactId,page,size,request);
    }

    //java detail
    @GetMapping("/api/roadmap/java/{javaId}")
    public ResponseDto<?> showRoadmapJava(@PathVariable Long javaId,
                                          @RequestParam("page") int page,
                                          @RequestParam("size") int size,
                                          HttpServletRequest request){
        page = page -1;
        return roadMapDetailServercie.showRoadmapJava(javaId,page,size,request);
    }

    //spring detail
    @GetMapping("/api/roadmap/spring/{springId}")
    public ResponseDto<?> showRoadmapSpring(@PathVariable Long springId,
                                            @RequestParam("page") int page,
                                            @RequestParam("size") int size,
                                            HttpServletRequest request){
        page = page -1;
        return roadMapDetailServercie.showRoadmapSpring(springId,page,size,request);
    }
}
