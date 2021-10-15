package com.sparta.errorpool.article;

import com.sparta.errorpool.article.dto.*;
import com.sparta.errorpool.comment.Comment;
import com.sparta.errorpool.user.User;
import com.sparta.errorpool.util.Timestamped;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Article extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    private String imgUrl;

    private Integer viewCount = 0;

    @Enumerated(EnumType.STRING)
    private Skill skill;

    @Enumerated(EnumType.STRING)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @BatchSize(size = 20)
    @OneToMany(mappedBy = "article")
    private List<Comment> comments = new ArrayList<>();

    @BatchSize(size = 20)
    @OneToMany(mappedBy = "article")
    private List<LikeInfo> likes = new ArrayList<>();

    public Article(ArticleCreateRequestDto requestDto, User user) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.skill = Skill.getSkillById(requestDto.getSkillId());
        this.category = Category.getCategoryById(requestDto.getCategoryId());
        this.user = user;
    }

    public static Article of(ArticleCreateRequestDto requestDto, User user) {
        return new Article(requestDto, user);
    }

    public boolean isWritedBy(User user) {
        return this.user.equals(user);
    }

    public void update(ArticleUpdateRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
    }

    public ArticleResponseDto toArticleResponseDto(UserDetails userDetails) {
        if ( userDetails == null ) {
            return ArticleResponseDto.builder()
                    .articleId(this.id)
                    .title(this.title)
                    .content(this.content)
                    .viewCount(this.viewCount)
                    .skillId(skill.getNum())
                    .commentCount(this.comments.size())
                    .likeCount(this.likes.size())
                    .categoryId(category.getNum())
                    .username(user.getUsername())
                    .userSkillId((user.getSkill() == null) ? null : user.getSkill().getNum())
                    .isLiked(false)
                    .email(user.getEmail())
                    .regDt(this.getCreatedAt())
                    .build();
        } else {
            return ArticleResponseDto.builder()
                    .articleId(this.id)
                    .title(this.title)
                    .content(this.content)
                    .viewCount(this.viewCount)
                    .skillId(skill.getNum())
                    .commentCount(this.comments.size())
                    .likeCount(this.likes.size())
                    .categoryId(category.getNum())
                    .username(user.getUsername())
                    .userSkillId((user.getSkill() == null) ? null : user.getSkill().getNum())
                    .isLiked(this.likes.stream().anyMatch(likeInfo -> likeInfo.getUser().getEmail().equals(userDetails.getUsername())))
                    .email(user.getEmail())
                    .regDt(this.getCreatedAt())
                    .build();
        }
    }

    public ArticleDetailResponseDto toArticleDetailResponseDto(UserDetails userDetails) {
        if ( userDetails == null ) {
            return ArticleDetailResponseDto.builder()
                    .articleId(this.id)
                    .title(this.title)
                    .content(this.content)
                    .viewCount(this.viewCount)
                    .skillId(skill.getNum())
                    .commentCount(this.comments.size())
                    .likeCount(this.likes.size())
                    .categoryId(category.getNum())
                    .username(user.getUsername())
                    .userSkillId((user.getSkill() == null) ? null : user.getSkill().getNum())
                    .isLiked(false)
                    .email(user.getEmail())
                    .regDt(this.getCreatedAt())
                    .imgUrl(this.imgUrl)
                    .comments(addCommentsDtoListFrom(this.comments))
                    .build();
        } else {
            return ArticleDetailResponseDto.builder()
                    .articleId(this.id)
                    .title(this.title)
                    .content(this.content)
                    .viewCount(this.viewCount)
                    .skillId(skill.getNum())
                    .commentCount(this.comments.size())
                    .likeCount(this.likes.size())
                    .categoryId(category.getNum())
                    .username(user.getUsername())
                    .userSkillId((user.getSkill() == null) ? null : user.getSkill().getNum())
                    .isLiked(this.likes.stream().anyMatch(likeInfo -> likeInfo.getUser().getEmail().equals(userDetails.getUsername())))
                    .email(user.getEmail())
                    .regDt(this.getCreatedAt())
                    .imgUrl(this.imgUrl)
                    .comments(addCommentsDtoListFrom(this.comments))
                    .build();
        }
    }

    public List<CommentResponseDto> addCommentsDtoListFrom(List<Comment> comments) {
        List<CommentResponseDto> result = new ArrayList<>();
        for (Comment comment : comments) {
            result.add(CommentResponseDto.builder()
                    .commentId(comment.getId())
                    .content(comment.getContent())
                    .username(comment.getUser().getUsername())
                    .userSkillId((comment.getUser().getSkill() == null) ? null : comment.getUser().getSkill().getNum())
                    .email(comment.getUser().getEmail())
                    .regDt(comment.getCreatedAt())
                    .build()
            );
        }
        return result;
    }
}
