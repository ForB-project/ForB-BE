package com.innovationcamp.finalprojectforb.repository;

import com.innovationcamp.finalprojectforb.dto.ResponseDto;
import com.innovationcamp.finalprojectforb.model.StackType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StackTypeRepository extends JpaRepository<StackType, Long> {
    Optional<StackType> findByStackType(String type);
}
