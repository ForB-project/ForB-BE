package com.innovationcamp.finalprojectforb.model.roadmap;

import com.innovationcamp.finalprojectforb.model.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class Title {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @OneToMany(mappedBy = "title", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Html> html;

    @OneToMany(mappedBy = "title", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Css> css;

    @OneToMany(mappedBy = "title", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Js> js;

    @OneToMany(mappedBy = "title", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<React> react;

    @OneToMany(mappedBy = "title", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Java> java;

    @OneToMany(mappedBy = "title", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Spring> spring;

    @JoinColumn(name = "member_id", nullable = true)
    @ManyToOne(fetch = FetchType.EAGER)
    private Member member;


}
