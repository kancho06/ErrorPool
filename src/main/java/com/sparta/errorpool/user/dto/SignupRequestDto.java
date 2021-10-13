package com.sparta.errorpool.user.dto;



import com.sparta.errorpool.article.Skill;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequestDto {


    private String email;

    private String password;

    private String username;

    private Integer skillId;

    private boolean admin = false;

    private String adminToken = "";

    private Skill skill;
}