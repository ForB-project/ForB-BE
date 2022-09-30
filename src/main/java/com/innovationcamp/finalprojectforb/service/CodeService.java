package com.innovationcamp.finalprojectforb.service;

import com.innovationcamp.finalprojectforb.dto.CodeRequestDto;
import com.innovationcamp.finalprojectforb.dto.ResponseDto;
import org.springframework.stereotype.Service;

@Service
public class CodeService {
    public ResponseDto<String> showfirstCode(CodeRequestDto codeRequestDto) {
        String finalAnswer = "";

            int money = codeRequestDto.getInputInt1();
            if (money >= codeRequestDto.getInputInt2()) {
                finalAnswer = "택시를 타고 가라";
            } else {
                finalAnswer = "걸어가라";
            }
        return ResponseDto.success(finalAnswer);
    }

    public ResponseDto<String> showSecondCode(CodeRequestDto codeRequestDto) {
        String finalAnswer = "";
        int treeHit =  0;
        int howMany = codeRequestDto.getInputInt1();
            while (treeHit < howMany) {
                if (howMany >= 21) {
                    finalAnswer = "20이하로 설정해주세요!";
                    break;
                }
                treeHit++;
                finalAnswer += "나무를 " + treeHit + "번 찍었습니다.\n";
                if (treeHit == howMany) {
                    finalAnswer += "나무 넘어갑니다.\n";
                }
            }
        return ResponseDto.success(finalAnswer);
    }
}
