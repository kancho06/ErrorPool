package com.sparta.errorpool.article.dto;

import com.sparta.errorpool.article.dto.ArticleResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Top5ArticlesResponseDto {
    private List<ArticleResponseDto> top5Articles = new ArrayList<>();
    private List<ArticleResponseDto> top5ReactArticleList = new ArrayList<>();
    private List<ArticleResponseDto> top5SpringArticleList = new ArrayList<>();
    private List<ArticleResponseDto> top5NodeJsArticleList = new ArrayList<>();
}
