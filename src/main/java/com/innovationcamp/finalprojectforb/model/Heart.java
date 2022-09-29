package com.innovationcamp.finalprojectforb.model;

import com.innovationcamp.finalprojectforb.model.roadmap.Content;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name = "heart")
@Entity
public class Heart {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;


    @JoinColumn(name = "content_id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private Content content;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private Member member;

    public Heart(Content content, Member member){
        this.content = content;
        this.member = member;
    }
}