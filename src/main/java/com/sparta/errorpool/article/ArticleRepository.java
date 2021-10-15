package com.sparta.errorpool.article;

import com.sparta.errorpool.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    @EntityGraph(attributePaths = {"likes"})
    @Query("select distinct a from Article a " +
            "where a.skill = :skill " +
            "and a.category = :category " +
            "order by a.createdAt desc")
    Page<Article> findAllBySkillAndCategory(Pageable pageable, Skill skill, Category category);

    @EntityGraph(attributePaths = {"likes"})
    @Query("select distinct a from Article a " +
            "where a.skill = ?1 " +
            "and a.category = ?2 " +
            "and ( a.title like %?3% or a.content like %?3% ) " +
            "order by a.createdAt desc")
    Page<Article> findAllBySkillAndCategoryByQuery(Pageable pageable, Skill skillById, Category categoryById, String query);

    @Query("select a from Article a order by size(a.likes) desc")
    Page<Article> findTopByOrderByLikeCountDesc(Pageable pageable);

    @Query("select a from Article a where a.skill = :skill order by size(a.likes) desc")
    Page<Article> findTopBySkillOrderByLikeCountDesc(Pageable pageable, Skill skill);

    Page<Article> findAllByUserOrderByCreatedAtDesc(User user, Pageable pageable);
}
