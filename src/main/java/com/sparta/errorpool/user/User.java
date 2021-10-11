package com.sparta.errorpool.user;

import com.sparta.errorpool.article.Skill;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
public class User {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    @Column(unique = true)
    private String username;

    private Long socialId;

    @Enumerated(value = EnumType.STRING)
    private Skill

    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;


    public User(String username, String password, UserRoleEnum role, String email) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.socialId = null;
        this.role = role;
    }
    public User(String username, String password, String email, UserRoleEnum role, Long socialId) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.socialId = socialId;
        this.role = role;
    }

}
