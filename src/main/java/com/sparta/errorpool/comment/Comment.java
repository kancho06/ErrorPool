package com.sparta.errorpool.comment;

import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.persistence.*;

@Data
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private USER article;

    @JoinColumn(name = "ARTICLE_ID", nullable = false)
    private Article article;

    private String content;

}
