package com.sparta.errorpool.article;

import com.sparta.errorpool.exception.ArticleNotFoundException;
import com.sparta.errorpool.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final LikeRepository likeRepository;

    public List<Article> getArticlesInSkillAndCategory(Integer skillId, Integer categoryId) {
        return articleRepository.findAllBySkillAndCategory(Skill.getSkillById(skillId), Category.getCategoryById(categoryId));
    }

    public Article getArticleById(Long articleId) {
        return articleRepository.findById(articleId).orElseThrow(
                () -> new ArticleNotFoundException("게시글을 찾을 수 없습니다.")
        );
    }

    public void createArticle(Article article) {
        articleRepository.save(article);
    }

    public void updateArticle(Long articleId, ArticleUpdateRequestDto requestDto, User user) {
        Article article = getArticleById(articleId);
        if ( article.isWritedBy(user) ) {
            article.update(requestDto);
            articleRepository.save(article);
        } else {
            throw new AccessDeniedException("권한이 없습니다.");
        }
    }

    public void deleteArticle(Long articleId, User user) {
        Article article = getArticleById(articleId);
        if ( article.isWritedBy(user) ) {
            articleRepository.delete(article);
        } else {
            throw new AccessDeniedException("권한이 없습니다.");
        }
    }

    public List<Article> getBestArticleListOfAllSkill() {
        return articleRepository.findTop5ByOrderByLikeDesc();
    }

    public List<Article> getBestArticleListIn(Skill skill) {
        return articleRepository.findTop5BySkillOrderByLikeDesc(skill);
    }

    public void likeArticle(Long article_id, User user) {
        if ( likeRepository.findByArticleIdAndUserId(article_id, user.getId()).isPresent() ) {
            //todo 이미 like 이력이 있으면 어떻게 할까?
            return;
        } else {
            likeRepository.save(new Like(article_id, user.getId()));
        }
    }

    public Integer getLikesOfArticle(Long articleId) {
        return likeRepository.countByArticleId(articleId);
    }
}
