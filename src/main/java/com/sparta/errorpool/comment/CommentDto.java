package com.sparta.errorpool.comment;

import lombok.Getter;

@Getter
public class CommentDto {
    private Long commetId;
    private String username;
    private Long ArticleId;
    private String content;
}
