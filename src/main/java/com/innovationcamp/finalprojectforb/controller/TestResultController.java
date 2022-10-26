package com.innovationcamp.finalprojectforb.controller;

import com.innovationcamp.finalprojectforb.dto.ResponseDto;
import com.innovationcamp.finalprojectforb.dto.TestResultRequestDto;
import com.innovationcamp.finalprojectforb.enums.ErrorCode;
import com.innovationcamp.finalprojectforb.service.TestResultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TestResultController {

    private final TestResultService testResultService;

//    DB에 문항 및 테스트 결과 넣기 (한 번 넣고 주석 처리하기)
//    @GetMapping("/api/create")
//    public void create(){
//        testResultService.createDB2();
//    }

    @PostMapping("/api/test/result")
    public ResponseDto<?> result(@RequestBody TestResultRequestDto testResultRequestDto, HttpServletRequest request){
        try {
            return testResultService.result(testResultRequestDto, request);
        } catch (Exception e){
            log.error(e.getMessage());
            return new ResponseDto<>(null, ErrorCode.INVALID_ERROR);
        }
    }

}
