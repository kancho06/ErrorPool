package com.sparta.errorpool.article.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ArticleCreateRequestDto {
    private final String title;
    private final String img;
    private final String content;
    private final Integer skillId;
    private final Integer categoryId;
}
