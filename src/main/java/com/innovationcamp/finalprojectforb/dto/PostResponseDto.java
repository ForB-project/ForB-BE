package com.innovationcamp.finalprojectforb.dto;

import com.innovationcamp.finalprojectforb.model.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {
    private Long id;
    private String nickname;
    private String title;
    private String content;
    private String postImg;
    private Long likes;
    private LocalDateTime createdAt;
    private List<CommentResponseDto> commentList;


    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.nickname = post.getMember().getNickname();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.postImg = post.getPostImg();
        this.likes = post.getLikes();
        this.createdAt = post.getCreatedAt();
    }
}
