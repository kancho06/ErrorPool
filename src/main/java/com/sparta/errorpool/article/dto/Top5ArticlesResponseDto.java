package com.sparta.errorpool.article.dto;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@ApiModel(value = "추천 게시글 목록 응답", description = "추천 게시글 목록 응답 DTO")
public class Top5ArticlesResponseDto {
    private List<ArticleResponseDto> top5Articles;
    private List<ArticleResponseDto> top5ReactArticleList;
    private List<ArticleResponseDto> top5SpringArticleList;
    private List<ArticleResponseDto> top5NodeJsArticleList;
}
