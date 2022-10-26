package com.innovationcamp.finalprojectforb.repository;

import com.innovationcamp.finalprojectforb.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);
    Page<Post> findByMemberId(Long memberId, Pageable pageable);
    Page<Post> findByTitleContainingOrderByCreatedAtDesc(String keyword, Pageable pageable);
}
