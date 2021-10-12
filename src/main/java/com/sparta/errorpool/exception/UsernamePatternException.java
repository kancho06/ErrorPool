package com.sparta.errorpool.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;


public class UsernamePatternException extends RuntimeException{
    public UsernamePatternException(String message) {
        super(message);
    }
}
