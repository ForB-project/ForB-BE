package com.innovationcamp.finalprojectforb.controller;

import com.innovationcamp.finalprojectforb.dto.CodeRequestDto;
import com.innovationcamp.finalprojectforb.dto.ResponseDto;
import com.innovationcamp.finalprojectforb.service.CodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class CodeController {
    private final CodeService codeService;

    @PostMapping("/api/firstCode")
    public ResponseDto<String> showfirstCode(
                                             @RequestBody CodeRequestDto codeRequestDto){

        return codeService.showfirstCode(codeRequestDto);
    }

    @PostMapping("/api/secondCode")
    public ResponseDto<String> showSecondCode(
                                              @RequestBody CodeRequestDto codeRequestDto){
        return codeService.showSecondCode(codeRequestDto);
    }
}
