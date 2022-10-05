package com.innovationcamp.finalprojectforb.controller;

import com.innovationcamp.finalprojectforb.dto.MyRoadMapResDto;
import com.innovationcamp.finalprojectforb.dto.ResponseDto;
import com.innovationcamp.finalprojectforb.service.MyRoadMapService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class MyRoadMapController {

    private final MyRoadMapService myRoadMapService;

    @GetMapping("/api/myroadmap")
    public ResponseDto<List<MyRoadMapResDto>> showMyRoadMap(HttpServletRequest request,
                                                            @RequestParam("page") int page,
                                                            @RequestParam("size") int size,
                                                            @RequestParam("sortBy") String sortBy) {
        return myRoadMapService.showMyRoadMap(request, page, size, sortBy);
    }
}
