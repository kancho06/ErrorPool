package com.sparta.errorpool.article.dto;

import lombok.Getter;

@Getter
public class LikeResponseDto {

    private final boolean isLiked;

    private LikeResponseDto(boolean isLiked) {
        this.isLiked = isLiked;
    }

    public static LikeResponseDto of(boolean isLiked) {
        return new LikeResponseDto(isLiked);
    }
}
