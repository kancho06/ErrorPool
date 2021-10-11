package com.sparta.errorpool.article;

import com.sparta.errorpool.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("/articles/skill/{skill_id}/{category_id}")
    public List<Article> getArticlesInSkillAndCategory(@PathVariable("skill_id") Integer skillId,
                                      @PathVariable("category_id") Integer categoryId) {
        return articleService.getArticlesInSkillAndCategory(skillId, categoryId);
    }

    @GetMapping("/articles/{article_id}")
    public Article getArticle(@PathVariable("article_id") Long articleId) {
        return articleService.getArticleById(articleId);
    }

    @PostMapping("/articles")
    public void createArticle(@RequestBody ArticleCreateRequestDto requestDto) {
        User user = null;
        Article article = Article.of(requestDto, user);
        articleService.createArticle(article);
    }

    @PutMapping("/articles/{article_id}")
    public void updateArticle(@PathVariable("article_id") Long articleId,
                              @RequestBody ArticleUpdateRequestDto requestDto) {
        User user = null;
        articleService.updateArticle(articleId, requestDto, user);
    }
}
