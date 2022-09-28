package com.innovationcamp.finalprojectforb.model.roadmap;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;


@Getter
@Entity
public class Html {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String category;

    @OneToMany(mappedBy = "html", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Content> content;

    @JoinColumn(name = "title_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Title title;

    public Long getId(Long htmlId) {
        this.id = htmlId;
        return htmlId;
    }
}
