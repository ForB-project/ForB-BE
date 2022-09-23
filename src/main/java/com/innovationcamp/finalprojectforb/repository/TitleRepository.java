package com.innovationcamp.finalprojectforb.repository;

import com.innovationcamp.finalprojectforb.model.roadmap.Html;
import com.innovationcamp.finalprojectforb.model.roadmap.Title;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TitleRepository  extends JpaRepository<Title, Long> {
}
