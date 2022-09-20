package com.innovationcamp.finalprojectforb.repository;

import com.innovationcamp.finalprojectforb.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmailAndProvider(String email, String provider);

    Optional<Member> findByEmail(String email);

    Optional<Member> findById(Long id);

}
