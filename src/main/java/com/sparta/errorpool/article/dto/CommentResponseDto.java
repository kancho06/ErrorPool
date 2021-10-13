package com.sparta.errorpool.article.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentResponseDto {
    private Long commentId;
    private String content;
    private String username;
    private Integer userSkillId;
    private String email;
    private LocalDateTime regDt;
}
