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
    private Skill skillId;

    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;


    public User(String email, String password, String username ,UserRoleEnum role, Integer skillId) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.skillId = Skill.getSkillById(skillId);
        this.socialId = null;
        this.role = role;
    }
    public User( String email, String password, String username,  UserRoleEnum role, Long socialId, Integer skillId) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.skillId = null;
        this.socialId = socialId;
        this.role = role;
    }

}
