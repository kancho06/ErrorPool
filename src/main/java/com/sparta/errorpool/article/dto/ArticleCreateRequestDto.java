package com.sparta.errorpool.article.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@RequiredArgsConstructor
@ApiModel(value = "게시글 생성 정보", description = "게시글 생성 정보")
public class ArticleCreateRequestDto {
    @NotBlank
    private final String title;
    private final MultipartFile img;
    @NotBlank
    private final String content;
    @NotNull
    private final Integer skillId;
    @NotNull
    private final Integer categoryId;
}
