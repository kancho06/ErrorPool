package com.sparta.errorpool.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity // 스프링 Security 지원을 가능하게 함
@EnableGlobalMethodSecurity(securedEnabled = true) // @Secured 어노테이션 활성화
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public BCryptPasswordEncoder encodePassword() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) {
// h2-console 사용에 대한 허용 (CSRF, FrameOptions 무시)
        web
                .ignoring()
                .antMatchers("/h2-console/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //disable 은 POST 권한 제한 푸는것
        http.csrf().disable();
        //.ignoringAntMatchers("/user/**"); 이건 권한 각자 설정해 주는것

        //나중에확인
//        http.headers().frameOptions().disable();

        http.authorizeRequests()

//javaScript

// image 폴더를 login 없이 허용
                .antMatchers("/chars/**").permitAll()
                .antMatchers("/uikit/**").permitAll()
                .antMatchers("/uikitjs/**").permitAll()
                .antMatchers("/images/**").permitAll()
// css 폴더를 login 없이 허용
                .antMatchers("/css1/**").permitAll()
// 회원 관리 처리 API 전부를 login 없이 허용
                .antMatchers("/api/**").permitAll()
                .antMatchers("/").permitAll()
                .antMatchers("/detail.html**").permitAll()
                .antMatchers("/goPost").permitAll()

                //어나니머스 설정 후 로그인시 출입하면 제한페이지 뜨게함
                .antMatchers("/user/kakao/callback").anonymous()
                .antMatchers("/user/signup").anonymous()
                .antMatchers("/user/login").anonymous()
// 그 외 어떤 요청이든 '인증'
                .anyRequest().authenticated()
                .and()
// [로그인 기능]
                .formLogin()
// 로그인 View 제공 (GET /user/login)
                .loginPage("/user/login")
// 로그인 처리 (POST /user/login)
                .loginProcessingUrl("/user/login")
// 로그인 처리 후 성공 시 URL
                .defaultSuccessUrl("/")
// 로그인 처리 후 실패 시 URL
                .failureUrl("/user/login?error")

                .and()
// [로그아웃 기능]
                .logout()
// 로그아웃 요청 처리 URL
                .logoutUrl("/user/logout")
                .logoutSuccessUrl("/")
                .permitAll()
                .and()
                .exceptionHandling()
// "접근 불가" 페이지 URL 설정
                .accessDeniedPage("/forbidden.html");
    }


}