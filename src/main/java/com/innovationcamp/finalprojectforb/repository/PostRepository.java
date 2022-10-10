package com.innovationcamp.finalprojectforb.repository;

import com.innovationcamp.finalprojectforb.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    Post findPostById(Long id);
    List<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);
    Page<Post> findByMemberId(Long memberId, Pageable pageable);
    List<Post> findByTitleContainingOrderByCreatedAtDesc(String keyword);
}
