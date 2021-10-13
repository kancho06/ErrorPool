package com.sparta.errorpool.article;

import com.sparta.errorpool.article.dto.*;
import com.sparta.errorpool.defaultResponse.DefaultResponse;
import com.sparta.errorpool.defaultResponse.ResponseMessage;
import com.sparta.errorpool.defaultResponse.StatusCode;
import com.sparta.errorpool.defaultResponse.SuccessYn;
import com.sparta.errorpool.exception.UnauthenticatedException;
import com.sparta.errorpool.security.UserDetailsImpl;
import com.sparta.errorpool.user.User;
import com.sparta.errorpool.util.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    public ResponseEntity<DefaultResponse<List<ArticleResponseDto>>> getArticlesInSkillAndCategory(@AuthenticationPrincipal UserDetails userDetails,
                                                                  @PathVariable("skill_id") Integer skillId,
                                                                  @PathVariable("category_id") Integer categoryId) {
        List<Article> articleList = articleService.getArticlesInSkillAndCategory(skillId, categoryId).toList();
        List<ArticleResponseDto> data = articleListToArticleResponseDto(articleList, userDetails);
        return ResponseEntity.ok(DefaultResponse.res(SuccessYn.OK, StatusCode.OK, ResponseMessage.GET_ARTICLE_SUCCESS, data));
    }

    private ArticleDetailResponseDto getArticleDetailResponseDto(UserDetails userDetails,
                                                                 Article article) {
        return article.toArticleDetailResponseDto(userDetails);
    }

    @GetMapping("/articles/recommended")
    public Top5ArticlesResponseDto getBestArticles(@AuthenticationPrincipal UserDetails userDetails) {
        return Top5ArticlesResponseDto.builder()
                .top5Articles(articleListToArticleResponseDto(articleService.getBestArticleListOfAllSkill().toList(), userDetails))
                .top5ReactArticleList(articleListToArticleResponseDto(articleService.getBestArticleListIn(Skill.REACT).toList(), userDetails))
                .top5SpringArticleList(articleListToArticleResponseDto(articleService.getBestArticleListIn(Skill.SPRING).toList(), userDetails))
                .top5NodeJsArticleList(articleListToArticleResponseDto(articleService.getBestArticleListIn(Skill.REACT).toList(), userDetails))
                .build();
    }

    private List<ArticleResponseDto> articleListToArticleResponseDto(List<Article> articleList,
                                                                     UserDetails userDetails) {
        List<ArticleResponseDto> articleResponseDtoList = new ArrayList<>();
        articleList.stream()
                .map(article -> article.toArticleResponseDto(userDetails))
                .forEach(articleResponseDtoList::add);
        return articleResponseDtoList;
    }

    @GetMapping("/articles/{article_id}")
    public ResponseEntity<DefaultResponse<ArticleDetailResponseDto>> getArticleDetails(@AuthenticationPrincipal UserDetails userDetails,
                                                                                       @PathVariable("article_id") Long articleId) {
        Article article = articleService.getArticleById(articleId);

        ArticleDetailResponseDto data = getArticleDetailResponseDto(userDetails, article);
        return ResponseEntity.ok(DefaultResponse.res(SuccessYn.OK, StatusCode.OK, ResponseMessage.GET_ARTICLE_SUCCESS, data));
    }

    @PostMapping("/articles")
    public ResponseEntity<DefaultResponse<Void>> createArticle(@AuthenticationPrincipal UserDetails userDetails,
                              @RequestBody ArticleCreateRequestDto requestDto) {
        Article article = Article.of(requestDto, userFromUserDetails(userDetails));
            if ( requestDto.getImg() != null ) {
                Path imgUrl = imageService.saveFile(requestDto.getImg());
                article.setImgUrl(imgUrl.toString());
            }
            articleService.createArticle(article);
        return ResponseEntity.ok(DefaultResponse.res(SuccessYn.OK, StatusCode.OK, "게시글 추가 성공", null));
    }

    @PutMapping("/articles/{article_id}")
    public ResponseEntity<DefaultResponse<Void>> updateArticle(@AuthenticationPrincipal UserDetails userDetails,
                              @PathVariable("article_id") Long articleId,
                              @RequestBody ArticleUpdateRequestDto requestDto) {
        articleService.updateArticle(articleId, requestDto, userFromUserDetails(userDetails));
        return ResponseEntity.ok(DefaultResponse.res(SuccessYn.OK, StatusCode.OK, "게시글 수정 성공", null));
    }

    @DeleteMapping("/articles/{article_id}")
    public ResponseEntity<DefaultResponse<Void>> deleteArticle(@AuthenticationPrincipal UserDetails userDetails,
                              @PathVariable("article_id") Long articleId) {
        articleService.deleteArticle(articleId, userFromUserDetails(userDetails));
        return ResponseEntity.ok(DefaultResponse.res(SuccessYn.OK, StatusCode.OK, "게시글 삭제 성공", null));
    }

    private User userFromUserDetails(UserDetails userDetails) {
        if ( userDetails instanceof UserDetailsImpl ) {
            return ((UserDetailsImpl) userDetails).getUser();
        } else {
            throw new UnauthenticatedException("로그인이 필요합니다.");
        }
    }

    @PostMapping("/articles/{article_id}/like")
    public ResponseEntity<DefaultResponse<Void>> likeArticle(@AuthenticationPrincipal UserDetails userDetails,
                            @PathVariable Long article_id) {
        if ( userDetails instanceof UserDetailsImpl ) {
            User user = ((UserDetailsImpl) userDetails).getUser();
            articleService.likeArticle(article_id, user);
        }
        return ResponseEntity.ok(DefaultResponse.res(SuccessYn.OK, StatusCode.OK, "좋아요 성공", null));
    }
}
