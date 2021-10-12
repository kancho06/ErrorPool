package com.sparta.errorpool.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;


public class PasswordLengthException extends RuntimeException{
    public PasswordLengthException(String message) {
        super(message);
    }
}
