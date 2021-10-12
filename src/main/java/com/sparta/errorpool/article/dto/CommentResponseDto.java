package com.sparta.errorpool.article.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentResponseDto {
    private Long commentId;
    private String content;
    private String username;
    private Integer userSkill;
    private String email;
}
