package com.innovationcamp.finalprojectforb.repository;

import com.innovationcamp.finalprojectforb.model.Member;
import com.innovationcamp.finalprojectforb.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByMember(Member member);
}
