package com.sparta.errorpool.article;

import com.sparta.errorpool.article.dto.ArticleCreateRequestDto;
import com.sparta.errorpool.article.dto.ArticleDetailResponseDto;
import com.sparta.errorpool.article.dto.ArticleResponseDto;
import com.sparta.errorpool.article.dto.ArticleUpdateRequestDto;
import com.sparta.errorpool.comment.Comment;
import com.sparta.errorpool.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
@NoArgsConstructor
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    private Integer viewCount;

    @Enumerated(EnumType.STRING)
    private Skill skill;

    @Enumerated(EnumType.STRING)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

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
                .build();
    }

    public ArticleDetailResponseDto toArticleDetailResponseDto() {
        return ArticleDetailResponseDto.builder()
                .articleId(this.id)
                .title(this.title)
                .content(this.content)
                .viewCount(this.viewCount)
                .skillId(skill.getNum())
                .commentCount(this.comments.size())
                .categoryId(category.getNum())
                .username(user.getUsername())
                .userSkillId((user.getSkill() == null) ? null : user.getSkill().getNum())
                .email(user.getEmail())
                .build();
    }
}
