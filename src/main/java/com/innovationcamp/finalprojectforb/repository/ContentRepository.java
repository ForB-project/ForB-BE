package com.innovationcamp.finalprojectforb.repository;

import com.innovationcamp.finalprojectforb.model.roadmap.Content;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContentRepository extends JpaRepository<Content, Long> {
    List<Content> findByHtmlId(Long htmlId);

    List<Content> findByCssId(Long cssId);
}
