package com.sparta.errorpool.article;

import com.sparta.errorpool.security.UserDetailsImpl;
import com.sparta.errorpool.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("/articles/skill/{skill_id}/{category_id}")
    public List<ArticleResponseDto> getArticlesInSkillAndCategory(@PathVariable("skill_id") Integer skillId,
                                      @PathVariable("category_id") Integer categoryId) {
        List<Article> articleList = articleService.getArticlesInSkillAndCategory(skillId, categoryId);
        return articleListToArticleResponseDto(articleList);
    }

    @GetMapping("/articles/best")
    public Top5ArticlesResponseDto getBestArticles() {
        Top5ArticlesResponseDto responseDto = new Top5ArticlesResponseDto();
        responseDto.setTop5Articles(
                articleListToArticleResponseDto(articleService.getBestArticleListOfAllSkill())
        );
        responseDto.setTop5ReactArticleList(
                articleListToArticleResponseDto(articleService.getBestArticleListIn(Skill.REACT))
        );
        responseDto.setTop5SpringArticleList(
                articleListToArticleResponseDto(articleService.getBestArticleListIn(Skill.SPRING))
        );
        responseDto.setTop5NodeJsArticleList(
                articleListToArticleResponseDto(articleService.getBestArticleListIn(Skill.REACT))
        );
        return responseDto;
    }

    @GetMapping("/articles/{article_id}")
    public ArticleResponseDto getArticle(@PathVariable("article_id") Long articleId) {
        Article article = articleService.getArticleById(articleId);
        ArticleResponseDto responseDto = article.toArticleResponseDto();
        responseDto.setLike(articleService.getLikesOfArticle(articleId));
        return responseDto;
    }

    @PostMapping("/articles")
    public void createArticle(@AuthenticationPrincipal UserDetailsImpl userDetails,
                              @RequestBody ArticleCreateRequestDto requestDto) {
        Article article = Article.of(requestDto, userDetails.getUser());
        articleService.createArticle(article);
    }

    @PutMapping("/articles/{article_id}")
    public void updateArticle(@AuthenticationPrincipal UserDetailsImpl userDetails,
                              @PathVariable("article_id") Long articleId,
                              @RequestBody ArticleUpdateRequestDto requestDto) {
        User user = userDetails.getUser();
        articleService.updateArticle(articleId, requestDto, user);
    }

    @DeleteMapping("/articles/{article_id}")
    public void deleteArticle(@AuthenticationPrincipal UserDetailsImpl userDetails,
                              @PathVariable("article_id") Long articleId) {
        User user = userDetails.getUser();
        articleService.deleteArticle(articleId, user);
    }

    @PostMapping("/articles/{article_id}")
    public void likeArticle(@AuthenticationPrincipal UserDetailsImpl userDetails,
                            @PathVariable Long article_id) {
        User user = userDetails.getUser();
        articleService.likeArticle(article_id, user);
    }

    private List<ArticleResponseDto> articleListToArticleResponseDto(List<Article> articleList) {
        List<ArticleResponseDto> articleResponseDtoList = new ArrayList<>();
        articleList.stream()
                .parallel()
                .map(Article::toArticleResponseDto)
                .peek(responseDto -> responseDto.setLike(articleService.getLikesOfArticle(responseDto.getArticleId())))
                .forEach(articleResponseDtoList::add);
        return articleResponseDtoList;
    }
}
