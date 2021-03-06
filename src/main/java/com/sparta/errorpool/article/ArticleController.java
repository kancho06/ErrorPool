package com.sparta.errorpool.article;

import com.sparta.errorpool.article.dto.*;
import com.sparta.errorpool.defaultResponse.DefaultResponse;
import com.sparta.errorpool.defaultResponse.ResponseMessage;
import com.sparta.errorpool.defaultResponse.StatusCode;
import com.sparta.errorpool.defaultResponse.SuccessYn;
import com.sparta.errorpool.user.User;
import com.sparta.errorpool.user.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = "Article Controller Api V1")
public class ArticleController {

    private final ArticleService articleService;
    private final UserService userService;

    @ApiOperation(value = "항목 별 게시글 조회")
    @GetMapping("/articles/skill/{skill_id}/{category_id}")
    public ResponseEntity<DefaultResponse<ArticlePageResponseDto>> getArticlesInSkillAndCategory(
            @ApiIgnore @AuthenticationPrincipal UserDetails userDetails,
            @ApiParam(value = "페이지 번호", required = true) @RequestParam("page") Integer page,
            @ApiParam(value = "주특기 번호", required = true) @PathVariable("skill_id") Integer skillId,
            @ApiParam(value = "카테고리 번호", required = true) @PathVariable("category_id") Integer categoryId,
            @ApiParam(value = "검색키", required = false) @RequestParam(value = "query", required = false) String query) {
        Page<Article> articlePage = articleService.getArticlesInSkillAndCategory(page,skillId,categoryId, query);
        ArticlePageResponseDto data = ArticlePageResponseDto.builder()
                .totalPage(articlePage.getTotalPages())
                .page(articlePage.getNumber()+1)
                .articleList(articleListToArticleResponseDto(articlePage, userDetails)).build();
        return ResponseEntity.ok(DefaultResponse.res(SuccessYn.OK, StatusCode.OK, ResponseMessage.GET_ARTICLE_SUCCESS, data));
    }

    private ArticleDetailResponseDto getArticleDetailResponseDto(UserDetails userDetails,
                                                                 Article article) {
        return article.toArticleDetailResponseDto(userDetails);
    }

    @ApiOperation(value = "추천 게시글 조회")
    @GetMapping("/articles/recommended")
    public ResponseEntity<DefaultResponse<Top5ArticlesResponseDto>> getBestArticles(@ApiIgnore @AuthenticationPrincipal UserDetails userDetails) {
        Top5ArticlesResponseDto data = Top5ArticlesResponseDto.builder()
                .top5Articles(articleListToArticleResponseDto(articleService.getBestArticleListOfAllSkill(), userDetails))
                .top5ReactArticleList(articleListToArticleResponseDto(articleService.getBestArticleListIn(Skill.REACT), userDetails))
                .top5SpringArticleList(articleListToArticleResponseDto(articleService.getBestArticleListIn(Skill.SPRING), userDetails))
                .top5NodeJsArticleList(articleListToArticleResponseDto(articleService.getBestArticleListIn(Skill.NODEJS), userDetails))
                .build();
        return ResponseEntity.ok(DefaultResponse.res(SuccessYn.OK, StatusCode.OK, ResponseMessage.GET_ARTICLE_SUCCESS, data));
    }

    private List<ArticleResponseDto> articleListToArticleResponseDto(Page<Article> articlePage,
                                                                     UserDetails userDetails) {
        List<ArticleResponseDto> articleResponseDtoList = new ArrayList<>();
        articlePage.stream()
                .map(article -> article.toArticleResponseDto(userDetails))
                .forEach(articleResponseDtoList::add);
        return articleResponseDtoList;
    }

    @ApiOperation(value = "게시글 상세 조회")
    @GetMapping("/articles/{article_id}")
    public ResponseEntity<DefaultResponse<ArticleDetailResponseDto>> getArticleDetails(
            @ApiIgnore @AuthenticationPrincipal UserDetails userDetails,
            @ApiParam(value = "게시글 ID", required = true) @PathVariable("article_id") Long articleId) {
        Article article = articleService.getArticleAndUpViewCountById(articleId);

        ArticleDetailResponseDto data = getArticleDetailResponseDto(userDetails, article);
        return ResponseEntity.ok(DefaultResponse.res(SuccessYn.OK, StatusCode.OK, ResponseMessage.GET_ARTICLE_SUCCESS, data));
    }

    @ApiOperation(value = "게시글 생성")
    @PostMapping("/articles")
    public ResponseEntity<DefaultResponse<Void>> createArticle(
            @ApiIgnore @AuthenticationPrincipal UserDetails userDetails,
            @ApiParam(value = "게시글 생성 정보", required = true) @Valid @ModelAttribute ArticleCreateRequestDto requestDto) {
        User user = userService.userFromUserDetails(userDetails);
        articleService.createArticle(requestDto, user);
        return ResponseEntity.ok(DefaultResponse.res(SuccessYn.OK, StatusCode.OK, "게시글 추가 성공", null));
    }

    @ApiOperation(value = "게시글 수정")
    @PutMapping("/articles/{article_id}")
    public ResponseEntity<DefaultResponse<Void>> updateArticle(
            @ApiIgnore @AuthenticationPrincipal UserDetails userDetails,
            @ApiParam(value = "게시글 ID", required = true) @PathVariable("article_id") Long articleId,
            @ApiParam(value = "게시글 수정 정보", required = true) @ModelAttribute ArticleUpdateRequestDto requestDto) {
        articleService.updateArticle(articleId, requestDto, userService.userFromUserDetails(userDetails));
        return ResponseEntity.ok(DefaultResponse.res(SuccessYn.OK, StatusCode.OK, "게시글 수정 성공", null));
    }

    @ApiOperation(value = "게시글 삭제")
    @DeleteMapping("/articles/{article_id}")
    public ResponseEntity<DefaultResponse<Void>> deleteArticle(
            @ApiIgnore @AuthenticationPrincipal UserDetails userDetails,
            @ApiParam(value = "게시글 ID", required = true) @PathVariable("article_id") Long articleId) {
        articleService.deleteArticle(articleId, userService.userFromUserDetails(userDetails));
        return ResponseEntity.ok(DefaultResponse.res(SuccessYn.OK, StatusCode.OK, "게시글 삭제 성공", null));
    }

    @ApiOperation(value = "게시글 좋아요")
    @PostMapping("/articles/{article_id}/like")
    public ResponseEntity<DefaultResponse<LikeResponseDto>> likeArticle(
            @ApiIgnore @AuthenticationPrincipal UserDetails userDetails,
            @ApiParam(value = "게시글 ID", required = true) @PathVariable Long article_id) {
        User user = userService.userFromUserDetails(userDetails);
        LikeResponseDto responseDto = LikeResponseDto.of(articleService.likeArticle(article_id, user));
        return ResponseEntity.ok(DefaultResponse.res(SuccessYn.OK, StatusCode.OK, "좋아요 성공", responseDto));
    }
}
