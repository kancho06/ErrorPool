package com.sparta.errorpool.article.dto;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@ApiModel(value = "항목 별 게시글 목록 응답", description = "항목 별 게시글 목록 응답 DTO")
public class ArticlePageResponseDto {
    Integer page;
    Integer totalPage;
    List<ArticleResponseDto> articleList;
}
