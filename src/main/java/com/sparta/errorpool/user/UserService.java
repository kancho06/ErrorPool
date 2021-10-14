package com.sparta.errorpool.user;


import com.sparta.errorpool.article.Skill;
import com.sparta.errorpool.security.JwtTokenProvider;
import com.sparta.errorpool.security.UserDetailsImpl;
import com.sparta.errorpool.security.UserDetailsServiceImpl;
import com.sparta.errorpool.user.dto.SignupRequestDto;
import com.sparta.errorpool.user.dto.UserRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final SignupValidator signupValidator;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsServiceImpl userDetailsService;


    public boolean registerUser(SignupRequestDto requestDto) {
        User user = signupValidator.validate(requestDto);
        userRepository.save(user);
        return true;
    }


    public boolean updateSkill(Long userId, SignupRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NullPointerException("아이디가 존재하지 않습니다.")
        );
        Integer skillId = requestDto.getSkillId();
        Skill skill = Skill.getSkillById(skillId);
        user.setSkill(skill);
        userRepository.save(user);
        return true;
    }

    public User findUserByEmail(UserRequestDto userRequestDto) {
        String email = userRequestDto.getEmail();
        User user = userRepository.findByEmail(email).orElseThrow(
                ()-> new NullPointerException("아이디가 존재하지 않습니다.")
        );
        return user;
    }
    public String createToken(UserRequestDto userRequestDto ) {
        String email = userRequestDto.getEmail();
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        UsernamePasswordAuthenticationToken authenticationToken =
//                new UsernamePasswordAuthenticationToken(userRequestDto.getEmail(),userRequestDto.getPassword());
                new UsernamePasswordAuthenticationToken(userDetails, "",userDetails.getAuthorities());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        return jwtTokenProvider.createToken(authentication);

    }




//      new UsernamePasswordAuthenticationToken(UserDetails, "",UserDetails.getAuthorities());
}