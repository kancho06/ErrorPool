package com.sparta.errorpool.user;

import com.sparta.errorpool.article.Skill;
import com.sparta.errorpool.exception.*;
import com.sparta.errorpool.user.dto.SignupRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class SignupValidator {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;




    public User validate(SignupRequestDto requestDto) {

        String email = requestDto.getEmail();
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();
        UserRoleEnum role = UserRoleEnum.USER;
        Optional<User> found = repository.findByEmail(email);
        String pattern = "^[a-zA-Z0-9가-힣]*$";

        if (found.isPresent()) {
            throw new DuplicateUserException("중복된 이메일 입니다");
        } else if (!isValidEmail(email)) {
            throw new EmailFormException("이메일 형식이 올바르지 않습니다");
        } else if (!Pattern.matches(pattern, username)) {
            throw new UsernamePatternException("특수문자는 사용하실 수 없습니다");
        } else if(username.length() < 3 || username.length() > 12) {
            throw new UsernameLengthException("닉네임을 3자 이상 12자 이하로 입력하세요");
        } else if (password.length() < 6 || password.length() > 12) {
            throw new PasswordLengthException("비밀번호를 6자 이상  12자 이하로 입력하세요");
        } else if (password.contains(email)) {
            throw new PasswordContainsException("패스워드는 아이디를 포함할 수 없습니다.");
        }
        // 패스워드 인코딩
        password = passwordEncoder.encode(password);
        requestDto.setPassword(password);

        User user = new User(email,password, username, role, Skill.NONE);

        return user;
        }



    public static boolean isValidEmail(String email) {
        boolean err = false;
        String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        if(m.matches()) { err = true; } return err; }

}
