package com.innovationcamp.finalprojectforb.repository;

import com.innovationcamp.finalprojectforb.model.MyRoadmap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MyRoadmapRepository extends JpaRepository<MyRoadmap, Long> {
    Optional<MyRoadmap> findById(Long id);
}
