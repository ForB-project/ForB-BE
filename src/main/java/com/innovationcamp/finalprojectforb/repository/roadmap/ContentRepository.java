package com.innovationcamp.finalprojectforb.repository.roadmap;

import com.innovationcamp.finalprojectforb.model.roadmap.Content;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContentRepository extends JpaRepository<Content, Long> {
    Page<Content> findByHtmlId(Long htmlId, Pageable pageable);

    Page<Content> findByCssId(Long cssId, Pageable pageable);

    Page<Content> findByJsId(Long jsId, Pageable pageable);

    Page<Content> findByReactId(Long reactId, Pageable pageable);

    Page<Content> findByJavaId(Long javaId, Pageable pageable);

    Page<Content> findBySpringId(Long springId, Pageable pageable);

    List<Content> findByTitleContaining(String keyword);

    Page<Content> findByMemberId(Long memberId, Pageable pageable);
}
