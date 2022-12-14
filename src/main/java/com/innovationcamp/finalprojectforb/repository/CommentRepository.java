package com.innovationcamp.finalprojectforb.repository;

import com.innovationcamp.finalprojectforb.model.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByOrderByCreatedAtDesc();
    List<Comment> findAllByPostIdOrderByCreatedAtDesc(Long postId, Pageable pageable);
}
