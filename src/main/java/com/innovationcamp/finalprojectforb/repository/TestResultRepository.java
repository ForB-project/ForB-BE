package com.innovationcamp.finalprojectforb.repository;

import com.innovationcamp.finalprojectforb.model.TestResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestResultRepository extends JpaRepository<TestResult, Long> {
    List<TestResult> findByStackType(String type);
}
