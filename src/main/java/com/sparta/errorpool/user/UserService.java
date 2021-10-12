package com.sparta.errorpool.user;


import com.sparta.errorpool.article.Skill;
import com.sparta.errorpool.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
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



    public String registerUser(SignupRequestDto requestDto) {
        userRepository.save(signupValidator.validate(requestDto));
        return "";
    }


    public Long update(Long userId, SignupRequestDto requestDto) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NullPointerException("아이디가 존재하지 않습니다.")
        );
        Integer skillId = requestDto.getSkillId();
        Skill skill = Skill.getSkillById(skillId);
        requestDto.setSkill(skill);
        user.update(requestDto);
        return userId;
    }


}