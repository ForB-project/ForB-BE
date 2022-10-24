package com.innovationcamp.finalprojectforb.repository.roadmap;

import com.innovationcamp.finalprojectforb.model.roadmap.Title;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface TitleRepository  extends JpaRepository<Title, Long> {
    List<Title> findTop4ByOrderById();

    List<Title> findAllByIdNot(Long Id1);
}
