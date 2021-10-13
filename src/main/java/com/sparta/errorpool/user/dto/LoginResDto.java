package com.sparta.errorpool.user.dto;

import com.sparta.errorpool.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginResDto {
    private String jwtToken;
    private User user;
}
