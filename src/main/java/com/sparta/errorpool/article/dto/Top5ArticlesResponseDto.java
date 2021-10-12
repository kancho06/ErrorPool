package com.sparta.errorpool.article.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Top5ArticlesResponseDto {
    private List<ArticleResponseDto> top5Articles;
    private List<ArticleResponseDto> top5ReactArticleList;
    private List<ArticleResponseDto> top5SpringArticleList;
    private List<ArticleResponseDto> top5NodeJsArticleList;
}
