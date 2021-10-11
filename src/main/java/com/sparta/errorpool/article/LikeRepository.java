package com.sparta.errorpool.article;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByArticleIdAndUserId(Long articleId, Long userId);

    Integer countByArticleId(Long articleId);
}
