package com.sparta.errorpool.user;



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


}