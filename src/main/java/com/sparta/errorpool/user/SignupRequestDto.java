package com.sparta.errorpool.user;


import com.sparta.errorpool.article.Skill;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequestDto {


    private String email;

    private String password;

    private String username;

    private String skill;

    private boolean admin = false;

    private String adminToken = "";


}