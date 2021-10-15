package com.sparta.errorpool.comment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.errorpool.article.Article;
import com.sparta.errorpool.user.User;
import com.sparta.errorpool.util.Timestamped;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @Column
    @ApiModelProperty(value = "댓글 아이디")
    private Long id;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "USER_ID", nullable = false)
    @ApiModelProperty(value = "유저 정보")
    private User user;

    @ManyToOne
    @JoinColumn(name = "ARTICLE_ID", nullable = false)
    @ApiModelProperty(value = "게시글 정보")
    private Article article;

    @Column
    @ApiModelProperty(value = "댓글 내용")
    private String content;

    public Comment(User user, Article article, String content) {
        this.user = user;
        this.article = article;
        this.content = content;
    }
}