package com.innovationcamp.finalprojectforb.service;

import com.innovationcamp.finalprojectforb.dto.TestResultRequestDto;
import com.innovationcamp.finalprojectforb.dto.TestResultResponseDto;
import com.innovationcamp.finalprojectforb.model.StackType;
import com.innovationcamp.finalprojectforb.model.Test;
import com.innovationcamp.finalprojectforb.repository.StackTypeRepository;
import com.innovationcamp.finalprojectforb.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TestService {

    private final TestRepository testRepository;
    private final StackTypeRepository stackTypeRepository;

    public void createDB1(){
        String[] testNumber_List = {"1","2","3","4","5","6","7","8","9"};
        String[] question_List = {
                "마법학교에 입학하면서 부엉이를 선물 받게 되었다. 받고싶은 부엉이는?",
                "기대하던 마법 빗자루가 택배로 도착했다. 하지만 약간의 조립이 필요한 상태.",
                "날아다니는 빗자루를 타고 나갈 일이 생겼다. 아뿔싸! 빗자루 정비를 하지 않아 매우 더러운 상태이다.",
                "여행중에 길을 잃은 당신,",
                "새로운 마법 한 가지를 배울 수 있는 스크롤이 생겼다. 어떤 마법이 들어있을까?",
                "곧 다가온 마법시험..멍하니 허공을 응시하는 당신. 어떤생각을 하고 있을까?",
                "용이 학교에 쳐들어 왔다! 당장 용을 물리쳐야하는데,",
                "화려한 마법을 쓰자 사람들의 이목이 집중되었다.",
                "연금술을 이용해 펜을 날아다니는 빗자루로 바꾸는 수업, 빗자루가 잘 날긴하지만 펜뚜껑이 달린채다."
        };

        String[] answer1_List = {
                "화려하고 풍성한 깃털을 가진 멋진 부엉이",
                "신난다! 이 부품이 예뻐보이는데 일단 붙여보자.",
                "빗자루를 정돈하고 깔끔한 상태를 만들어 출전한다.",
                "뭐 어때 경치를 즐기며 도시락을 먹자.",
                "사람을 현혹시키는 마법이 들어있다.",
                "용 1000마리를 혼자서 물리치는 상상.",
                "성능이 다소 의심스럽지만 멋진 보석이 박힌 지팡이.",
                "이 상황을 즐긴다.",
                "뚜껑을 없애자."
        };

        String[] answer2_List = {
                "강인한 깃으로 빠르게 날아다닐 수 있는 부엉이",
                "설명서가 어디있지? 차분하게 조립해보자.",
                "무슨상관이야 잘 날기만 하면 돼.",
                "마법지도를 펼쳐서 내 위치를 빠르게 확인하자.",
                "조용하지만 확실하게 동작하는 살상 마법이다.",
                "마법시험을 잘 통과해서 진급하는 상상.",
                "투박하지만 날이 잘 서있는 도끼.",
                "관심은 좋지만 숨고싶어진다.",
                "기능에는 문제가 없으니 제출하자."
        };

        for(int i=0; i<9;i++){
            Test test = new Test(testNumber_List[i],question_List[i],answer1_List[i],answer2_List[i]);
            testRepository.save(test);
    }
}

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

    public List<Test> getAllTestList() {
       return testRepository.findAll();
    }

    public TestResultResponseDto result(TestResultRequestDto testResultRequestDto) {
        int[] result = testResultRequestDto.getResult_list();
        String FB = (result[0] / 100 > result[0] % 100) ? "F" : "B";
        if (FB.equals("F")) {
            String type = (result[1] / 100 > result[1] % 100) ? "G" : "H";
            Optional<StackType> stackType = stackTypeRepository.findByStackType(type);
            return new TestResultResponseDto(stackType);
        } else {
            String type = (result[2] / 100 > result[2] % 100) ? "R" : "S";
            Optional<StackType> stackType = stackTypeRepository.findByStackType(type);
            return new TestResultResponseDto(stackType);
        }
    }
}
