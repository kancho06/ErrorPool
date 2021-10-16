package com.sparta.errorpool.globalController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.errorpool.defaultResponse.DefaultResponse;
import com.sparta.errorpool.defaultResponse.StatusCode;
import com.sparta.errorpool.defaultResponse.SuccessYn;
import com.sparta.errorpool.exception.InvalidTokenException;
import com.sparta.errorpool.exception.JwtTokenExpiredException;
import com.sparta.errorpool.exception.TokenNullException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtExceptionHandlerFilter extends OncePerRequestFilter {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            filterChain.doFilter(request,response);
        } catch (RuntimeException ex){
            setErrorResponse(response,ex);
        }
    }

    public void setErrorResponse(HttpServletResponse response, Throwable ex){
        response.setContentType("application/json");
        ResponseEntity<DefaultResponse<Void>> errorResponse =
                ResponseEntity.ok(DefaultResponse.res(
                        SuccessYn.NO,
                        StatusCode.UNAUTHORIZED,
                        ex.getMessage(),
                                null));
        try{
            String json = objectMapper.writeValueAsString(errorResponse.getBody());
            response.getWriter().write(json);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}