package com.sparta.errorpool.comment;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentDto {
    private Long commentId;
    private String username;
    private Long articleId;
    private String content;
}
