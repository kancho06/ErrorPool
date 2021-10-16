package com.sparta.errorpool.article.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@ApiModel(value = "게시글 생성 정보", description = "게시글 생성 정보")
public class ArticleCreateRequestDto {
    @NotBlank
    private final String title;

    private MultipartFile img = null;

    @NotBlank
    private final String content;

    @NotNull
    private final Integer skillId;

    @NotNull
    private final Integer categoryId;
}
