package com.innovationcamp.finalprojectforb.controller;

import com.innovationcamp.finalprojectforb.dto.ResponseDto;
import com.innovationcamp.finalprojectforb.dto.TestResultRequestDto;
import com.innovationcamp.finalprojectforb.dto.TestResultResponseDto;
import com.innovationcamp.finalprojectforb.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;

    //DB에 문항 및 테스트 결과 넣기 (한 번 넣고 주석 처리하기)
//    @GetMapping("/api/create")
//    public void create(){
//        testService.createDB2();
//    }


    @PostMapping("/api/test/result")
    public ResponseDto<TestResultResponseDto> result(@RequestBody TestResultRequestDto testResultRequestDto){
        TestResultResponseDto testResultResponseDto;
        testResultResponseDto = testService.result(testResultRequestDto);
        return new ResponseDto<>(testResultResponseDto);
    }
}
