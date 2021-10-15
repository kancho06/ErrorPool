package com.sparta.errorpool.article;

import com.sparta.errorpool.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    @EntityGraph(attributePaths = {"likes"})
    @Query("select distinct a from Article a " +
            "where a.skill = :skill " +
            "and a.category = :category " +
            "order by a.createdAt desc")
    Page<Article> findAllBySkillAndCategory(Pageable pageable, Skill skill, Category category);

    @Query("select a from Article a order by size(a.likes) desc")
    Page<Article> findTopByOrderByLikeCountDesc(Pageable pageable);

    @Query("select a from Article a where a.skill = :skill order by size(a.likes) desc")
    Page<Article> findTopBySkillOrderByLikeCountDesc(Pageable pageable, Skill skill);

    List<Article> findAllByUserOrderByCreatedAtDesc(User user);
}
