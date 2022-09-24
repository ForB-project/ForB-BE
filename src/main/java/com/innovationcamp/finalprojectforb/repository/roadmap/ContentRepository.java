package com.innovationcamp.finalprojectforb.repository.roadmap;

import com.innovationcamp.finalprojectforb.model.roadmap.Content;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContentRepository extends JpaRepository<Content, Long> {
    List<Content> findByHtmlId(Long htmlId);

    List<Content> findByCssId(Long cssId);

    List<Content> findByJsId(Long jsId);

    List<Content> findByReactId(Long reactId);

    List<Content> findBySpringId(Long springId);

    List<Content> findByTitleContaining(String keyword);
}
