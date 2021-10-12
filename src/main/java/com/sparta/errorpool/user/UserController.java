package com.sparta.errorpool.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final KakaoUserService kakaoUserService;

    @Autowired
    public UserController(UserService userService, KakaoUserService kakaoUserService) {
        this.userService = userService;
        this.kakaoUserService = kakaoUserService;
    }

    @PostMapping("/register")
    public boolean createUser (@RequestBody SignupRequestDto requestDto) {
        if (userService.registerUser(requestDto) == "") {
            userService.registerUser(requestDto);
            return true;
        }
        return false;
    }
    @PostMapping("/kakao")
    public boolean kakaoLogin(@RequestParam String code) throws JsonProcessingException {
        kakaoUserService.kakaoLogin(code);
        return true;
    }





}
