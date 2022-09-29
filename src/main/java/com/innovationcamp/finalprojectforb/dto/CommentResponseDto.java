package com.innovationcamp.finalprojectforb.dto;

import com.innovationcamp.finalprojectforb.model.Comment;
import com.innovationcamp.finalprojectforb.model.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDto {
    private Long postId;
    private Long id;
    private String nickname;
    private String content;
    private LocalDateTime createdAt;

    public CommentResponseDto(Comment comment) {
        this.postId = comment.getPost().getId();
        this.id = comment.getId();
        this.nickname = comment.getMember().getNickname();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
    }
}
