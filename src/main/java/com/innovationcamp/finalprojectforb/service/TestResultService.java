package com.innovationcamp.finalprojectforb.service;

import com.innovationcamp.finalprojectforb.dto.ResponseDto;
import com.innovationcamp.finalprojectforb.dto.TestResultRequestDto;
import com.innovationcamp.finalprojectforb.dto.TestResultResponseDto;
import com.innovationcamp.finalprojectforb.model.TestResult;
import com.innovationcamp.finalprojectforb.repository.TestResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TestResultService {
    private final TestResultRepository testResultRepository;

    public void createDB2() {
        String[] stackType_List = {
                "G",
                "R",
                "H",
                "S"
        };

        String[] title1_List = {
                "그리핀도르",
                "레번클로",
                "후플푸프",
                "슬리데린"
        };

        String[] title2_List = {
                "'가장 화려하고 눈에 띄는 결과물을 만들어내는 개발자'",
                "'가장 독창적이고 효율적인 로직을 작성하는 개발자'",
                "'가장 사용자에게 친화적이고 편리한 서비스를 고민하는 개발자'",
                "'가장 기본에 충실하고 지능적인 로직을 생각해내는 개발자'"
        };

        String[] description1_List = {
                "<div>자네의 생각속에는 화려하고 멋진 결과물들이 잔뜩 들어있는 것 같군.<br/> 그 누구보다 아름답고, 우아하고, 멋진 결과물을 만들기 위해 고뇌하는 자네의 세심하면서도 과감한 꾸미기 능력은 프론트엔드 개발자에게 딱 필요한 능력이라네. 너무 지나친건 아닐까 고민이 된다고? 걱정말게 모두들 자네의 결과물을 보고 두 눈이 휘둥그레질 테니까.</div>",
                "<div>자네는 상당히 지능적이고, 독창적인 면모를 지니고 있는 듯 하군.<br/> 보통사람은 생각하지 못 할, 더 효율적인 방법을 고민하는 자네의 모습을 보니 백엔드개발자로서 훌륭한 자질을 지닌 인재임이 틀림이 없는 것 같군. 다소 괴짜스럽고 엉뚱한 해결방법을 생각해냈다고? 효율적으로 기능한다면 전혀 문제가 없으니 걱정말게. 자네는 훌륭한 백엔드 개발자가 될 수 있을꺼야. 내가 보증하지!</div>",
                "<div>세상을 향한 자네의 상냥함은 정말 놀라울 정도로군.<br/> 그 누구보다도 타인의 입장에서 생각하고, 타인을 위해 고민하는 자네의 성격은 프론트엔드 개발자에게 정말 잘 어울린다고 자신있게 얘기할 수 있네. 스스로의 실력에 자신이 없다고? 걱정말게 자네의 상냥함과 친절함은 모두를 감동시킬 테니까. 자네의 강점을 자각하고 잘 사용한다면 틀림없이 훌륭한 프론트엔드 개발자가 될 수 있다네.</div>",
                "<div>일에 대한 자네의 엄격함과 곧은 심지는 그 누구도 꺾을 수 없을 듯 하군.<br/> 자신이 정한 것에 흔들림이 없고, 눈앞에 닥친 문제에 대해 누구보다 이성적으로 대처하는 자네의 자질은 백엔드 개발자로서 크나큰 장점임에 틀림이 없지. 다소 독선적인 것은 아닐까 고민된다고? 너무 걱정하진 말게. 자네는 늘 이성적으로 생각하기에 가장 좋은 결과를 향해 갈 것이 틀림없고 카리스마 있는 모습에 모두들 자네를 따를 테니까.</div>"
        };

        String[] description2_List = {
                "묵묵히 자기일을 해내는 레번클로의 백엔드 개발자와 만난다면 훌륭한 팀을 이룰 듯 하군. 참고하게.",
                "리더쉽을 갖춘 그리핀도르의 프론트엔드 개발자와 잘 어울릴 것 같으니 참고하게.",
                "강한 리더쉽을 가진 슬리데린의 벡엔드 개발자와 함께한다면 좋은 결과물을 만들 수 있을거야. 참고하게.",
                "타인의 입장에서 생각하는데 능숙한 후플푸프의 프론트엔트 개발자를 동료로 영입한다면 자네의 단점을 최소화하고 훌륭한 결과물을 만들어 낼 수 있을테니 참고하게."
        };
        for (int i = 0; i < 4; i++) {
            TestResult testResult = new TestResult(stackType_List[i], title1_List[i], title2_List[i], description1_List[i], description2_List[i]);
            testResultRepository.save(testResult);
        }
    }


    public ResponseDto<?> result(TestResultRequestDto testResultRequestDto) {
        String type = testResultRequestDto.getType();
        int answerSum = testResultRequestDto.getAnswerSum();

        if (Objects.equals(type, "F")) {
            String GH = (answerSum / 100 > answerSum % 100) ? "G" : "H";
            List<TestResult> testResultList = testResultRepository.findByStackType(GH);
            List<TestResultResponseDto> testResultResponseDtoList = new ArrayList<>();

            for (TestResult testResult : testResultList) {
                testResultResponseDtoList.add(
                        TestResultResponseDto.builder()
                                .id(testResult.getId())
                                .stackType(testResult.getStackType())
                                .title1(testResult.getTitle1())
                                .title2(testResult.getTitle2())
                                .description1(testResult.getDescription1())
                                .description2(testResult.getDescription2())
                                .build());
            }

            return ResponseDto.success(testResultResponseDtoList);

        } else {
            String RS = (answerSum / 100 > answerSum % 100) ? "R" : "S";
            List<TestResult> testResultList = testResultRepository.findByStackType(RS);
            List<TestResultResponseDto> testResultResponseDtoList = new ArrayList<>();

            for (TestResult testResult : testResultList) {
                testResultResponseDtoList.add(
                        TestResultResponseDto.builder()
                                .id(testResult.getId())
                                .stackType(testResult.getStackType())
                                .title1(testResult.getTitle1())
                                .title2(testResult.getTitle2())
                                .description1(testResult.getDescription1())
                                .description2(testResult.getDescription2())
                                .build());
            }

            return ResponseDto.success(testResultResponseDtoList);

        }
    }
}
