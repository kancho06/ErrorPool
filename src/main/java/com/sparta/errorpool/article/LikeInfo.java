package com.sparta.errorpool.article;

import com.sparta.errorpool.user.User;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class LikeInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Article article;

    public LikeInfo(User user, Article article) {
        this.user = user;
        this.article = article;
    }
}
