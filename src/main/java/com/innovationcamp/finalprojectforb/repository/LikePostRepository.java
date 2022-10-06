package com.innovationcamp.finalprojectforb.repository;

import com.innovationcamp.finalprojectforb.model.LikePost;
import com.innovationcamp.finalprojectforb.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikePostRepository extends JpaRepository<LikePost, Long> {
    Long countAllByPostId(Long postId);

    List<LikePost> findByMember(Member member);
    LikePost findByPostIdAndMemberId(Long postId, Long memberId);
}
