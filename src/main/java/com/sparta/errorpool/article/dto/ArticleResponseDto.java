package com.sparta.errorpool.article.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
public class ArticleResponseDto {
    private Long articleId;
    private String title;
    private String content;
    private Integer skillId;
    private Integer categoryId;

    @Setter
    @Builder.Default
    private boolean isLiked = false;
    @Setter
    private Integer likeCount;
    private Integer viewCount;
    private Integer commentCount;
    private String username;
    private Integer userSkillId;
    private String email;
    private LocalDateTime regDt;
}
