package com.innovationcamp.finalprojectforb.repository;

import com.innovationcamp.finalprojectforb.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    Member findMemberById (Long id);
    boolean existsMemberByNickname(String nickname);
}
