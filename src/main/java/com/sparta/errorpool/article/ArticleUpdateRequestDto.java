package com.sparta.errorpool.article;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

//{title:title, img:img, content:content}
@Getter
@RequiredArgsConstructor
public class ArticleUpdateRequestDto {
    private final String title;
    private final String img;
    private final String content;
}
