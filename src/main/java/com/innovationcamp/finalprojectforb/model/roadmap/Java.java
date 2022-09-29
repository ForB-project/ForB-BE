package com.innovationcamp.finalprojectforb.model.roadmap;

import lombok.Getter;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity
public class Java {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String category;

    @OneToMany(mappedBy = "java", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Content> content;

    @JoinColumn(name = "title_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Title title;

    public Long getId(Long javaId) {
        this.id = javaId;
        return javaId;
    }
}
