package com.sparta.errorpool.article;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Top5ArticlesResponseDto {
    private List<Article> top5Articles = new ArrayList<>();
    private List<Article> top5ReactArticleList = new ArrayList<>();
    private List<Article> top5SpringArticleList = new ArrayList<>();
    private List<Article> top5NodeJsArticleList = new ArrayList<>();
}
