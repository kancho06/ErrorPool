package com.sparta.errorpool.article.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@RequiredArgsConstructor
public class ArticleCreateRequestDto {
    private final String title;
    private final MultipartFile img;
    private final String content;
    private final Integer skillId;
    private final Integer categoryId;
}
