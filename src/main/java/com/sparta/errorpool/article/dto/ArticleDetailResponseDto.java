package com.sparta.errorpool.article.dto;

import com.sparta.errorpool.comment.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
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
    private List<CommentResponseDto> comments = new ArrayList<>();

    public void addCommentsDtoListFrom(List<Comment> comments) {
        for (Comment comment : comments) {
            this.comments.add(CommentResponseDto.builder()
                    .commentId(comment.getId())
                    .content(comment.getContent())
                    .username(comment.getUser().getUsername())
                    .userSkillId(comment.getUser().getSkill().getNum())
                    .email(comment.getUser().getEmail()).build());
        }
    }
}