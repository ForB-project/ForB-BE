package com.innovationcamp.finalprojectforb.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "test_id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String testNumber;

    @Column(nullable = false)
    private String question;

    @Column(nullable = false)
    private String answer1;

    @Column(nullable = false)
    private String answer2;

    public Test(String testNumber, String question, String answer1, String answer2) {
        this.testNumber = testNumber;
        this.question = question;
        this.answer1 = answer1;
        this.answer2 = answer2;
    }

    //이미지 넣기
}
