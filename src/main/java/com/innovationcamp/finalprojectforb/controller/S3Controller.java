package com.innovationcamp.finalprojectforb.controller;

import com.innovationcamp.finalprojectforb.dto.ResponseDto;
import com.innovationcamp.finalprojectforb.service.S3Upload;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class S3Controller {

    private final S3Upload s3Upload;

    @PostMapping("/api/auth/image")
    public ResponseDto imageUpload(@RequestPart(required = false) MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            return new ResponseDto("파일이 유효하지 않습니다.");
        }
        try {
            return new ResponseDto(s3Upload.uploadFiles(multipartFile, "static"));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseDto("파일이 유효하지 않습니다.");
        }
    }

}
