package com.sparta.errorpool.article.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class Top5ArticlesResponseDto {
    private List<ArticleResponseDto> top5Articles;
    private List<ArticleResponseDto> top5ReactArticleList;
    private List<ArticleResponseDto> top5SpringArticleList;
    private List<ArticleResponseDto> top5NodeJsArticleList;
}
