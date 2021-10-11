package com.sparta.errorpool.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final SignupValidator signupValidator;


    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,SignupValidator signupValidator) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.signupValidator = signupValidator;
    }



    public String registerUser(SignupRequestDto requestDto) {
        userRepository.save(signupValidator.validate(requestDto));
        return "";


    }


}