package com.sparta.errorpool.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;


public class UsernameLengthException extends RuntimeException{
    public UsernameLengthException(String message) {
        super(message);
    }
}
