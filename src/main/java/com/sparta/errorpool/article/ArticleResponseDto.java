package com.sparta.errorpool.article;

import lombok.*;

@Getter
@Builder
public class ArticleResponseDto {
    private Long articleId;
    private String title;
    private String content;
    private Integer viewCount;
    private Integer commentCount;
    private Integer skillId;
    private Integer categoryId;
    private String username;
    private String email;

    @Setter
    private Integer like;

}
