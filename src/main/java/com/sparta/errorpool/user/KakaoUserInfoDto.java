package com.sparta.errorpool.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KakaoUserInfoDto {
    private Long socialId;
    private String email;
    private String username;
}
