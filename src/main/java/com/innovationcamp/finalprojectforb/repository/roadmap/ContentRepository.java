package com.innovationcamp.finalprojectforb.repository.roadmap;

import com.innovationcamp.finalprojectforb.model.roadmap.Content;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContentRepository extends JpaRepository<Content, Long> {
    Page<Content> findByHtmlIdOrderByHeartCntDesc(Long htmlId, Pageable pageable);

    Page<Content> findByCssIdOrderByHeartCntDesc(Long cssId, Pageable pageable);

    Page<Content> findByJsIdOrderByHeartCntDesc(Long jsId, Pageable pageable);

    Page<Content> findByReactIdOrderByHeartCntDesc(Long reactId, Pageable pageable);

    Page<Content> findByJavaIdOrderByHeartCntDesc(Long javaId, Pageable pageable);

    Page<Content> findBySpringIdOrderByHeartCntDesc(Long springId, Pageable pageable);

    List<Content> findByTitleContaining(String keyword);
}
