package com.sparta.errorpool.article;

import com.sparta.errorpool.user.User;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    private Long userId;
//    private Long articleId;

    @ManyToOne
    private User user;

    @ManyToOne
    private Article article;

    public Like(User user, Article article) {
        this.user = user;
        this.article = article;
    }
}
