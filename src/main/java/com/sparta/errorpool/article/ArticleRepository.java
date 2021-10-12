package com.sparta.errorpool.article;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findAllBySkillAndCategory(Skill skill, Category category);

    @Query("select a from Article a order by a.likes.size desc")
    Page<Article> findTop5ByOrderByLikeCountDesc(Pageable pageable);

    @Query("select a from Article a where a.skill = :skill order by a.likes.size desc")
    Page<Article> findTop5BySkillOrderByLikeCountDesc(Pageable pageable, Skill skill);
}
