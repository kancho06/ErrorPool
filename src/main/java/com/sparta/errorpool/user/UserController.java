package com.sparta.errorpool.user;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.sparta.errorpool.defaultResponse.DefaultResponse;
import com.sparta.errorpool.defaultResponse.ResponseMessage;
import com.sparta.errorpool.defaultResponse.StatusCode;
import com.sparta.errorpool.defaultResponse.SuccessYn;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;


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
    public ResponseEntity createUser (@RequestBody SignupRequestDto requestDto){

        if (userService.registerUser(requestDto) == "") {
            userService.registerUser(requestDto);
            return new ResponseEntity(DefaultResponse.res(SuccessYn.OK, StatusCode.OK , ResponseMessage.CREATED_USER,null ), HttpStatus.OK);
        }
        return new ResponseEntity(DefaultResponse.res(SuccessYn.NO, StatusCode.BAD_REQUEST, ResponseMessage.CREATED_USER_FAILED, null), HttpStatus.OK);

    }

    @PostMapping("/kakao")
    public ResponseEntity kakaoLogin(@RequestParam String code) throws JsonProcessingException {
        kakaoUserService.kakaoLogin(code);
        return new ResponseEntity(DefaultResponse.res(SuccessYn.OK,StatusCode.OK ,ResponseMessage.CREATED_USER,null ), HttpStatus.OK);
    }








}
