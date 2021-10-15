package com.sparta.errorpool.comment;

import com.sparta.errorpool.article.Article;
import com.sparta.errorpool.user.User;
import com.sparta.errorpool.util.Timestamped;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Entity
@ApiModel(value = "댓글 정보", description = "댓글 아이디, 유저 정보, 게시글 정보, 댓글 내용을 가진 Domain Class")
public class Comment extends Timestamped {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "댓글 아이디")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    @ApiModelProperty(value = "유저 정보")
    private User user;

    @ManyToOne
    @JoinColumn(name = "ARTICLE_ID", nullable = false)
    @ApiModelProperty(value = "게시글 정보")
    private Article article;

    @ApiModelProperty(value = "댓글 내용")
    private String content;

    public Comment(User user, Article article, String content) {
        this.user = user;
        this.article = article;
        this.content = content;
    }
}