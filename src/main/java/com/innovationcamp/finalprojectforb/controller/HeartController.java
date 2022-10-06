package com.innovationcamp.finalprojectforb.controller;

import com.innovationcamp.finalprojectforb.dto.ResponseDto;
import com.innovationcamp.finalprojectforb.service.HeartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class HeartController {

    private final HeartService heartService;

    // 좋아요 + 좋아요 해제 둘다 가능
    @PostMapping("/api/roadmap/heart/{contentId}")
    public ResponseDto<?> heartContent(@PathVariable Long contentId, HttpServletRequest request) throws IOException {
        return heartService.heartContent(contentId,request);
    }

    @PostMapping("/api/auth/post/Like/{postId}")
    public ResponseDto<?> LikePost(@PathVariable Long postId, HttpServletRequest request) {
        return heartService.LikePost(postId,request);
    }

}
