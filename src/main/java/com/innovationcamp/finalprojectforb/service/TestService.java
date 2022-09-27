package com.innovationcamp.finalprojectforb.service;

import com.innovationcamp.finalprojectforb.dto.TestResultRequestDto;
import com.innovationcamp.finalprojectforb.dto.TestResultResponseDto;
import com.innovationcamp.finalprojectforb.model.StackType;
import com.innovationcamp.finalprojectforb.repository.StackTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TestService {
    private final StackTypeRepository stackTypeRepository;

    public void createDB2() {
        String[] stackType_List = {
                "G",
                "R",
                "H",
                "S"
        };

        String[] description_List = {
                "그리핀다르, 가장 화려하고 눈에 띄는 CSS만 만들어보도록 하세",
                "레반클로, 가장 논리적인 로직을 작성해보도록 하세",
                "후플푸푸, 가장 사용자에게 친화적이고 편리한 화면을 만들도보도록 하세",
                "슬로데린, 가장 효율적인 기능을 지닌 코드만 뽑아 쓰도록 하세"
        };
        for (int i = 0; i < 4; i++) {
            StackType stackType = new StackType(stackType_List[i], description_List[i]);
            stackTypeRepository.save(stackType);
        }
    }


    public TestResultResponseDto result(TestResultRequestDto testResultRequestDto) {
        String type = testResultRequestDto.getType();
        int answerSum = testResultRequestDto.getAnswerSum();
        if (Objects.equals(type, "F")) {
            String GH = (answerSum / 100 > answerSum % 100) ? "G" : "H";
            Optional<StackType> stackType = stackTypeRepository.findByStackType(GH);
            return new TestResultResponseDto(stackType);
        } else {
            String RS = (answerSum / 100 > answerSum % 100) ? "R" : "S";
            Optional<StackType> stackType = stackTypeRepository.findByStackType(RS);
            return new TestResultResponseDto(stackType);
        }
    }
//        int[] result = testResultRequestDto.getResult_list();
//        String FB = (result[0] / 100 > result[0] % 100) ? "F" : "B";
//        if (FB.equals("F")) {
//            String GH = (result[1] / 100 > result[1] % 100) ? "G" : "H";
//            Optional<StackType> stackType = stackTypeRepository.findByStackType(GH);
//            return new TestResultResponseDto(stackType);
//        } else {
//            String RS = (result[2] / 100 > result[2] % 100) ? "R" : "S";
//            Optional<StackType> stackType = stackTypeRepository.findByStackType(RS);
//            return new TestResultResponseDto(stackType);
//        }
//    }
}
