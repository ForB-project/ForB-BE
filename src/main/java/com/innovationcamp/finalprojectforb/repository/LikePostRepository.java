package com.innovationcamp.finalprojectforb.repository;

import com.innovationcamp.finalprojectforb.model.LikePost;
import com.innovationcamp.finalprojectforb.model.Member;
import com.innovationcamp.finalprojectforb.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikePostRepository extends JpaRepository<LikePost, Long> {
    Long countAllByPostId(Long postId);
    LikePost findByPostIdAndMemberId(Long postId, Long memberId);
    boolean existsByMemberAndPost(Member member, Post post);

}
