//package com.sparta.errorpool.security;
//
//
//import com.sparta.errorpool.user.UserRoleEnum;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import javax.persistence.Column;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import java.nio.charset.StandardCharsets;
//import java.util.Base64;
//
//@RequiredArgsConstructor
//@Component
//public class JwtTokenProvider {
//    private String secretKey = "";
//
//    private Long tokenValidTime = 120 * 60 * 1000L;
//    private final UserDetailsImpl userDetails;
//
//    @PostConstruct
//    protected void init() {
//        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
//    }
//
//    public String createToken(String userPk, Long id, UserRoleEnum role, String email, String username) {
//        Claims claims =
//    }
//}
//
