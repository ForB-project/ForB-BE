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

    @JsonIgnore
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


}
