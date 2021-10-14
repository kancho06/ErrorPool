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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = "Article Controller Api V1")
public class ArticleController {

    private final ArticleService articleService;
    private final ImageService imageService;

    @ApiOperation(value = "항목 별 게시글 조회")
    @GetMapping("/articles/skill/{skill_id}/{category_id}")
    public ResponseEntity<DefaultResponse<List<ArticleResponseDto>>>
    getArticlesInSkillAndCategory(@ApiIgnore @AuthenticationPrincipal UserDetails userDetails,
                                  @ApiParam(value = "주특기 번호", required = true) @PathVariable("skill_id") Integer skillId,
                                  @ApiParam(value = "카테고리 번호", required = true) @PathVariable("category_id") Integer categoryId) {
        List<Article> articleList = articleService.getArticlesInSkillAndCategory(skillId,categoryId).toList();
        List<ArticleResponseDto> data = articleListToArticleResponseDto(articleList, userDetails);
        return ResponseEntity.ok(DefaultResponse.res(SuccessYn.OK, StatusCode.OK, ResponseMessage.GET_ARTICLE_SUCCESS, data));
    }

    private ArticleDetailResponseDto getArticleDetailResponseDto(UserDetails userDetails,
                                                                 Article article) {
        return article.toArticleDetailResponseDto(userDetails);
    }

    @ApiOperation(value = "추천 게시글 조회")
    @GetMapping("/articles/recommended")
    public TopArticlesResponseDto getBestArticles(@ApiIgnore @AuthenticationPrincipal UserDetails userDetails) {
        return TopArticlesResponseDto.builder()
                .topArticles(articleListToArticleResponseDto(articleService.getBestArticleListOfAllSkill().toList(), userDetails))
                .topReactArticleList(articleListToArticleResponseDto(articleService.getBestArticleListIn(Skill.REACT).toList(), userDetails))
                .topSpringArticleList(articleListToArticleResponseDto(articleService.getBestArticleListIn(Skill.SPRING).toList(), userDetails))
                .topNodeJsArticleList(articleListToArticleResponseDto(articleService.getBestArticleListIn(Skill.REACT).toList(), userDetails))
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

    @ApiOperation(value = "게시글 상세 조회")
    @GetMapping("/articles/{article_id}")
    public ResponseEntity<DefaultResponse<ArticleDetailResponseDto>>
    getArticleDetails(@ApiIgnore @AuthenticationPrincipal UserDetails userDetails,
                      @ApiParam(value = "게시글 ID", required = true) @PathVariable("article_id") Long articleId) {
        Article article = articleService.getArticleById(articleId);

        ArticleDetailResponseDto data = getArticleDetailResponseDto(userDetails, article);
        return ResponseEntity.ok(DefaultResponse.res(SuccessYn.OK, StatusCode.OK, ResponseMessage.GET_ARTICLE_SUCCESS, data));
    }

    @ApiOperation(value = "게시글 생성")
    @PostMapping("/articles")
    public ResponseEntity<DefaultResponse<Void>> createArticle(
            @ApiIgnore @AuthenticationPrincipal UserDetails userDetails,
            @ApiParam(value = "게시글 생성 정보", required = true) @RequestBody ArticleCreateRequestDto requestDto) {
        Article article = Article.of(requestDto, userFromUserDetails(userDetails));
            if ( requestDto.getImg() != null ) {
                Path imgUrl = imageService.saveFile(requestDto.getImg());
                article.setImgUrl(imgUrl.toString());
            }
            articleService.createArticle(article);
        return ResponseEntity.ok(DefaultResponse.res(SuccessYn.OK, StatusCode.OK, "게시글 추가 성공", null));
    }

    @ApiOperation(value = "게시글 수정")
    @PutMapping("/articles/{article_id}")
    public ResponseEntity<DefaultResponse<Void>> updateArticle(
            @ApiIgnore @AuthenticationPrincipal UserDetails userDetails,
            @ApiParam(value = "게시글 ID", required = true) @PathVariable("article_id") Long articleId,
            @ApiParam(value = "게시글 수정 정보", required = true) @RequestBody ArticleUpdateRequestDto requestDto) {
        articleService.updateArticle(articleId, requestDto, userFromUserDetails(userDetails));
        return ResponseEntity.ok(DefaultResponse.res(SuccessYn.OK, StatusCode.OK, "게시글 수정 성공", null));
    }

    @ApiOperation(value = "게시글 삭제")
    @DeleteMapping("/articles/{article_id}")
    public ResponseEntity<DefaultResponse<Void>> deleteArticle(
            @ApiIgnore @AuthenticationPrincipal UserDetails userDetails,
            @ApiParam(value = "게시글 ID", required = true) @PathVariable("article_id") Long articleId) {
        articleService.deleteArticle(articleId, userFromUserDetails(userDetails));
        return ResponseEntity.ok(DefaultResponse.res(SuccessYn.OK, StatusCode.OK, "게시글 삭제 성공", null));
    }

    @ApiOperation(value = "게시글 좋아요")
    @PostMapping("/articles/{article_id}/like")
    public ResponseEntity<DefaultResponse<Void>> likeArticle(
            @ApiIgnore @AuthenticationPrincipal UserDetails userDetails,
            @ApiParam(value = "게시글 ID", required = true) @PathVariable Long article_id) {
        User user = userFromUserDetails(userDetails);
        articleService.likeArticle(article_id, user);
        return ResponseEntity.ok(DefaultResponse.res(SuccessYn.OK, StatusCode.OK, "좋아요 성공", null));
    }

    private User userFromUserDetails(UserDetails userDetails) {
        if ( userDetails instanceof UserDetailsImpl ) {
            return ((UserDetailsImpl) userDetails).getUser();
        } else {
            throw new UnauthenticatedException("로그인이 필요합니다.");
        }
    }
}
