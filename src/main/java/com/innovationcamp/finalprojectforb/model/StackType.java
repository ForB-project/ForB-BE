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
    private String description;

    //이미지 넣기

    public StackType(String stackType, String description) {
        this.stackType = stackType;
        this.description = description;
    }

}
