package com.sparta.errorpool.article;

import com.sparta.errorpool.article.dto.*;
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
    public List<ArticleResponseDto> getArticlesInSkillAndCategory(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                  @PathVariable("skill_id") Integer skillId,
                                                                  @PathVariable("category_id") Integer categoryId) {
        List<Article> articleList = articleService.getArticlesInSkillAndCategory(skillId, categoryId);
        return articleListToArticleResponseDto(articleList, userDetails);
    }

    private ArticleDetailResponseDto getArticleDetailResponseDto(UserDetailsImpl userDetails,
                                                                 Article article) {
        ArticleDetailResponseDto responseDto = article.toArticleDetailResponseDto();
        setLikeAndLikeCountAndCommentsInto(responseDto, userDetails);
        return responseDto;
    }

    private void setLikeAndLikeCountAndCommentsInto(ArticleDetailResponseDto responseDto,
                                                    UserDetailsImpl userDetails) {
        responseDto.setLikeCount(articleService.getLikesOfArticle(responseDto.getArticleId()));
        if ( userDetails != null ) {
            responseDto.setLiksStatus(articleService.IsLikedBy(userDetails.getUser().getId(), responseDto.getArticleId()));
        }
        responseDto.addCommentsDtoListFrom(articleService.getComments(responseDto.getArticleId()));
    }

    @GetMapping("/articles/mainLists")
    public Top5ArticlesResponseDto getBestArticles(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return Top5ArticlesResponseDto.builder()
                .top5Articles(articleListToArticleResponseDto(articleService.getBestArticleListOfAllSkill().toList(), userDetails))
                .top5ReactArticleList(articleListToArticleResponseDto(articleService.getBestArticleListIn(Skill.REACT).toList(), userDetails))
                .top5SpringArticleList(articleListToArticleResponseDto(articleService.getBestArticleListIn(Skill.SPRING).toList(), userDetails))
                .top5NodeJsArticleList(articleListToArticleResponseDto(articleService.getBestArticleListIn(Skill.REACT).toList(), userDetails))
                .build();
    }

    private List<ArticleResponseDto> articleListToArticleResponseDto(List<Article> articleList,
                                                                     UserDetailsImpl userDetails) {
        List<ArticleResponseDto> articleResponseDtoList = new ArrayList<>();
        articleList.stream()
                .parallel()
                .map(Article::toArticleResponseDto)
                .map(responseDto -> setLikeAndLikeCountOf(responseDto, userDetails))
                .forEach(articleResponseDtoList::add);
        return articleResponseDtoList;
    }

    private ArticleResponseDto setLikeAndLikeCountOf(ArticleResponseDto responseDto,
                                                     UserDetailsImpl userDetails) {
        responseDto.setLikeCount(articleService.getLikesOfArticle(responseDto.getArticleId()));
        if ( userDetails != null ) {
            responseDto.setLiksStatus(articleService.IsLikedBy(userDetails.getUser().getId(), responseDto.getArticleId()));
        }
        return responseDto;
    }

    @GetMapping("/articles/{article_id}")
    public ArticleDetailResponseDto getArticleDetails(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                      @PathVariable("article_id") Long articleId) {
        Article article = articleService.getArticleById(articleId);

        return getArticleDetailResponseDto(userDetails, article);
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
}
