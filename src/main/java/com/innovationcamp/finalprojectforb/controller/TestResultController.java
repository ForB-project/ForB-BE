package com.innovationcamp.finalprojectforb.controller;

import com.innovationcamp.finalprojectforb.dto.ResponseDto;
import com.innovationcamp.finalprojectforb.dto.TestResultRequestDto;
import com.innovationcamp.finalprojectforb.service.TestResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestResultController {

    private final TestResultService testResultService;

//    DB에 문항 및 테스트 결과 넣기 (한 번 넣고 주석 처리하기)
    @GetMapping("/api/create")
    public void create(){
        testResultService.createDB2();
    }

    @PostMapping("/api/test/result")
    public ResponseDto<?> result(@RequestBody TestResultRequestDto testResultRequestDto){
        return testResultService.result(testResultRequestDto);
    }

}
