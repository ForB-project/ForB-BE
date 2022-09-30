package com.innovationcamp.finalprojectforb.service;

import com.innovationcamp.finalprojectforb.dto.CodeRequestDto;
import com.innovationcamp.finalprojectforb.dto.ResponseDto;
import org.springframework.stereotype.Service;

@Service
public class CodeService {
    public ResponseDto<String> showfirstCode(CodeRequestDto codeRequestDto) {
        String finalAnswer = "";

        if (codeRequestDto.isCorrect() == true) {
            int money = codeRequestDto.getInputInt();
            if (money >= 4000) {
                finalAnswer = "택시를 타고 가라";
            } else {
                finalAnswer = "걸어가라";
            }
        } else if (codeRequestDto.isCorrect() == false){
            finalAnswer = "코드를 다시 입력하세요!";
        }
        return ResponseDto.success(finalAnswer);
    }

    public ResponseDto<String> showSecondCode(CodeRequestDto codeRequestDto) {
        String finalAnswer = "";
        int treeHit =  0;
        int howMany = codeRequestDto.getInputInt();
        if (codeRequestDto.isCorrect() == true) {
            while (treeHit < howMany) {
                treeHit++;
                finalAnswer += "나무를 " + treeHit + "번 찍었습니다.";
                if (treeHit == howMany) {
                    finalAnswer += "나무 넘어갑니다.";
                }
            }
        } else if (codeRequestDto.isCorrect() == false){
            finalAnswer = "코드를 다시 입력하세요!";
        }
        return ResponseDto.success(finalAnswer);
    }
}
