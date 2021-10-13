package com.sparta.errorpool.user;

import com.sparta.errorpool.article.Skill;
import com.sparta.errorpool.user.dto.SignupRequestDto;
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

    @Column
    private String password;

    @Column(unique = true)
    private String username;

    private Long socialId;

    @Enumerated(value = EnumType.STRING)
    private Skill skill;

    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;


    public User(String email, String password, String username ,UserRoleEnum role, Skill skill) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.skill = null;
        this.socialId = null;
        this.role = role;
    }
    public User( String email, String password, String username,  UserRoleEnum role, Long socialId, Skill skill) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.skill = null;
        this.socialId = socialId;
        this.role = role;
    }

    public void update(SignupRequestDto requestDto){
        this.skill = requestDto.getSkill();
    }

}
