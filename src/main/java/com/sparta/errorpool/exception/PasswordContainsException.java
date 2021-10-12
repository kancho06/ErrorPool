package com.sparta.errorpool.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;


public class PasswordContainsException extends RuntimeException{
    public PasswordContainsException(String message) {
        super(message);
    }
}
