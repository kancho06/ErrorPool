package com.sparta.errorpool.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.errorpool.article.Skill;
import com.sparta.errorpool.defaultResponse.DefaultResponse;
import com.sparta.errorpool.defaultResponse.ResponseMessage;
import com.sparta.errorpool.defaultResponse.StatusCode;
import com.sparta.errorpool.defaultResponse.SuccessYn;
import com.sparta.errorpool.exception.JwtTokenExpiredException;
import com.sparta.errorpool.exception.UnauthenticatedException;
import com.sparta.errorpool.security.JwtTokenProvider;
import com.sparta.errorpool.security.UserDetailsImpl;
import com.sparta.errorpool.user.dto.LoginResDto;
import com.sparta.errorpool.user.dto.SignupRequestDto;
import com.sparta.errorpool.user.dto.UpdateSkillRequestDto;
import com.sparta.errorpool.user.dto.UserRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final KakaoUserService kakaoUserService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserController(UserService userService, KakaoUserService kakaoUserService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.kakaoUserService = kakaoUserService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    @ResponseBody
    public LoginResDto login(@RequestBody UserRequestDto userRequestDto) {
        String token = userService.createToken(userRequestDto);
        LoginResDto loginResDto = new LoginResDto();
        loginResDto.setJwtToken(token);
        User user = userService.findUserByEmail(userRequestDto);
        loginResDto.setUser(user);
        String username = user.getUsername();
        log.info(username + "님 로그인을 환영합니다");
        return loginResDto;
    }


    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity createUser (@RequestBody SignupRequestDto requestDto){
        if(userService.registerUser(requestDto)) {
            String username = requestDto.getUsername();
            log.info(username + "님 환영합니다!");
            return new ResponseEntity(DefaultResponse.res(SuccessYn.OK, StatusCode.OK , ResponseMessage.CREATED_USER,null ), HttpStatus.OK);
        }
        return new ResponseEntity(DefaultResponse.res(SuccessYn.NO, StatusCode.BAD_REQUEST, ResponseMessage.UPDATE_SKILL_FAILED, null), HttpStatus.OK);
    }

    @PostMapping("/kakao")
    @ResponseBody
    public ResponseEntity kakaoLogin(@RequestParam String code) throws JsonProcessingException {
        if (kakaoUserService.kakaoLogin(code)) {
            return new ResponseEntity(DefaultResponse.res(SuccessYn.OK, StatusCode.OK , ResponseMessage.CREATED_USER,null ), HttpStatus.OK);
        }
        return new ResponseEntity(DefaultResponse.res(SuccessYn.NO, StatusCode.BAD_REQUEST, ResponseMessage.UPDATE_SKILL_FAILED, null), HttpStatus.OK);
    }

    @PutMapping("/changeskill")
    @ResponseBody
    public ResponseEntity updateSkill(@RequestBody UpdateSkillRequestDto requestDto , @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.updateSkill(Skill.getSkillById(requestDto.getSkillId()), userDetails);
        String username = userDetails.getName();
        log.info(username + "님 주특기가 변경되었습니다.");
        return new ResponseEntity(DefaultResponse.res(SuccessYn.OK, StatusCode.OK , ResponseMessage.UPDATE_SKILL_SUCCESS,null ), HttpStatus.OK);
    }

    @GetMapping("/info")
    @ResponseBody
    public LoginResDto getUserInfoFromToken(@RequestHeader(value="X-AUTH-TOKEN") String token) {
        if ( jwtTokenProvider.validateToken(token) ) {
            return getLoginResDtoFromToken(token);
        } else {
            throw new JwtTokenExpiredException("토큰이 만료되었습니다.");
        }
    }

    private LoginResDto getLoginResDtoFromToken(String token) {
        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        Object principal = authentication.getPrincipal();
        if ( principal instanceof UserDetailsImpl ) {
            LoginResDto loginResDto = getLoginResDtoFromPrincipal((UserDetailsImpl) principal);
            loginResDto.setJwtToken(token);
            return loginResDto;
        } else {
            throw new UnauthenticatedException("유효하지 않은 토큰입니다.");
        }
    }

    private LoginResDto getLoginResDtoFromPrincipal(UserDetailsImpl principal) {
        LoginResDto loginResDto = new LoginResDto();
        User user = principal.getUser();
        loginResDto.setUser(user);
        return loginResDto;
    }
}