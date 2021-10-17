package com.sparta.errorpool.article.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@RequiredArgsConstructor
@ApiModel(value = "게시글 수정 정보", description = "게시글 수정 정보")
public class ArticleUpdateRequestDto {
    private final String title;
    private final MultipartFile img;
    private final String content;
}
