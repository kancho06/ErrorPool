package com.sparta.errorpool.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

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
        web
                .ignoring()
                .antMatchers("/h2-console/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();

        http.authorizeRequests()


// API comment
                .antMatchers("/comment/{comment_id}").permitAll()

// API articles
                .antMatchers("/articles").permitAll()
                .antMatchers("/articles/skill/{skill_id}/{category_id}").permitAll()
                .antMatchers("/articles/mainList").permitAll()
                .antMatchers("/articles/{article_id}").permitAll()
                .antMatchers("/articles/mypage/{userid}").permitAll()
                .antMatchers("/articles?query={value}").permitAll()

                //어나니머스 설정 후 로그인시 출입하면 제한페이지 뜨게함
                .antMatchers("/user/kakao").anonymous()
                .antMatchers("/user/gogle").anonymous()
                .antMatchers("/user/register").anonymous()
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
// 로그인 처리 후 성공 시 URL GET
                .defaultSuccessUrl("/")
//                .successHandler(successHandler())
// 로그인 처리 후 실패 시 Handler
                .failureUrl("/user/login?error=true")
//                .failureHandler(failureHandler())
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