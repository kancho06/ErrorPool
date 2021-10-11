package com.sparta.errorpool.article;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findAllBySkillAndCategory(Skill skill, Category category);
}
