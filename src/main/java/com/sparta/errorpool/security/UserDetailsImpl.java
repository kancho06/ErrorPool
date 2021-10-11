package com.sparta.errorpool.security;


import com.sparta.errorpool.user.User;
import com.sparta.errorpool.user.UserRoleEnum;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class UserDetailsImpl implements UserDetails {

    private final User user;

    public UserDetailsImpl(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
    //인증관리자가 대조할때 쓰는 메서드
    @Override
    public String getPassword() {
        return user.getPassword();
    }
    //인증관리자가 대조할때 쓰는 메서드
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    //스프링 시큐리티의 권한 파악 (유저권한으로 주소창에 api 주소치면 관리자로 접속되던 문제 해결)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {



        UserRoleEnum role = user.getRole();
        String authority = role.getAuthority();

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(simpleGrantedAuthority);
        return authorities;
    }
}