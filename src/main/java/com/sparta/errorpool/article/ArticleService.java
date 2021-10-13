package com.sparta.errorpool.article;

import com.sparta.errorpool.article.dto.ArticleUpdateRequestDto;
import com.sparta.errorpool.comment.Comment;
import com.sparta.errorpool.comment.CommentRepository;
import com.sparta.errorpool.exception.ArticleNotFoundException;
import com.sparta.errorpool.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final LikeInfoRepository likeRepository;
    private final CommentRepository commentRepository;

    public Page<Article> getArticlesInSkillAndCategory(Integer skillId, Integer categoryId) {
        return articleRepository.findAllBySkillAndCategory
                (PageRequest.of(0, 5), Skill.getSkillById(skillId), Category.getCategoryById(categoryId));

    }

    public Article getArticleById(Long articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow(
                () -> new ArticleNotFoundException("게시글을 찾을 수 없습니다.")
        );
        article.setViewCount(article.getViewCount()+1);

        return article;
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

    public Page<Article> getBestArticleListOfAllSkill() {
        return articleRepository.findTopByOrderByLikeCountDesc(PageRequest.of(0, 5));
    }

    public Page<Article> getBestArticleListIn(Skill skill) {
        return articleRepository.findTopBySkillOrderByLikeCountDesc(PageRequest.of(0, 5), skill);
    }

    public void likeArticle(Long article_id, User user) {
        Article article = getArticleById(article_id);
        Optional<LikeInfo> optionalLike = likeRepository.findByArticleIdAndUserId(article_id, user.getId());
        if ( optionalLike.isPresent() ) {
            likeRepository.delete(optionalLike.get());
        } else {
            likeRepository.save(new LikeInfo(user, article));
        }
    }

    public Page<Article> getArticles(User user) {
        return articleRepository.findAllByUserOrderByCreatedAtDesc(user, PageRequest.of(0,5));
    }
}
