package com.sparta.errorpool.user;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.sparta.errorpool.defaultResponse.DefaultResponse;
import com.sparta.errorpool.defaultResponse.ResponseMessage;
import com.sparta.errorpool.defaultResponse.StatusCode;
import com.sparta.errorpool.defaultResponse.SuccessYn;
import com.sparta.errorpool.security.UserDetailsImpl;
import com.sparta.errorpool.user.dto.LoginResDto;
import com.sparta.errorpool.user.dto.SignupRequestDto;
import com.sparta.errorpool.user.dto.UserRequestDto;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @PostMapping("/login")
    @ResponseBody
    public LoginResDto login(@RequestBody UserRequestDto userRequestDto) {
        String token = userService.createToken(userRequestDto);
        LoginResDto loginResDto = new LoginResDto();
        loginResDto.setJwtToken(token);
        User user = userService.findUserByEmail(userRequestDto);
        loginResDto.setUser(user);
        return loginResDto;
    }


    @PostMapping("/register")
    public ResponseEntity createUser (@RequestBody SignupRequestDto requestDto){
        userService.registerUser(requestDto);
        return new ResponseEntity(DefaultResponse.res(SuccessYn.OK, StatusCode.OK , ResponseMessage.CREATED_USER,null ), HttpStatus.OK);
    }

    @PostMapping("/kakao")
    public ResponseEntity kakaoLogin(@RequestParam String code) throws JsonProcessingException {
        kakaoUserService.kakaoLogin(code);
        return new ResponseEntity(DefaultResponse.res(SuccessYn.OK, StatusCode.OK , ResponseMessage.CREATED_USER,null ), HttpStatus.OK);
    }

    @PutMapping("/{userid}")
    public ResponseEntity updateSkill(@PathVariable Long userid, @RequestBody SignupRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if(userService.updateSkill(userid, requestDto, userDetails)) {

            return new ResponseEntity(DefaultResponse.res(SuccessYn.OK, StatusCode.OK , ResponseMessage.UPDATE_SKILL_SUCCESS,null ), HttpStatus.OK);
        }
        return new ResponseEntity(DefaultResponse.res(SuccessYn.NO, StatusCode.BAD_REQUEST, ResponseMessage.UPDATE_SKILL_FAILED, null), HttpStatus.OK);
    }


}