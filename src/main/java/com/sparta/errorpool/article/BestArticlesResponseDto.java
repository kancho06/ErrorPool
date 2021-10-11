package com.sparta.errorpool.article;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

//{
//        best:[articles]
//        react:[articles]
//        spring:[articles]
//        node_js:[articles]
//        }
@Getter
@Setter
@NoArgsConstructor
public class BestArticlesResponseDto {
    private List<Article> bestList = new ArrayList<>();
    private List<Article> bestReactArticleList = new ArrayList<>();
    private List<Article> bestSpringArticleList = new ArrayList<>();
    private List<Article> bestNodeJsArticleList = new ArrayList<>();
}
