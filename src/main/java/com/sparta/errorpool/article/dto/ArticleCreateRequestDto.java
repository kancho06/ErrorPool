package com.sparta.errorpool.article.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@RequiredArgsConstructor
@ApiModel(value = "게시글 생성 정보", description = "게시글 생성 정보")
public class ArticleCreateRequestDto {
    private final String title;
    private final MultipartFile img;
    private final String content;
    private final Integer skillId;
    private final Integer categoryId;
}
