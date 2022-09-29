package com.innovationcamp.finalprojectforb.repository;

import com.innovationcamp.finalprojectforb.model.StackType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StackTypeRepository extends JpaRepository<StackType, Long> {
    List<StackType> findByStackType(String type);
}
