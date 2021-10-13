package com.sparta.errorpool.article;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeInfoRepository extends JpaRepository<LikeInfo, Long> {
    Optional<LikeInfo> findByArticleIdAndUserId(Long articleId, Long userId);

    Integer countByArticleId(Long articleId);
}
