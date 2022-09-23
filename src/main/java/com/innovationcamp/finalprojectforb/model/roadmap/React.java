package com.innovationcamp.finalprojectforb.model.roadmap;

import javax.persistence.*;

@Entity
public class React {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String category;

    @JoinColumn(name = "title_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Title title;


}
