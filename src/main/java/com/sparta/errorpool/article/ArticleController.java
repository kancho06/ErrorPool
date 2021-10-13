package com.sparta.errorpool.article;

import com.sparta.errorpool.article.dto.*;
import com.sparta.errorpool.defaultResponse.DefaultResponse;
import com.sparta.errorpool.defaultResponse.StatusCode;
import com.sparta.errorpool.defaultResponse.SuccessYn;
import com.sparta.errorpool.security.UserDetailsImpl;
import com.sparta.errorpool.user.User;
import com.sparta.errorpool.util.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final ImageService imageService;

    @GetMapping("/articles/skill/{skill_id}/{category_id}")
    public ResponseEntity<DefaultResponse<List<ArticleResponseDto>>> getArticlesInSkillAndCategory(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                  @PathVariable("skill_id") Integer skillId,
                                                                  @PathVariable("category_id") Integer categoryId) {
        List<Article> articleList = articleService.getArticlesInSkillAndCategory(skillId, categoryId);
        List<ArticleResponseDto> data = articleListToArticleResponseDto(articleList, userDetails);
        return ResponseEntity.ok(DefaultResponse.res(SuccessYn.OK, StatusCode.OK, null, data));
    }

    private ArticleDetailResponseDto getArticleDetailResponseDto(UserDetailsImpl userDetails,
                                                                 Article article) {
        ArticleDetailResponseDto responseDto = article.toArticleDetailResponseDto();
        setLikeAndLikeCountAndCommentsInto(responseDto, userDetails);
        return responseDto;
    }

    private void setLikeAndLikeCountAndCommentsInto(ArticleDetailResponseDto responseDto,
                                                    UserDetailsImpl userDetails) {
        if ( userDetails != null ) {
            responseDto.setLiked(articleService.IsLikedBy(userDetails.getUser().getId(), responseDto.getArticleId()));
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
                .map(article -> article.toArticleResponseDto(userDetails))
                .forEach(articleResponseDtoList::add);
        return articleResponseDtoList;
    }

    @GetMapping("/articles/{article_id}")
    public ArticleDetailResponseDto getArticleDetails(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                      @PathVariable("article_id") Long articleId) {
        Article article = articleService.getArticleById(articleId);

        return getArticleDetailResponseDto(userDetails, article);
    }

    @PostMapping("/articles")
    public ResponseEntity<DefaultResponse<Void>> createArticle(@AuthenticationPrincipal UserDetailsImpl userDetails,
                              @RequestBody ArticleCreateRequestDto requestDto) {
        Article article = Article.of(requestDto, userDetails.getUser());
        if ( requestDto.getImg() != null ) {
            Path imgUrl = imageService.saveFile(requestDto.getImg());
            article.setImgUrl(imgUrl.toString());
        }
        articleService.createArticle(article);

        return ResponseEntity.ok(DefaultResponse.res(SuccessYn.OK, StatusCode.OK, null, null));
    }

    @PutMapping("/articles/{article_id}")
    public ResponseEntity<DefaultResponse<Void>> updateArticle(@AuthenticationPrincipal UserDetailsImpl userDetails,
                              @PathVariable("article_id") Long articleId,
                              @RequestBody ArticleUpdateRequestDto requestDto) {
        User user = userDetails.getUser();
        articleService.updateArticle(articleId, requestDto, user);
        return ResponseEntity.ok(DefaultResponse.res(SuccessYn.OK, StatusCode.OK, null, null));
    }

    @DeleteMapping("/articles/{article_id}")
    public ResponseEntity<DefaultResponse<Void>> deleteArticle(@AuthenticationPrincipal UserDetailsImpl userDetails,
                              @PathVariable("article_id") Long articleId) {
        User user = userDetails.getUser();
        articleService.deleteArticle(articleId, user);
        return ResponseEntity.ok(DefaultResponse.res(SuccessYn.OK, StatusCode.OK, null, null));
    }

    @PostMapping("/articles/{article_id}")
    public ResponseEntity<DefaultResponse<Void>> likeArticle(@AuthenticationPrincipal UserDetailsImpl userDetails,
                            @PathVariable Long article_id) {
        User user = userDetails.getUser();
        articleService.likeArticle(article_id, user);
        return ResponseEntity.ok(DefaultResponse.res(SuccessYn.OK, StatusCode.OK, null, null));
    }
}
