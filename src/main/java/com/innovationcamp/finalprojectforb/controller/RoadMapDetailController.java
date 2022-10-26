package com.innovationcamp.finalprojectforb.controller;

import com.innovationcamp.finalprojectforb.dto.ResponseDto;
import com.innovationcamp.finalprojectforb.service.RoadMapDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@RestController
public class RoadMapDetailController {

    private final RoadMapDetailService roadMapDetailService;

    //html detail
    @GetMapping("/api/roadmap/html/{htmlId}")
    public ResponseDto<?> showRoadmapHtml(@PathVariable Long htmlId,
                                          @RequestParam("page") int page,
                                          @RequestParam("size") int size,
                                          @RequestParam("sortBy") String sortBy,
                                          HttpServletRequest request){

        log.info(Optional.ofNullable("request 받아지는 accessToken : " + request.getHeader("Authorization")).orElse("UnknownUser"));
        log.info(Optional.ofNullable("request 받아지는 refreshToken : " + request.getHeader("Refresh-Token")).orElse("UnknownUser"));
        page = page -1;
        return roadMapDetailService.showRoadmapHtml(htmlId,page,size,sortBy,request);
    }

    //css detail
    @GetMapping("/api/roadmap/css/{cssId}")
    public ResponseDto<?> showRoadmapCss(@PathVariable Long cssId,
                                         @RequestParam("page") int page,
                                         @RequestParam("size") int size,
                                         @RequestParam("sortBy") String sortBy,
                                         HttpServletRequest request){
        page = page -1;
        return roadMapDetailService.showRoadmapCss(cssId,page,size,sortBy,request);
    }

    //js detail
    @GetMapping("/api/roadmap/js/{jsId}")
    public ResponseDto<?> showRoadmapJs(@PathVariable Long jsId,
                                        @RequestParam("page") int page,
                                        @RequestParam("size") int size,
                                        @RequestParam("sortBy") String sortBy,
                                        HttpServletRequest request){
        page = page -1;
        return roadMapDetailService.showRoadmapJs(jsId,page,size,sortBy,request);
    }

    //react detail
    @GetMapping("/api/roadmap/react/{reactId}")
    public ResponseDto<?> showRoadmapReact(@PathVariable Long reactId,
                                           @RequestParam("page") int page,
                                           @RequestParam("size") int size,
                                           @RequestParam("sortBy") String sortBy,
                                           HttpServletRequest request){
        page = page -1;
        return roadMapDetailService.showRoadmapReact(reactId,page,size,sortBy,request);
    }

    //java detail
    @GetMapping("/api/roadmap/java/{javaId}")
    public ResponseDto<?> showRoadmapJava(@PathVariable Long javaId,
                                          @RequestParam("page") int page,
                                          @RequestParam("size") int size,
                                          @RequestParam("sortBy") String sortBy,
                                          HttpServletRequest request){
        page = page -1;
        return roadMapDetailService.showRoadmapJava(javaId,page,size,sortBy,request);
    }

    //spring detail
    @GetMapping("/api/roadmap/spring/{springId}")
    public ResponseDto<?> showRoadmapSpring(@PathVariable Long springId,
                                            @RequestParam("page") int page,
                                            @RequestParam("size") int size,
                                            @RequestParam("sortBy") String sortBy,
                                            HttpServletRequest request){
        page = page -1;
        return roadMapDetailService.showRoadmapSpring(springId,page,size,sortBy,request);
    }
}
