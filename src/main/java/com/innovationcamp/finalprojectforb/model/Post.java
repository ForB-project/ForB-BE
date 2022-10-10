package com.innovationcamp.finalprojectforb.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.innovationcamp.finalprojectforb.dto.PostRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Builder
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Post extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column
    private String postImage;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne
    private Member member;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;
    @Column(nullable = false)
    private Long likes = 0L;

    @JsonIgnore
    @OneToMany(mappedBy ="post", cascade = CascadeType.REMOVE)
    private List<LikePost> likePost;

    public Post(PostRequestDto postRequestDto, Member member, String postImage) {
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
        this.postImage = postImage;
        this.member = member;
    }

    public void update(PostRequestDto postRequestDto, String postUpdateImage) {
        String title = postRequestDto.getTitle();
        String content = postRequestDto.getContent();
        String postImage = postUpdateImage;
        if(title != null){
            this.title = title;
        }
        if(content != null){
            this.content = content;
        }
        if(postImage != null){
            this.postImage = postImage;
        } // 수정되지 않은 데이터 기존 유지
    }

    public void updateLikes(Long likes) {
        this.likes = likes;
    }
}
