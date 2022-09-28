package com.innovationcamp.finalprojectforb.model.roadmap;

import lombok.Getter;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity
public class React {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String category;

    @OneToMany(mappedBy = "react", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Content> content;

    @JoinColumn(name = "title_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Title title;

    public Long getId(Long reactId) {
        this.id = reactId;
        return reactId;
    }
}
