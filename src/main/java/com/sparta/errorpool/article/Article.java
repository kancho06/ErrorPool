package com.sparta.errorpool.article;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.errorpool.article.dto.*;
import com.sparta.errorpool.comment.Comment;
import com.sparta.errorpool.user.User;
import com.sparta.errorpool.util.Timestamped;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(indexes = @Index(name = "idx_skill_category", columnList = "skill, category"))
public class Article extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column
    private String imgUrl;

    @Column
    private Integer viewCount = 0;

    @Enumerated(EnumType.STRING)
    @Column
    private Skill skill;

    @Enumerated(EnumType.STRING)
    @Column
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @BatchSize(size = 20)
    @JsonIgnore
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @BatchSize(size = 20)
    @JsonIgnore
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
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
        comments.stream()
                .sorted(Comparator.comparing(Timestamped::getCreatedAt).reversed())
                .forEach(comment -> commentToResponseDto(result, comment));
        return result;
    }

    private void commentToResponseDto(List<CommentResponseDto> responseDtoList, Comment comment) {
        responseDtoList.add(CommentResponseDto.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .username(comment.getUser().getUsername())
                .userSkillId((comment.getUser().getSkill() == null) ? null : comment.getUser().getSkill().getNum())
                .email(comment.getUser().getEmail())
                .regDt(comment.getCreatedAt())
                .build()
        );
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", viewCount=" + viewCount +
                ", skill=" + skill +
                ", category=" + category +
                ", user=" + user +
                ", comments=" + comments +
                ", likes=" + likes +
                '}';
    }
}
