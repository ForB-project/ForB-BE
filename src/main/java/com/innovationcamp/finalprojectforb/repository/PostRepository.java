package com.innovationcamp.finalprojectforb.repository;

import com.innovationcamp.finalprojectforb.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByCreatedAtDesc();
}
