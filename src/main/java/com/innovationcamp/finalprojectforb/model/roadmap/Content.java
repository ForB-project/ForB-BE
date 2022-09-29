package com.innovationcamp.finalprojectforb.model.roadmap;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.innovationcamp.finalprojectforb.model.Heart;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
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

    @Column
    private Long heartCnt = 0L;//개수 보여줄 용도

    @JsonIgnore// Restcontroller에서  Heart엔티티를 JSON으로 반환하는 과정에서 recursion 에러 발생 => serialize(직렬화) 과정에서 무한재귀 발생 해결방안
    @OneToMany(mappedBy = "content", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    //지우는 용도
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


}
