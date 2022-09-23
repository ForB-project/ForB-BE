package com.innovationcamp.finalprojectforb.repository.roadmap;

import com.innovationcamp.finalprojectforb.model.Member;
import com.innovationcamp.finalprojectforb.model.roadmap.Content;
import com.innovationcamp.finalprojectforb.model.roadmap.Html;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HtmlRepository extends JpaRepository<Html, Long> {

}
