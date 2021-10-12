package com.sparta.errorpool.article.dto;

import com.sparta.errorpool.comment.Comment;
import com.sparta.errorpool.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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
    private boolean liksStatus;
    @Setter
    private Integer likeCount;
    private Integer viewCount;
    private Integer commentCount;
    private String username;
    private Integer userSkillId;
    private String email;
    //todo private File image
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