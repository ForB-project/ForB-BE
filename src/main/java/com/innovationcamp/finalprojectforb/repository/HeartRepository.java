package com.innovationcamp.finalprojectforb.repository;

import com.innovationcamp.finalprojectforb.model.Heart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HeartRepository  extends JpaRepository<Heart, Long> {
    // 동일한 콘텐츠에 동일한 계정으로 이미 좋아요한 내역이 있는지 찾을 때 사용할 메소드
    List<Heart> findByMemberIdAndContentId(Long member, Long contentId);

    List<Heart> findByMemberId(Long memberId);

}
