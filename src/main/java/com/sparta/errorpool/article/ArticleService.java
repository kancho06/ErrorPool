package com.sparta.errorpool.article;

import com.sparta.errorpool.user.User;
import lombok.RequiredArgsConstructor;
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
                //todo 찾을 수 없는 게시글 예외 만들고 변경
                () -> new RuntimeException()
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
            //todo 권한 예외 생성 및 변경 필요
            throw new RuntimeException();
        }
    }

    public void deleteArticle(Long articleId, User user) {
        Article article = getArticleById(articleId);
        if ( article.isWritedBy(user) ) {
            articleRepository.delete(article);
        } else {
            //todo 권한 예외 생성 및 변경 필요
            throw new RuntimeException();
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
