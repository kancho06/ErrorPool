package com.sparta.errorpool.article.dto;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@ApiModel(value = "댓글 응답 정보", description = "댓글 응답 정보 DTO")
public class CommentResponseDto {
    private Long commentId;
    private String content;
    private String username;
    private Integer userSkillId;
    private String email;
    private LocalDateTime regDt;
}
