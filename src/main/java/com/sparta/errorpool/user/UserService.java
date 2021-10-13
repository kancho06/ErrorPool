package com.sparta.errorpool.user;


import com.sparta.errorpool.article.Skill;
import com.sparta.errorpool.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



@Service
public class UserService {
    private final UserRepository userRepository;
    private final SignupValidator signupValidator;


    @Autowired
    public UserService(UserRepository userRepository, SignupValidator signupValidator) {
        this.userRepository = userRepository;
        this.signupValidator = signupValidator;
    }



    public User registerUser(SignupRequestDto requestDto) {
        return userRepository.save(signupValidator.validate(requestDto));
    }


    public Long update(Long userId, SignupRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NullPointerException("아이디가 존재하지 않습니다.")
        );
        if(user.getEmail().equals(userDetails.getUsername())) {
            Integer skillId = requestDto.getSkillId();
            Skill skill = Skill.getSkillById(skillId);
            requestDto.setSkill(skill);
            user.update(requestDto);
        }
        return userId;
    }


}