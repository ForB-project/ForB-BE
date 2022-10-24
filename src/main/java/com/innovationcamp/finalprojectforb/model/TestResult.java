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
public class TestResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "testResult_id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String stackType;

    @Column(nullable = false)
    private String title1;

    @Column(nullable = false)
    private String title2;

    @Column(nullable = false)
    private String description1;

    @Column(nullable = false)
    private String description2;

    public TestResult(String stackType, String title1, String title2, String description1, String description2) {
        this.stackType = stackType;
        this.title1 = title1;
        this.title2 = title2;
        this.description1 = description1;
        this.description2 = description2;
    }

}
