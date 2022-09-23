package com.innovationcamp.finalprojectforb.model.roadmap;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
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

    @Column
    private String thumbnail;

    @JoinColumn(name = "html_id", nullable = true)
    @ManyToOne(fetch = FetchType.EAGER)
    private Html html;

    @JoinColumn(name = "css_id", nullable = true)
    @ManyToOne(fetch = FetchType.EAGER)
    private Css css;

    @JoinColumn(name = "js_id", nullable = true)
    @ManyToOne(fetch = FetchType.EAGER)
    private Js js;

    @JoinColumn(name = "react_id", nullable = true)
    @ManyToOne(fetch = FetchType.EAGER)
    private React react;

    @JoinColumn(name = "spring_id", nullable = true)
    @ManyToOne(fetch = FetchType.EAGER)
    private Spring spring;

}
