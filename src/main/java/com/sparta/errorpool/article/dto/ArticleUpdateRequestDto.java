package com.sparta.errorpool.article.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@ApiModel(value = "게시글 수정 정보", description = "게시글 수정 정보")
public class ArticleUpdateRequestDto {
    private final String title;
    private final String img;
    private final String content;
}
