package com.sparta.errorpool.article.dto;

import com.sparta.errorpool.comment.Comment;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@ApiModel(value = "게시글 상세 정보 응답", description = "게시글 상세 정보 응답 DTO")
public class ArticleDetailResponseDto {
    private Long articleId;
    private String title;
    private String content;
    private Integer skillId;
    private Integer categoryId;

    @Setter
    private boolean isLiked;
    @Setter
    private Integer likeCount;
    private Integer viewCount;
    private Integer commentCount;
    private String username;
    private Integer userSkillId;
    private String email;
    private LocalDateTime regDt;
    private MultipartFile file;
    @Builder.Default
    private List<CommentResponseDto> comments = new ArrayList<>();
}