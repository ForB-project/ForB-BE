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
public class StackType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stackType_id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String stackType;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description1;

    @Column(nullable = false)
    private String description2;


    //이미지 넣기

    public StackType(String stackType, String title, String description1, String description2) {
        this.stackType = stackType;
        this.title = title;
        this.description1 = description1;
        this.description2 = description2;
    }

}
