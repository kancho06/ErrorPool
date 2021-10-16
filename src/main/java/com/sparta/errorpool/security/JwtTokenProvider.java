package com.sparta.errorpool.security;

import com.sparta.errorpool.exception.InvalidTokenException;
import com.sparta.errorpool.exception.TokenNullException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.StringJoiner;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    @Value("${jwt.token.key}")
    private String secretKey;


    private long tokenValidTime = 120 * 60 * 1000L;
    private final UserDetailsServiceImpl userDetailsService;


    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    private Key getSigninKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(Authentication authentication) {
        StringJoiner joiner = new StringJoiner(",");
        for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
            String authority = grantedAuthority.getAuthority();
            joiner.add(authority);
        }
        String authorities = joiner.toString();

        Date now = new Date();
        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim("authorities", authorities)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidTime))
                .signWith(getSigninKey(), SignatureAlgorithm.HS256)
                .compact();

    }


    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
        System.out.println("============getAuthentication===========");
        System.out.println(userDetails);
        System.out.println("============getAuthentication===========");
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }


    public String getUserPk(String token) {
        JwtParser parser = Jwts.parserBuilder().setSigningKey(getSigninKey()).build();
        Jws<Claims> claims = parser.parseClaimsJws(token);
        return claims.getBody().getSubject();
    }


    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("X-AUTH-TOKEN");
    }


    public boolean validateToken(String jwtToken) {
        if ( jwtToken == null ) {
            throw new TokenNullException("토큰이 존재하지 않습니다.");
        }
        try {
            JwtParser parser = Jwts.parserBuilder().setSigningKey(getSigninKey()).build();
            Jws<Claims> claims = parser.parseClaimsJws(jwtToken);

            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            throw new InvalidTokenException("정상적인 토큰이 아닙니다." +e.getClass()+"//"+e.getMessage() +"//" +e.getCause());
        }
    }
}