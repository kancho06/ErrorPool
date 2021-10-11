package com.sparta.errorpool.article;

import com.sparta.errorpool.comment.Comment;
import com.sparta.errorpool.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
    private Integer likeCount;

    @Enumerated(EnumType.STRING)
    private Skill skill;

    @Enumerated(EnumType.STRING)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "article")
    private List<Comment> comments = new ArrayList<>();

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

    public ArticleResponseDto toArticleResponseDto() {
        return ArticleResponseDto.builder()
                .articleId(this.id)
                .title(this.title)
                .content(this.content)
                .viewCount(this.viewCount)
                .skillId(skill.getNum())
                .commentCount(this.comments.size())
                .categoryId(category.getNum())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }
}
