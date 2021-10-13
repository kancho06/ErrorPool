package com.sparta.errorpool.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.errorpool.security.UserDetailsImpl;
import com.sparta.errorpool.user.dto.KakaoUserInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
public class KakaoUserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;


    @Autowired
    public KakaoUserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }



    public void kakaoLogin(String code) throws JsonProcessingException {
// 1. "인가 코드"로 "액세스 토큰" 요청
        String accessToken = getAccessToken(code);

// 2. "액세스 토큰"으로 "카카오 사용자 정보" 가져오기
        KakaoUserInfoDto kakaoUserInfo = getKakaoUserInfo(accessToken);
// 3. 필요시 회원가입 (리팩토링)
        User kakaoUser = registerKakaoUserIfNeeded(kakaoUserInfo);
        // 4. 강제 로그인 처리
        //UserDetails로 감싸주고 토큰으로 감싸준다
        forceLogin(kakaoUser);

    }

    //------------------------여기부터 kakaoLogin에 관한 함수---------------------------
    private String getAccessToken(String code) throws JsonProcessingException {
// HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

// HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "0c207c8530bd178646fd2cbbc6a80e80");
        body.add("redirect_uri", "http://localhost:8080/user/kakao");
        body.add("code", code);

// HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

// HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }

    private KakaoUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
// HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

// HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        Long socialId = jsonNode.get("id").asLong();
        String username = jsonNode.get("properties")
                .get("nickname").asText();
        String email = jsonNode.get("kakao_account")
                .get("email").asText();


        return new KakaoUserInfoDto(socialId, username, email);
    }
    //카카오 회원 가입 후 강제 로그인
    private void forceLogin(User kakaoUser) {
        UserDetails userDetails = new UserDetailsImpl(kakaoUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    private User registerKakaoUserIfNeeded(KakaoUserInfoDto kakaoUserInfo) {
        // DB 에 중복된 Kakao Id 가 있는지 확인
        Long socialId = kakaoUserInfo.getSocialId();
        User kakaoUser = userRepository.findBySocialId(socialId)
                .orElse(null);
        if (kakaoUser == null) {
            String kakaoEmail = kakaoUserInfo.getEmail();
            User sameEmailUser = userRepository.findByEmail(kakaoEmail).orElse(null);
            //동일한 이메일 확인
            if(sameEmailUser != null) {
                kakaoUser = sameEmailUser;
                //기존 정보에 카카오 아이디 추가
                kakaoUser.setSocialId(socialId);
            } else{
                // 회원가입
// username: kakao nickname
                String username = kakaoUserInfo.getUsername();
                // password: random UUID
                //UUID를 써서 랜덤 배열 패스워드를 만들더라도
                //인코딩을 해줘서 암호화를 시켜야한다.
                String password = UUID.randomUUID().toString();
                String encodedPassword = passwordEncoder.encode(password);

// email: kakao email
                String email = kakaoUserInfo.getEmail();
// role: 일반 사용자
                UserRoleEnum role = UserRoleEnum.USER;

                kakaoUser = new User(email, encodedPassword, username, role, socialId, null);

            }
            userRepository.save(kakaoUser);
        }
        return kakaoUser;
    }
}
