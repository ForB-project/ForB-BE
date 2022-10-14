package com.innovationcamp.finalprojectforb.controller;

import com.innovationcamp.finalprojectforb.dto.MyRoadMapResDto;
import com.innovationcamp.finalprojectforb.dto.ResponseDto;
import com.innovationcamp.finalprojectforb.service.MyRoadMapService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class MyRoadMapController {

    private final MyRoadMapService myRoadMapService;

    @GetMapping("/api/myroadmap/{pathId}")
    public ResponseDto<List<MyRoadMapResDto>> showMyRoadMap(HttpServletRequest request,
                                                            @PathVariable Long pathId,
                                                            @RequestParam("page") int page,
                                                            @RequestParam("size") int size) {
        page = page -1;
        return myRoadMapService.showMyRoadMap(request,pathId, page, size);
    }

    @DeleteMapping("/api/myroadmap/{contentId}")
    public ResponseDto<?> deleteMyRoadMap(HttpServletRequest request,@PathVariable Long contentId){
        return myRoadMapService.deleteMyRoadMap(request,contentId);
    }

    @GetMapping("/api/myroadmap/post")
    public ResponseDto<?> showMyPost(HttpServletRequest request,
                                     @RequestParam("page") int page,
                                     @RequestParam("size") int size){
        page = page -1;
        return myRoadMapService.showMyPost(request, page, size);
    }

    @GetMapping("/api/myroadmap/likePost")
    public ResponseDto<?> showMyLikePost (HttpServletRequest request,
                                          @RequestParam("page") int page,
                                          @RequestParam("size") int size) {
        page = page -1;
        return myRoadMapService.showMyLikePost(request, page, size);
    }

}
