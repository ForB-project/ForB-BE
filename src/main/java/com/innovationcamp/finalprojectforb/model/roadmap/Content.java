package com.innovationcamp.finalprojectforb.model.roadmap;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.innovationcamp.finalprojectforb.model.Heart;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
public class Content {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String contentLink;

    @Column(nullable = true)
    private String thumbnail;

    @Column
    private String description;

    @Builder.Default//@Builder 사용시 초기화할 필드가 있다면
    @Column(nullable = false)
    private Long heartCnt = 0L;

    @JsonIgnore// Restcontroller에서  Heart엔티티를 JSON으로 반환하는 과정에서 recursion 에러 발생 => serialize(직렬화) 과정에서 무한재귀 발생 해결방안
    @OneToMany(mappedBy = "content", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Heart> heart;

    @JoinColumn(name = "html_id", nullable = true)
    @ManyToOne(fetch = FetchType.EAGER)
    private Html html;

    @JoinColumn(name = "css_id", nullable = true)
    @ManyToOne(fetch = FetchType.EAGER)
    private Css css;

    @JoinColumn(name = "js_id", nullable = true)
    @ManyToOne(fetch = FetchType.EAGER)
    private Js js;

    @JoinColumn(name = "java_id", nullable = true)
    @ManyToOne(fetch = FetchType.EAGER)
    private Java java;

    @JoinColumn(name = "react_id", nullable = true)
    @ManyToOne(fetch = FetchType.EAGER)
    private React react;

    @JoinColumn(name = "spring_id", nullable = true)
    @ManyToOne(fetch = FetchType.EAGER)
    private Spring spring;

    public void setHeartCnt(Long heartCnt){
        this.heartCnt = heartCnt;
    }
}
