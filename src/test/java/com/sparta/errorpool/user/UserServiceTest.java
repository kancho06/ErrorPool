package com.sparta.errorpool.user;

import com.sparta.errorpool.article.Skill;
import com.sparta.errorpool.user.dto.SignupRequestDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;


    Long userId = null;



    @Test
    @Order(1)
    @DisplayName("회원 가입")
    void test2() {
        // given
        String email = "retan1@spartacodingclub.kr";
        String username = "르탄이";
        String password = "nobodynoboy";
        Skill skill = null;
        boolean admin = false;


        SignupRequestDto signupRequestDto = new SignupRequestDto();

        signupRequestDto.setUsername(username);
        signupRequestDto.setPassword(password);
        signupRequestDto.setEmail(email);
        signupRequestDto.setAdmin(admin);
        signupRequestDto.setSkill(skill);

        // when
        User user = userService.registerUser(signupRequestDto);

        // then
        assertNotNull(user.getId());
        assertEquals(username, user.getUsername());
        assertTrue(passwordEncoder.matches(password, user.getPassword()));
        assertEquals(email, user.getEmail());
        assertEquals(skill, user.getSkill());
        assertEquals(UserRoleEnum.USER, user.getRole());

        userId = user.getId();
    }

}